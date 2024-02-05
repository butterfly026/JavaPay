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
//import com.city.city_collector.common.util.MD5Util;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
///**
// * @author nb
// * @Description:
// */
//public class YouFuClientChannel extends ClientChannel {
//
//    @Override
//    public OrderInfo dealRequestData(String data) {
//        if (StringUtils.isBlank(data)) {
//            return null;
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//
//        if (!"1".equals(jobj.get("status").getAsString())) {
//            return null;
//        }
//
//
//        OrderInfo oi = new OrderInfo();
//        oi.setFlag(true);
//        oi.setData(data);
////        jobj=jobj.get("data").getAsJsonObject();
//        oi.setSn("");
//        oi.setPayUrl(jobj.get("next_url").getAsString());
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
//        params.put("customerid", getMerchantNo());
//        params.put("version", "1.0");
//        params.put("sdorderno", sn);
//
//        params.put("total_fee", amount.setScale(2));
////        params.put("payType", "CP");
////        params.put("nonceStr", System.currentTimeMillis()+"");
//        params.put("notifyurl", domain + asyncUrl + "/" + this.getId());
//        params.put("returnurl", domain + "/api/pay/success");
//        params.put("json", 1);
//
//        if (getParams() != null) params.putAll(getParams());
//
//
//        params.put("sign", MD5Util.MD5Encode("version=" + params.get("version") + "&customerid=" + params.get("customerid") + "&total_fee=" + params.get("total_fee") + "&sdorderno=" + params.get("sdorderno") + "&notifyurl=" + params.get("notifyurl") + "&returnurl=" + params.get("returnurl") + "&" + getMerchantMy(), "UTF-8").toLowerCase());
//
//        return requestData(getUrlpay(), RequestType.POST, params, new HashMap<String, String>(), "UTF-8", merchantId, sn, 0);
//    }
//
//    @Override
//    public NotifyInfo notify(Map<String, Object> params, String merchantNo, String merchantMy,
//                             Map<String, Object> channelParams) {
//        try {
//            String status = params.get("status") == null ? "" : (String) params.get("status");
//            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
//            if (!"1".equals(status) || StringUtils.isBlank(sign)) {
//                return null;
//            }
//
//            String sign1 = MD5Util.MD5Encode("customerid=" + params.get("customerid") + "&status=" + params.get("status") + "&sdpayno=" + params.get("sdpayno") + "&sdorderno=" + params.get("sdorderno") + "&total_fee=" + params.get("total_fee") + "&paytype=" + params.get("paytype") + "&" + merchantMy, "UTF-8").toLowerCase();
//
//            if (sign.equals(sign1)) {//配对成功
//
//                NotifyInfo ni = new NotifyInfo();
//                ni.setSn((String) params.get("sdorderno"));
//                ni.setClientSn((String) params.get("sdpayno"));
////                String status=(String)params.get("code");
////                if("succ".equals(status)) {
////                    ni.setStatus("success");
////                }else {
////                    ni.setStatus(status);
////                }
//                ni.setStatus("success");
//
//                ni.setAmount(new BigDecimal((String) (params.get("total_fee"))));
//                ni.setActualAmount(new BigDecimal((String) (params.get("total_fee"))));
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
//        params.put("customerid", merchantNo);
//        params.put("version", "1.0");
//        params.put("sdorderno", sn);
//
//        params.put("total_fee", amount.setScale(2));
////        params.put("payType", "CP");
////        params.put("nonceStr", System.currentTimeMillis()+"");
//        params.put("notifyurl", domain + Constants.ASYNC_URL + "/" + id);
//        params.put("returnurl", domain + "/api/pay/success");
//        params.put("json", 1);
//
//
//        if (channelParams != null) params.putAll(channelParams);
//
//        Gson gson = new Gson();
//        ApiInfo apiInfo = new ApiInfo();
//
//        apiInfo.setSignData(gson.toJson(params));
//
//        apiInfo.setSignStr("version=" + params.get("version") + "&customerid=" + params.get("customerid") + "&total_fee=" + params.get("total_fee") + "&sdorderno=" + params.get("sdorderno") + "&notifyurl=" + params.get("notifyurl") + "&returnurl=" + params.get("returnurl") + "&" + merchantMy);
//
//        String sign = MD5Util.MD5Encode("version=" + params.get("version") + "&customerid=" + params.get("customerid") + "&total_fee=" + params.get("total_fee") + "&sdorderno=" + params.get("sdorderno") + "&notifyurl=" + params.get("notifyurl") + "&returnurl=" + params.get("returnurl") + "&" + merchantMy, "UTF-8").toLowerCase();
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
//    @Override
//    public String getNotifySuccess() {
//        return "success";
//    }
//
//    @Override
//    public CashInfo clientCash(Map<String, Object> data, PayCash cash, SysUser client, String domain, String urlcash,
//                               String keyname) {
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("customerid", client.getMerchantNo());
//        params.put("version", "1.0");
//        params.put("sdorderno", cash.getSn());
//        params.put("bankcode", cash.getBankCode());
//
//        params.put("bankno", cash.getBankAccno());
//        params.put("customer_name", cash.getBankAccname());
//        params.put("customer_phone", cash.getBankAccmobile());
//
//
//        params.put("total_fee", cash.getClientMoney().setScale(2));
//        params.put("notifyurl", domain + "/api/pay/cashnotify/" + keyname + "_" + client.getId());
//
//        params.put("sign", MD5Util.MD5Encode("version=" + params.get("version") + "&customerid=" + params.get("customerid") + "&sdorderno=" + params.get("sdorderno") + "&bankcode=" + params.get("bankcode") + "&bankno=" + params.get("bankno") + "&customer_name=" + params.get("customer_name") + "&customer_phone=" + params.get("customer_phone") + "&total_fee=" + params.get("total_fee") + "&" + client.getMerchantMy(), "UTF-8").toLowerCase());
//        return null;
//    }
//
//    @Override
//    public CashInfo dealRequestData_Cash(String data) {
//        if (StringUtils.isBlank(data)) {
//            return new CashInfo(false, "发起代付失败!");
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        int status = jobj.get("status").getAsInt();
//        if (status != 1) {
//            return new CashInfo(false, "发起代付失败:" + jobj.get("msg"));
//        }
//
//        CashInfo ci = new CashInfo(true, "发起代付成功");
//
//        jobj = jobj.get("data").getAsJsonObject();
//        ci.setSn(jobj.get("sdorderno").getAsString());
//
//        return ci;
//    }
//
//
//}
