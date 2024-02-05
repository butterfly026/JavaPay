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
//public class MiDianClientChannel extends ClientChannel {
////private final static String URL="http://api.sxwdcm.com";
////
////    private final static String APPID="17306";
////
////    private final static String KEY="eb20661a5768f456b203af2aae4083c2";
//
//    @Override
//    public OrderInfo dealRequestData(String data) {
//        if (StringUtils.isBlank(data)) {
//            return null;
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        if (!"true".equals(jobj.get("success").getAsString())) {
//            return null;
//        }
//
//        OrderInfo oi = new OrderInfo();
//        oi.setFlag(true);
//        oi.setData(data);
//
//        String payUrl = jobj.get("url").getAsString();
////        int i1=payUrl.lastIndexOf("id=");
////        if(i1==-1) return null;
//
//        oi.setSn(jobj.get("porderid").getAsString());
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
////        String payUrl=URL+"/payv2.aspx";
////        String type="88ali";//测试
//        String type = "hf1001";
//
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("partner", getMerchantNo());
//
////        params.put("type","hf1001");
//        params.put("type", type);
//
//        params.put("ordernumber", sn);
//
//        params.put("paymoney", amount);
//
//        params.put("callbackurl", domain + returnUrl + "/" + this.getId());
//
//        params.put("attach", "");
//        System.out.println(getParams());
//        if (getParams() != null) {
//            params.putAll(getParams());
//        }
//
//        params.put("sign", MD5Util.MD5Encode("partner=" + getMerchantNo() + "&type=" + params.get("type") + "&paymoney=" + params.get("paymoney") + "&ordernumber=" + sn + "&callbackurl=" + domain + returnUrl + "/" + this.getId() + getMerchantMy(), "UTF-8").toLowerCase());
//
//        return requestData(getUrlpay(), RequestType.POST, params, new HashMap<String, String>(), "UTF-8", merchantId, sn, 0);
//    }
//
//    @Override
//    public NotifyInfo notify(Map<String, Object> params, String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        try {
//            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
//
//
//            String partner = params.get("partner") == null ? "" : params.get("partner").toString();
//            String ordernumber = params.get("ordernumber") == null ? "" : params.get("ordernumber").toString();
//
//            String paymoney = params.get("paymoney") == null ? "" : params.get("paymoney").toString();
//            String attach = params.get("attach") == null ? "" : params.get("attach").toString();
//            if (!partner.equals(merchantNo)) {//APPID不合法
//                return null;
//            }
//
//            if (sign.equals(MD5Util.MD5Encode("partner=" + partner + "&ordernumber=" + ordernumber + "&paymoney=" + paymoney + "&attach=" + attach + merchantMy, "UTF-8"))) {//配对成功
//
////                return new Gson().fromJson(body, NotifyInfo.class);
////                SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
//
//                NotifyInfo ni = new NotifyInfo();
//                ni.setSn(ordernumber);
//                ni.setClientSn("");
////                String status=(String)params.get("code");
////                if("succ".equals(status)) {
////                    ni.setStatus("success");
////                }else {
////                    ni.setStatus(status);
////                }
//                ni.setStatus("success");
//
//                ni.setAmount(new BigDecimal(paymoney));
//                ni.setActualAmount(new BigDecimal(paymoney));
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
//        String type = "hf1001";
//
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("partner", merchantNo);
//
////        params.put("type","hf1001");
//        params.put("type", type);
//
//        params.put("ordernumber", sn);
//
//        params.put("paymoney", amount);
//
//        params.put("callbackurl", domain + returnUrl + "/" + id);
//
//        params.put("attach", "");
//
//        if (channelParams != null) params.putAll(channelParams);
//
//        Gson gson = new Gson();
//        ApiInfo apiInfo = new ApiInfo();
//
//        apiInfo.setSignData(gson.toJson(params));
//
//        apiInfo.setSignStr("partner=" + merchantNo + "&type=" + params.get("type") + "&paymoney=" + params.get("paymoney") + "&ordernumber=" + sn + "&callbackurl=" + domain + returnUrl + "/" + id + merchantMy);
//
//        String sign = MD5Util.MD5Encode("partner=" + merchantNo + "&type=" + params.get("type") + "&paymoney=" + params.get("paymoney") + "&ordernumber=" + sn + "&callbackurl=" + domain + returnUrl + "/" + id + merchantMy, "UTF-8").toLowerCase();
//        apiInfo.setSign(sign);
//
//        params.put("sign", sign);
//
//        apiInfo.setRequstData(gson.toJson(params));
//        apiInfo.setDatas(params);
//        apiInfo.setGotype(0);
//        return apiInfo;
//    }
//}
