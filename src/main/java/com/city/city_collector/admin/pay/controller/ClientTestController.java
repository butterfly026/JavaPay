package com.city.city_collector.admin.pay.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayClientModel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.entity.PaytmOrder;
import com.city.city_collector.admin.pay.service.PayClientChannelService;
import com.city.city_collector.admin.pay.service.PayClientModelService;
import com.city.city_collector.admin.pay.service.PayOrderService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.ChannelManager1;
import com.city.city_collector.channel.PaytmChannelManager;
import com.city.city_collector.channel.bean.ApiInfo;
import com.city.city_collector.channel.bean.GroovyScript;
import com.city.city_collector.channel.bean.OrderInfo;
import com.city.city_collector.channel.bean.OrderRequestInfo;
import com.city.city_collector.channel.util.GroovyUtil;
import com.city.city_collector.channel.util.SignUtil;
import com.city.city_collector.common.util.AESUtil;
import com.city.city_collector.common.util.ApiMessage;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.util.SnUtil;
import com.city.city_collector.paylog.PayLogManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.paytm.pg.merchant.PaytmChecksum;

import groovy.lang.Binding;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Controller
@RequestMapping("/system/clientTest")
public class ClientTestController {

    @Autowired
    PayClientChannelService payClientChannelService;
    @Autowired
    PayOrderService payOrderService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    PayClientModelService payClientModelService;

    @AdminPermission(value = {"admin:clienttest:list"})
    @RequestMapping("/view")
    public String view(Model model) {
        model.addAttribute("channelList", payClientChannelService.querySelectListTest());
//        model.addAttribute("channelList", payClientChannelService.querySelectListTestPaytm());
        return "admin/pay/clienttest/view";
    }

    @AdminPermission(value = {"admin:clienttest:list"})
    @RequestMapping("/view_paytm")
    public String viewPaytm(Model model) {
//        model.addAttribute("channelList", payClientChannelService.querySelectListTest());
        model.addAttribute("channelList", payClientChannelService.querySelectListTestPaytm());
        return "admin/pay/clienttest/view1";
    }

    @AdminPermission(value = {"admin:clienttest:list"})
    @RequestMapping("/detail")
    public @ResponseBody
    Message detail(Long id) {
        if (id == null) {
            return Message.error("参数不合法");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        return Message.success("操作成功", payClientChannelService.queryDetail(params));
    }

    @AdminPermission(value = {"admin:clienttest:list"})
    @RequestMapping("/testApi")
    public @ResponseBody
    Message testApi(Long id, BigDecimal amount, String merchantSn, HttpServletRequest httpRequest) {
        if (id == null || StringUtils.isBlank(merchantSn)) {
            return Message.error("参数不合法");
        }

        try {
            //获取通道信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayClientChannel clientChannel = payClientChannelService.querySingle(params);

            //获取上游信息
            SysUser client = sysUserService.querySysUserById(clientChannel.getClientId());
            //上游商户号
            String merchantNo = client.getMerchantNo();
            Long time = System.currentTimeMillis();
            String notifyUrl = httpRequest.getAttribute("serverName").toString();

            Integer channelId = clientChannel.getChannelTypeId();

            //数据验证
            if (StringUtils.isBlank(merchantNo)) {
                return Message.error("上游商户号不能为空!");
            }
            if (StringUtils.isBlank(merchantSn)) {
                return Message.error("订单号不能为空!");
            }
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return Message.error("交易金额不合法!");
            }
            if (time == null) {
                return Message.error("时间戳不能为空!");
            }

            if (StringUtils.isBlank(notifyUrl)) {
                return Message.error("回调地址不能为空!");
            }

            //判断商户订单号是否已存在
            Map<String, Object> p1 = new HashMap<String, Object>();
            p1.put("merchantSn", merchantSn);
            if (payOrderService.querySingle(p1) != null) {
                return Message.error("商户订单号已存在!");
            }

            //获取通道对象
//            ClientChannel channel = ChannelManager.channels.get(clientChannel.getKeyname());

            //判断是否可以发起请求
            //判断通道是否开启
            Long minTime = 0L;
            Long maxTime = 999999L;
            if (StringUtils.isNotBlank(clientChannel.getStartTime())) {
                minTime = Long.parseLong(clientChannel.getStartTime().replace(":", ""));
            }
            if (StringUtils.isNotBlank(clientChannel.getEndTime())) {
                maxTime = Long.parseLong(clientChannel.getEndTime().replace(":", ""));
            }
            if (clientChannel.getStartTime() != null && clientChannel.getEndTime() != null) {

                SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                Long time_l = Long.parseLong(sdf.format(new Date()));
                if (time_l < minTime || time_l > maxTime) {//不在开启时间段内
                    return Message.error("上游通道未在开启的时间段内!");
                }
            }

            Integer type = clientChannel.getMtype();
            //通道模式
            if (type == null || type.intValue() == 0) {
                if (clientChannel.getMinMoney() != null && clientChannel.getMaxMoney() != null) {
                    if (amount.compareTo(clientChannel.getMinMoney()) < 0 || amount.compareTo(clientChannel.getMaxMoney()) > 0) {
                        return Message.error("金额不合法!应当在" + clientChannel.getMinMoney() + "~" + clientChannel.getMaxMoney() + "之间");
                    }
                }

            } else {
                List<BigDecimal> moneys = new ArrayList<BigDecimal>();
                String moneyStrs = clientChannel.getMoney();
                if (StringUtils.isNotBlank(moneyStrs)) {

                    String[] ms = moneyStrs.split(",");
                    for (int i = 0; i < ms.length; i++) {
                        if (StringUtils.isNotBlank(ms[i])) {
                            moneys.add(new BigDecimal(ms[i]));
                        }
                    }
                }

                boolean flag = false;
                for (BigDecimal m : moneys) {
                    if (m.compareTo(amount) == 0) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return Message.error("金额不合法!只能为：" + clientChannel.getMoney());
                }
            }

            String extraStr = "";

            Map<String, Object> channelParams = new HashMap<String, Object>();
            try {
                channelParams = new Gson().fromJson(clientChannel.getParams(), new TypeToken<HashMap<String, Object>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
                extraStr = "参数不合法，解析失败，略过：" + clientChannel.getParams();
            }

            //生成订单编号
            String sn = SnUtil.createSn(ApplicationData.getInstance().getConfig().getSnpre());
            //获取请求封装数据
            //获取模块对象
            Map<String, Object> p = new HashMap<String, Object>();
            p.put("keyname", clientChannel.getKeyname());
            PayClientModel pcm = payClientModelService.querySingle(p);
            if (pcm == null || pcm.getStatus().intValue() != 1 || pcm.getTestStatus().intValue() != 1) {
                return Message.error("测试模块不可用1！");
            }

            GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(clientChannel.getKeyname());
            if (gs == null || gs.getTestOrderParams() == null || gs.getDealOrderData() == null) {
                return Message.error("测试模块不可用2！");
            }
//            id,merchantNo,merchantMy,channelParams,sn,amount,domain
            Binding binding = new Binding();
            binding.setProperty("id", clientChannel.getId());
            binding.setProperty("merchantNo", merchantNo);
            binding.setProperty("merchantMy", client.getMerchantMy());
            binding.setProperty("channelParams", channelParams);
            binding.setProperty("sn", sn);
            binding.setProperty("amount", amount);
            binding.setProperty("domain", httpRequest.getAttribute("serverName").toString());
            binding.setProperty("platform", 1);
            //test createReqParams kahn
            Object obj = GroovyUtil.runScript(gs.getTestOrderParams(), binding);
            if (obj == null) {
                return Message.error("测试模块(下单)调用失败！");
            }
            ApiInfo apiInfo = (ApiInfo) obj;
//            ApiInfo apiInfo = channel.createOrderRequestData(clientChannel.getId(),sn, amount, httpRequest.getAttribute("serverName").toString(), channelId, merchantNo, client.getMerchantMy(), channelParams);

            apiInfo.setExtraStr(extraStr);

            if (apiInfo.getGotype().intValue() == 1) {
                if (apiInfo.isStatus()) {
                    String payUrl = apiInfo.getPayUrl();
                    if (!payUrl.startsWith("http")) {
                        payUrl = client.getUrlpay() + payUrl;
                    }

                    Map<String, Object> d1 = apiInfo.getDatas();

                    OrderInfo oi = new OrderInfo();
                    oi.setFlag(true);
                    oi.setData((String) d1.get("data"));
                    oi.setSn((String) d1.get("sn"));
                    oi.setPayUrl(payUrl);
                    oi.setName("");
                    oi.setCard("");
                    oi.setBankName("");

                    oi.setClientChannelId(clientChannel.getId());
                    oi.setClientId(client.getId());
                    oi.setClientNo(client.getUsername());
                    oi.setClientName(client.getName());

                    apiInfo.setDealResult("<span style='color:green;'>成功</span>");
                    //保存订单信息
                    PayOrder order = new PayOrder();

                    order.setAmount(amount);
                    order.setChannelTypeId(channelId.longValue());
                    order.setClientId(oi.getClientId());
                    //                order.setClientNo(oi.getClientNo());
                    order.setClientChannelId(oi.getClientChannelId());
                    order.setClientInfo(oi.getData());
                    order.setClientSn(oi.getSn());

                    order.setMerchantId(2L);
                    //                order.setMerchantNo(sysUser.getUsername());
                    order.setMerchantChannelId(100001L);
                    order.setMerchantSn(merchantSn);
                    order.setMoney(amount);

                    order.setNotifyStatus(1);//通知状态，已通知
                    order.setNotifyUrl(notifyUrl);
                    order.setOrderStatus(0);//订单状态，未支付
                    order.setPayTime(null);
                    order.setPayUrl(oi.getPayUrl());
                    order.setProductName("预留商品名");
                    order.setRemark(null);
                    order.setSn(sn);

                    if (apiInfo.getSavePayOrder().intValue() == 1) {
                        PaytmOrder paytmOrder = new PaytmOrder();
                        paytmOrder.setMid(merchantNo);
                        paytmOrder.setSn(sn);
                        paytmOrder.setTxntoken(oi.getSn());
                        paytmOrder.setAmount(amount);

                        payOrderService.addSaveByPaytm(order, paytmOrder);
                    } else {
                        payOrderService.addSave(order);
                    }
                    //接口返回的结果
                    Map<String, Object> data1 = new TreeMap<String, Object>();
                    data1.put("merchantNo", merchantNo);
                    data1.put("merchantSn", merchantSn);
                    data1.put("sn", sn);
                    data1.put("payUrl", oi.getPayUrl());
                    data1.put("sign", SignUtil.getMD5Sign(data1, client.getMerchantMy()));
                    apiInfo.setResult(new Gson().toJson(ApiMessage.success("success", data1)));
                    apiInfo.setPayUrl(oi.getPayUrl());
                }
                return Message.success("success", apiInfo);
            }

            apiInfo.setOrderUrl(client.getUrlpay());
            //数据请求
            OkHttpClient okhttp = new OkHttpClient.Builder().connectionPool(ApplicationData.getConnectionPool()).build();

            FormBody.Builder formBuilder = new FormBody.Builder();

            Map<String, Object> dparams = apiInfo.getDatas();

            if (dparams != null && !dparams.isEmpty()) {
                Set<String> keys = dparams.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    if (dparams.get(key) != null) {
                        formBuilder.add(key, dparams.get(key) + "");
                    }
                }
            }
            System.out.println("发起测试请求(reqType):" + apiInfo.getReqType());
            Request.Builder request = null;
            if (apiInfo.getReqType().intValue() == 1) {//发送json数据
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, apiInfo.getRequstData());
                request = new Request.Builder()
                        .url(client.getUrlpay())
                        .post(body)
                        .addHeader("content-type", "application/json");
            } else if (apiInfo.getReqType().intValue() == 0) {
                request = new Request.Builder()
                        .url(client.getUrlpay())
                        .post(formBuilder.build());
            } else if (apiInfo.getReqType().intValue() == 2) {
                System.out.println(client.getUrlpay() + "?" + apiInfo.getRequstData());
                request = new Request.Builder()
                        .url(client.getUrlpay() + "?" + apiInfo.getRequstData())
                        .get();
            }

            Map<String, String> headers = apiInfo.getHeaders();
            if (headers != null && headers.size() > 0 && request != null) {
                Request.Builder finalRequest = request;
                headers.forEach((key, value) -> {
                    finalRequest.header(key, value);
                });
            }
            Request build = request.build();

            System.out.println("发起测试请求....");
            Response response = okhttp.newCall(build).execute();
            System.out.println("发起测试请求1....");
            if (response != null && response.isSuccessful()) {
                String data = response.body().string();
                apiInfo.setResponseData(UnicodeToCN(data));
                response.close();

//                OrderInfo oi = channel.dealRequestData(data);
//                data,clientId,clientNo,clientName,clientChannelId
                binding = new Binding();
                binding.setProperty("data", data);
                binding.setProperty("clientId", client.getId());
                binding.setProperty("clientNo", client.getUsername());
                binding.setProperty("clientName", client.getName());
                binding.setProperty("clientChannelId", clientChannel.getId());

                binding.setProperty("url", client.getUrlpay());
                binding.setProperty("sn", sn);
                binding.setProperty("channelParams", channelParams);

                Object oiobj = GroovyUtil.runScript(gs.getDealOrderData(), binding);
                OrderInfo oi = (oiobj == null) ? null : (OrderInfo) oiobj;

                String logStr = "成功";
                if (oi != null) {

                    oi.setClientChannelId(clientChannel.getId());
                    oi.setClientId(client.getId());
                    oi.setClientNo(client.getUsername());
                    oi.setClientName(client.getName());

                    apiInfo.setDealResult("<span style='color:green;'>成功</span>");
                    //保存订单信息
                    PayOrder order = new PayOrder();

                    order.setAmount(amount);
                    order.setChannelTypeId(channelId.longValue());
                    order.setClientId(oi.getClientId());
//                    order.setClientNo(oi.getClientNo());
                    order.setClientChannelId(oi.getClientChannelId());
                    order.setClientInfo(oi.getData());
                    order.setClientSn(oi.getSn());

                    order.setMerchantId(2L);
//                    order.setMerchantNo(sysUser.getUsername());
                    order.setMerchantChannelId(100000L + channelId.longValue());
                    order.setMerchantSn(merchantSn);
                    order.setMoney(amount);

                    order.setNotifyStatus(1);//通知状态，已通知
                    order.setNotifyUrl(notifyUrl);
                    order.setOrderStatus(0);//订单状态，未支付
                    order.setPayTime(null);

                    //form转发
                    if (oi.getGtype() != null && oi.getGtype().intValue() == 1) {
                        oi.setPayUrl(httpRequest.getAttribute("serverName").toString() + oi.getPayUrl() + sn);
                    }
                    order.setPayUrl(oi.getPayUrl());
                    order.setProductName("预留商品名");
                    order.setRemark(null);
                    order.setSn(sn);

                    order.setOrderType(0);

                    payOrderService.addSave(order);

                    //接口返回的结果
                    Map<String, Object> data1 = new TreeMap<String, Object>();
                    data1.put("merchantNo", merchantNo);
                    data1.put("merchantSn", merchantSn);
                    data1.put("sn", sn);
                    data1.put("payUrl", oi.getPayUrl());
                    data1.put("sign", SignUtil.getMD5Sign(data1, client.getMerchantMy()));
                    apiInfo.setResult(new Gson().toJson(ApiMessage.success("success", data1)));
                    apiInfo.setPayUrl(oi.getPayUrl());
                } else {
                    apiInfo.setDealResult("<span style='color:red;'>失败</span>");
                    apiInfo.setResult(new Gson().toJson(ApiMessage.error("下单失败")));
                    logStr = "失败";
                }

                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "<span style='color:green;'>[通道测试]</span>上游下单" + logStr, apiInfo.getRequstData(), apiInfo.getResponseData(), client.getId(), clientChannel.getId(), 2L, 100000L + channelId.longValue());

            } else {
                apiInfo.setResponseError("<span style='color:red;'>API请求失败:" + response.code() + "," + response.message() + "</span>");
                apiInfo.setResult(new Gson().toJson(ApiMessage.error("下单失败")));
                if (response != null) response.close();
            }

            return Message.success("success", apiInfo);

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("调试失败:" + e.getLocalizedMessage());
        }
    }


    public String UnicodeToCN(String unicodeStr) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(unicodeStr);
        char ch;
        while (matcher.find()) {

            String group = matcher.group(2);

            ch = (char) Integer.parseInt(group, 16);

            String group1 = matcher.group(1);
            unicodeStr = unicodeStr.replace(group1, ch + "");
        }
        return unicodeStr.replace("\\", "").trim();
    }

    /**
     * API测试
     *
     * @param id
     * @param amount
     * @param merchantSn
     * @param httpRequest
     * @return
     */
    @AdminPermission(value = {"admin:clienttest:list"})
    @RequestMapping("/testApiPaytm")
    public @ResponseBody
    Message testApiPaytm(Long id, BigDecimal amount, String merchantSn, HttpServletRequest httpRequest) {
        if (id == null || StringUtils.isBlank(merchantSn)) {
            return Message.error("参数不合法");
        }

        try {
            //获取通道信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayClientChannel clientChannel = payClientChannelService.querySingle(params);

            //获取上游信息
            SysUser client = sysUserService.querySysUserById(clientChannel.getClientId());
            //上游商户号
            String merchantNo = client.getUsername();
            Long time = System.currentTimeMillis();
            String notifyUrl = httpRequest.getAttribute("serverName").toString() + "/api/paytm/testnotify";

            Integer channelId = clientChannel.getChannelTypeId();

            //数据验证
            if (StringUtils.isBlank(merchantNo)) {
                return Message.error("收款商户不存在!");
            }
            if (StringUtils.isBlank(merchantSn)) {
                return Message.error("订单号不能为空!");
            }
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return Message.error("交易金额不合法!");
            }
            if (time == null) {
                return Message.error("时间戳不能为空!");
            }

            if (StringUtils.isBlank(notifyUrl)) {
                return Message.error("回调地址不能为空!");
            }

            //判断商户订单号是否已存在
            Map<String, Object> p1 = new HashMap<String, Object>();
            p1.put("merchantSn", merchantSn);
            if (payOrderService.querySingle(p1) != null) {
                return Message.error("商户订单号已存在!");
            }

            //判断是否可以发起请求
            //判断通道是否开启
            Long minTime = 0L;
            Long maxTime = 999999L;
            if (StringUtils.isNotBlank(clientChannel.getStartTime())) {
                minTime = Long.parseLong(clientChannel.getStartTime().replace(":", ""));
            }
            if (StringUtils.isNotBlank(clientChannel.getEndTime())) {
                maxTime = Long.parseLong(clientChannel.getEndTime().replace(":", ""));
            }
            if (clientChannel.getStartTime() != null && clientChannel.getEndTime() != null) {

                SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                Long time_l = Long.parseLong(sdf.format(new Date()));
                if (time_l < minTime || time_l > maxTime) {//不在开启时间段内
                    return Message.error("收款商户通道未在开启的时间段内!");
                }
            }

            Integer type = clientChannel.getMtype();
            //通道模式
            if (type == null || type.intValue() == 0) {
                if (clientChannel.getMinMoney() != null && clientChannel.getMaxMoney() != null) {
                    if (amount.compareTo(clientChannel.getMinMoney()) < 0 || amount.compareTo(clientChannel.getMaxMoney()) > 0) {
                        return Message.error("金额不合法!应当在" + clientChannel.getMinMoney() + "~" + clientChannel.getMaxMoney() + "之间");
                    }
                }

            } else {
                List<BigDecimal> moneys = new ArrayList<BigDecimal>();
                String moneyStrs = clientChannel.getMoney();
                if (StringUtils.isNotBlank(moneyStrs)) {

                    String[] ms = moneyStrs.split(",");
                    for (int i = 0; i < ms.length; i++) {
                        if (StringUtils.isNotBlank(ms[i])) {
                            moneys.add(new BigDecimal(ms[i]));
                        }
                    }
                }

                boolean flag = false;
                for (BigDecimal m : moneys) {
                    if (m.compareTo(amount) == 0) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return Message.error("金额不合法!只能为：" + clientChannel.getMoney());
                }
            }

            String extraStr = "";

            //生成订单编号
            String sn = SnUtil.createSn(ApplicationData.getInstance().getConfig().getSnpre());

            ApiInfo apiInfo = new ApiInfo();

            JsonObject paytmParams = new JsonObject();

            JsonObject body = new JsonObject();
            body.addProperty("requestType", "Payment");
            body.addProperty("mid", clientChannel.getPaytmid());
            body.addProperty("websiteName", PaytmChannelManager.WEBSITE);
            body.addProperty("orderId", sn);
            body.addProperty("callbackUrl", httpRequest.getAttribute("serverName").toString() + "/api/paytm/notify");

            JsonObject txnAmount = new JsonObject();
            txnAmount.addProperty("value", amount);
            txnAmount.addProperty("currency", "INR");

            JsonObject userInfo = new JsonObject();
            //Unique reference ID for every customer which is generated by merchant. Special characters allowed in CustId are @, ! ,_ ,$, .
            //商家生成的每个客户的唯一参考ID。 CustId中允许的特殊字符是@ ! _ $ .
            userInfo.addProperty("custId", "CUST_001");

            body.add("txnAmount", txnAmount);
            body.add("userInfo", userInfo);

            /*
             * Generate checksum by parameters we have in body
             * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
             * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
             */
            //获取秘钥
            String md5 = clientChannel.getPaytmmd5();
            String uuid = clientChannel.getPaytmuid();

            String pwd = md5.substring(md5.length() - 7) + uuid.substring(uuid.length() - 4) + PaytmClientChannelController.KEY;

            String key = AESUtil.decrypt(clientChannel.getPaytmkey(), pwd);
            String checksum = PaytmChecksum.generateSignature(body.toString(), key);

            apiInfo.setSignData(body.toString());

            JsonObject head = new JsonObject();
            head.addProperty("signature", checksum);
            head.addProperty("version", "V1");
            head.addProperty("channelId", "WAP");
            head.addProperty("requestTimestamp", System.currentTimeMillis() / 1000);

            paytmParams.add("body", body);
            paytmParams.add("head", head);
            //请求数据
            String post_data = paytmParams.toString();
            System.out.println("请求数据：" + post_data);


            apiInfo.setSignStr(body.toString());
            apiInfo.setSign(checksum);
            apiInfo.setRequstData(post_data);
            apiInfo.setGotype(0);
            apiInfo.setExtraStr(extraStr);

            //数据请求
            OkHttpClient okhttp = new OkHttpClient.Builder().connectionPool(ApplicationData.getConnectionPool()).build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody reqbody = RequestBody.create(mediaType, post_data);
            Request request = new Request.Builder()
                    .url(PaytmChannelManager.ORDER_URL + "?mid=" + clientChannel.getPaytmid() + "&orderId=" + sn)
                    .post(reqbody)
                    .addHeader("content-type", "application/json")
                    .build();

            OrderRequestInfo ori = new OrderRequestInfo();
            ori.setMid(clientChannel.getPaytmid());
            ori.setMkey(key);

            String log = "";
            Response response = okhttp.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                String data = response.body().string();
                System.out.println("返回的数据：" + data);
                response.close();

                ori.setData(data);

                String txnToken = PaytmChannelManager.getTxnToken(data);
                if (StringUtils.isBlank(txnToken)) {
                    ori.setStatus(false);
                    log = ",请求成功,下单失败|" + data;

                    apiInfo.setDealResult("<span style='color:red;'>失败</span>");
                    apiInfo.setResult(new Gson().toJson(ApiMessage.error("下单失败")));

                } else {
                    ori.setStatus(true);
                    ori.setTxnToken(txnToken);

                    log = ",请求成功,下单成功|" + data;

                    ori.setClientChannelId(clientChannel.getId());
                    ori.setClientId(client.getId());
                    ori.setClientNo(client.getUsername());
                    ori.setPayUrl(httpRequest.getAttribute("serverName").toString() + "/api/paytm/paypre?sn=" + sn);


                    apiInfo.setDealResult("<span style='color:green;'>成功</span>");
                    //保存订单信息
                    PayOrder order = new PayOrder();

                    order.setAmount(amount);
                    order.setChannelTypeId(channelId.longValue());
                    order.setClientId(ori.getClientId());
//                    order.setClientNo(oi.getClientNo());
                    order.setClientChannelId(ori.getClientChannelId());
                    order.setClientInfo(ori.getData());
                    order.setClientSn("");

                    order.setMerchantId(2L);
//                    order.setMerchantNo(sysUser.getUsername());
                    order.setMerchantChannelId(100000L + channelId.longValue());
                    order.setMerchantSn(merchantSn);
                    order.setMoney(amount);

                    order.setNotifyStatus(1);//通知状态，已通知
                    order.setNotifyUrl(notifyUrl);
                    order.setOrderStatus(0);//订单状态，未支付
                    order.setPayTime(null);
                    order.setPayUrl(ori.getPayUrl());
                    order.setProductName("预留商品名");
                    order.setRemark(null);
                    order.setSn(sn);

                    order.setOrderType(1);

                    //支付链接对象
                    PaytmOrder paytmOrder = new PaytmOrder();
                    paytmOrder.setMid(ori.getMid());
                    paytmOrder.setSn(sn);
                    paytmOrder.setTxntoken(ori.getTxnToken());
                    paytmOrder.setAmount(amount);

                    payOrderService.addSaveByPaytm(order, paytmOrder);

                    //接口返回的结果
                    Map<String, Object> data1 = new TreeMap<String, Object>();
                    data1.put("merchantNo", merchantNo);
                    data1.put("merchantSn", merchantSn);
                    data1.put("sn", sn);
                    data1.put("payUrl", ori.getPayUrl());
                    data1.put("sign", SignUtil.getMD5Sign(data1, client.getMerchantMy()));
                    apiInfo.setResult(new Gson().toJson(ApiMessage.success("success", data1)));
                    apiInfo.setPayUrl(ori.getPayUrl());
                }

            } else {
                ori.setStatus(false);
                log = ",请求失败,响应码:" + response.code();

                apiInfo.setResponseError("<span style='color:red;'>API请求失败:" + response.message() + log + "</span>");
                apiInfo.setResult(new Gson().toJson(ApiMessage.error("下单失败")));
            }

            PayLogManager.getInstance().createPayLogByOrderRequest(sn, "<span style='color:green;'>[通道测试]</span>上游下单" + log, apiInfo.getRequstData(), apiInfo.getResponseData(), client.getId(), clientChannel.getId(), 2L, 100000L + channelId.longValue());

            return Message.success("success", apiInfo);

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("调试失败:" + e.getLocalizedMessage());
        }
    }
}
