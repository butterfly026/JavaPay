//
//package com.city.city_collector.web.controller;
//
//import java.io.UnsupportedEncodingException;
//import java.math.BigDecimal;
//import java.net.URLDecoder;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.TreeMap;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.city.city_collector.admin.pay.entity.PayBankCard;
//import com.city.city_collector.admin.pay.entity.PayBankCode;
//import com.city.city_collector.admin.pay.entity.PayCash;
//import com.city.city_collector.admin.pay.entity.PayChannelType;
//import com.city.city_collector.admin.pay.entity.PayClientChannel;
//import com.city.city_collector.admin.pay.entity.PayOrder;
//import com.city.city_collector.admin.pay.service.PayBankCardService;
//import com.city.city_collector.admin.pay.service.PayBankCodeService;
//import com.city.city_collector.admin.pay.service.PayCashService;
//import com.city.city_collector.admin.pay.service.PayChannelTypeService;
//import com.city.city_collector.admin.pay.service.PayClientChannelService;
//import com.city.city_collector.admin.pay.service.PayMerchantService;
//import com.city.city_collector.admin.pay.service.PayOrderService;
//import com.city.city_collector.admin.system.entity.SysUser;
//import com.city.city_collector.admin.system.service.SysUserService;
//import com.city.city_collector.channel.ChannelManager;
//import com.city.city_collector.channel.bean.CashNotifyInfo;
//import com.city.city_collector.channel.bean.ClientChannel;
//import com.city.city_collector.channel.bean.NotifyInfo;
//import com.city.city_collector.channel.bean.OrderInfo;
//import com.city.city_collector.channel.util.SignUtil;
//import com.city.city_collector.common.util.ApiMessage;
//import com.city.city_collector.common.util.CommonUtil;
//import com.city.city_collector.common.util.Message;
//import com.city.city_collector.common.util.SnUtil;
//import com.city.city_collector.paylog.PayLogManager;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
///**
// * @author nb
// * @Description:
// */
//@Controller("webPayOrderController1")
//@RequestMapping("/api/pay1")
//public class PayOrderController1 {
//    @Autowired
//    PayOrderService payOrderService;
//
//    @Autowired
//    PayMerchantService payMerchantService;
//
//    @Autowired
//    SysUserService sysUserService;
//
//    @Autowired
//    PayChannelTypeService payChannelTypeService;
//
//    @Autowired
//    PayClientChannelService payClientChannelService;
//
//    @Autowired
//    PayCashService payCashService;
//
////    @Autowired
////    PayBankCardService payBankCardService;
//
//    @Autowired
//    PayBankCodeService payBankCodeService;
//
//    /**
//     * 下单
//     *
//     * @param mercId      商户编号
//     * @param mercOrderId 商户订单编号
//     * @param amount      交易金额
//     * @param notifyUrl   通知URL
//     * @param productName 商品名称
//     * @param remark      订单备注(如果传此值，回调时会原样返回)
//     * @param channel     支付平台(alipay 支付宝扫码
//     *                    alipay_h5 支付宝 h5
//     *                    wechat 微信扫码
//     *                    wechat_h5 微信 h5
//     *                    ysf 云闪付
//     *                    union 银联扫码
//     *                    union_h5 银联 H5
//     *                    bank 网银)
//     * @param time        秒级时间戳
//     * @param signType    签名算法(固定值：md5_v2)
//     * @param sign        MD5校验码
//     * @param request
//     * @return
//     */
//    @PostMapping("/order")
//    public @ResponseBody
//    ApiMessage order(String merchantNo, String merchantSn, BigDecimal amount, String notifyUrl,
//                     String remark, String channel, Long time, String signType, String sign, HttpServletRequest request) {
//        try {
//            //数据验证
//            if (StringUtils.isBlank(merchantNo)) {
//                return ApiMessage.error("商户编号不能为空!");
//            }
//            if (StringUtils.isBlank(merchantSn)) {
//                return ApiMessage.error("订单号不能为空!");
//            }
//            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
//                return ApiMessage.error("交易金额不合法!");
//            }
//            if (time == null) {
//                return ApiMessage.error("时间戳不能为空!");
//            }
//
//            if (StringUtils.isBlank(notifyUrl)) {
//                return ApiMessage.error("回调地址不能为空!");
//            }
////            if(StringUtils.isBlank(productName)) {
////                return ApiMessage.error("商品名不能为空!");
////            }
//            if (StringUtils.isBlank(channel)) {
//                return ApiMessage.error("支付平台不能为空!");
//            }
//
//            PayChannelType ct = payChannelTypeService.queryChannelTypeByCode(channel);
//            if (ct == null) {
//                return ApiMessage.error("支付平台不合法!");
//            }
//            Long channelId = ct.getId();
//            if (StringUtils.isBlank(signType) || !"md5".equals(signType)) {
//                return ApiMessage.error("签名方式不合法!");
//            }
//            if (StringUtils.isBlank(sign)) {
//                return ApiMessage.error("签名不能为空!");
//            }
//
//            //根据商户编号获取商户对象
//            SysUser sysUser = sysUserService.querySysUserByUsername(merchantNo);
//
//            if (sysUser == null || sysUser.getType() == null || sysUser.getType().intValue() != 2) {
//                return ApiMessage.error("商户不存在!");
//            }
//
//            //判断用户IP是否合法
//            if (StringUtils.isNotBlank(sysUser.getApiip())) {
//                String[] ips = sysUser.getApiip().trim().split(",");
//                String uip = CommonUtil.getIp(request);
//                boolean flag = false;
//                for (int i = 0; i < ips.length; i++) {
//                    if (ips[i].equals(uip)) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!flag) {
//                    return ApiMessage.error("IP禁止访问");
//                }
//            }
//
//
//            //验证签名
//            Map<String, Object> params = new TreeMap<String, Object>();
//            params.put("merchantNo", merchantNo);
//            params.put("merchantSn", merchantSn);
//            params.put("amount", amount);
//            params.put("notifyUrl", notifyUrl);
////            params.put("productName", productName);
//            params.put("remark", remark);
//            params.put("channel", channel);
////            params.put("channel_id", channel_id);
////            params.put("channel_param", channel_param);
//            params.put("time", time);
//            params.put("signType", signType);
//            String sign1 = SignUtil.getMD5Sign(params, sysUser.getMerchantMy());
//            if (!sign.equals(sign1)) {
//                return ApiMessage.error("签名错误!");
//            }
//            //判断商户订单号是否已存在
//            Map<String, Object> p1 = new HashMap<String, Object>();
//            p1.put("merchantSn", merchantSn);
//            if (payOrderService.querySingle(p1) != null) {
//                return ApiMessage.error("商户订单号已存在!");
//            }
//
//            //生成订单编号
//            String sn = SnUtil.createSn("");
//
//            //发送订单请求
//            PayLogManager.getInstance().createPayLogByOrderRequest(sn, "开始请求,商户订单号:" + merchantSn + ",通道:" + channel, "", "", null, null, sysUser.getId(), null);
//            OrderInfo oi = ChannelManager.getInstance().createPayOrder(merchantNo, channelId, sn, amount, request.getAttribute("serverName").toString(), sysUser.getId());
//            if (oi != null) {
//                //保存订单信息
//                PayOrder order = new PayOrder();
//
//                order.setAmount(amount);
//                order.setChannelTypeId(channelId);
//                order.setClientId(oi.getClientId());
//                order.setClientNo(oi.getClientNo());
//                order.setClientChannelId(oi.getClientChannelId());
//                order.setClientInfo(oi.getData());
//                order.setClientSn(oi.getSn());
//
//                order.setMerchantId(sysUser.getId());
//                order.setMerchantNo(sysUser.getUsername());
//                order.setMerchantChannelId(oi.getMerchantChannelId());
//                order.setMerchantSn(merchantSn);
//                order.setMoney(amount);
//
//                order.setNotifyStatus(0);//通知状态，未通知
//                order.setNotifyUrl(notifyUrl);
//                order.setOrderStatus(0);//订单状态，未支付
//                order.setPayTime(null);
//                order.setPayUrl(oi.getPayUrl());
//                order.setProductName("预留商品名");
//                order.setRemark(remark);
//                order.setSn(sn);
//
//                order.setTname(ct.getName());
//
//                payOrderService.addSave(order);
//
//                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "商户" + merchantNo + "下单成功", "", "", order.getClientId(), order.getClientChannelId(), order.getMerchantId(), order.getMerchantChannelId());
//                //返回订单数据
//                Map<String, Object> data = new TreeMap<String, Object>();
//                data.put("merchantNo", merchantNo);
//                data.put("merchantSn", merchantSn);
//                data.put("sn", sn);
//                data.put("payUrl", oi.getPayUrl());
//                data.put("sign", SignUtil.getMD5Sign(data, sysUser.getMerchantMy()));
//
//                return ApiMessage.success("success", data);
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiMessage.error("下单失败:" + e.getMessage());
//        }
//        return ApiMessage.error("下单失败");
//    }
//
//    /**
//     * 通知接口
//     *
//     * @param key
//     * @param request
//     * @return
//     * @author:nb
//     */
//    @RequestMapping("/notify/{clientId}")
//    public @ResponseBody
//    String notify(@PathVariable Long clientId, @RequestParam Map<String, Object> param, HttpServletRequest request) {
//        System.out.println("notify_param:" + new Gson().toJson(param) + "--" + clientId);
//        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
//        if (StringUtils.isBlank(remoteAddr)) {
//            remoteAddr = request.getRemoteAddr();
//        }
//
//        PayLogManager.getInstance().createPayLogByClientNotify("", "上游回调通知到达:" + remoteAddr, new Gson().toJson(param), "", null, clientId, null, null);
//
//        if (clientId == null) {
//            return "fail";
//        }
//        try {
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("id", clientId);
//            PayClientChannel pcc = payClientChannelService.querySingle(params);
//            if (pcc == null) return "fail";
//            ClientChannel clientChannel = ChannelManager.channels.get(pcc.getKeyname());
//            if (clientChannel == null) return "fail";
//
//            SysUser client = sysUserService.querySysUserById(pcc.getClientId());
//            if (client == null) {
//                return "fail";
//            }
//            //验证IP是否合法
//            if (StringUtils.isNotBlank(client.getMerchantIp())) {
//
//                System.out.println("IP================:" + remoteAddr);
//                String mip = "," + client.getMerchantIp() + ",";
//
//                if (mip.indexOf("," + remoteAddr + ",") == -1) {
//                    return "fail.";
//                }
//            }
//
//            Map<String, Object> channelParams = new HashMap<String, Object>();
//            if (StringUtils.isNotBlank(pcc.getParams())) {
//                try {
//                    channelParams = new Gson().fromJson(pcc.getParams(), new TypeToken<HashMap<String, Object>>() {
//                    }.getType());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            // 处理通知请求
//            NotifyInfo ni = clientChannel.notify(param, client.getMerchantNo(), client.getMerchantMy(), channelParams);
//            if (ni == null) {
//                return "fail";
//            }
//            //根据订单号查询订单
//            Map<String, Object> param1 = new HashMap<String, Object>();
//            param1.put("sn", ni.getSn());
//            PayOrder po = payOrderService.querySingle(param1);
//            if (po == null) return "fail";
//            if (po.getOrderStatus() != null && po.getOrderStatus().intValue() == 1) {//订单已处理
//                return clientChannel.getNotifySuccess();
//            }
//
//            //更新订单状态等信息
//            po.setAmount(po.getAmount());
//            po.setMoney(po.getMoney());
//
//            po.setPayTime(ni.getPayTime());
//            if ("success".equals(ni.getStatus())) {
//                po.setOrderStatus(1);
//            } else {
//                po.setOrderStatus(0);
//            }
//
//            payOrderService.updateOrderPayStatus(po);
//
//            PayLogManager.getInstance().createPayLogByClientNotify(po.getSn(), "上游回调通知处理成功", new Gson().toJson(param), "", null, clientId, null, null);
//
//            return clientChannel.getNotifySuccess();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "fail";
//        }
//    }
//
//    @RequestMapping("/notify_json/{clientId}")
//    public @ResponseBody
//    String notifyJson(@PathVariable Long clientId, @RequestBody String data, HttpServletRequest request) {
//        System.out.println("notify_param:" + data + "--" + clientId);
//
//        if (data.indexOf("{") == -1) {
//            try {
//                data = URLDecoder.decode(data, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//
//                e.printStackTrace();
//                return "fail";
//            }
//        }
//
//        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
//        if (StringUtils.isBlank(remoteAddr)) {
//            remoteAddr = request.getRemoteAddr();
//        }
//
//        PayLogManager.getInstance().createPayLogByClientNotify("", "上游回调通知到达,json形式:" + remoteAddr, data, "", null, clientId, null, null);
//
//        if (clientId == null) {
//            return "fail";
//        }
//        Map<String, Object> param = new HashMap<String, Object>();
//        try {
//            param = new Gson().fromJson(data, new TypeToken<HashMap<String, Object>>() {
//            }.getType());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "fail";
//        }
//        try {
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("id", clientId);
//            PayClientChannel pcc = payClientChannelService.querySingle(params);
//            if (pcc == null) return "fail";
//            ClientChannel clientChannel = ChannelManager.channels.get(pcc.getKeyname());
//            if (clientChannel == null) return "fail";
//
//            SysUser client = sysUserService.querySysUserById(pcc.getClientId());
//            if (client == null) {
//                return "fail";
//            }
//
//            //验证IP是否合法
//            if (StringUtils.isNotBlank(client.getMerchantIp())) {
////                String remoteAddr = request.getHeader("X-FORWARDED-FOR");
////                if(remoteAddr == null || "".equals(remoteAddr)){
////                    remoteAddr = request.getRemoteAddr();
////                }
//                System.out.println("IP================:" + remoteAddr);
//                String mip = "," + client.getMerchantIp() + ",";
//
//                if (mip.indexOf("," + remoteAddr + ",") == -1) {
//                    return "fail.";
//                }
//            }
//
//            Map<String, Object> channelParams = new HashMap<String, Object>();
//            if (StringUtils.isNotBlank(pcc.getParams())) {
//                try {
//                    channelParams = new Gson().fromJson(pcc.getParams(), new TypeToken<HashMap<String, Object>>() {
//                    }.getType());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            // 处理通知请求
//            NotifyInfo ni = clientChannel.notify(param, client.getMerchantNo(), client.getMerchantMy(), channelParams);
//            if (ni == null) {
//                return "fail";
//            }
//            //根据订单号查询订单
//            Map<String, Object> param1 = new HashMap<String, Object>();
//            param1.put("sn", ni.getSn());
//            PayOrder po = payOrderService.querySingle(param1);
//            if (po == null) return "fail";
//            if (po.getOrderStatus() != null && po.getOrderStatus().intValue() == 1) {//订单已处理
//                return clientChannel.getNotifySuccess();
//            }
//
//            //更新订单状态等信息
//            po.setAmount(po.getAmount());
//            po.setMoney(po.getMoney());
//
//            po.setPayTime(ni.getPayTime());
//            if ("success".equals(ni.getStatus())) {
//                po.setOrderStatus(1);
//            } else {
//                po.setOrderStatus(0);
//            }
//
//            payOrderService.updateOrderPayStatus(po);
//
//            PayLogManager.getInstance().createPayLogByClientNotify(po.getSn(), "上游回调通知处理成功", new Gson().toJson(param), "", null, clientId, null, null);
//
//            return clientChannel.getNotifySuccess();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "fail";
//        }
//    }
//
//    @RequestMapping("/success")
//    public String success(HttpServletRequest request) {
//        return "/admin/success";
//    }
//
//    @RequestMapping("/error")
//    public String error(HttpServletRequest request) {
//        return "/admin/error";
//    }
//
//    /**
//     * 表单请求转发
//     *
//     * @param sn
//     * @param request
//     * @return
//     * @author:nb
//     */
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
//
//            ClientChannel clientChannel = ChannelManager.getInstance().getClientChannelByMerchant(merchant.getUsername(), po.getChannelTypeId(), po.getClientChannelId());
//            if (clientChannel == null) {
//                request.setAttribute("data", "error");
//                return "/admin/form";
//            }
//
//            String data = clientChannel.getFormBody(sn, po.getMoney(), request.getAttribute("serverName").toString(), po.getChannelTypeId());
//            request.setAttribute("data", data);
//            return "/admin/form";
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("data", "error");
//            return "/admin/form";
//        }
//    }
//
//    /**
//     * 测试专用
//     *
//     * @param sn
//     * @param request
//     * @return
//     * @author:nb
//     */
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
//            String data = clientChannel.getFormBody_test(pcc.getId(), sn, po.getMoney(), request.getAttribute("serverName").toString(),
//                    po.getChannelTypeId().intValue(), client.getMerchantNo(), client.getMerchantMy(), channelParams, client.getUrlpay());
//            request.setAttribute("data", data);
//            return "/admin/form";
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("data", "error");
//            return "/admin/form";
//        }
//    }
//
//    /**
//     * @param sn
//     * @param merchantSn
//     * @param signType
//     * @param time
//     * @param sign
//     * @param request
//     * @return
//     * @author:nb
//     */
//    @PostMapping("/orderInfo")
//    public @ResponseBody
//    ApiMessage orderInfo(@RequestParam(required = false) String sn, String merchantSn, String signType, Long time, String sign, HttpServletRequest request) {
//        try {
////            if(StringUtils.isBlank(sn)) {
////                return ApiMessage.error("平台订单号不能为空!");
////            }
//            if (StringUtils.isBlank(merchantSn)) {
//                return ApiMessage.error("商户订单号不能为空!");
//            }
//            if (time == null) {
//                return ApiMessage.error("时间戳不能为空!");
//            }
//            if (StringUtils.isBlank(signType) || !"md5".equals(signType)) {
//                return ApiMessage.error("签名方式不合法!");
//            }
//            if (StringUtils.isBlank(sign)) {
//                return ApiMessage.error("签名不能为空!");
//            }
//            //获取订单
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("sn", sn);
//            params.put("merchantSn", merchantSn);
//            PayOrder po = payOrderService.queryOrderInfo(params);
//            if (po == null) {
//                return ApiMessage.error("订单不存在!");
//            }
////            if(!merchantSn.equals(po.getMerchantSn())) {
////                return ApiMessage.error("商户订单号有误!");
////            }
//
//            //获取商户
//            SysUser sysUser = sysUserService.querySysUserById(po.getMerchantId());
//            if (sysUser == null || sysUser.getType() == null || sysUser.getType().intValue() != 2) {
//                return ApiMessage.error("商户不存在!");
//            }
//
//            //判断用户IP是否合法
//            if (StringUtils.isNotBlank(sysUser.getApiip())) {
//                String[] ips = sysUser.getApiip().split(",");
//                String uip = CommonUtil.getIp(request);
//                boolean flag = false;
//                for (int i = 0; i < ips.length; i++) {
//                    if (ips[i].equals(uip)) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!flag) {
//                    return ApiMessage.error("IP禁止访问");
//                }
//            }
//
//            //验证签名
//            Map<String, Object> data = new TreeMap<String, Object>();
//            data.put("sn", sn);
//            data.put("merchantSn", merchantSn);
//            data.put("signType", signType);
//            data.put("time", time);
//            String sign1 = SignUtil.getMD5Sign(data, sysUser.getMerchantMy());
//            if (!sign.equals(sign1)) {
//                return ApiMessage.error("签名错误!");
//            }
//
//            //返回数据
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Map<String, Object> result = new TreeMap<String, Object>();
//            result.put("merchantNo", sysUser.getUsername());
//            result.put("sn", po.getSn());
//            result.put("merchantSn", merchantSn);
//            result.put("orderStatus", po.getOrderStatus() == null ? 0 : po.getOrderStatus());
//            result.put("notifyStatus", po.getDealStatus() == null ? 0 : po.getDealStatus());
//            result.put("payTime", po.getPayTime() == null ? null : sdf.format(po.getPayTime()));
//            result.put("createTime", po.getCreateTime() == null ? null : sdf.format(po.getCreateTime()));
//
//            result.put("money", po.getMoney());
//            result.put("amount", po.getAmount());
//            result.put("remark", po.getRemark());
//            result.put("sign", SignUtil.getMD5Sign(result, sysUser.getMerchantMy()));
//
//            return ApiMessage.success("查询成功", result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiMessage.error("订单查询失败:" + e.getMessage());
//        }
//    }
//
//    /**
//     * 商户提现
//     *
//     * @param merchantNo
//     * @param merchantSn
//     * @param amount
//     * @param notifyUrl
//     * @param bankAccountNo
//     * @param bankAccountName
//     * @param bankName
//     * @param bankCode
//     * @param bankNameSub
//     * @param bankId
//     * @param bankAccountIdNo
//     * @param bankAccountMobile
//     * @param remark
//     * @param time
//     * @param signType
//     * @param sign
//     * @param request
//     * @return
//     * @author:nb
//     */
//    @PostMapping("/issued")
//    public @ResponseBody
//    ApiMessage issued(String merchantNo, String merchantSn, BigDecimal amount, String notifyUrl,
//                      String bankAccountNo, String bankAccountName, String bankName, String bankCode,
//                      String bankNameSub, String bankId, String bankAccountIdNo, String bankAccountMobile,
//                      String remark, Long time, String signType, String sign, HttpServletRequest request) {
//        try {
//            //数据验证
//            if (StringUtils.isBlank(merchantNo)) {
//                return ApiMessage.error("商户编号不能为空!");
//            }
//            if (StringUtils.isBlank(merchantSn)) {
//                return ApiMessage.error("商户订单号不能为空!");
//            }
//            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
//                return ApiMessage.error("交易金额不合法!");
//            }
//            if (time == null) {
//                return ApiMessage.error("时间戳不能为空!");
//            }
//            if (StringUtils.isBlank(notifyUrl)) {
//                return ApiMessage.error("回调地址不能为空!");
//            }
//
//            if (StringUtils.isBlank(bankAccountNo)) {
//                return ApiMessage.error("银行卡号不能为空!");
//            }
//            if (StringUtils.isBlank(bankAccountName)) {
//                return ApiMessage.error("持卡人姓名不能为空!");
//            }
//            if (StringUtils.isBlank(bankName)) {
//                return ApiMessage.error("银行名不能为空!");
//            }
//            if (StringUtils.isBlank(bankCode)) {
//                return ApiMessage.error("银行编码不能为空!");
//            }
//
//
//            if (StringUtils.isBlank(signType) || !"md5".equals(signType)) {
//                return ApiMessage.error("签名方式不合法!");
//            }
//            if (StringUtils.isBlank(sign)) {
//                return ApiMessage.error("签名不能为空!");
//            }
//
//            //根据商户编号获取商户对象
//            SysUser user = sysUserService.querySysUserByUsername(merchantNo);
//            if (user == null || user.getType() == null || user.getType().intValue() != 2) {
//                return ApiMessage.error("商户不存在!");
//            }
//
//            //判断用户IP是否合法
//            if (StringUtils.isNotBlank(user.getApiip())) {
//                String[] ips = user.getApiip().split(",");
//                String uip = CommonUtil.getIp(request);
//                boolean flag = false;
//                for (int i = 0; i < ips.length; i++) {
//                    if (ips[i].equals(uip)) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!flag) {
//                    return ApiMessage.error("IP禁止访问");
//                }
//            }
//            //验证签名
//            Map<String, Object> params = new TreeMap<String, Object>();
//
//            params.put("merchantNo", merchantNo);
//            params.put("merchantSn", merchantSn);
//            params.put("amount", amount);
//            params.put("notifyUrl", notifyUrl);
//
//            params.put("bankAccountNo", bankAccountNo);
//            params.put("bankAccountName", bankAccountName);
//            params.put("bankName", bankName);
//            params.put("bankCode", bankCode);
//
//            params.put("bankNameSub", bankNameSub);
//            params.put("bankId", bankId);
//            params.put("bankAccountIdNo", bankAccountIdNo);
//            params.put("bankAccountMobile", bankAccountMobile);
//
//            params.put("remark", remark);
//            params.put("time", time);
//            params.put("signType", signType);
//            String sign1 = SignUtil.getMD5Sign(params, user.getMerchantMy());
//            if (!sign.equals(sign1)) {
//                return ApiMessage.error("签名错误!");
//            }
//            //判断商户订单号是否已存在
//            Map<String, Object> p1 = new HashMap<String, Object>();
//            p1.put("merchantSn", merchantSn);
//            if (payCashService.querySingle(p1) != null) {
//                return ApiMessage.error("商户订单号已存在!");
//            }
//
//            //判断卡信息是否正确
//
//            Map<String, Object> params1 = new HashMap<String, Object>();
//            params1.put("code", bankCode);
//            PayBankCode cardCode = payBankCodeService.querySingle(params1);
//            if (cardCode == null) {
//                return ApiMessage.error("银行编码有误!");
//            }
//
////            //生成订单编号
////            String sn=SnUtil.createSn("CA");
//
//            if (user.getMinCommission() != null && user.getMinCommission().compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(user.getMinCommission()) < 0) {
//                return ApiMessage.error("商户最小提现金额为" + user.getMinCommission() + "元");
//            }
//
//            BigDecimal commission = BigDecimal.ZERO;
//            if (user.getCashCommission() != null) commission = user.getCashCommission();
//            BigDecimal realMoney = amount;
//            //实际扣减金额
//            amount = amount.add(commission);
//
//            BigDecimal money1 = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
//            BigDecimal frozenMoney = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
//            if (money1.compareTo(amount) < 0 || money1.subtract(frozenMoney).compareTo(amount) < 0) {
//                return ApiMessage.error("可提现金额不足！");
//            }
//
//
//            //创建提现订单
//            PayCash payCash = new PayCash();
//
//            payCash.setMerchantId(user.getId());
//
//            payCash.setAmount(amount);
//            payCash.setMerchantSn(merchantSn);
//            payCash.setSn(SnUtil.createSn("CA"));
//
//            //API接口生成
//            payCash.setCashType(1);
//
//            payCash.setCommission(commission);
//            payCash.setRealMoney(realMoney);
//
//            payCash.setBankAccno(bankAccountNo);
//            payCash.setBankAccname(bankAccountName);
//            payCash.setBankName(bankName);
//            payCash.setBankSubname(bankNameSub);
//            payCash.setBankId(bankId);
//            payCash.setBankAccid(bankAccountIdNo);
//            payCash.setBankAccmobile(bankAccountMobile);
//
//            payCash.setBankCode(bankCode);
//
//            payCash.setNotifyUrl(notifyUrl);
////            payCash.setNotifyStatus(0);
////            payCash.setRemark(remark);
//            payCash.setApiremark(remark);
//            payCash.setStatus(0);
//            payCash.setPayStatus(0);
//
////            payCashService.addSave(payCash);
//            payCashService.createCash(payCash, user, 1);
//
//            //返回数据
//            Map<String, Object> result = new TreeMap<String, Object>();
//            result.put("sn", payCash.getSn());
//            result.put("merchantSn", merchantSn);
//            result.put("amount", amount);
//            result.put("realMoney", realMoney);
//            result.put("fee", commission);
//
//            result.put("sign", SignUtil.getMD5Sign(result, user.getMerchantMy()));
//            return ApiMessage.success("创建下发订单成功", result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiMessage.error("创建下发订单失败:" + e.getMessage());
//        }
//    }
//
//
//    /**
//     * @param sn
//     * @param merchantSn
//     * @param signType
//     * @param time
//     * @param sign
//     * @param request
//     * @return
//     * @author:nb
//     */
//    @PostMapping("/issuedInfo")
//    public @ResponseBody
//    ApiMessage issuedInfo(String merchantSn, String signType, Long time, String sign, HttpServletRequest request) {
//        try {
////            if(StringUtils.isBlank(sn)) {
////                return ApiMessage.error("平台订单号不能为空!");
////            }
//            if (StringUtils.isBlank(merchantSn)) {
//                return ApiMessage.error("商户订单号不能为空!");
//            }
//            if (time == null) {
//                return ApiMessage.error("时间戳不能为空!");
//            }
//            if (StringUtils.isBlank(signType) || !"md5".equals(signType)) {
//                return ApiMessage.error("签名方式不合法!");
//            }
//            if (StringUtils.isBlank(sign)) {
//                return ApiMessage.error("签名不能为空!");
//            }
//            //获取订单
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("merchantSn", merchantSn);
//            PayCash pc = payCashService.querySingle(params);
//            if (pc == null) {
//                return ApiMessage.error("订单不存在!");
//            }
////            if(!merchantSn.equals(pc.getMerchantSn())) {
////                return ApiMessage.error("商户订单号有误!");
////            }
//
//            //获取商户
//            SysUser merchant = sysUserService.querySysUserById(pc.getMerchantId());
//            if (merchant == null || merchant.getType() == null || merchant.getType().intValue() != 2) {
//                return ApiMessage.error("商户不存在!");
//            }
//
//            //判断用户IP是否合法
//            if (StringUtils.isNotBlank(merchant.getApiip())) {
//                String[] ips = merchant.getApiip().split(",");
//                String uip = CommonUtil.getIp(request);
//                boolean flag = false;
//                for (int i = 0; i < ips.length; i++) {
//                    if (ips[i].equals(uip)) {
//                        flag = true;
//                        break;
//                    }
//                }
//                if (!flag) {
//                    return ApiMessage.error("IP禁止访问");
//                }
//            }
//
//
//            //验证签名
//            Map<String, Object> data = new TreeMap<String, Object>();
////            data.put("sn", sn);
//            data.put("merchantSn", merchantSn);
//            data.put("signType", signType);
//            data.put("time", time);
//            String sign1 = SignUtil.getMD5Sign(data, merchant.getMerchantMy());
//            if (!sign.equals(sign1)) {
//                return ApiMessage.error("签名错误!");
//            }
//
//            //返回数据
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Map<String, Object> result = new TreeMap<String, Object>();
//
////            result.put("merchantNo",merchant.getUsername());
//            result.put("sn", pc.getSn());
//            result.put("merchantSn", pc.getMerchantSn());
//            result.put("payStatus", pc.getPayStatus() == null ? 0 : pc.getPayStatus());
//            result.put("notifyStatus", pc.getNotifyStatus() == null ? 0 : pc.getNotifyStatus());
//            result.put("payTime", pc.getPayTime() == null ? null : sdf.format(pc.getPayTime()));
//            result.put("createTime", sdf.format(pc.getCreateTime()));
//            result.put("amount", pc.getAmount());
//            result.put("fee", pc.getCommission());//手续费
//            result.put("realMoney", pc.getRealMoney());//实际支付
//            result.put("remark", pc.getRemark());
//
//            result.put("sign", SignUtil.getMD5Sign(result, merchant.getMerchantMy()));
//
//            return ApiMessage.success("查询成功", result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiMessage.error("订单查询失败:" + e.getMessage());
//        }
//    }
//
//    /**
//     * 通知接口
//     *
//     * @param key
//     * @param request
//     * @return
//     * @author:nb
//     */
//    @RequestMapping("/cashnotify/{keyname}_{clientId}")
//    public @ResponseBody
//    String cashnotify(@PathVariable String keyname, @PathVariable Long clientId, @RequestParam Map<String, Object> param, HttpServletRequest request) {
////        System.out.println("cashnotify_param:"+new Gson().toJson(param)+"--"+keyname+"--"+clientId);
////        if(StringUtils.isBlank(keyname) || clientId==null) {
////            return "fail";
////        }
////        try {
////            Map < String, Object > params=new HashMap<String,Object>();
//////            params.put("id", clientId);
//////            PayClientChannel pcc=payClientChannelService.querySingle(params);
//////            if(pcc==null) return "fail";
////            ClientChannel clientChannel = ChannelManager.channels.get(keyname);
////            if(clientChannel==null) return "fail";
////            //验证IP是否合法
////            if(StringUtils.isNotBlank(clientChannel.getMerchatnIp())) {
////                String remoteAddr = request.getHeader("X-FORWARDED-FOR");
////                if(remoteAddr == null || "".equals(remoteAddr)){
////                    remoteAddr = request.getRemoteAddr();
////                }
////                System.out.println("IP================:"+remoteAddr);
////                if(clientChannel.getMerchatnIp().indexOf(","+remoteAddr+",")==-1) {
////                    return "fail.";
////                }
////            }
////            SysUser client=sysUserService.querySysUserById(clientId);
////            if(client==null) {
////                return "fail";
////            }
////            // 处理通知请求
////            CashNotifyInfo cni=clientChannel.cashnotify(params, client.getMerchantNo(),client.getMerchantMy());
////            if(!cni.getStatus()) {
////                return "fail";
////            }
//////            NotifyInfo ni = clientChannel.notify(param,client.getMerchantNo(),client.getMerchantMy());
//////            if(ni==null) {
//////                return "fail";
//////            }
////            //根据订单号查询订单
////            Map < String, Object > param1=new HashMap<String,Object>();
////            param1.put("sn", cni.getSn());
////
////            PayCash cash = payCashService.querySingle(param1);
////            if(cash==null) return "fail";
////
////            if(cash.getPayStatus().intValue()!=4) {
////                return "fail";
////            }
////
////            cash.setPayTime(new Date());
////            if(cni.getStatus()) {
////                if(cni.getDealStatus()) {
////                    cash.setPayStatus(5);
////                }else {
////                    cash.setPayStatus(1);
////                }
////                payCashService.updateCashStatus(cash);
////            }
////
////            return clientChannel.getNotifySuccess();
////        }catch(Exception e) {
////            e.printStackTrace();
////            return "fail";
////        }
//        return "fail";
//    }
//}
