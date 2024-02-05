package com.city.city_collector.web.controller;

import java.io.UnsupportedEncodingException;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.city.util.Logger;
import com.city.city_collector.admin.pay.controller.PayCashController;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayChannelType;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayClientModel;
import com.city.city_collector.admin.pay.entity.PayMerchantChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.service.PayBankCodeService;
import com.city.city_collector.admin.pay.service.PayCashService;
import com.city.city_collector.admin.pay.service.PayChannelTypeService;
import com.city.city_collector.admin.pay.service.PayClientChannelService;
import com.city.city_collector.admin.pay.service.PayClientModelService;
import com.city.city_collector.admin.pay.service.PayMerchantChannelService;
import com.city.city_collector.admin.pay.service.PayMerchantService;
import com.city.city_collector.admin.pay.service.PayOrderService;
import com.city.city_collector.admin.pay.service.PaytmOrderService;
import com.city.city_collector.admin.pay.service.YajinService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.ChannelManager1;
import com.city.city_collector.channel.bean.GroovyScript;
import com.city.city_collector.channel.bean.NotifyInfo;
import com.city.city_collector.channel.bean.OrderInfo;
import com.city.city_collector.channel.bean.QueryOrderInfo;
import com.city.city_collector.channel.util.GroovyUtil;
import com.city.city_collector.channel.util.HttpUtil;
import com.city.city_collector.channel.util.SignUtil;
import com.city.city_collector.common.util.ApiMessage;
import com.city.city_collector.common.util.ApiMessageCode;
import com.city.city_collector.common.util.CommonUtil;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.util.PayOrderRequestCacheManager;
import com.city.city_collector.common.util.SnCacheManager;
import com.city.city_collector.common.util.SnUtil;
import com.city.city_collector.paylog.PayLogManager;
import com.google.gson.Gson;
import groovy.lang.Binding;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.city.city_collector.admin.pay.controller.PayCashController;
import com.city.city_collector.admin.pay.entity.*;
import com.city.city_collector.admin.pay.service.*;
import com.city.city_collector.channel.util.TimeUtil;
import com.city.city_collector.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.taskdefs.Sleep;
import org.apache.jasper.compiler.TagConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.city.util.Logger;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.ChannelManager;
import com.city.city_collector.channel.ChannelManager1;
import com.city.city_collector.channel.bean.CashConfig;
import com.city.city_collector.channel.bean.ClientChannel;
import com.city.city_collector.channel.bean.GroovyScript;
import com.city.city_collector.channel.bean.NotifyInfo;
import com.city.city_collector.channel.bean.OrderInfo;
import com.city.city_collector.channel.bean.PayRatio;
import com.city.city_collector.channel.bean.QueryOrderInfo;
import com.city.city_collector.channel.bean.QueryOrderResult;
import com.city.city_collector.channel.util.GroovyUtil;
import com.city.city_collector.channel.util.HttpUtil;
import com.city.city_collector.channel.util.SignUtil;
import com.city.city_collector.paylog.PayLogManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import groovy.lang.Binding;

/**
 * @author nb
 * @Description:
 */
@Controller("webPayOrderController")
@RequestMapping("/api/pay")
public class PayOrderController {

    @Value("${spring.profiles.active}")
    private String serverType;

    @Autowired
    PayOrderService payOrderService;

    @Autowired
    PayMerchantService payMerchantService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    PayChannelTypeService payChannelTypeService;

    @Autowired
    PayClientChannelService payClientChannelService;

    @Autowired
    PayCashService payCashService;

//    @Autowired
//    PayBankCardService payBankCardService;

    @Autowired
    PayBankCodeService payBankCodeService;

    @Autowired
    PaytmOrderService paytmOrderService;

    @Autowired
    PayMerchantChannelService payMerchantChannelService;

    @Autowired
    PayClientModelService payClientModelService;

    @Autowired
    PayCashController payCashController;

    @Autowired
    private YajinService yajinService;

    /**
     * 下单
     *
     * @param merchantNo 商户编号
     * @param merchantSn 商户订单编号
     * @param amount     交易金额
     * @param notifyUrl  通知URL
     * @param remark     订单备注(如果传此值，回调时会原样返回)
     * @param channel    支付平台(alipay 支付宝扫码
     *                   alipay_h5 支付宝 h5
     *                   wechat 微信扫码
     *                   wechat_h5 微信 h5
     *                   ysf 云闪付
     *                   union 银联扫码
     *                   union_h5 银联 H5
     *                   bank 网银
     *                   paytm PAYTM)
     * @param time       秒级时间戳
     * @param signType   签名算法(固定值：md5_v2)
     * @param sign       MD5校验码
     * @param request
     * @return
     */
    @PostMapping("/order")
    public @ResponseBody
    ApiMessage order(String merchantNo, String merchantSn, BigDecimal amount, String notifyUrl,
                     String remark, String channel, Long time, String signType, String sign, HttpServletRequest request) {
        try {
            //创建一个订单请求日志
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"od:" + merchantNo + "-" + merchantSn + "channel:"+channel);
            PayLogManager.getInstance().createOrderAccessLog();


            //数据验证
            if (StringUtils.isBlank(merchantNo)) {
                return ApiMessage.error("商户编号不能为空!");
            }
            if (StringUtils.isBlank(merchantSn)) {
                return ApiMessage.error("订单号不能为空!");
            }
            if (!SnCacheManager.getInstance().isValidMerchantSn(merchantSn)) {
                return ApiMessage.error("订单号不能重复!");
            }

            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return ApiMessage.error("交易金额不合法!");
            }
            if (time == null) {
                return ApiMessage.error("时间戳不能为空!");
            }

            if (StringUtils.isBlank(notifyUrl)) {
                return ApiMessage.error("回调地址不能为空!");
            }
//            if(StringUtils.isBlank(productName)) {
//                return ApiMessage.error("商品名不能为空!");
//            }
            if (StringUtils.isBlank(channel)) {
                return ApiMessage.error("支付平台不能为空!");
            }
            
            PayChannelType ct = payChannelTypeService.queryChannelTypeByCode(channel);
            if (ct == null) {
                return ApiMessage.error("支付平台不合法!");
            }
            Long channelId = ct.getId();
            if (StringUtils.isBlank(signType) || !"md5".equals(signType)) {
                return ApiMessage.error("签名方式不合法!");
            }
            if (StringUtils.isBlank(sign)) {
                return ApiMessage.error("签名不能为空!");
            }

            
            //根据商户编号获取商户对象
            SysUser sysUser = sysUserService.querySysUserByUsername(merchantNo);

            if (sysUser == null || sysUser.getType() == null || sysUser.getType().intValue() != 2) {
                return ApiMessage.error("商户不存在!");
            }

            //判断用户IP是否合法
            if (StringUtils.isNotBlank(sysUser.getApiip())) {
                String[] ips = sysUser.getApiip().trim().split(",");
                String uip = CommonUtil.getIp(request);
                boolean flag = false;
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i].equals(uip)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return ApiMessage.error("IP禁止访问");
                }
            }


            //验证签名
            Map<String, Object> params = new TreeMap<>();
            params.put("merchantNo", merchantNo);
            params.put("merchantSn", merchantSn);
            params.put("amount", amount);
            params.put("notifyUrl", notifyUrl);
            params.put("remark", remark);
            params.put("channel", channel);
            params.put("time", time);
            params.put("signType", signType);
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"req order data" + params);

            String sign1 = SignUtil.getMD5Sign(params, sysUser.getMerchantMy());
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"req order sign-" + sign + "-" + sign1);
            if (!sign.equals(sign1)) {
                //TODO:  三方签名测试
                return ApiMessage.error("签名错误!");
            }
            //判断商户订单号是否已存在
            Map<String, Object> p1 = new HashMap<>();
            p1.put("merchantSn", merchantSn);
            if (payOrderService.querySingle1(p1) != null) {
                return ApiMessage.error("商户订单号已存在!");
            }

            //商户没匹配到上游 不记录单.
            boolean isOpen = ChannelManager1.getInstance().merchantOpen(sysUser.getUsername(), channelId, amount);
            if (!isOpen) {
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"od:" + merchantNo + "-" + merchantSn + " 商户通道未开启或通道未匹配");
                return ApiMessage.error("商户通道未开启!");
            }


            //生成订单编号
            String sn = SnUtil.createSn(ApplicationData.getInstance().getConfig().getSnpre());

            return orderPayInner(sn, merchantSn, channel, merchantNo, channelId, amount, sysUser, notifyUrl, remark, ct.getName(), request);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiMessage.error("Create Order Fail:" + e.getMessage());
        }
    }
    
    @PostMapping({"/mqResponseNotify"})
    @ResponseBody
    public Message mqResponseNotifyPost(HttpServletRequest request) {
        try {
            String data = request.getParameter(TagConstants.BODY_ACTION);
            Integer len = Integer.valueOf(data.length());
            if (len.intValue() > 0 && len.intValue() < 2048) {
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "mqResponseNotify-订单回调请求发送成功：\r\n" + data);
            }
        } catch (Exception e) {
        }
        return Message.success(ApiMessageCode.MSG_SUCCESS);
    }

    public ApiMessage orderPayInner(String sn, String merchantSn, String channel, String merchantNo, Long channelId, BigDecimal amount, SysUser sysUser, String notifyUrl, String remark, String ctName, HttpServletRequest request) {

        //生成订单信息
        PayOrder order = new PayOrder();
        order.setSn(sn);
        order.setChannelTypeId(channelId);
        order.setAmount(amount);
        order.setNotifyUrl(notifyUrl);
        order.setRemark(remark);
        order.setTname(ctName);


        order.setMerchantId(sysUser.getId());
        order.setMerchantNo(sysUser.getUsername());
        order.setMerchantChannelId(0L);
        order.setMerchantSn(merchantSn);
        order.setMoney(amount);

        order.setClientId(0L);
        order.setClientNo("");
        order.setClientChannelId(0L);
        order.setClientInfo("");
        order.setClientSn("");


        order.setNotifyStatus(0);//通知状态，未通知
        order.setNotifyUrl(notifyUrl);
        order.setOrderStatus(-2);//订单状态，未支付 未获取链接
        order.setPayTime(null);
        String localPage = "";
        if (ApplicationData.getInstance().getConfig().getServermodel() == 1) {
            localPage = ApplicationData.getInstance().getConfig().getNotifyurl() + "/api/pay/orderPage/" + sn;
        } else {
            localPage = ApplicationData.getInstance().getConfig().getPayClientUrl() + "/api/pay/orderPage/" + sn;
        }
        order.setProductName("Four");
        order.setOrderType(0);


        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("channelTypeId", channelId);
        params.put("merchantId", sysUser.getId());
        //获取需要修改的记录数据
        PayMerchantChannel payMerchantChannel = payMerchantChannelService.querySingleByMerchantId(params);

        if (payMerchantChannel == null) {
            order.setPayUrl("");
            payOrderService.addSave(order);
            return ApiMessage.error("商户通道不存在！");
        }


        ApiMessage apiMessage=null;
        String retstr="未获取到支付链接";
        if(payMerchantChannel.getUseChannelPayurl()==0){
            payMerchantChannelService.querySingle(params);

            String userAgent = request.getHeader("User-Agent");
            if(userAgent==null){
                userAgent="";
            }
            String serverName = request.getAttribute("serverName").toString();
            order.setPayUrl("");
           
            payOrderService.addSave(order);
            apiMessage=orderPageInner(sn,userAgent,serverName,CommonUtil.getIp(request));
            if (apiMessage.getCode() == 200) {
                retstr = (String)apiMessage.getData();
            }else{
                
                 return ApiMessage.error(retstr + " : "+(String)apiMessage.getMsg());
            }
           
           
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"orderPayInner 上游落地页: " + retstr + " 订单ID：" + sn);

        }else{
            order.setPayUrl(localPage);
            payOrderService.addSave(order);
            retstr = localPage;
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"orderPayInner 本地落地页: " + localPage + " 订单ID：" + sn);
            
        }

        
        //返回跳转链接
        PayLogManager.getInstance().createPayLogByOrderRequest(sn, "商户订单号创建: 1 " + merchantSn + " 通道: " + channel, "", "", null, null, sysUser.getId(), null);

        //返回订单数据
        Map<String, Object> data = new TreeMap<String, Object>();
        data.put("merchantNo", merchantNo);
        data.put("merchantSn", merchantSn);
        data.put("sn", sn);
        data.put("payUrl", retstr);
        data.put("sign", SignUtil.getMD5Sign(data, sysUser.getMerchantMy()));

        return ApiMessage.success(ApiMessageCode.MSG_SUCCESS, data);
    }


    @RequestMapping("/orderPage/{orderId}")
    public String orderPage(@PathVariable String orderId, Model model, HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        String serverName = request.getAttribute("serverName").toString();
        String clientIp = CommonUtil.getIp(request);
        ApiMessage apiMessage = orderPageInner(orderId, userAgent, serverName, clientIp);
        if (apiMessage.getCode() == 200) {
            model.addAttribute("payUrl", apiMessage.getData());
        } else {
            model.addAttribute("error", apiMessage.getMsg());
        }
        return "admin/order/form";
    }


    @PostMapping("/orderPageRemote")
    public @ResponseBody
    ApiMessage orderPageRemote(String verifyStr, String orderId, String time, String agent, String serverName, String clientIp) {
        if (verifyStr != null && verifyStr.equals("Gkks@11@skd=HSQWoiL^2")) {
            return orderPageInner(orderId, agent, serverName, clientIp);
        }
        return ApiMessage.error("信息错误!");
    }

    private ApiMessage orderPageInner(String orderId, String agent, String serverName, String clientIp) {

        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"orderPageInner start: " + orderId + " " + System.currentTimeMillis() + " " + clientIp);

        ApiMessage apiMessage = new ApiMessage();
        Map<String, Object> param = new HashMap<>();
        param.put("sn", orderId);
        PayOrder payOrder = payOrderService.querySingle(param);
        if (payOrder == null) {
            apiMessage.setCode(-1);
            apiMessage.setMsg("订单信息错误");
            return apiMessage;
        }
//        if (payOrder.getOrderStatus() == -2 || (payOrder.getPayUrlCount() != null && payOrder.getPayUrlCount() < 3)) {
        if (payOrder.getOrderStatus() == -2) {
            //Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36
            //来源: 1 ios  2 安卓 3 其它
            Integer deviceType = checkAgent(agent);

            payOrder.setPlatform(deviceType);
            payOrderService.updateOrderPlat(payOrder);

            //锁单开始  -2 状态的单,才需要处理
            if (PayOrderRequestCacheManager.getInstance().exists(orderId)) {
                apiMessage.setCode(408);
                apiMessage.setMsg("未获取到支付链接,请稍候重试1--" + orderId);
                return apiMessage;
            }

            PayOrderRequestCacheManager.getInstance().addSn(orderId);

            SysUser sysUser = sysUserService.querySysUserById(payOrder.getMerchantId());
            
            PayOrder orderResult = null;
            try {
                orderResult = orderPayNormal(payOrder, payOrder.getMerchantSn(), payOrder.getMerchantNo(), payOrder.getChannelTypeId(), payOrder.getAmount(),sysUser, deviceType, serverName, clientIp);
            } catch (Exception e) {
                e.printStackTrace();
            }

            PayOrderRequestCacheManager.getInstance().removeSn(orderId);
            //锁单结束

            if (orderResult == null) {
                payOrder.setPayUrlCount(payOrder.getPayUrlCount() + 1);
                payOrder.setOrderStatus(-1);//订单状态，未获取到链接
                payOrderService.updateOrderStatus(payOrder);

                apiMessage.setCode(408);
                apiMessage.setMsg("未获取到支付链接,请重试2--" + orderId);
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"orderPageInner <未获取到支付链接> 订单：" + orderId + " 通道：" + payOrder.getClientChannelId());
                return apiMessage;
            } else {
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"orderPageInner <取到支付链接> 订单：" + orderId + " 通道：" + payOrder.getClientChannelId() + " 链接：" + orderResult.getPayUrl());
                payOrder.setPayUrl(orderResult.getPayUrl());
            }

        } else if (payOrder.getOrderStatus() == -1) {
            //落地页超时再刷新出现超时
            apiMessage.setCode(-1);
            apiMessage.setMsg("订单已超时--" + orderId);
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"orderPageInner <订单已超时> 订单：" + orderId + " 通道：" + payOrder.getClientChannelId());
            return apiMessage;
        }

        apiMessage.setCode(200);
        apiMessage.setMsg("获取支付地址成功--" + orderId);
        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"orderPageInner <获取支付地址成功> 订单：" + orderId + " 通道：" + payOrder.getClientChannelId() + " 订单状态: " + payOrder.getOrderStatus());
        apiMessage.setData(payOrder.getPayUrl());
        return apiMessage;
    }


    /**
     * 普通订单支付
     */
    public PayOrder orderPayNormal(PayOrder payOrder, String merchantSn, String merchantNo,Long channelId, BigDecimal amount, SysUser sysUser, Integer platform, String serverName, String clientIp) {
//        发送订单请求
        PayLogManager.getInstance().createPayLogByOrderRequest(payOrder.getSn(), "开始请求,商户订单号 2 :" + merchantSn + " 平台:" + platform, "", "", null, null, sysUser.getId(), null);
        //分配通道，判断费率倒挂
        
        OrderInfo oi = ChannelManager1.getInstance().createPayOrder(merchantNo, channelId, payOrder.getSn(), amount, serverName, sysUser.getId(), platform, payOrder.getRemark(), clientIp,payMerchantChannelService,payClientChannelService);
        if (oi != null) {
            //form转发
            if (oi.getGtype() != null && oi.getGtype().intValue() == 1) {
                oi.setPayUrl(serverName + oi.getPayUrl() + payOrder.getSn());
            }

            payOrder.setClientId(oi.getClientId());
            payOrder.setClientNo(oi.getClientNo());
            payOrder.setClientChannelId(oi.getClientChannelId());
            payOrder.setClientInfo(oi.getData());
            payOrder.setClientSn(oi.getSn());
            payOrder.setMerchantChannelId(oi.getMerchantChannelId());

            payOrder.setPayUrlCount(payOrder.getPayUrlCount() + 1);
            payOrder.setOrderStatus(0);//订单状态，获取到链接, 未支付

            payOrder.setPayUrl(oi.getPayUrl());

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", oi.getClientChannelId());
            PayClientChannel clientChannel = payClientChannelService.querySingle(params);

            params.put("id", oi.getMerchantChannelId());
            PayMerchantChannel merchantChannel = payMerchantChannelService.querySingle(params);

            payOrder.setClientRatio(clientChannel.getRatio());
            payOrder.setProxyRatio(merchantChannel.getProxyRatio());
            payOrder.setMerchantRatio(merchantChannel.getMerchantRatio());


            payOrderService.updateOrderSuccess(payOrder);

            PayLogManager.getInstance().createPayLogByOrderRequest(payOrder.getSn(), "商户" + merchantNo + "下单成功 4", "", "", payOrder.getClientId(), payOrder.getClientChannelId(), payOrder.getMerchantId(), payOrder.getMerchantChannelId());

            return payOrder;
        }
        return null;
    }


    /**
     * 通知接口
     */
    @RequestMapping("/notify/{clientId}")
    public @ResponseBody
    String notify(@PathVariable Long clientId, @RequestParam Map<String, Object> param, @RequestBody(required = false) String body, HttpServletRequest request) {
        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"notify_param:" + new Gson().toJson(param) + "..." + new Gson().toJson(body) + "--" + clientId);
        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }

        //TODO: 三方回调测试
//        ClientTest tianHongPayClientTest = new WangYiPayClientTest();
//        Object helloasd = tianHongPayClientTest.notifyData(param, "LN20210718210222100757ed", "bacf078a7412f1744fb1bd11804c5fab", null, body);

        PayLogManager.getInstance().createPayLogByClientNotify("", "上游回调通知到达:" + remoteAddr, new Gson().toJson(param) + body, "", null, clientId, null, null);

        if (clientId == null) {
            return "fail1";
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", clientId);
            PayClientChannel pcc = payClientChannelService.querySingle(params);
            if (pcc == null) return "fail2";
//            ClientChannel clientChannel = ChannelManager.channels.get(pcc.getKeyname());
//            if(clientChannel==null) return "fail";
//            ClientChannel clientChannel = new AkPayClientChannel();


            SysUser client = sysUserService.querySysUserById(pcc.getClientId());
            if (client == null) {
                return "fail3";
            }
            //验证IP是否合法
            if (StringUtils.isNotBlank(client.getMerchantIp())) {

                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"IP================:" + remoteAddr);
                String mip = "," + client.getMerchantIp() + ",";

                if (mip.indexOf("," + remoteAddr + ",") == -1) {
                    return "fail4";
                }
            }

            Map<String, Object> channelParams = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(pcc.getParams())) {
                try {
                    channelParams = new Gson().fromJson(pcc.getParams(), new TypeToken<HashMap<String, Object>>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //new
            String keyname = pcc.getKeyname();

            Map<String, Object> clientParam = new HashMap<String, Object>();
            clientParam.put("keyname", keyname);
            PayClientModel clientModel = payClientModelService.querySingle(clientParam);
            if (clientModel == null) {
                return "fail5";
            }

            GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(keyname);
            if (gs == null || gs.getNotifyData() == null) {
                return "fail6";
            }

            Binding binding = new Binding();
            
            binding.setProperty("params", param);
            binding.setProperty("merchantNo", client.getMerchantNo());
            binding.setProperty("merchantMy", client.getMerchantMy());
            binding.setProperty("channelParams", channelParams);
            binding.setProperty("body", body);

            Object data = GroovyUtil.runScript(gs.getNotifyData(), binding);
            if (data == null) {
                return "fail7";
            }


            NotifyInfo ni = (NotifyInfo) data;
            //------new end

            // 处理通知请求
//            NotifyInfo ni = clientChannel.notify(param,client.getMerchantNo(),client.getMerchantMy(),channelParams);

//            if (ni == null) {
//                return "fail";
//            }
            //根据订单号查询订单
            Map<String, Object> param1 = new HashMap<String, Object>();
            param1.put("sn", ni.getSn());
            PayOrder po = payOrderService.querySingle1(param1);
            if (po == null) {
                return "fail8";
            }
            if (po.getOrderStatus() != null && po.getOrderStatus().intValue() == 1) {//订单已处理
//                return clientChannel.getNotifySuccess();
                return clientModel.getNotifystr();
            }
            //kahn 判断金额是否相等
            if(ni.getVerifyAmount()==1){
                if(po.getAmount().compareTo(ni.getActualAmount())!=0){
                    System.out.println("实际金额 与 订单金额不相等1...." + po.getAmount() + " : " + ni.getActualAmount());
                    return "faildx";
                }
            }
            //更新订单状态等信息
            po.setAmount(po.getAmount());
            po.setMoney(po.getMoney());

            po.setPayTime(ni.getPayTime());
            if ("success".equals(ni.getStatus())) {
                if (ApplicationData.getInstance().isNeedOutOrder(po)) {//如果需要掉单
                    return clientModel.getNotifystr();
                }
                //如果开启强验证模式
                if (ApplicationData.getInstance().getConfig().getOrdervalid().intValue() == 1) {
                    Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"订单强验证模式....");
                    //查询订单
                    boolean flag = queryOrderInfo(pcc, client, channelParams, po);
                    Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"订单强验证模式，验证结果:" + flag);
                    if (flag) {
                        po.setOrderStatus(1);
                    } else {
                        po.setOrderStatus(0);
                    }
                } else {
                    po.setOrderStatus(1);
                }

                if (po.getOrderStatus() == 1) {
                    po.setClientId(pcc.getClientId());
                    po.setClientChannelId(pcc.getId());
                    po.setClientNo(pcc.getName());
                    po.setTname(pcc.getName());
                }
            } else {
                po.setOrderStatus(0);
            }

            payOrderService.updateOrderPayStatus(po);

            PayLogManager.getInstance().createPayLogByClientNotify(po.getSn(), "上游回调通知处理成功", new Gson().toJson(param), "", pcc.getClientId(), pcc.getId(), null, null);

            if (po.getOrderStatus().intValue() == 0) return "fail9";

            yajinService.addMoney(po);

            return clientModel.getNotifystr();
        } catch (Exception e) {
            e.printStackTrace();
            return "fail10";
        }
    }

    //    @RequestMapping("/notify_json/{clientId}")
//    public @ResponseBody
    String notifyJson(@PathVariable Long clientId, @RequestBody String data, HttpServletRequest request, @RequestBody(required = false) String body) {
        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"notify_param:" + data + "--" + clientId);

        if (data.indexOf("{") == -1) {
            try {
                data = URLDecoder.decode(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
                return "fail";
            }
        }

        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }

        PayLogManager.getInstance().createPayLogByClientNotify("", "上游回调通知到达,json形式:" + remoteAddr, data, "", null, clientId, null, null);

        if (clientId == null) {
            return "fail";
        }
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            param = new Gson().fromJson(data, new TypeToken<HashMap<String, Object>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", clientId);
            PayClientChannel pcc = payClientChannelService.querySingle(params);
            if (pcc == null) return "fail";
//            ClientChannel clientChannel = ChannelManager.channels.get(pcc.getKeyname());
//            if(clientChannel==null) return "fail";
//            ClientChannel clientChannel = new AkPayClientChannel();

            SysUser client = sysUserService.querySysUserById(pcc.getClientId());
            if (client == null) {
                return "fail";
            }

            //验证IP是否合法
            if (StringUtils.isNotBlank(client.getMerchantIp())) {
//                String remoteAddr = request.getHeader("X-FORWARDED-FOR");
//                if(remoteAddr == null || "".equals(remoteAddr)){
//                    remoteAddr = request.getRemoteAddr();
//                }
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"IP================:" + remoteAddr);
                String mip = "," + client.getMerchantIp() + ",";

                if (mip.indexOf("," + remoteAddr + ",") == -1) {
                    return "fail.";
                }
            }

            Map<String, Object> channelParams = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(pcc.getParams())) {
                try {
                    channelParams = new Gson().fromJson(pcc.getParams(), new TypeToken<HashMap<String, Object>>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //new
            String keyname = pcc.getKeyname();

            Map<String, Object> clientParam = new HashMap<String, Object>();
            clientParam.put("keyname", keyname);
            PayClientModel clientModel = payClientModelService.querySingle(clientParam);
            if (clientModel == null) {
                return "fail";
            }

            GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(keyname);
            if (gs == null || gs.getNotifyData() == null) {
                return "fail";
            }

            Binding binding = new Binding();
            binding.setProperty("params", param);
            binding.setProperty("merchantNo", client.getMerchantNo());
            binding.setProperty("merchantMy", client.getMerchantMy());
            binding.setProperty("channelParams", channelParams);
            binding.setProperty("body", body);

            Object obj = GroovyUtil.runScript(gs.getNotifyData(), binding);
            if (data == null) {
                return "fail";
            }
            NotifyInfo ni = (NotifyInfo) obj;
            // 处理通知请求
            //根据订单号查询订单
            Map<String, Object> param1 = new HashMap<String, Object>();
            param1.put("sn", ni.getSn());
            PayOrder po = payOrderService.querySingle1(param1);
            if (po == null) {
                return "fail";
            } 
            if (po.getOrderStatus() != null && po.getOrderStatus().intValue() == 1) {//订单已处理
                return clientModel.getNotifystr();
            }
            //kahn 判断金额是否相等
            if(ni.getVerifyAmount()==1){
                if(po.getAmount().compareTo(ni.getActualAmount())!=0){
                    System.out.println("实际金额 与 订单金额不相等2...." + po.getAmount() + " : " + ni.getActualAmount());
                    return "faildx";
                }
            }
            //更新订单状态等信息
            po.setAmount(po.getAmount());
            po.setMoney(po.getMoney());

            po.setPayTime(ni.getPayTime());
            if ("success".equals(ni.getStatus())) {
                if (ApplicationData.getInstance().isNeedOutOrder(po)) {//如果需要掉单
                    return clientModel.getNotifystr();
                }
                //如果开启强验证模式
                if (ApplicationData.getInstance().getConfig().getOrdervalid().intValue() == 1) {
                    //查询订单
                    boolean flag = queryOrderInfo(pcc, client, channelParams, po);
                    if (flag) {
                        po.setOrderStatus(1);
                    } else {
                        po.setOrderStatus(0);
                    }
                } else {
                    po.setOrderStatus(1);
                }
            } else {
                po.setOrderStatus(0);
            }

            payOrderService.updateOrderPayStatus(po);

            PayLogManager.getInstance().createPayLogByClientNotify(po.getSn(), "上游回调通知处理成功", new Gson().toJson(param), "", null, clientId, null, null);

//            return clientChannel.getNotifySuccess();
            if (po.getOrderStatus().intValue() == 0) {
                return "fail";
            }

            return clientModel.getNotifystr();
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping("/success")
    public String success(HttpServletRequest request) {
        return "/admin/success";
    }

    @RequestMapping("/error")
    public String error(HttpServletRequest request) {
        return "/admin/error";
    }

    /**
     * 表单请求转发
     *
     * @param sn
     * @param request
     * @return
     * @author:nb
     */
//shanji notify_server version
 @RequestMapping("/formRequest")
    public String formRequest(String sn, HttpServletRequest request) {
        if (StringUtils.isBlank(sn)) {
            request.setAttribute("data", "error");
            return "/admin/form";
        }
        try {
            //根据订单号查询订单
            Map<String, Object> param1 = new HashMap<String, Object>();
            param1.put("sn", sn);
            PayOrder po = payOrderService.querySingle(param1);
            if (po == null) {
                request.setAttribute("data", "error");
                return "/admin/form";
            }
            if (po.getClientInfo() == null) {
                request.setAttribute("data", "error!");
                return "/admin/form";
            }

            request.setAttribute("data", po.getClientInfo());
            return "/admin/form";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("data", "error");
            return "/admin/form";
        }
    }

//    @RequestMapping("/formRequest")
//    public String formRequest(String sn, HttpServletRequest request) {
//        if (StringUtils.isBlank(sn)) {
//            request.setAttribute("data", "error");
//            return "/admin/form";
//        }
//        try {
//            //根据订单号查询订单
//            Map<String, Object> param1 = new HashMap<String, Object>();
//            param1.put("sn", sn);
//            PayOrder po = payOrderService.querySingle(param1);
//            if (po == null) {
//                request.setAttribute("data", "error");
//                return "/admin/form";
//            }
//            if (po.getOrderStatus() != null && po.getOrderStatus().intValue() != 0) {
//                request.setAttribute("data", "error!");
//                return "/admin/form";
//            }
//
////            if(StringUtils.isNotBlank(po.getClientSn())) {
////                request.setAttribute("data","error!");
////                return "/admin/form";
////            }
////
////            Map < String, Object > params=new HashMap<String,Object>();
////            params.put("id", po.getClientChannelId());
////            PayClientChannel pcc=payClientChannelService.querySingle(params);
////            if(pcc==null) {
////                request.setAttribute("data","error");
////                return "/admin/form";
////            }
////            //获取商户对象
////            SysUser merchant=sysUserService.querySysUserById(po.getMerchantId());
////            if(merchant==null) {
////                request.setAttribute("data","error");
////                return "/admin/form";
////            }
////
////            ClientChannel clientChannel = ChannelManager.getInstance().getClientChannelByMerchant(merchant.getUsername(),po.getChannelTypeId(),po.getClientChannelId());
////            if(clientChannel==null) {
////                request.setAttribute("data","error");
////                return "/admin/form";
////            }
////
////            String data=clientChannel.getFormBody(sn, po.getMoney(), request.getAttribute("serverName").toString(), po.getChannelTypeId());
//            request.setAttribute("data", po.getClientInfo());
//            return "/admin/form";
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("data", "error");
//            return "/admin/form";
//        }
//    }

    /**
     * 测试专用
     *
     * @param sn
     * @param request
     * @return
     * @author:nb
     */
//    @RequestMapping("/formRequest_test")
//    public String formRequest_test(String sn, HttpServletRequest request) {
//        if (StringUtils.isBlank(sn)) {
//            request.setAttribute("data", "error");
//            return "/admin/form";
//        }
//        try {
//            //根据订单号查询订单
//            Map<String, Object> param1 = new HashMap<String, Object>();
//            param1.put("sn", sn);
//            PayOrder po = payOrderService.querySingle(param1);
//            if (po == null) {
//                request.setAttribute("data", "error");
//                return "/admin/form";
//            }
//            if (po.getOrderStatus() != null && po.getOrderStatus().intValue() != 0) {
//                request.setAttribute("data", "error!");
//                return "/admin/form";
//            }
//
//            if (StringUtils.isNotBlank(po.getClientSn())) {
//                request.setAttribute("data", "error!");
//                return "/admin/form";
//            }
//
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("id", po.getClientChannelId());
//            PayClientChannel pcc = payClientChannelService.querySingle(params);
//            if (pcc == null) {
//                request.setAttribute("data", "error");
//                return "/admin/form";
//            }
//            //获取商户对象
//            SysUser merchant = sysUserService.querySysUserById(po.getMerchantId());
//            if (merchant == null) {
//                request.setAttribute("data", "error");
//                return "/admin/form";
//            }
//            //只有test_merchant可以使用此功能
//            if (!"test_merchant".equals(merchant.getUsername())) {
//                request.setAttribute("data", "invalid merchant");
//                return "/admin/form";
//            }
//            SysUser client = sysUserService.querySysUserById(po.getClientId());
//            if (client == null) {
//                request.setAttribute("data", "error");
//                return "/admin/form";
//            }
//
//            ClientChannel clientChannel = ChannelManager.channels.get(pcc.getKeyname());
//            if (clientChannel == null) {
//                request.setAttribute("data", "error");
//                return "/admin/form";
//            }
//
//            Map<String, Object> channelParams = new HashMap<String, Object>();
//            try {
//                channelParams = new Gson().fromJson(pcc.getParams(), new TypeToken<HashMap<String, Object>>() {
//                }.getType());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
////            String data = clientChannel.getFormBody_test(pcc.getId(), sn, po.getMoney(), request.getAttribute("serverName").toString(),
////                    po.getChannelTypeId().intValue(), client.getMerchantNo(), client.getMerchantMy(), channelParams, client.getUrlpay());
////            request.setAttribute("data", data);
//            return "/admin/form";
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("data", "error");
//            return "/admin/form";
//        }
//    }

    /**
     * @param sn
     * @param merchantSn
     * @param signType
     * @param time
     * @param sign
     * @param request
     * @return
     * @author:nb
     */
    @PostMapping("/orderInfo")
    public @ResponseBody
    ApiMessage orderInfo(@RequestParam(required = false) String sn, String merchantSn, String signType, Long time, String sign, HttpServletRequest request) {
        try {
//            if(StringUtils.isBlank(sn)) {
//                return ApiMessage.error("平台订单号不能为空!");
//            }
            if (StringUtils.isBlank(merchantSn)) {
                return ApiMessage.error("商户订单号不能为空!");
            }
            if (time == null) {
                return ApiMessage.error("时间戳不能为空!");
            }
            if (StringUtils.isBlank(signType) || !"md5".equals(signType)) {
                return ApiMessage.error("签名方式不合法!");
            }
            if (StringUtils.isBlank(sign)) {
                return ApiMessage.error("签名不能为空!");
            }
            //获取订单
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("sn", sn);
            params.put("merchantSn", merchantSn);
            PayOrder po = payOrderService.queryOrderInfo(params);
            if (po == null) {
                return ApiMessage.error("订单不存在!");
            }
//            if(!merchantSn.equals(po.getMerchantSn())) {
//                return ApiMessage.error("商户订单号有误!");
//            }

            //获取商户
            SysUser sysUser = sysUserService.querySysUserById(po.getMerchantId());
            if (sysUser == null || sysUser.getType() == null || sysUser.getType().intValue() != 2) {
                return ApiMessage.error("商户不存在!");
            }

            //判断用户IP是否合法
            if (StringUtils.isNotBlank(sysUser.getApiip())) {
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"orderinfo begin:" + merchantSn + "-" + sysUser.getApiip());
                String[] ips = sysUser.getApiip().split(",");
                String uip = CommonUtil.getIp(request);
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"orderinfo user:" + merchantSn + "-" + uip);
                boolean flag = false;
                for (int i = 0; i < ips.length; i++) {
                    Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"orderinfo sub:" + merchantSn + "-" + ips[i]);
                    if (ips[i].equals(uip)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return ApiMessage.error("IP禁止访问");
                }
            }

            //验证签名
            Map<String, Object> data = new TreeMap<String, Object>();
            data.put("sn", sn);
            data.put("merchantSn", merchantSn);
            data.put("signType", signType);
            data.put("time", time);
            String sign1 = SignUtil.getMD5Sign(data, sysUser.getMerchantMy());
            if (!sign.equals(sign1)) {
                return ApiMessage.error("签名错误!");
            }

            //返回数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, Object> result = new TreeMap<String, Object>();
            result.put("merchantNo", sysUser.getUsername());
            result.put("sn", po.getSn());
            result.put("merchantSn", merchantSn);
            result.put("orderStatus", po.getOrderStatus() == null ? 0 : po.getOrderStatus());
            result.put("notifyStatus", po.getDealStatus() == null ? 0 : po.getDealStatus());
            result.put("payTime", po.getPayTime() == null ? null : sdf.format(po.getPayTime()));
            result.put("createTime", po.getCreateTime() == null ? null : sdf.format(po.getCreateTime()));

            result.put("money", po.getMoney() + "");
            result.put("amount", po.getAmount() + "");
            result.put("remark", po.getRemark());
            result.put("sign", SignUtil.getMD5Sign(result, sysUser.getMerchantMy()));

            return ApiMessage.success("查询成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiMessage.error("订单查询失败:" + e.getMessage());
        }
    }

    @PostMapping("/merchantBalance")
    @ResponseBody
    public ApiMessage merchantBalance(String merchantNo, String signType, Long time, String sign, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(merchantNo)) {
                return ApiMessage.error("商户号不能为空!");
            }
            if (time == null) {
                return ApiMessage.error("时间戳不能为空!");
            }
            if (StringUtils.isBlank(signType) || !"md5".equals(signType)) {
                return ApiMessage.error("签名方式不合法!");
            }
            if (StringUtils.isBlank(sign)) {
                return ApiMessage.error("签名不能为空!");
            }

            //获取商户
            SysUser merchant = sysUserService.querySysUserByUsername(merchantNo);
            if (merchant == null || merchant.getType() == null || merchant.getType() != 2) {
                return ApiMessage.error("商户不存在!");
            }

            //判断用户IP是否合法
            if (StringUtils.isNotBlank(merchant.getApiip())) {
                String[] ips = merchant.getApiip().split(",");
                String uip = CommonUtil.getIp(request);
                boolean flag = false;
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i].equals(uip)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return ApiMessage.error("IP禁止访问");
                }
            }

            //验证签名
            Map<String, Object> data = new TreeMap<String, Object>();
            data.put("merchantNo", merchantNo);
            data.put("signType", signType);
            data.put("time", time);
            String sign1 = SignUtil.getMD5Sign(data, merchant.getMerchantMy());
            if (!sign.equals(sign1)) {
                return ApiMessage.error("签名错误!");
            }

            BigDecimal balance = BigDecimal.ZERO;
            if (merchant.getMoney() != null && merchant.getFrozenMoney() != null) {
                balance = merchant.getMoney().subtract(merchant.getFrozenMoney());
            }

            Map<String, Object> result = new TreeMap<String, Object>();

            result.put("merchantNo", merchant.getUsername());
            result.put("amount", balance);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            result.put("time", sdf.format(new Date()));
            result.put("sign", SignUtil.getMD5Sign(result, merchant.getMerchantMy()));

            return ApiMessage.success("查询成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiMessage.error("订单查询失败:" + e.getMessage());
        }
    }

    /**
     * 商户提现
     *
     * @param merchantNo
     * @param merchantSn
     * @param amount
     * @param notifyUrl
     * @param bankAccountNo
     * @param bankAccountName
     * @param bankName
     * @param bankCode
     * @param bankNameSub
     * @param bankId
     * @param bankAccountIdNo
     * @param bankAccountMobile
     * @param remark
     * @param time
     * @param signType
     * @param sign
     * @param request
     * @return
     * @author:nb
     */
    @PostMapping("/issued")
    public @ResponseBody
    ApiMessage issued(String merchantNo, String merchantSn, BigDecimal amount, String notifyUrl,
                      String bankAccountNo, String bankAccountName, String bankName, String bankCode,
                      String bankNameSub, String bankId, String bankAccountIdNo, String bankAccountMobile,
                      Integer cashType, String bankIfsc, String bankNation, Integer channelType,
                      String remark, Long time, String signType, String sign, HttpServletRequest request) {
        try {
            String parmStr = new Gson().toJson(request.getParameterMap());
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"issued 1: " + merchantNo + "-" + merchantSn + " " + parmStr);

            //数据验证
            if (StringUtils.isBlank(merchantNo)) {
                return ApiMessage.error("商户编号不能为空!");
            }

            if (StringUtils.isBlank(merchantSn)) {
                return ApiMessage.error("商户订单号不能为空!");
            }
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return ApiMessage.error("交易金额不合法!");
            }
            if (time == null) {
                return ApiMessage.error("时间戳不能为空!");
            }
            if (StringUtils.isBlank(notifyUrl)) {
                return ApiMessage.error("回调地址不能为空!");
            }

            if (StringUtils.isBlank(bankAccountNo)) {
                return ApiMessage.error("提现账号不能为空!");
            }

            //订单号重复请求处理
            if (!SnCacheManager.getInstance().isValidMerchantSn(merchantSn)) {
                return ApiMessage.error("订单号不能重复!");
            }

            Config config = ApplicationData.getInstance().getConfig();
            if (config.getCashMode().intValue() == 0) {//india
                if (cashType == null || (cashType.intValue() != 0 && cashType.intValue() != 1)) {
                    return ApiMessage.error("提现类型不合法!");
                }
                if (cashType == 0) {
                    if (StringUtils.isBlank(bankAccountName)) {
                        return ApiMessage.error("持卡人姓名不能为空!");
                    }
                    if (StringUtils.isBlank(bankName)) {
                        return ApiMessage.error("银行名不能为空!");
                    }
                    if (StringUtils.isBlank(bankIfsc)) {
                        return ApiMessage.error("IFSC不能为空!");
                    }
                    if (StringUtils.isBlank(bankNation)) {
                        return ApiMessage.error("国家不能为空!");
                    }
                }
            } else {//china
                if (StringUtils.isBlank(bankAccountName)) {
                    return ApiMessage.error("持卡人姓名不能为空!");
                }
                if (StringUtils.isBlank(bankName)) {
                    return ApiMessage.error("银行名不能为空!");
                }
                if (StringUtils.isBlank(bankCode)) {
                    return ApiMessage.error("银行编码不能为空!");
                }
            }

            if (StringUtils.isBlank(signType) || !"md5".equals(signType)) {
                return ApiMessage.error("签名方式不合法!");
            }
            if (StringUtils.isBlank(sign)) {
                return ApiMessage.error("签名不能为空!");
            }

            //根据商户编号获取商户对象
            SysUser user = sysUserService.querySysUserByUsername(merchantNo);
            if (user == null || user.getType() == null || user.getType().intValue() != 2) {
                return ApiMessage.error("商户不存在!");
            }

            //判断用户IP是否合法
            if (StringUtils.isNotBlank(user.getApiip())) {
                String[] ips = user.getApiip().split(",");
                String uip = CommonUtil.getIp(request);
                boolean flag = false;
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i].equals(uip)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return ApiMessage.error("IP禁止访问");
                }
            }

            //验证签名
            Map<String, Object> params = new TreeMap<String, Object>();

            params.put("merchantNo", merchantNo);
            params.put("merchantSn", merchantSn);
            params.put("amount", amount);
            params.put("notifyUrl", notifyUrl);

            params.put("bankAccountNo", bankAccountNo);
            params.put("bankAccountName", bankAccountName);
            params.put("bankName", bankName);
            params.put("bankCode", bankCode);

            params.put("bankNameSub", bankNameSub);
            params.put("bankId", bankId);
            params.put("bankAccountIdNo", bankAccountIdNo);
            params.put("bankAccountMobile", bankAccountMobile);

            params.put("bankIfsc", bankIfsc);
            params.put("bankNation", bankNation);
            params.put("cashType", cashType);

            params.put("channelType", channelType);

            params.put("remark", remark);
            params.put("time", time);
            params.put("signType", signType);
            String sign1 = SignUtil.getMD5Sign(params, user.getMerchantMy());
            if (!sign.equals(sign1)) {
//                TODO: 代付签名测试
                return ApiMessage.error("签名错误!");
            }

            //单个银行卡. 每日限制三次
            if(!serverType.equalsIgnoreCase("xiafapay")) {
                Map<String, Object> queryData = new HashMap<String, Object>();
                queryData.put("bankAccountNo", bankAccountNo);
                int count = payCashService.queryCashOrderCountByMerchant(queryData);
                if (count >= 3) {
                    return ApiMessage.error("每日提现不大于三次!");
                }
            }

            //判断商户订单号是否已存在
            Map<String, Object> p1 = new HashMap<String, Object>();
            p1.put("merchantSn", merchantSn);
            if (payCashService.querySingle(p1) != null) {
                return ApiMessage.error("商户订单号已存在!");
            }

            //判断卡信息是否正确
            Map<String, Object> params1 = new HashMap<String, Object>();
            params1.put("code", bankCode);
            PayBankCode cardCode = payBankCodeService.querySingle(params1);
            if (cardCode == null) {
                return ApiMessage.error("银行编码有误!");
            }

//            //生成订单编号
//            String sn=SnUtil.createSn("CA");

            BigDecimal minCommission = user.getMinCommission();
            String cname = "下发";
            if (channelType == null) {
                channelType = 0;
            }
            if (cashType == null) {
                cashType = 0;
            }

            if (channelType.intValue() == 1) {
                minCommission = user.getDfminCommission();
                cname = "代付";
            }

            if (minCommission != null && minCommission.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(minCommission) < 0) {
                return ApiMessage.error("最小" + cname + "金额为" + minCommission + "元");
            }

//            if(user.getMinCommission()!=null && user.getMinCommission().compareTo(BigDecimal.ZERO)>0 && amount.compareTo(user.getMinCommission())<0) {
//                return ApiMessage.error("商户最小提现金额为"+user.getMinCommission()+"元");
//            }

//            BigDecimal commission=BigDecimal.ZERO;
//            if(user.getCashCommission()!=null) commission=user.getCashCommission();

//            BigDecimal proxyCommission=BigDecimal.ZERO;
//            BigDecimal commission=BigDecimal.ZERO;
//            Integer cashMode=0;
//
//            if(cashType.intValue()==0) {//银行卡
//                commission=user.getCashCommission()==null?BigDecimal.ZERO:user.getCashCommission();
//                cashMode=user.getCashMode()==null?0:user.getCashMode();
//                proxyCommission=user.getMcashCommission()==null?BigDecimal.ZERO:user.getMcashCommission();
//            }else if(cashType.intValue()==1) {//paytm账号
//                commission=user.getPtmcashCommission()==null?BigDecimal.ZERO:user.getPtmcashCommission();
//                cashMode=user.getPtmcashMode()==null?0:user.getPtmcashMode();
//                proxyCommission=user.getMptmcashCommission()==null?BigDecimal.ZERO:user.getMptmcashCommission();
//            }
//
//
//            //计算手续费
//            if(cashMode.intValue()==1) {//按百分比
//                commission=amount.multiply(commission).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//                proxyCommission=amount.multiply(proxyCommission).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//            }

            //代理佣金
            BigDecimal proxyCommission = BigDecimal.ZERO;
            //手续费
            BigDecimal commission = BigDecimal.ZERO;

            CashConfig config1 = new CashConfig();
            String cashConfig = user.getCashConfig();
            if (StringUtils.isNotBlank(cashConfig)) {
                try {
                    config1 = new GsonBuilder().serializeNulls().create().fromJson(cashConfig, CashConfig.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //获取费率
            PayRatio ratio = new PayRatio();
            if (channelType.intValue() == 0) {//下发
                if (cashType.intValue() == 0) {//银行卡
                    ratio = config1.getBankRatio();
                } else if (cashType.intValue() == 1) {//upi
                    ratio = config1.getUpiRatio();
                } else {
                    return ApiMessage.error("未知的账号类型");
                }
            } else {//代付
                if (cashType.intValue() == 0) {//银行卡
                    ratio = config1.getDfbankRatio();
                } else if (cashType.intValue() == 1) {//upi
                    ratio = config1.getDfupiRatio();
                } else {
                    return ApiMessage.error("未知的账号类型");
                }
            }
            BigDecimal h = new BigDecimal(100);
            //计算用户手续费
            commission = amount.multiply(ratio.getRatioCommission()).divide(h).add(ratio.getCommission());
            //计算代理佣金
            proxyCommission = amount.multiply(ratio.getProxyRatioCommission()).divide(h).add(ratio.getProxyCommission());

            if (proxyCommission.compareTo(commission) > 0) {
                return ApiMessage.error(cname + "费率配置有误！");
            }

            BigDecimal realMoney = amount;
            //实际扣减金额
            amount = amount.add(commission);


            CashMessage cashMsg = CashUtil.createMerchantCash(sysUserService, merchantNo, amount, merchantSn, proxyCommission, commission, realMoney, bankAccountNo, bankAccountName, bankName, bankNameSub, bankId, bankAccountIdNo, bankAccountMobile, bankCode, bankIfsc, bankNation, cashType, notifyUrl, remark, channelType, payCashService);

//            BigDecimal money1=user.getMoney()==null?BigDecimal.ZERO:user.getMoney();
//            BigDecimal frozenMoney=user.getFrozenMoney()==null?BigDecimal.ZERO:user.getFrozenMoney();
//            if(money1.compareTo(amount)<0 || money1.subtract(frozenMoney).compareTo(amount)<0) {
//                return ApiMessage.error("可提现金额不足！");
//            }

            if (!cashMsg.isStatus()) {
                return ApiMessage.error(cashMsg.getMsg());
            }


            //返回数据
            Map<String, Object> result = new TreeMap<String, Object>();
            result.put("sn", cashMsg.getSn());
            result.put("merchantSn", merchantSn);
            result.put("amount", amount);
            result.put("realMoney", realMoney);
            result.put("fee", commission);

            result.put("sign", SignUtil.getMD5Sign(result, user.getMerchantMy()));
            return ApiMessage.success("创建下发订单成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiMessage.error("创建下发订单失败:" + e.getMessage());
        }
    }


    /**
     * @param merchantSn
     * @param signType
     * @param time
     * @param sign
     * @param request
     */
    @PostMapping("/issuedInfo")
    public @ResponseBody
    ApiMessage issuedInfo(String merchantSn, String signType, Long time, String sign, HttpServletRequest request) {
        try {
//            if(StringUtils.isBlank(sn)) {
//                return ApiMessage.error("平台订单号不能为空!");
//            }
            if (StringUtils.isBlank(merchantSn)) {
                return ApiMessage.error("商户订单号不能为空!");
            }
            if (time == null) {
                return ApiMessage.error("时间戳不能为空!");
            }
            if (StringUtils.isBlank(signType) || !"md5".equals(signType)) {
                return ApiMessage.error("签名方式不合法!");
            }
            if (StringUtils.isBlank(sign)) {
                return ApiMessage.error("签名不能为空!");
            }
            //获取订单
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("merchantSn", merchantSn);
            PayCash pc = payCashService.querySingle(params);
            if (pc == null) {
                return ApiMessage.error("订单不存在!");
            }

            //获取商户
            SysUser merchant = sysUserService.querySysUserById(pc.getMerchantId());
            if (merchant == null || merchant.getType() == null || merchant.getType().intValue() != 2) {
                return ApiMessage.error("商户不存在!");
            }

            //判断用户IP是否合法
            if (StringUtils.isNotBlank(merchant.getApiip())) {
                String[] ips = merchant.getApiip().split(",");
                String uip = CommonUtil.getIp(request);
                boolean flag = false;
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i].equals(uip)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return ApiMessage.error("IP禁止访问");
                }
            }


            //验证签名
            Map<String, Object> data = new TreeMap<String, Object>();
//            data.put("sn", sn);
            data.put("merchantSn", merchantSn);
            data.put("signType", signType);
            data.put("time", time);
            String sign1 = SignUtil.getMD5Sign(data, merchant.getMerchantMy());
            if (!sign.equals(sign1)) {
                return ApiMessage.error("签名错误!");
            }

            //返回数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, Object> result = new TreeMap<String, Object>();

//            result.put("merchantNo",merchant.getUsername());
            result.put("sn", pc.getSn());
            result.put("merchantSn", pc.getMerchantSn());
            result.put("payStatus", pc.getPayStatus() == null ? 0 : pc.getPayStatus());
            result.put("notifyStatus", pc.getNotifyStatus() == null ? 0 : pc.getNotifyStatus());
            result.put("payTime", pc.getPayTime() == null ? null : sdf.format(pc.getPayTime()));
            result.put("createTime", sdf.format(pc.getCreateTime()));
            result.put("amount", pc.getAmount());
            result.put("fee", pc.getCommission());//手续费
            result.put("realMoney", pc.getRealMoney());//实际支付
            result.put("remark", pc.getApiremark());

            result.put("sign", SignUtil.getMD5Sign(result, merchant.getMerchantMy()));

            return ApiMessage.success("查询成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiMessage.error("订单查询失败:" + e.getMessage());
        }
    }

    /**
     * 通知接口
     */
    @RequestMapping("/cashnotify/{keyname}_{clientId}")
    public @ResponseBody
    String cashnotify(@PathVariable String keyname, @PathVariable Long clientId, @RequestParam Map<String, Object> param, HttpServletRequest request, @RequestBody(required = false) String body) {
        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"issued 4 : notify_param:" + new Gson().toJson(param) + "..." + new Gson().toJson(body) + "--" + clientId);
        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }

        PayLogManager.getInstance().createPayLogByClientNotify("", "代付回调通知到达:" + remoteAddr, new Gson().toJson(param) + body, "", null, clientId, null, null);

        if (clientId == null) {
            return "fail1";
        }
        try {

            SysUser client = sysUserService.querySysUserById(clientId);
            if (client == null) {
                return "fail2";
            }
            //验证IP是否合法
            if (StringUtils.isNotBlank(client.getMerchantIp())) {
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"IP================:" + remoteAddr);
                String mip = "," + client.getMerchantIp() + ",";

                if (mip.indexOf("," + remoteAddr + ",") == -1) {
                    return "fail3.";
                }
            }

            //new
//            String keyname = client.getPaykey();

            Map<String, Object> clientParam = new HashMap<String, Object>();
            clientParam.put("keyname", keyname);
            PayClientModel clientModel = payClientModelService.querySingle(clientParam);
            if (clientModel == null) {
                return "fail4";
            }

            //TODO: 代付回调测试
//            ClientTest clientTest = new AnAnDaiFuClientTest();
//            NotifyInfo ni = (NotifyInfo) clientTest.notifyData(param, client.getMerchantNo(), client.getMerchantMy(), new HashMap<>(), body);

            GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(keyname);
            if (gs == null || gs.getNotifyData() == null) {
                return "fail5";
            }

            Binding binding = new Binding();
            binding.setProperty("params", param);
            binding.setProperty("merchantNo", client.getMerchantNo());
            binding.setProperty("merchantMy", client.getMerchantMy());
            binding.setProperty("channelParams", new HashMap<>());
            binding.setProperty("body", body);

            Object data = GroovyUtil.runScript(gs.getNotifyData(), binding);
            if (data == null) {
                return "fail6";
            }

            NotifyInfo ni = (NotifyInfo) data;


            //------new end

            //根据订单号查询订单
            Map<String, Object> param1 = new HashMap<String, Object>();
            param1.put("sn", ni.getSn());

            PayCash cash = payCashService.querySingle(param1);
            if (cash == null) {
                return "fail7";
            }
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"issued 4-2: 代付回调结果 sn: " + cash.getSn());

            cash.setPayTime(new Date());
            Message processMsg = null;
            if ("success".equals(ni.getStatus()) && cash.getPayStatus().intValue() == 4) {
                processMsg = payCashController.cashSuc(cash.getId(), "自动通知成功");
            } else if ("refuse".equals(ni.getStatus())){
                //订单状态：0,待审核。1，失败。2，已拒绝。3，审核通过。4，处理中。5，已支付。
                processMsg = payCashController.cashRefuse(cash.getId(), "驳回");
            }
            else {
                processMsg = payCashController.cashFail(cash.getId(), "自动接收失败通知");
            }
            String desc = processMsg == null ? "" : processMsg.getMsg();
            PayLogManager.getInstance().createPayLogByClientNotify(cash.getSn(), "代付回调通知处理结果 " + desc, new Gson().toJson(param) + new Gson().toJson(body), "", null, clientId, null, null);

            return clientModel.getNotifystr();
        } catch (Exception e) {
            e.printStackTrace();
            return "fail8";
        }
    }

//    @RequestMapping("/rzpay")
//    public String rzpay(String sn, Model model) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("sn", sn);
//        PaytmOrder paytmOrder = paytmOrderService.querySingle(params);
//        if (paytmOrder == null) {
//            model.addAttribute("error", "Sorry,Order does not exist...");
//        } else {
//            model.addAttribute("mid", paytmOrder.getMid());
//            model.addAttribute("orderId", paytmOrder.getTxntoken());
//            model.addAttribute("amount", paytmOrder.getAmount());
//        }
//        return "/admin/test/pay_razorpay";
//    }

//    @RequestMapping("/rznotify")
//    public String rznotify(@RequestParam Map<String, Object> param, HttpServletRequest request) {
//        try {
//            if (param.get("razorpay_order_id") == null || param.get("razorpay_payment_id") == null || param.get("razorpay_signature") == null) {
//                return "admin/error";
//            }
//
//            String orderId = (String) param.get("razorpay_order_id");
//            String paymentId = (String) param.get("razorpay_payment_id");
//            String signature = (String) param.get("razorpay_signature");
//
//            Map<String, Object> param1 = new HashMap<String, Object>();
//            param1.put("clientSn", orderId);
//            PayOrder po = payOrderService.querySingle1(param1);
//            if (po == null) {
//                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"订单不存在");
//                return "/admin/error";
//            }
//            SysUser user = sysUserService.querySysUserById(po.getClientId());
//            if (user == null) {
//                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"用户不存在");
//                return "/admin/error";
//            }
//
//            String sign = RazorpaySignature.calculateRFC2104HMAC(orderId + "|" + paymentId, user.getMerchantMy());
//            if (sign.equals(signature)) {//签名验证成功
//                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"签名验证成功");
//                RazorpayClient razorpayClient = new RazorpayClient(user.getMerchantNo(), user.getMerchantMy());
//                Map<String, String> headers = new HashMap<String, String>();
//                razorpayClient.addHeaders(headers);
//
//                Order order = razorpayClient.Orders.fetch(orderId);
//                String status = order.get("status");
//                if ("paid".equals(status)) {
//                    po.setPayTime(new Date());
//                    po.setOrderStatus(1);
//                    payOrderService.updateOrderPayStatus(po);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "/admin/error";
//        }
//        return "/admin/success";
//    }

    /**
     * 查询订单
     *
     * @param po
     * @return
     */
    public boolean queryOrderInfo(PayClientChannel pcc, SysUser user, Map<String, Object> channelParams, PayOrder po) {
        try {
            String keyname = pcc.getKeyname();
            GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(keyname);
            if (gs == null || gs.getQueryOrderData() == null || gs.getDealQueryOrderData() == null) {
                return true;
            }
            //获取上游通道的处理脚本
            //订单对象，通道对象，上游对象，通道参数
            Binding binding = new Binding();
            binding.setProperty("order", po);
            binding.setProperty("clientChannel", pcc);
            binding.setProperty("client", user);
            binding.setProperty("channelParams", channelParams);

            Object data = GroovyUtil.runScript(gs.getQueryOrderData(), binding);
            if (data == null) {
                return false;
            }
            QueryOrderInfo qoi = (QueryOrderInfo) data;
            if (!qoi.isNeedRequest()) {//不需要发起请求

                return qoi.isOrderSuccess();//订单支付成功

            }

            QueryOrderResult qos = HttpUtil.requestData_QueryOrder(user.getUrlquery(), qoi.getRequestType(), qoi.getParams(), new HashMap<String, String>(), qoi.getCharset(),
                    qoi.getReqType(), gs.getDealQueryOrderData(), po, pcc, user, channelParams);
            return qos.isStatus(); //订单支付成功
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    @RequestMapping("/request/testapi")
//    public @ResponseBody
//    String test(@RequestParam Map<String, Object> param, @RequestBody(required = false) String body, HttpServletRequest request) {
//        String str = "testapi: param: ";
//        str += new Gson().toJson(param);
//        str += " body: ";
//        str += body;
//        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,str);
//        return "test api success";
//    }


    public static Integer checkAgent(String agent) {
        if (agent.contains("iPhone") || agent.contains("iPod") || agent.contains("iPad")) {
            return 1;
        } else if (agent.contains("Android")) {
            return 2;
        } else {
            return 3;
        }
    }


}
