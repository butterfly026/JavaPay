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
// * @Description:
// */
//public class TianMaClientChannel extends ClientChannel {
//
//    @Override
//    public OrderInfo dealRequestData(String data) {
//        if (StringUtils.isBlank(data)) {
//            return null;
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        if (!"200".equals(jobj.get("code").getAsString())) {
//            return null;
//        }
//        jobj = jobj.get("body").getAsJsonObject();
//        OrderInfo oi = new OrderInfo();
//        oi.setFlag(true);
//        oi.setData(data);
//
//        oi.setSn(jobj.get("tradeNoThird").getAsString());
//        oi.setPayUrl(jobj.get("payLink").getAsString());
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
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("merchantId", getMerchantNo());
//
//        params.put("tradeNoThird", sn);
//
//        params.put("notifyUrl", domain + returnUrl + "/" + this.getId());
//
//        params.put("successUrl", domain + "/api/pay/success");
//        params.put("errorUrl", domain + "/api/pay/error");
//
//        params.put("amount", amount.setScale(2) + "");
//        params.put("payType", "1");
//
//        params.put("businessType", "1");
//
//
//        if (getParams() != null) {
//            params.putAll(getParams());
//        }
//
//        params.put("signstr", MD5Util.MD5Encode(getMerchantNo() + params.get("tradeNoThird") + params.get("notifyUrl") + params.get("successUrl") + params.get("errorUrl") + params.get("amount") + params.get("payType") + params.get("businessType") + getMerchantMy(), "utf-8"));
//
//        return requestData(getUrlpay(), RequestType.POST, params, new HashMap<String, String>(), "UTF-8", merchantId, sn, 0);
//    }
//
//    @Override
//    public NotifyInfo notify(Map<String, Object> params, String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        try {
//            String code = params.get("code") == null ? "" : (String) params.get("code");
//            if (!"200".equals(code)) {
//                return null;
//            }
//
//            String sign = params.get("signstr") == null ? "" : (String) params.get("signstr");
//            if (StringUtils.isBlank(sign)) {
//                return null;
//            }
//            System.out.println(code + nullVal(params.get("tradeNoThird")) + nullVal(params.get("msg")) + nullVal(params.get("tradeNo")) + merchantMy);
//            if (sign.toLowerCase().equals(MD5Util.MD5Encode(code + nullVal(params.get("tradeNoThird")) + nullVal(params.get("msg")) + nullVal(params.get("tradeNo")) + merchantMy, "utf-8").toLowerCase())) {//配对成功
//
//                NotifyInfo ni = new NotifyInfo();
//                ni.setSn((String) params.get("tradeNoThird"));
//                ni.setClientSn((String) params.get("tradeNo"));
//                ni.setStatus("success");
//
//                ni.setAmount(BigDecimal.ZERO);
//                ni.setActualAmount(BigDecimal.ZERO);
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
//    private String nullVal(Object obj) {
//        if (obj == null) return "";
//        return String.valueOf(obj);
//    }
//
//    @Override
//    public ApiInfo createOrderRequestData(Long id, String sn, BigDecimal amount, String domain, Integer payType,
//                                          String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("merchantId", merchantNo);
//
//        params.put("tradeNoThird", sn);
//
//        params.put("notifyUrl", domain + Constants.RETURN_URL + "/" + id);
//
//        params.put("successUrl", domain + "/api/pay/success");
//        params.put("errorUrl", domain + "/api/pay/error");
//
//        params.put("amount", amount.setScale(2) + "");
//        params.put("payType", "1");
//
//        params.put("businessType", "1");
//
//        if (channelParams != null) params.putAll(channelParams);
//
//        Gson gson = new Gson();
//        ApiInfo apiInfo = new ApiInfo();
//
//        apiInfo.setSignData(gson.toJson(params));
//
//        apiInfo.setSignStr(merchantNo + params.get("tradeNoThird") + params.get("notifyUrl") + params.get("successUrl") + params.get("errorUrl") + params.get("amount") + params.get("payType") + params.get("businessType") + merchantMy);
//
//        String sign = MD5Util.MD5Encode(merchantNo + params.get("tradeNoThird") + params.get("notifyUrl") + params.get("successUrl") + params.get("errorUrl") + params.get("amount") + params.get("payType") + params.get("businessType") + merchantMy, "UTF-8");
//        apiInfo.setSign(sign);
//
//        params.put("signstr", sign);
//
//        apiInfo.setRequstData(gson.toJson(params));
//        apiInfo.setDatas(params);
//        apiInfo.setGotype(0);
//        return apiInfo;
//    }
//
//}
