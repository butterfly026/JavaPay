//
//package com.city.city_collector.channel.bean.clients;
//
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.tomcat.util.security.MD5Encoder;
//
//import com.city.city_collector.channel.bean.ApiInfo;
//import com.city.city_collector.channel.bean.ClientChannel;
//import com.city.city_collector.channel.bean.NotifyInfo;
//import com.city.city_collector.channel.bean.OrderInfo;
//import com.city.city_collector.channel.bean.ClientChannel.RequestType;
//import com.city.city_collector.channel.util.Constants;
//import com.city.city_collector.channel.util.SignUtil;
//import com.city.city_collector.common.util.MD5Util;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
///**
// * @author nb
// * @Description:jhtr通道
// */
//public class Alibank55ClientChannel extends ClientChannel {
//
////    private final static String URL="http://www.ycpest.com";
////
////    private final static String APPID="D1C7728669051BE6658EA03516197D20";
////
////    private final static String KEY="519fa06930d5f4cc0f13bed3545da31f";
//
//    @Override
//    public OrderInfo dealRequestData(String data) {
//        if (StringUtils.isBlank(data)) {
//            return null;
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        if (!"1".equals(jobj.get("state").getAsString())) {
//            return null;
//        }
//
//        String payUrl = jobj.get("data").getAsString();
//
//        OrderInfo oi = new OrderInfo();
//        oi.setFlag(true);
//        oi.setData(data);
//
//        int i1 = payUrl.lastIndexOf("id=");
//        if (i1 == -1) return null;
//
//        oi.setSn(payUrl.substring(i1 + 3));
//        oi.setPayUrl(payUrl);
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
////        String payUrl=URL+"/api/reqPay.aspx";
//
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("appId", getMerchantNo());
//        params.put("money", amount);
//
//        params.put("user_order", sn);
//
//
//        params.put("payType", "2");
//
//        params.put("notifyurl", domain + returnUrl + "/" + this.getId());
//
//        params.put("extend", "");
//        params.put("format", "utf-8");
//        params.put("isOpenBank", "1");
//
//        if (getParams() != null) {
//            params.putAll(getParams());
//        }
//
//        params.put("sign", MD5Util.MD5Encode(getMerchantNo() + getMerchantMy() + amount + sn + domain + returnUrl + "/" + this.getId(), "utf-8"));
//
//        return requestData(getUrlpay(), RequestType.POST, params, new HashMap<String, String>(), "UTF-8", merchantId, sn, 0);
//    }
//
//    @Override
//    public NotifyInfo notify(Map<String, Object> params, String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        try {
//            String orderId = params.get("orderId") == null ? "" : (String) params.get("orderId");
//            String money = params.get("money") == null ? "" : (String) params.get("money");
//            String extend = params.get("extend") == null ? "" : (String) params.get("extend");
//
//            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
//
//
//            if (sign.equals(MD5Util.MD5Encode(merchantNo + merchantMy + money + extend + orderId, "utf-8"))) {//配对成功
//
////                return new Gson().fromJson(body, NotifyInfo.class);
////                SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
//
//                NotifyInfo ni = new NotifyInfo();
//                ni.setSn(orderId);
//                ni.setClientSn("");
////                String status=(String)params.get("code");
////                if("succ".equals(status)) {
////                    ni.setStatus("success");
////                }else {
////                    ni.setStatus(status);
////                }
//                ni.setStatus("success");
//
//                ni.setAmount(new BigDecimal(money));
//                ni.setActualAmount(new BigDecimal(money));
//                ni.setSign(sign);
//                ni.setPayTime(new Date());
//                return ni;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public ApiInfo createOrderRequestData(Long id, String sn, BigDecimal amount, String domain, Integer payType,
//                                          String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("appId", merchantNo);
//        params.put("money", amount);
//
//        params.put("user_order", sn);
//
//
//        params.put("payType", "2");
//
//        params.put("notifyurl", domain + Constants.RETURN_URL + "/" + id);
//
//        params.put("extend", "");
//        params.put("format", "utf-8");
//        params.put("isOpenBank", "1");
//
//        if (channelParams != null) params.putAll(channelParams);
//
//        Gson gson = new Gson();
//        ApiInfo apiInfo = new ApiInfo();
//
//        apiInfo.setSignData(gson.toJson(params));
//
//        apiInfo.setSignStr(merchantNo + merchantMy + amount + sn + domain + Constants.RETURN_URL + "/" + id);
//
//        String sign = MD5Util.MD5Encode(merchantNo + merchantMy + amount + sn + domain + Constants.RETURN_URL + "/" + id, "UTF-8").toLowerCase();
//        apiInfo.setSign(sign);
//
//        params.put("sign", sign);
//
//        apiInfo.setRequstData(gson.toJson(params));
//        apiInfo.setDatas(params);
//        apiInfo.setGotype(0);
//        return apiInfo;
//    }
//
//}
