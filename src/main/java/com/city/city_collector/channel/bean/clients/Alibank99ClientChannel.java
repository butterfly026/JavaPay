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
//import com.city.city_collector.admin.pay.entity.PayCash;
//import com.city.city_collector.admin.system.entity.SysUser;
//import com.city.city_collector.channel.bean.ApiInfo;
//import com.city.city_collector.channel.bean.CashInfo;
//import com.city.city_collector.channel.bean.ClientChannel;
//import com.city.city_collector.channel.bean.NotifyInfo;
//import com.city.city_collector.channel.bean.OrderInfo;
//import com.city.city_collector.channel.bean.ClientChannel.RequestType;
//import com.city.city_collector.channel.util.Constants;
//import com.city.city_collector.channel.util.SignUtil;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//public class Alibank99ClientChannel extends ClientChannel {
//
////    private final static String URL="http://api.juhetairun.com";
////
////    private final static String APPID="100113";
////
////    private final static String KEY="Qc7Bs3CmWhuILIdOxotzE8im4BXQoQLE";
//
//    @Override
//    public OrderInfo dealRequestData(String data) {
//        if (StringUtils.isBlank(data)) {
//            return null;
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        if (!"success".equals(jobj.get("code").getAsString())) {
//            return null;
//        }
//
//        OrderInfo oi = new OrderInfo();
//        oi.setFlag(true);
//        oi.setData(data);
//
//        oi.setSn(jobj.get("out_order_no").getAsString());
//        oi.setPayUrl(jobj.get("pay_url").getAsString());
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
////        String payUrl=URL+"/pay/index.php/trade/pay";
//
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("merch_id", getMerchantNo());
//        params.put("product", "802");
//        params.put("order_id", sn);
//
//        params.put("amount", amount);
//        params.put("bank_code", "");
//
//        params.put("notify_url", domain + returnUrl + "/" + this.getId());
//        params.put("return_url", "");
//
//        params.put("extends", "");
//
//        if (getParams() != null) {
//            params.putAll(getParams());
//        }
//
//        params.put("sign", SignUtil.getMD5SignLower(params, getMerchantMy()));
//
//        return requestData(getUrlpay(), RequestType.POST, params, new HashMap<String, String>(), "UTF-8", merchantId, sn, 0);
//    }
//
//    @Override
//    public NotifyInfo notify(Map<String, Object> params, String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        try {
//            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
//
//            if (!"success".equals(params.get("code"))) {
//                return null;
//            }
//
//            Map<String, Object> map = new TreeMap<String, Object>();
//            map.put("out_order_no", params.get("out_order_no"));
//            map.put("order_id", params.get("order_id"));
//            map.put("merch_id", params.get("merch_id"));
//            map.put("fee", params.get("fee"));
//            map.put("amount", params.get("amount"));
//            map.put("status", params.get("status"));
//            map.put("pay_time", params.get("pay_time"));
//            map.put("code", params.get("code"));
//            map.put("msg", params.get("msg"));
//
//            if (sign.equals(SignUtil.getMD5SignLower(map, merchantMy))) {//配对成功
//
////                return new Gson().fromJson(body, NotifyInfo.class);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//
//                NotifyInfo ni = new NotifyInfo();
//                ni.setSn((String) params.get("order_id"));
//                ni.setClientSn((String) params.get("out_order_no"));
////                String status=(String)params.get("code");
////                if("succ".equals(status)) {
////                    ni.setStatus("success");
////                }else {
////                    ni.setStatus(status);
////                }
//                ni.setStatus("success");
//
//                ni.setAmount(new BigDecimal((String) params.get("amount")));
//                ni.setActualAmount(new BigDecimal((String) params.get("amount")));
//                ni.setSign(sign);
////                ni.setPayTime(sdf.parse((String)params.get("pay_time")));
//                ni.setPayTime(new Date());
//                return ni;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
////    @Override
////    public CashInfo clientCash(Map < String, Object > data,PayCash cash,SysUser client,String domain,String urlcash,String keyname) {
////        return new CashInfo();
////    }
//
//    @Override
//    public ApiInfo createOrderRequestData(Long id, String sn, BigDecimal amount, String domain, Integer payType,
//                                          String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("merch_id", merchantNo);
//        params.put("product", "802");
//        params.put("order_id", sn);
//
//        params.put("amount", amount);
//        params.put("bank_code", "");
//
//        params.put("notify_url", domain + Constants.RETURN_URL + "/" + id);
//        params.put("return_url", "");
//
//        params.put("extends", "");
//
//        if (channelParams != null) params.putAll(channelParams);
//
//        Gson gson = new Gson();
//        ApiInfo apiInfo = new ApiInfo();
//
//        apiInfo.setSignData(gson.toJson(params));
//
//        apiInfo.setSignStr(SignUtil.joinKeyValue(new TreeMap<String, Object>(params), null, "&key=" + merchantMy, "&", true, "sign_type", "sign"));
//
//        String sign = SignUtil.getMD5SignLower(params, merchantMy);
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
