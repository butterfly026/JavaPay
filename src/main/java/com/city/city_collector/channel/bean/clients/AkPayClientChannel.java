//
//package com.city.city_collector.channel.bean.clients;
//
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
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
//import com.city.city_collector.channel.bean.CashNotifyInfo;
//import com.city.city_collector.channel.bean.ClientChannel;
//import com.city.city_collector.channel.bean.NotifyInfo;
//import com.city.city_collector.channel.bean.OrderInfo;
//import com.city.city_collector.channel.util.Constants;
//import com.city.city_collector.channel.util.SignUtil;
//import com.city.city_collector.common.util.MD5Util;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
///**
// * @author nb
// * @Description: AK通道
// */
//public class AkPayClientChannel extends ClientChannel {
//    //域名
////    private final static String URL="https://api.kuaipay.info";
////
////    private final static String APPID="201909151500453813";
////
////    private final static String KEY="2af0f809f73e41e5ac895f60c0c9d48e";
//
//    @Override
//    public OrderInfo dealRequestData(String data) {
//        if (StringUtils.isBlank(data)) {
//            return null;
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        boolean flag = jobj.get("success").getAsBoolean();
//        if (!flag) return null;
//
//        OrderInfo oi = new OrderInfo();
//        oi.setFlag(true);
//        oi.setData(data);
//        jobj = jobj.get("data").getAsJsonObject();
//        oi.setSn(jobj.get("sn").getAsString());
//        oi.setPayUrl(jobj.get("payUrl").getAsString());
//        oi.setName(jobj.get("name").getAsString());
//        oi.setCard(jobj.get("card").getAsString());
//        oi.setBankName(jobj.get("bankName").getAsString());
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
//
//    @Override
//    public OrderInfo createOrder(String sn, BigDecimal amount, String domain, Long payType, Long merchantId) {
////        String payUrl=URL+"/api/v1/pay";
//
//        //数据规则  [{"appId":"${APPID}"},{"returnUrl":"${DOMAIN}/${ASYNCURL}"}]
//
//        //签名    字段名如(sign)  签名方式：md5小写，md5大写
//
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("appId", getMerchantNo());
//        params.put("outTradeNo", sn);
//        params.put("amount", amount);
////        params.put("payType", "CP");
//        params.put("nonceStr", System.currentTimeMillis() + "");
//        params.put("asyncUrl", domain + asyncUrl + "/" + this.getId());
//        params.put("returnUrl", domain + returnUrl + "/" + this.getId());
////        params.put("ip", "127.0.0.1");
//
//        if (getParams() != null) params.putAll(getParams());
//
//        params.put("sign", SignUtil.getMD5Sign(params, getMerchantMy()));
//
//        return requestData(getUrlpay(), RequestType.POST, params, new HashMap<String, String>(), "UTF-8", merchantId, sn, 0);
//    }
//
//
//    @Override
//    public NotifyInfo notify(Map<String, Object> params, String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        try {
//            String body = params.get("body") == null ? "" : (String) params.get("body");
//            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
//            if (StringUtils.isBlank(body) || StringUtils.isBlank(sign)) {
//                return null;
//            }
//            Map<String, Object> map = new TreeMap<String, Object>();
//            map.put("body", body);
//            if (sign.equals(SignUtil.getMD5Sign(map, merchantMy))) {//配对成功
//
////                return new Gson().fromJson(body, NotifyInfo.class);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                JsonParser jp = new JsonParser();
//                JsonObject jobj = jp.parse(body).getAsJsonObject();
//                NotifyInfo ni = new NotifyInfo();
//                ni.setSn(jobj.get("outTradeNo").getAsString());
//                ni.setClientSn(jobj.get("sn").getAsString());
//                ni.setStatus(jobj.get("status").getAsString());
//                ni.setAmount(jobj.get("amount").getAsBigDecimal());
//                ni.setActualAmount(jobj.get("actualAmount").getAsBigDecimal());
//                ni.setSign(sign);
//                ni.setPayTime(sdf.parse(jobj.get("payTime").getAsString()));
//                return ni;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public CashInfo clientCash(Map<String, Object> data, PayCash cash, SysUser client, String domain, String urlcash, String keyname) {
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("appId", client.getMerchantNo());
//        params.put("outTradeNo", cash.getSn());
//        params.put("password", "");
////        params.put("payType", "CP");
//        params.put("amount", cash.getClientMoney());
//
//        params.put("name", cash.getBankAccname());
//        params.put("card", cash.getBankAccno());
//        params.put("bankBranch", cash.getBankName());
//        params.put("bankCode", cash.getBankCode());
//
//        params.put("asyncUrl", domain + "/api/pay/cashnotify/" + keyname + "_" + client.getId());
//        params.put("returnUrl", domain + "/api/pay/cashnotify/" + keyname + "_" + client.getId());
//        params.put("nonceStr", System.currentTimeMillis() + "");
//
//        params.put("sign", SignUtil.getMD5Sign(params, client.getMerchantMy()));
//
//        return requestData_Cash(urlcash, RequestType.POST, params, new HashMap<String, String>(), "UTF-8", cash.getSn(), 0);
//    }
//
//    public CashInfo dealRequestData_Cash(String data) {
//
//        if (StringUtils.isBlank(data)) {
//            return new CashInfo(false, "发起代付失败!");
//        }
//        JsonParser jp = new JsonParser();
//
//        JsonObject jobj = jp.parse(data).getAsJsonObject();
//        boolean flag = jobj.get("success").getAsBoolean();
//        if (!flag) return new CashInfo(false, "发起代付失败:" + jobj.get("message"));
//
//        CashInfo ci = new CashInfo(true, "发起代付成功");
//
//        jobj = jobj.get("data").getAsJsonObject();
//        ci.setSn(jobj.get("sn").getAsString());
//
//        return ci;
//    }
//
//    /**
//     * 提现回调
//     */
//    public CashNotifyInfo cashnotify(Map<String, Object> params, String merchantNo, String merchantMy) {
//        try {
//
//            String body = params.get("body") == null ? "" : (String) params.get("body");
//            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
//            if (StringUtils.isBlank(body) || StringUtils.isBlank(sign)) {
//                return null;
//            }
//            Map<String, Object> map = new TreeMap<String, Object>();
//            map.put("body", body);
//            if (sign.equals(SignUtil.getMD5Sign(map, merchantMy))) {//配对成功
//                JsonParser jp = new JsonParser();
//                JsonObject jobj = jp.parse(body).getAsJsonObject();
//
//                CashNotifyInfo cni = new CashNotifyInfo(true);
//                cni.setSn(jobj.get("outTradeNo").getAsString());
//                cni.setClientSn(jobj.get("sn").getAsString());
//                cni.setAmount(new BigDecimal(jobj.get("amount").getAsString()));
//                if ("success".equals(jobj.get("status").getAsString())) {
//                    cni.setDealStatus(true);
//                    cni.setStatus(true);
//                } else if ("failure".equals(jobj.get("status").getAsString())) {
//                    cni.setDealStatus(false);
//                    cni.setStatus(true);
//                } else {
//                    cni.setStatus(false);
//                }
//
//                return cni;
//
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return new CashNotifyInfo(false);
//    }
//
//
//    @Override
//    public ApiInfo createOrderRequestData(Long id, String sn, BigDecimal amount, String domain, Integer payType,
//                                          String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("appId", merchantNo);
//        params.put("outTradeNo", sn);
//        params.put("amount", amount);
////        params.put("payType", "CP");
//        params.put("nonceStr", System.currentTimeMillis() + "");
//        params.put("asyncUrl", domain + Constants.ASYNC_URL + "/" + id);
//        params.put("returnUrl", domain + Constants.RETURN_URL + "/" + id);
////        params.put("ip", "127.0.0.1");
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
//        String sign = SignUtil.getMD5Sign(params, merchantMy);
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
