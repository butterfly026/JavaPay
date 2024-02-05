//
//package com.city.city_collector.channel.bean.clients;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//import java.util.Map.Entry;
//
//import org.apache.commons.lang.StringUtils;
//
//import com.city.city_collector.admin.pay.entity.PayCash;
//import com.city.city_collector.admin.system.entity.SysUser;
//import com.city.city_collector.channel.bean.ApiInfo;
//import com.city.city_collector.channel.bean.CashInfo;
//import com.city.city_collector.channel.bean.CashNotifyInfo;
//import com.city.city_collector.channel.bean.ClientChannel;
//import com.city.city_collector.channel.bean.NotifyInfo;
//import com.city.city_collector.channel.bean.OrderInfo;
//import com.city.city_collector.channel.util.Constants;
//import com.city.city_collector.channel.bean.ClientChannel.RequestType;
//import com.city.city_collector.common.util.MD5Util;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.JsonPrimitive;
//import com.google.gson.JsonSerializationContext;
//import com.google.gson.JsonSerializer;
//
///**
// * @author nb
// * @Description:
// */
//public class MingXingClientChannel extends ClientChannel {
//
//    @Override
//    public OrderInfo dealRequestData(String data) {
//        if (StringUtils.isBlank(data)) {
//            return null;
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        if (!jobj.get("result").getAsBoolean()) {
//            return null;
//        }
//        data = jobj.get("data").getAsString();
////        jobj=jobj.get("data").getAsString();
//        jobj = jp.parse(data).getAsJsonObject();
//
//        OrderInfo oi = new OrderInfo();
//        oi.setFlag(true);
//        oi.setData(data);
//
//        oi.setSn("");
//        oi.setPayUrl(jobj.get("payUrl").getAsString());
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
//        Map<String, Object> params = new TreeMap<String, Object>();
//
//        BigDecimal h = new BigDecimal("100");
//
//        params.put("order_id", sn);
//        params.put("amount", amount.multiply(h).setScale(0) + "");
//
//        params.put("channelkey", getMerchantNo());
//        params.put("client_ip", "113.52.132.164");
//
//        params.put("sign_type", "MD5");
//
//        params.put("body", "order " + sn);
//
//        params.put("payType", 2);
//
//        params.put("callback", domain + Constants.RETURN_URL + "_json/" + this.getId());
////        params.put("ip", "127.0.0.1");
//
//        if (getParams() != null) params.putAll(getParams());
//
////        if(params.get("payType")!=null) params.put("payType", Math.round(Double.parseDouble(params.get("payType").toString())));
//
//        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
//        StringBuffer sbuf = new StringBuffer("");
//        while (it.hasNext()) {
//            Entry<String, Object> et = it.next();
//
//            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
//                sbuf.append(et.getKey());
//                sbuf.append("=");
//                sbuf.append(String.valueOf(et.getValue()));
//                sbuf.append("&");
//            }
//        }
//        sbuf.append("signKey=");
//        sbuf.append(getMerchantMy());
//        System.out.println(sbuf.toString());
//
//        params.put("sign", MD5Util.MD5Encode(sbuf.toString(), "utf-8").toUpperCase());
////        params.put("sign", SignUtil.getMD5Sign(params, getMerchantMy()));
//
//        return requestData(getUrlpay(), RequestType.POST, params, new HashMap<String, String>(), "UTF-8", merchantId, sn, 1);
//    }
//
//    @Override
//    public NotifyInfo notify(Map<String, Object> params, String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        String sign = params.get("sign") == null ? "" : (String) params.get("sign");
//
//        String state = params.get("state") == null ? "" : params.get("state").toString();
//
//
//        if (!"00".equals(state)) {
//            return null;
//        }
//
//        Map<String, Object> map = new TreeMap<String, Object>();
//        map.put("third_trade_no", params.get("third_trade_no"));
//        map.put("amount", params.get("amount"));
//        map.put("sign_type", params.get("sign_type"));
//        map.put("state", params.get("state"));
//
//        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
//        StringBuffer sbuf = new StringBuffer("");
//        while (it.hasNext()) {
//            Entry<String, Object> et = it.next();
//
//            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
//                sbuf.append(et.getKey());
//                sbuf.append("=");
//                sbuf.append(String.valueOf(et.getValue()));
//                sbuf.append("&");
//            }
//        }
//        sbuf.append("Key=");
//        sbuf.append(merchantMy);
//        System.out.println(sbuf.toString());
//
//        if (sign.equals(MD5Util.MD5Encode(sbuf.toString(), "utf-8").toUpperCase())) {
//            NotifyInfo ni = new NotifyInfo();
//            ni.setSn((String) params.get("third_trade_no"));
//            ni.setClientSn("");
////            String status=(String)params.get("code");
////            if("succ".equals(status)) {
////                ni.setStatus("success");
////            }else {
////                ni.setStatus(status);
////            }
//            ni.setStatus("success");
//            BigDecimal h = new BigDecimal("100.00");
//
//            ni.setAmount(new BigDecimal((String) params.get("amount")).divide(h));
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
//        Map<String, Object> params = new TreeMap<String, Object>();
//
//        BigDecimal h = new BigDecimal("100");
//
//        params.put("order_id", sn);
//        params.put("amount", amount.multiply(h).setScale(0) + "");
//
//        params.put("channelkey", merchantNo);
//        params.put("client_ip", "113.52.132.164");
//
//        params.put("sign_type", "MD5");
//
//        params.put("body", "order_" + sn);
//
//        params.put("payType", 2);
//
//        params.put("callback", domain + returnUrl + "_json/" + id);
//
//        if (channelParams != null) params.putAll(channelParams);
//
////        if(params.get("payType")!=null) params.put("payType", Math.round(Double.parseDouble(params.get("payType").toString())));
//
//
//        Gson gson = new Gson();
//        ApiInfo apiInfo = new ApiInfo();
//
//        apiInfo.setSignData(gson.toJson(params));
//
//        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
//        StringBuffer sbuf = new StringBuffer("");
//        while (it.hasNext()) {
//            Entry<String, Object> et = it.next();
//
//            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
//                sbuf.append(et.getKey());
//                sbuf.append("=");
//                sbuf.append(String.valueOf(et.getValue()));
//                sbuf.append("&");
//            }
//        }
//        sbuf.append("signKey=");
//        sbuf.append(merchantMy);
//
//        apiInfo.setSignStr(sbuf.toString());
//
//        String sign = MD5Util.MD5Encode(sbuf.toString(), "utf-8").toUpperCase();
//        apiInfo.setSign(sign);
//
//        params.put("sign", sign);
//
//        apiInfo.setRequstData(gson.toJson(params));
//        apiInfo.setDatas(params);
//        apiInfo.setGotype(0);
//        apiInfo.setReqType(1);
//        return apiInfo;
//    }
//
//    @Override
//    public String getNotifySuccess() {
//        return "SUCCESS";
//    }
//
//    @Override
//    public CashInfo clientCash(Map<String, Object> data, PayCash cash, SysUser client, String domain, String urlcash,
//                               String keyname) {
//        Map<String, Object> params = new TreeMap<String, Object>();
//        BigDecimal h = new BigDecimal("100");
//
//        params.put("order_id", cash.getSn());
//        params.put("amount", cash.getClientMoney().multiply(h).setScale(0) + "");
//
//        params.put("channelkey", client.getMerchantNo());
//
//        List<TreeMap<String, String>> bankData = new ArrayList<TreeMap<String, String>>();
//        TreeMap<String, String> bank = new TreeMap<String, String>();
//        bank.put("name", cash.getBankAccname());
//        bank.put("bank", cash.getBankName());
//        bank.put("address", cash.getBankSubname());
//        bank.put("account", cash.getBankAccno());
//        bank.put("amount", cash.getClientMoney().multiply(h).setScale(0) + "");
//        if (StringUtils.isNotBlank(cash.getBankProvince())) bank.put("privince", cash.getBankProvince());
//        if (StringUtils.isNotBlank(cash.getBankCity())) bank.put("city", cash.getBankCity());
//
//
//        bankData.add(bank);
//
//        params.put("data", new Gson().toJson(bankData));
////        params.put("callback", domain+"/api/pay/cashnotify_json/"+keyname+"_"+client.getId());
//
//        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
//        StringBuffer sbuf = new StringBuffer("");
//        while (it.hasNext()) {
//            Entry<String, Object> et = it.next();
//
//            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
//                sbuf.append(et.getKey());
//                sbuf.append("=");
//                sbuf.append(String.valueOf(et.getValue()));
//                sbuf.append("&");
//            }
//        }
//        sbuf.append("signKey=");
//        sbuf.append(client.getMerchantMy());
//        System.out.println(sbuf.toString());
//
//        params.put("sign", MD5Util.MD5Encode(sbuf.toString(), "utf-8").toUpperCase());
//        System.out.println(urlcash);
//        CashInfo ci = requestData_Cash(urlcash, RequestType.POST, params, new HashMap<String, String>(), "UTF-8", cash.getSn(), 1);
////        ci.setSn(cash.getSn());
//        return ci;
//    }
//
//    @Override
//    public CashInfo dealRequestData_Cash(String data) {
//        System.out.println(data);
//        if (StringUtils.isBlank(data)) {
//            return new CashInfo(false, "发起代付失败!");
//        }
//        JsonParser jp = new JsonParser();
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        if (!"00".equals(jobj.get("code").getAsString())) {
//            return new CashInfo(false, jobj.get("message") == null ? "未知错误" : jobj.get("message").getAsString());
//        }
//
//        return new CashInfo(true, "发起代付成功！");
//    }
//
//    @Override
//    public CashNotifyInfo cashnotify(Map<String, Object> params, String merchantNo, String merchantMy) {
//        //
//        return super.cashnotify(params, merchantNo, merchantMy);
//    }
//
//
//}
