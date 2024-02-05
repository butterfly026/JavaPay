//
//package com.city.city_collector.channel.bean.clients;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Random;
//import java.util.TreeMap;
//import java.util.Map.Entry;
//
//import org.apache.commons.lang.StringUtils;
//import org.bouncycastle.jcajce.provider.asymmetric.util.DESUtil;
//
//import com.city.city_collector.channel.bean.ApiInfo;
//import com.city.city_collector.channel.bean.ClientChannel;
//import com.city.city_collector.channel.bean.NotifyInfo;
//import com.city.city_collector.channel.bean.OrderInfo;
//import com.city.city_collector.channel.bean.ClientChannel.RequestType;
//import com.city.city_collector.channel.util.DesCodeUtil;
//import com.city.city_collector.channel.util.SignUtil;
//import com.city.city_collector.common.util.MD5Util;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
///**
// * @author nb
// * @Description:
// */
//public class SuYunClientChannel extends ClientChannel {
//
//    @Override
//    public OrderInfo dealRequestData(String data) {
//        if (StringUtils.isBlank(data)) {
//            return null;
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        if (jobj.get("code").getAsInt() != 1) {
//            return null;
//        }
////        data=jobj.get("data").getAsString();
//        jobj = jobj.get("data").getAsJsonObject();
////        jobj=jp.parse(data).getAsJsonObject();
//
//        OrderInfo oi = new OrderInfo();
//        oi.setFlag(true);
//        oi.setData(data);
//
//        oi.setSn(jobj.get("order_number").getAsString());
//        int model = jobj.get("order_pay_mode").getAsInt();
//        if (model == 2) {
//            oi.setPayUrl(jobj.get("order_pay_url").getAsString());
//        } else if (model == 3 || model == 4) {
//            oi.setPayUrl(jobj.get("order_pay_img").getAsString());
//        } else if (model == 1 || model == 5) {
//            oi.setPayUrl(jobj.get("order_pay_html").getAsString());
//        } else if (model == 6) {
//            oi.setPayUrl(jobj.get("order_pay_info").getAsString());
//        }
//
//        oi.setName("");
//        oi.setCard("");
//        oi.setBankName("");
//
//        oi.setClientId(this.getClient() == null ? null : this.getClient().getId());
//        oi.setClientNo(this.getClient() == null ? null : this.getClient().getNo());
//        oi.setClientName(this.getClient() == null ? null : this.getClient().getName());
//
//        oi.setClientChannelId(this.getId());
//
//        return oi;
//    }
//
//    @Override
//    public OrderInfo createOrder(String sn, BigDecimal amount, String domain, Long payType, Long merchantId) {
//
//        BigDecimal h = new BigDecimal("100");
//
//        TreeMap<String, String> map = new TreeMap<String, String>();
//        map.put("appid", getMerchantNo());// 应用key
//        map.put("order_trano_in", sn);// 商户单号
//        map.put("order_goods", "good:" + sn);// 商品名称
//        map.put("order_amount", amount.multiply(h).setScale(0) + "");// 订单金额，单位分 (不能小于100)
//        map.put("order_extend", "kkpay");// 扩展参数，最大长度64位
//        map.put("order_ip", "113.52.132.164");// 客户端真实ip
//        map.put("order_return_url", domain + "/api/pay/success");// 成功后同步地址
//        map.put("order_notify_url", domain + returnUrl + "_json/" + this.getId());// 异步通知地址
//
//        Map<String, Object> expMap = getParams();
//        if (expMap != null) {
//
//            if (expMap.get("order_ip") != null) map.put("order_ip", (String) expMap.get("order_ip"));
//            if (expMap.get("order_extend") != null) map.put("order_extend", (String) expMap.get("order_extend"));
//            if (expMap.get("order_goods") != null) map.put("order_goods", (String) expMap.get("order_goods"));
//        }
//
//        String jsonString = new Gson().toJson(map);
//        String sortStr = sortSign(map);
//        // 时间戳
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        //随机字符串
//        String nonce = genNonceStr();
//        // DES加密key 0-8
//        String signDesKey = MD5Util.MD5Encode(timestamp + getMerchantMy() + nonce, "UTF-8").substring(0, 8).toUpperCase();
//        // 支付加密类型
//        String signtype = "MD5";
//
//        // 公钥加密
//        String data = DesCodeUtil.encrypt(jsonString, signDesKey);
//        // 私钥签名
//        String sign = MD5Util.MD5Encode(timestamp + nonce + sortStr + expMap.get("sign_key"), "UTF-8").toUpperCase();
//        // 拼接请求用json字符串
//        Map<String, Object> reqMap = new HashMap<String, Object>();
//        reqMap.put("data", data);
//        reqMap.put("sign", sign);
//        reqMap.put("timestamp", timestamp);
//        reqMap.put("nonce", nonce);
//        reqMap.put("signtype", signtype);
//
//        return requestData(getUrlpay(), RequestType.POST, reqMap, new HashMap<String, String>(), "UTF-8", merchantId, sn, 1);
//    }
//
//    @Override
//    public NotifyInfo notify(Map<String, Object> params, String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        String sign = params.get("signature") == null ? "" : (String) params.get("signature");
//
//        String state = params.get("order_state") == null ? "" : params.get("order_state").toString();
//
//
//        if (!"1".equals(state)) {
//            return null;
//        }
//
//        TreeMap<String, String> map = new TreeMap<String, String>();
//
//        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
//        while (it.hasNext()) {
//            Entry<String, Object> et = it.next();
//            if (!et.getKey().equals("signature")) {
//                map.put(et.getKey(), String.valueOf(et.getValue()));
//            }
//        }
//
//        String sortStr = sortSign(map) + channelParams.get("sign_key");
//
//        if (sign.equals(MD5Util.MD5Encode(sortStr, "utf-8").toUpperCase())) {
//            NotifyInfo ni = new NotifyInfo();
//            ni.setSn((String) params.get("order_trano_in"));
//            ni.setClientSn("");
//
//            ni.setStatus("success");
//            BigDecimal h = new BigDecimal("100.00");
//
//            ni.setAmount(new BigDecimal((String) params.get("order_amount")).divide(h));
//            ni.setActualAmount(ni.getAmount());
//            ni.setSign(sign);
//            ni.setPayTime(new Date());
//            return ni;
//        }
//
//        return null;
//    }
//
//    @Override
//    public ApiInfo createOrderRequestData(Long id, String sn, BigDecimal amount, String domain, Integer payType,
//                                          String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        BigDecimal h = new BigDecimal("100");
//
//        TreeMap<String, String> map = new TreeMap<String, String>();
//        map.put("appid", merchantNo);// 应用key
//        map.put("order_trano_in", sn);// 商户单号
//        map.put("order_goods", "good:" + sn);// 商品名称
//        map.put("order_amount", amount.multiply(h).setScale(0) + "");// 订单金额，单位分 (不能小于100)
//        map.put("order_extend", "kkpay");// 扩展参数，最大长度64位
//        map.put("order_ip", "113.52.132.164");// 客户端真实ip
//        map.put("order_return_url", domain + "/api/pay/success");// 成功后同步地址
//        map.put("order_notify_url", domain + returnUrl + "_json/" + id);// 异步通知地址
//
//        if (channelParams != null) {
//
//            if (channelParams.get("order_ip") != null) map.put("order_ip", (String) channelParams.get("order_ip"));
//            if (channelParams.get("order_extend") != null)
//                map.put("order_extend", (String) channelParams.get("order_extend"));
//            if (channelParams.get("order_goods") != null)
//                map.put("order_goods", (String) channelParams.get("order_goods"));
//        }
//
//        String jsonString = new Gson().toJson(map);
////        System.out.println("jsonString:"+jsonString);
//
//        String sortStr = SignUtil.sortSign(map);
////        System.out.println("sortStr:"+sortStr);
//        // 时间戳
//        String timestamp = String.valueOf(System.currentTimeMillis());
////        System.out.println("timestamp:"+timestamp);
//        //随机字符串
//        String nonce = SignUtil.genNonceStr();
////        System.out.println("nonce:"+nonce);
//        // DES加密key 0-8
//        String signDesKey = MD5Util.MD5Encode(timestamp + merchantMy + nonce, "UTF-8").substring(0, 8).toUpperCase();
////        System.out.println("signDesKey:"+signDesKey);
//        // 支付加密类型
//        String signtype = "MD5";
//
//        Gson gson = new Gson();
//        ApiInfo apiInfo = new ApiInfo();
//
//        apiInfo.setSignData(gson.toJson(timestamp + nonce + sortStr + channelParams.get("sign_key")));
//
//        // 公钥加密
//        String data = DesCodeUtil.encrypt(jsonString, signDesKey);
////        System.out.println("data:"+data);
//
//        apiInfo.setSignStr("公钥加密数据:" + jsonString + ",秘钥:" + signDesKey + "，加密后数据:" + data);
//
//        // 私钥签名
//        String sign = MD5Util.MD5Encode(timestamp + nonce + sortStr + channelParams.get("sign_key"), "UTF-8").toUpperCase();
////        System.out.println("sign:"+sign);
//        // 拼接请求用json字符串
//        Map<String, Object> reqMap = new HashMap<String, Object>();
//        reqMap.put("data", data);
//        reqMap.put("sign", sign);
//        reqMap.put("timestamp", timestamp);
//        reqMap.put("nonce", nonce);
//        reqMap.put("signtype", signtype);
//
//        apiInfo.setSign(sign);
//
//        //System.out.println("reqMap:"+gson.toJson(reqMap));
//
//        apiInfo.setRequstData(gson.toJson(reqMap));
//        apiInfo.setDatas(reqMap);
//        apiInfo.setGotype(0);
//        apiInfo.setReqType(1);
//        return apiInfo;
//
//    }
//
//    @Override
//    public String getNotifySuccess() {
//        return "ok";
//    }
//
//    private String sortSign(TreeMap<String, String> map) {
//        StringBuffer sb = new StringBuffer();
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            String value = entry.getValue();
//            if (!value.isEmpty()) {
//                sb.append(String.format("%s%s", entry.getKey(), value));
//            }
//        }
//        return sb.toString();
//    }
//
//    private String genNonceStr() {
//        Random random = new Random();
//        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "utf-8");
//    }
//}
