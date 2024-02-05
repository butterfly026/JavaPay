//
//package com.city.city_collector.channel.bean.clients;
//
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.apache.commons.lang.StringUtils;
//
//import java.util.TreeMap;
//
//import com.city.city_collector.channel.bean.ApiInfo;
//import com.city.city_collector.channel.bean.ClientChannel;
//import com.city.city_collector.channel.bean.NotifyInfo;
//import com.city.city_collector.channel.bean.OrderInfo;
//import com.city.city_collector.channel.bean.ClientChannel.RequestType;
//import com.city.city_collector.channel.util.Constants;
//import com.city.city_collector.channel.util.SignUtil;
//import com.city.city_collector.common.util.MD5Util;
//
///**
// * @author nb
// * @Description:
// */
//public class HongXingClientChannel extends ClientChannel {
////    private final static String URL="http://hxzfpay.icu";
////
////    private final static String APPID="10033";
////
////    private final static String KEY="aarbpq2miradbtbv9pwpkxiqf4oc2xe6";
//
//    @Override
//    public OrderInfo dealRequestData(String data) {
//        System.out.println(data);
//        //
//        return null;
//    }
//
//    @Override
//    public OrderInfo createOrder(String sn, BigDecimal amount, String domain, Long payType, Long merchantId) {
//        OrderInfo oi = new OrderInfo();
//        oi.setFlag(true);
//        oi.setData("");
//        oi.setSn("");
//        oi.setPayUrl(domain + "/api/pay/formRequest?sn=" + sn);
//        oi.setName("");
//        oi.setCard("");
//        oi.setBankName("");
//
//        oi.setClientId(this.getClient() == null ? null : this.getClient().getId());
//        oi.setClientNo(this.getClient() == null ? null : this.getClient().getNo());
//        oi.setClientName(this.getClient() == null ? null : this.getClient().getName());
//
//        oi.setClientChannelId(this.getClient() == null ? null : this.getId());
//
//        return oi;
//    }
//
//    @Override
//    public NotifyInfo notify(Map<String, Object> params, String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        try {
//            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
//
//            //商户编号
//            String memberid = params.get("memberid") == null ? "" : params.get("memberid").toString();
//            //订单号
//            String orderid = params.get("orderid") == null ? "" : params.get("orderid").toString();
//            //支付金额
//            String amount = params.get("amount") == null ? "" : params.get("amount").toString();
//            //交易流水号
//            String transactionId = params.get("transaction_id") == null ? "" : params.get("transaction_id").toString();
//            //返回值
//            String returncode = params.get("returncode") == null ? "" : params.get("returncode").toString();
//            //交易时间
//            String datetime = params.get("datetime") == null ? "" : params.get("datetime").toString();
//            //拓展返回
//            String attach = params.get("attach") == null ? "" : params.get("attach").toString();
//
//            if (!memberid.equals(merchantNo)) {//APPID不合法
//                return null;
//            }
//            if (!"00".equals(returncode)) {
//                return null;
//            }
//
//            Map<String, Object> signs = new TreeMap<String, Object>();
//            signs.put("memberid", memberid);
//            signs.put("orderid", orderid);
//            signs.put("amount", amount);
//            signs.put("transaction_id", transactionId);
//            signs.put("returncode", returncode);
//            signs.put("datetime", datetime);
//            signs.put("attach", attach);
//
//            Iterator<Entry<String, Object>> it = signs.entrySet().iterator();
//            StringBuffer sbuf = new StringBuffer("");
//            while (it.hasNext()) {
//                Entry<String, Object> et = it.next();
//                System.out.println(String.valueOf(et.getValue()));
//                if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
//                    sbuf.append(et.getKey());
//                    sbuf.append("=");
//                    sbuf.append(String.valueOf(et.getValue()));
//                    sbuf.append("&");
//                }
//            }
//            sbuf.append("key=");
//            sbuf.append(merchantMy);
//            System.out.println(sbuf.toString());
//
//            if (sign.equals(MD5Util.MD5Encode(sbuf.toString(), "utf-8").toUpperCase())) {//配对成功
//
////                return new Gson().fromJson(body, NotifyInfo.class);
////                SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
//
//                NotifyInfo ni = new NotifyInfo();
//                ni.setSn(orderid);
//                ni.setClientSn(transactionId);
////                String status=(String)params.get("code");
////                if("succ".equals(status)) {
////                    ni.setStatus("success");
////                }else {
////                    ni.setStatus(status);
////                }
//                ni.setStatus("success");
//
//                ni.setAmount(new BigDecimal(amount));
//                ni.setActualAmount(new BigDecimal(amount));
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
//    public String getNotifySuccess() {
//        return "OK";
//    }
//
//    public String getFormBody(String sn, BigDecimal amount, String domain, Long payType) {
////        if(payType==null) return null;
////        String payBankcode="";
////        if(payType.longValue()==8L) {//网银
////            payBankcode="917";
////        }else if(payType.longValue()==2L) {//支付宝H5
////            payBankcode="921";
////        }else {
////            return null;
////        }
//
////        String payUrl=URL+"/Pay_Index.html";
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = sdf.format(new Date());
//
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("pay_memberid", getMerchantNo());
//        params.put("pay_orderid", sn);
//        params.put("pay_applydate", date);
//
////        params.put("pay_bankcode", payBankcode);
//
//
//        params.put("pay_notifyurl", domain + asyncUrl + "/" + this.getId());
//        params.put("pay_callbackurl", domain + "/api/pay/success");
//
//        params.put("pay_amount", amount);
//
////        params.put("pay_productname", sn);
//
//        if (getParams() != null) {
//            params.putAll(getParams());
//        }
//
//        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
//        StringBuffer sbuf = new StringBuffer("");
//        while (it.hasNext()) {
//            Entry<String, Object> et = it.next();
//            System.out.println(String.valueOf(et.getValue()));
//            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
//                sbuf.append(et.getKey());
//                sbuf.append("=");
//                sbuf.append(String.valueOf(et.getValue()));
//                sbuf.append("&");
//            }
//        }
//        sbuf.append("key=");
//        sbuf.append(getMerchantMy());
//        System.out.println(sbuf.toString());
//        params.put("pay_md5sign", MD5Util.MD5Encode(sbuf.toString(), "utf-8").toUpperCase());
////        params.put("pay_md5sign", MD5Util.MD5Encode("pay_amount="+amount+"&pay_applydate="+date+"&pay_bankcode="+payBankcode+"&pay_callbackurl="+domain+asyncUrl+"/"+this.getId()+"&pay_memberid="+APPID+"&pay_notifyurl="+domain+asyncUrl+"/"+this.getId()+"&pay_orderid="+sn+"&key="+KEY, "utf-8").toUpperCase());
//
//        sbuf = new StringBuffer("");
//        sbuf.append("<form action=\"" + getUrlpay() + "\" name=\"form1\" method=\"post\" >");
//
//        it = params.entrySet().iterator();
//        while (it.hasNext()) {
//            Entry<String, Object> et = it.next();
//            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
//                sbuf.append("<input type=\"hidden\" name=\"" + et.getKey() + "\" value=\"" + String.valueOf(et.getValue()) + "\" />");
//            }
//        }
////        sbuf.append("<input type=\"hidden\" name=\"bank\" value=\"ICBC\" />");
//
//        sbuf.append("</form>");
//        return sbuf.toString();
//    }
//
//    @Override
//    public ApiInfo createOrderRequestData(Long id, String sn, BigDecimal amount, String domain, Integer payType,
//                                          String merchantNo, String merchantMy, Map<String, Object> channelParams) {
//        ApiInfo apiInfo = new ApiInfo();
//        apiInfo.setGotype(1);
//        return apiInfo;
//    }
//
//
//    public String getFormBody_test(Long id, String sn, BigDecimal amount, String domain, Integer payType,
//                                   String merchantNo, String merchantMy, Map<String, Object> channelParams, String urlpay) {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = sdf.format(new Date());
//
//        Map<String, Object> params = new TreeMap<String, Object>();
//        params.put("pay_memberid", merchantNo);
//        params.put("pay_orderid", sn);
//        params.put("pay_applydate", date);
//
////      params.put("pay_bankcode", payBankcode);
//
//
//        params.put("pay_notifyurl", domain + Constants.ASYNC_URL + "/" + id);
//        params.put("pay_callbackurl", domain + "/api/pay/success");
//
//        params.put("pay_amount", amount);
//
////      params.put("pay_productname", sn);
//
//        if (channelParams != null) params.putAll(channelParams);
//
//        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
//        StringBuffer sbuf = new StringBuffer("");
//        while (it.hasNext()) {
//            Entry<String, Object> et = it.next();
//            System.out.println(String.valueOf(et.getValue()));
//            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
//                sbuf.append(et.getKey());
//                sbuf.append("=");
//                sbuf.append(String.valueOf(et.getValue()));
//                sbuf.append("&");
//            }
//        }
//        sbuf.append("key=");
//        sbuf.append(merchantMy);
//        System.out.println(sbuf.toString());
//        params.put("pay_md5sign", MD5Util.MD5Encode(sbuf.toString(), "utf-8").toUpperCase());
////      params.put("pay_md5sign", MD5Util.MD5Encode("pay_amount="+amount+"&pay_applydate="+date+"&pay_bankcode="+payBankcode+"&pay_callbackurl="+domain+asyncUrl+"/"+this.getId()+"&pay_memberid="+APPID+"&pay_notifyurl="+domain+asyncUrl+"/"+this.getId()+"&pay_orderid="+sn+"&key="+KEY, "utf-8").toUpperCase());
//
//        sbuf = new StringBuffer("");
//        sbuf.append("<form action=\"" + urlpay + "\" name=\"form1\" method=\"post\" >");
//
//        it = params.entrySet().iterator();
//        while (it.hasNext()) {
//            Entry<String, Object> et = it.next();
//            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
//                sbuf.append("<input type=\"hidden\" name=\"" + et.getKey() + "\" value=\"" + String.valueOf(et.getValue()) + "\" />");
//            }
//        }
////      sbuf.append("<input type=\"hidden\" name=\"bank\" value=\"ICBC\" />");
//
//        sbuf.append("</form>");
//        return sbuf.toString();
//    }
//
//}
