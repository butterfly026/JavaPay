//
//package com.city.city_collector.web.controller;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.TreeMap;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.city.city_collector.admin.pay.controller.PayClientChannelController;
//import com.city.city_collector.admin.pay.controller.PaytmClientChannelController;
//import com.city.city_collector.admin.pay.entity.PayClientChannel;
//import com.city.city_collector.admin.pay.entity.PayMerchantChannel;
//import com.city.city_collector.admin.pay.entity.PayOrder;
//import com.city.city_collector.admin.pay.entity.PaytmOrder;
//import com.city.city_collector.admin.pay.service.PayClientChannelService;
//import com.city.city_collector.admin.pay.service.PayMerchantChannelService;
//import com.city.city_collector.admin.pay.service.PayMerchantService;
//import com.city.city_collector.admin.pay.service.PayOrderService;
//import com.city.city_collector.admin.pay.service.PaytmOrderService;
//import com.city.city_collector.admin.system.entity.SysUser;
//import com.city.city_collector.admin.system.service.SysUserService;
//import com.city.city_collector.channel.PaytmChannelManager;
//import com.city.city_collector.channel.bean.OrderRequestInfo;
//import com.city.city_collector.channel.util.SignUtil;
//import com.city.city_collector.common.util.AESUtil;
//import com.city.city_collector.common.util.ApiMessage;
//import com.city.city_collector.common.util.CommonUtil;
//import com.city.city_collector.common.util.SnUtil;
//import com.city.city_collector.paylog.PayLogManager;
//import com.google.gson.Gson;
//import com.paytm.pg.merchant.PaytmChecksum;
//
///**
// * Test Merchant ID
// * ALjyAW46365937462744
// * <p>
// * Test Merchant Key
// * 72GTr6ov7JZ&8pTQ
// * <p>
// * Website
// * WEBSTAGING
// * <p>
// * Industry Type
// * Retail
// * <p>
// * Channel ID (For Website)
// * WEB
// * <p>
// * Channel ID (For Mobile Apps)
// * WAP
// * <p>
// * Transaction URL
// * https://securegw-stage.paytm.in/order/process
// * <p>
// * Transaction Status URL
// * https://securegw-stage.paytm.in/order/status
// */
//
///**
// * @Description:
// * @author nb
// */
//@Controller("paytmOrderController")
//@RequestMapping("/api/paytm")
//public class PaytmOrderController {
//
//    @Autowired
//    PayOrderService payOrderService;
//
//    @Autowired
//    PaytmOrderService paytmOrderService;
//
//    @Autowired
//    PayMerchantService payMerchantService;
//
//    @Autowired
//    SysUserService sysUserService;
//
//    @Autowired
//    PayClientChannelService payClientChannelService;
//
//    @Autowired
//    PayMerchantChannelService payMerchantChannelService;
//
////    @RequestMapping("/start")
////    public String start(BigDecimal amount) {
////        return "/admin/test/start";
////    }
//
//    @PostMapping("/order")
//    public @ResponseBody
//    ApiMessage order(String merchantNo, String merchantSn, BigDecimal amount, String notifyUrl,
//                     String remark, Long time, String signType, String sign, HttpServletRequest request) {
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
//
//            Long channelId = 9L; //9为paytm
//
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
//            params.put("remark", remark);
//            params.put("time", time);
//            params.put("signType", signType);
//            String sign1 = SignUtil.getMD5Sign(params, sysUser.getMerchantMy());
//            if (!sign.equals(sign1)) {
//                return ApiMessage.error("签名错误!");
//            }
//            //判断商户订单号是否已存在
//            Map<String, Object> p1 = new HashMap<String, Object>();
//            p1.put("merchantSn", merchantSn);
//            if (payOrderService.querySingle1(p1) != null) {
//                return ApiMessage.error("商户订单号已存在!");
//            }
//
//            //查询商户通道
//            p1.put("channelTypeId", channelId);
//            p1.put("merchantId", sysUser.getId());
//            PayMerchantChannel pmc = payMerchantChannelService.querySingleByMerchantId(p1);
//
//            //生成订单编号
//            String sn = SnUtil.createSn("");
//            //发起下单请求
//            OrderRequestInfo ori = PaytmChannelManager.getInstance().createPayOrder(pmc.getId(), sn, amount, request.getAttribute("serverName").toString());
//            //记录下单日志
//            PayLogManager.getInstance().createPayLogByOrderRequest(sn, "发起下单请求,商户订单号:" + merchantSn + ",平台订单号:" + sn + ",下单结果:" + ori.getData() + ";" + ori.getDesc(), "", "", null, null, sysUser.getId(), null);
//
//            if (ori.getStatus()) {
//                //保存订单信息
//                PayOrder order = new PayOrder();
//
//                order.setAmount(amount);
//                order.setChannelTypeId(channelId);
//                order.setClientId(ori.getClientId());
//                order.setClientNo(ori.getClientNo());
//                order.setClientChannelId(ori.getClientChannelId());
//                order.setClientInfo(ori.getData());
//                order.setClientSn("");
//
//                order.setMerchantId(sysUser.getId());
//                order.setMerchantNo(sysUser.getUsername());
//                order.setMerchantChannelId(ori.getMerchantChannelId());
//                order.setMerchantSn(merchantSn);
//                order.setMoney(amount);
//
//                order.setNotifyStatus(0);//通知状态，未通知
//                order.setNotifyUrl(notifyUrl);
//                order.setOrderStatus(0);//订单状态，未支付
//                order.setPayTime(null);
//                order.setPayUrl(ori.getPayUrl());
//                order.setProductName("");
//                order.setRemark(remark);
//                order.setSn(sn);
//
//                order.setTname("paytm");
//
//                //支付链接对象
//                PaytmOrder paytmOrder = new PaytmOrder();
//                paytmOrder.setMid(ori.getMid());
//                paytmOrder.setSn(sn);
//                paytmOrder.setTxntoken(ori.getTxnToken());
//
//                payOrderService.addSaveByPaytm(order, paytmOrder);
//
//                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "商户" + merchantNo + "下单成功", "", "", order.getClientId(), order.getClientChannelId(), order.getMerchantId(), order.getMerchantChannelId());
//                //返回订单数据
//                Map<String, Object> data = new TreeMap<String, Object>();
//                data.put("merchantNo", merchantNo);
//                data.put("merchantSn", merchantSn);
//                data.put("sn", sn);
//                data.put("payUrl", ori.getPayUrl());
//                data.put("sign", SignUtil.getMD5Sign(data, sysUser.getMerchantMy()));
//
//                return ApiMessage.success("success", data);
//            }
//
//            return ApiMessage.error("下单失败");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiMessage.error("下单失败:" + e.getMessage());
//        }
//
//    }
//
//    /**
//     * 进入付款页面
//     * @author:nb
//     * @param sn
//     * @param model
//     * @return
//     */
//    @RequestMapping("/paypre")
//    public String paypre(String sn, Model model) {
//
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("sn", sn);
//        PaytmOrder paytmOrder = paytmOrderService.querySingle(params);
//        if (paytmOrder == null) {
//            model.addAttribute("error", "Sorry,Order does not exist...");
//        } else {
//            model.addAttribute("orderId", sn);
//            model.addAttribute("mid", paytmOrder.getMid());
//            model.addAttribute("txnToken", paytmOrder.getTxntoken());
//            model.addAttribute("amount", paytmOrder.getAmount());
//        }
//        return "/admin/test/pay";
//    }
//
//    @RequestMapping("/testnotify")
//    public @ResponseBody
//    String testnotify() {
//        return "success";
//    }
//
//    /**
//     * 订单回调，收到订单通知后，若验证通过，则主动请求paytm获取订单真实状态
//     * @param param
//     * @param request
//     * @return
//     */
//    @RequestMapping("/notify")
//    public String notify(@RequestParam Map<String, Object> param, HttpServletRequest request) {
//        System.out.println("notify_param:" + new Gson().toJson(param));
//        try {
//            //由商家生成并在请求中发送的交易的唯一参考ID
//            String sn = (String) param.get("ORDERID");
//            //Paytm为每笔交易发行的唯一的Paytm交易ID
//            String txnId = (String) param.get("TXNID");
////            //Paytm提供给每个商家的唯一标识符
////            String mid=(String)param.get("MID");
////            //客户以印度卢比支付的金额
////            String amount=(String)param.get("TXNAMOUNT");
//            //支付事务状态，并且只有三个值：TXN_SUCCESS，TXN_FAILURE和PENDING
//            String status = (String) param.get("STATUS");
//
//            //写入日志
//            PayLogManager.getInstance().createPayLogByClientNotify(sn, "paytm回调通知", new Gson().toJson(param), "", null, null, null, null);
//            if (!"TXN_SUCCESS".equalsIgnoreCase(status)) {
//                return "/admin/error";
//            }
//
//            //根据订单号查询订单
//            Map<String, Object> param1 = new HashMap<String, Object>();
//            param1.put("sn", sn);
//            PayOrder po = payOrderService.querySingle1(param1);
//            if (po == null) {
//                System.out.println("订单不存在");
//                return "/admin/error";
//            }
//
//            //根据订单的 收款通道ID获取通道信息
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("id", po.getClientChannelId());
//            PayClientChannel pcc = payClientChannelService.querySingle(params);
//            if (pcc == null) {
//                System.out.println("通道不存在");
//                return "/admin/error";
//            }
//
//            //获取秘钥
//            String md5 = pcc.getPaytmmd5();
//            String uuid = pcc.getPaytmuid();
//
//            String pwd = md5.substring(md5.length() - 7) + uuid.substring(uuid.length() - 4) + PaytmClientChannelController.KEY;
//
//            String key = AESUtil.decrypt(pcc.getPaytmkey(), pwd);
//
//            if (validChecksumhash(param, key)) {//验证签名
//
//                if (po.getOrderStatus() != null && po.getOrderStatus().intValue() == 1) {//订单已处理
//                    return "/admin/success";
//                }
//
//                //此处应改为发起查询请求，获取订单状态。以防套取商户key发起伪造请求
////                String orderStatus=PaytmChannelManager.getInstance().queryOrderStatus(pcc.getPaytmid(),key,sn);
//                Map<String, Object> datas = PaytmChannelManager.getInstance().queryOrderStatus(pcc.getPaytmid(), key, sn);
//                System.out.println("订单状态:" + datas);
//
//                BigDecimal amount = new BigDecimal((String) datas.get("amount"));
//
//                if ("01".equals(datas.get("code")) && amount.compareTo(po.getAmount()) == 0) {
//                    po.setPayTime(new Date());
//                    po.setClientSn(txnId);
//                    po.setOrderStatus(1);
//
//                    payOrderService.updateOrderPayStatus(po);
//                }
//                PayLogManager.getInstance().createPayLogByClientNotify(sn, "paytm通知处理成功", new Gson().toJson(param), "", null, po.getClientId(), null, null);
//            }
//            return "/admin/success";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "/admin/error";
//
//    }
//
//    /**
//     * 验证签名
//     * @author:nb
//     * @param param
//     * @param key 秘钥
//     * @return
//     */
//    public boolean validChecksumhash(Map<String, Object> param, String key) {
//        TreeMap<String, String> paytmParams = new TreeMap<String, String>();
//        for (Entry<String, Object> requestParamsEntry : param.entrySet()) {
//            if (!"CHECKSUMHASH".equalsIgnoreCase(requestParamsEntry.getKey())) {
//                paytmParams.put(requestParamsEntry.getKey(), (String) requestParamsEntry.getValue());
//            }
//        }
////      System.out.println(paytmParams);
//        /**
//         * Verify checksum
//         * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
//         */
//        try {
//            return PaytmChecksum.verifySignature(paytmParams, key, (String) param.get("CHECKSUMHASH"));
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//        return false;
//    }
//
////  @RequestMapping("/order")
////  public String order(BigDecimal amount,Model model) {
////      Map < String, Object > rst=new HashMap<String,Object>();
////      try {
////          String sn=SnUtil.createSn("");
////          String mid="ALjyAW46365937462744";//商户ID
////          String merchantKey="72GTr6ov7JZ&8pTQ";//商户KEY
////          String webSite="WEBSTAGING";
//////          String url="https://securegw-stage.paytm.in/order/process";
////          String url="https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction";
////
////          JsonObject paytmParams = new JsonObject();
////
////          JsonObject body = new JsonObject();
////          body.addProperty("requestType", "Payment");
////          body.addProperty("mid", mid);
////          body.addProperty("websiteName", webSite);
////          body.addProperty("orderId", sn);
////          body.addProperty("callbackUrl", "http://147.139.35.155/api/paytm/notify");
////
////          JsonObject txnAmount = new JsonObject();
////          txnAmount.addProperty("value", amount);
////          txnAmount.addProperty("currency", "INR");
////
////          JsonObject userInfo = new JsonObject();
////          //Unique reference ID for every customer which is generated by merchant. Special characters allowed in CustId are @, ! ,_ ,$, .
////          //商家生成的每个客户的唯一参考ID。 CustId中允许的特殊字符是@ ! _ $ .
////          userInfo.addProperty("custId", "CUST_001");
////
////          body.add("txnAmount", txnAmount);
////          body.add("userInfo", userInfo);
////
////          /*
////          * Generate checksum by parameters we have in body
////          * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
////          * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
////          */
////
////          String checksum = PaytmChecksum.generateSignature(body.toString(), merchantKey);
////
////          JsonObject head = new JsonObject();
////          head.addProperty("signature", checksum);
////          head.addProperty("version", "V1");
////          head.addProperty("channelId", "WEB");
////          head.addProperty("requestTimestamp", System.currentTimeMillis()/1000);
////
////          paytmParams.add("body", body);
////          paytmParams.add("head", head);
////          //请求数据
////          String post_data = paytmParams.toString();
////          System.out.println("请求数据："+post_data);
////
////          //数据请求
////          OkHttpClient okhttp=new OkHttpClient.Builder().build();
////          MediaType mediaType = MediaType.parse("application/json");
////          RequestBody reqbody = RequestBody.create(mediaType, post_data);
////          Request request=new Request.Builder()
////                  .url(url+"?mid="+mid+"&orderId="+sn)
////                  .post(reqbody)
////                  .addHeader("content-type", "application/json")
////                  .build();
////
////          Response response = okhttp.newCall(request).execute();
//////          System.out.println("headers:"+response.headers().toString());
////
////
////          if(response!=null && response.isSuccessful()){
////              String data=response.body().string();
////              System.out.println("返回的数据："+data);
////              rst.put("data", data);
//////              rst.put("txnToken", getTxnToken(data));
////              model.addAttribute("txnToken", getTxnToken(data));
////              response.close();
////          }else {
////              rst.put("error",response.code());
////          }
////
////          model.addAttribute("orderId", sn);
////          model.addAttribute("mid", mid);
////
////      }catch(Exception e) {
////          e.printStackTrace();
////          rst.put("error", e.getLocalizedMessage());
////      }
////      model.addAttribute("data", new Gson().toJson(rst));
////
////      return "/admin/test/pay";
////  }
//
//
////    public static void main(String[] args) {
//////        String data="{\"head\":{\"responseTimestamp\":\"1603441001973\",\"version\":\"v1\",\"signature\":\"b6t1kiGu7qkthj1UsKMHZGYxyNdti/F3/uohrLJeiYkCVTJsso8HIaZUhxVOeYjN3X1OEVwRbQEfbRe2AW38ZEM5LYaW/HoMSM1HsVOxs10=\"},\"body\":{\"resultInfo\":{\"resultStatus\":\"S\",\"resultCode\":\"0000\",\"resultMsg\":\"Success\"},\"txnToken\":\"d04a30adba6d4203a850a3e8c1b4d9631603441001628\",\"isPromoCodeValid\":false,\"authenticated\":false}}";
//////        System.out.println(getTxnToken(data));
//////        JsonParser jp=new JsonParser();
//////        JsonObject jobj=jp.parse(data).getAsJsonObject();
////////        String d1=jobj.get("data").getAsString();
////////        jobj=jp.parse(d1).getAsJsonObject();
////////        System.out.println(jobj.get("head"));
////////        System.out.println(jobj.get("body"));
//////        jobj=jobj.get("body").getAsJsonObject();
//////        String txnToken=jobj.get("txnToken").getAsString();
//////        System.out.println(txnToken);
//////        System.out.println("d04a30adba6d4203a850a3e8c1b4d9631603441001628".length());
////
////        String data="{\"ORDERID\":\"20201023161637909393\",\"MID\":\"ALjyAW46365937462744\",\"TXNID\":\"20201023111212800110168632302015891\",\"TXNAMOUNT\":\"1.00\",\"PAYMENTMODE\":\"NB\",\"CURRENCY\":\"INR\",\"TXNDATE\":\"2020-10-23 13:46:41.0\",\"STATUS\":\"TXN_SUCCESS\",\"RESPCODE\":\"01\",\"RESPMSG\":\"Txn Success\",\"GATEWAYNAME\":\"AXIS\",\"BANKTXNID\":\"19482683448\",\"BANKNAME\":\"AXIS\",\"CHECKSUMHASH\":\"45/6dWLLDhDXptJhuSWD1PlZvm1FOk9YKjlMhkZakzWj7qfygifyyLI+u3hUTkVOUl/iLy1F/f/mjAzPNfL4UD1swqR2ZpP3tVR6drbiAY8\\u003d\"}";
////        JsonParser jp=new JsonParser();
////        JsonObject jobj=jp.parse(data).getAsJsonObject();
////        //由商家生成并在请求中发送的交易的唯一参考ID
////        String orderId=jobj.get("ORDERID").getAsString();
////        //Paytm为每笔交易发行的唯一的Paytm交易ID
////        String txnId=jobj.get("TXNID").getAsString();
////        //Paytm提供给每个商家的唯一标识符
////        String mid=jobj.get("MID").getAsString();
////        //客户以印度卢比支付的金额
////        String amount=jobj.get("TXNAMOUNT").getAsString();
////        //支付事务状态，并且只有三个值：TXN_SUCCESS，TXN_FAILURE和PENDING
////        String status=jobj.get("STATUS").getAsString();
////
////        String checksumhash=jobj.get("CHECKSUMHASH").getAsString();
////
////        //银行发送的交易ID。对于Paytm专有工具，Paytm系统也会生成唯一的参考号。如果交易未到达银行，则为NULL或空字符串。主要原因是用户在交易到达银行服务器之前退出支付流程。
////        String banktxnid=jobj.get("BANKTXNID").getAsString();
////
////
////        System.out.println(jobj.get("ORDERID").getAsString());
////
////        String paytmChecksum = checksumhash;
////
////        Map < String, Object > param=new GsonBuilder().disableHtmlEscaping().create().fromJson(data,new TypeToken<HashMap<String,Object>>(){}.getType());
////        System.out.println(param);
////
////        /* Create a TreeMap from the parameters received in POST */
////        TreeMap<String, String> paytmParams = new TreeMap<String, String>();
//////        for (Entry<String, String[]> requestParamsEntry : request.getParameterMap().entrySet()) {
//////            if ("CHECKSUMHASH".equalsIgnoreCase(requestParamsEntry.getKey())){
//////                paytmChecksum = requestParamsEntry.getValue()[0];
//////            } else {
//////                paytmParams.put(requestParamsEntry.getKey(), requestParamsEntry.getValue()[0]);
//////            }
//////        }
////        for(Entry<String, Object> requestParamsEntry :param.entrySet()) {
////            if (!"CHECKSUMHASH".equalsIgnoreCase(requestParamsEntry.getKey())){
////                paytmParams.put(requestParamsEntry.getKey(), (String)requestParamsEntry.getValue());
////            }
////        }
////        System.out.println(paytmParams);
////        /**
////        * Verify checksum
////        * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
////        */
////
////        try {
////            boolean isVerifySignature = PaytmChecksum.verifySignature(paytmParams, "72GTr6ov7JZ&8pTQ", paytmChecksum);
////            if (isVerifySignature) {
////                System.out.append("Checksum Matched");
////            } else {
////                System.out.append("Checksum Mismatched");
////            }
////        }
////        catch (Exception e) {
////
////            e.printStackTrace();
////        }
////
////    }
//}
