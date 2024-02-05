//
//package com.city.city_collector.channel.bean.clients;
//
//import java.io.UnsupportedEncodingException;
//import java.math.BigDecimal;
//import java.net.URLEncoder;
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
//import com.city.city_collector.channel.bean.OrderPreInfo;
//import com.city.city_collector.channel.util.SignUtil;
//import com.city.city_collector.channel.util.TimeUtil;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
///**
// * @author nb
// * @Description:
// */
//public class LeapGerryClientChannel extends ClientChannel {
//
//    @Override
//    public OrderInfo dealRequestData(String data) {
//        if (StringUtils.isBlank(data)) {
//            return null;
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        if (!"1".equals(jobj.get("code").getAsString())) {
//            return null;
//        }
//
//        OrderInfo oi = new OrderInfo();
//        oi.setFlag(true);
//        oi.setData(data);
//        jobj = jobj.get("data").getAsJsonObject();
//        oi.setSn("");
//        oi.setPayUrl(jobj.get("url").getAsString());
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
//        OrderPreInfo opi = new OrderPreInfo();
//        opi.setCharset("UTF-8");
//        opi.setReqType(0);
//        opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);
//
//
//        Map<String, Object> params = new TreeMap<String, Object>();
//
//
//        params.put("mch_id", getMerchantNo());
//        params.put("ptype", 1);
//        params.put("order_sn", sn);
//        params.put("money", amount);
//        params.put("goods_desc", sn);
//        params.put("client_ip", "127.0.0.1");
//
//        params.put("format", "page");
//
//        params.put("notify_url", domain + returnUrl + "/" + this.getId());
//        params.put("time", TimeUtil.getSystemTimeSecond());
//
//        if (getParams() != null) params.putAll(getParams());
//
//        params.put("sign", SignUtil.getMD5Sign(params, getMerchantMy()).toLowerCase());
//
//
//        try {
//            Map<String, Object> p = new HashMap<String, Object>();
//            Gson gson = new Gson();
//            p.put("json", URLEncoder.encode(gson.toJson(params), "utf-8"));
//            opi.setParams(p);
////            return opi;
//        } catch (UnsupportedEncodingException e) {
//
//            e.printStackTrace();
//        }
//
//        return requestData(getUrlpay(), RequestType.POST, params, new HashMap<String, String>(), "UTF-8", merchantId, sn, 0);
//    }
//
//    @Override
//    public NotifyInfo notify(Map<String, Object> params, String merchantNo, String merchantMy,
//                             Map<String, Object> channelParams) {
//        try {
//            if (!"1".equals(params.get("state"))) {
//                return null;
//            }
//            String sn = params.get("sh_order") == null ? "" : (String) params.get("sh_order");
//            String clientSn = params.get("pt_order") == null ? "" : (String) params.get("pt_order");
//
//            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
//            if (StringUtils.isBlank(sn) || StringUtils.isBlank(sign)) {
//                return null;
//            }
//
//            Map<String, Object> map = new TreeMap<String, Object>();
//            map.put("sh_order", params.get("sh_order"));
//            map.put("pt_order", params.get("pt_order"));
//            map.put("money", params.get("money"));
//            map.put("time", params.get("time"));
//            map.put("state", params.get("state"));
//            map.put("goods_desc", params.get("goods_desc"));
//
//            if (sign.equals(SignUtil.getMD5Sign(map, merchantMy))) {//配对成功
//
//                NotifyInfo ni = new NotifyInfo();
//                ni.setSn(sn);
//                ni.setClientSn(clientSn);
//                ni.setStatus("success");
//                ni.setAmount(new BigDecimal((String) params.get("money")));
//                ni.setActualAmount(new BigDecimal((String) params.get("money")));
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
//        //
//        return null;
//    }
//
//}
