
package com.city.city_collector.channel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.channel.bean.OrderRequestInfo;
import com.city.city_collector.channel.bean.PaytmClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paytm.pg.merchant.PaytmChecksum;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author nb
 * @Description:paytm通道管理
 */
public class PaytmChannelManager {

    private final static PaytmChannelManager pcm = new PaytmChannelManager();

    private PaytmChannelManager() {
    }

    public static PaytmChannelManager getInstance() {
        return pcm;
    }

    public final static MediaType mediaType = MediaType.parse("application/json");

    //    public final static String WEBSITE="WEBSTAGING";
    public final static String WEBSITE = "DEFAULT";

    private Map<Long, Vector<PaytmClient>> paytmMaps = new HashMap<Long, Vector<PaytmClient>>();

    private Map<Long, Long> paytmIndex = new HashMap<Long, Long>();

//    private int listIndex=0;

    //下单链接 - 测试环境
//    public final static String ORDER_URL="https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction";

    //下单链接 - 正式环境
    public final static String ORDER_URL = "https://securegw.paytm.in/theia/api/v1/initiateTransaction";

    //查询订单状态 - 测试环境
//    public final static String QUERY_ORDER_STATUS_URL="https://securegw-stage.paytm.in/v3/order/status";

    //查询订单状态 - 正式环境
    public final static String QUERY_ORDER_STATUS_URL = "https://securegw.paytm.in/v3/order/status";

    //流程交易 - 测试环境
//    public final static String PROCESS_TRANSACTION_ORDER_URL="https://securegw-stage.paytm.in/theia/api/v1/processTransaction?";

    //流程交易 - 正式环境
    public final static String PROCESS_TRANSACTION_ORDER_URL = "https://securegw.paytm.in/theia/api/v1/processTransaction?";

    public void updateChannelData(Map<Long, Vector<PaytmClient>> paytmMaps) {

        synchronized (this.paytmMaps) {
            //ID列表
            List<Long> idList = new ArrayList<Long>();
            for (Entry<Long, Vector<PaytmClient>> et : paytmMaps.entrySet()) {
                idList.add(et.getKey());
            }

            //old ID列表
            List<Long> oldidList = new ArrayList<Long>();
            for (Entry<Long, Vector<PaytmClient>> et : this.paytmMaps.entrySet()) {
                oldidList.add(et.getKey());
            }

            if (this.paytmMaps.isEmpty()) {  //首次初始化
                this.paytmMaps = paytmMaps;
                paytmIndex = new HashMap<Long, Long>();
                for (Long id : idList) {
                    paytmIndex.put(id, 0L);
                }
            } else {

                //移除不存在的数据
                for (Long oid : oldidList) {
                    boolean flag = false;
                    for (Long id : idList) {
                        if (oid.equals(id)) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        this.paytmMaps.remove(oid);
                    }
                }

                //添加新数据，更新已有数据
                for (Long id : idList) {
                    this.paytmMaps.put(id, paytmMaps.get(id));
                    this.paytmIndex.put(id, 0L);
                }
            }

            System.out.println(new Gson().toJson(this.paytmMaps));
        }

    }

    /**
     * 下单
     *
     * @param sn
     * @param amount
     * @param domain
     * @return
     * @author:nb
     */
    public OrderRequestInfo createPayOrder(Long mcid, String sn, BigDecimal amount, String domain) {
        PaytmClient paytmClient = getPaytmClient(mcid, amount);
        if (paytmClient == null) {
            return new OrderRequestInfo(false, "", "", "");
        }
        OrderRequestInfo ori = orderRequest(sn, amount, domain, paytmClient.getMid(), paytmClient.getMkey());
        if (ori.getStatus()) {//下单成功
            ori.setClientChannelId(paytmClient.getId());
            ori.setClientId(paytmClient.getClientId());
            ori.setClientNo(paytmClient.getClientNo());
            ori.setMerchantChannelId(paytmClient.getMerchantChannelId());
            ori.setPayUrl(domain + "/api/paytm/paypre?sn=" + sn);
        }
        return ori;
    }

    /**
     * 获取一个可以使用的paytm商户
     *
     * @return
     * @author:nb
     */
    public PaytmClient getPaytmClient(Long mcid, BigDecimal amount) {
        synchronized (paytmMaps) {
            //获取索引
            if (paytmIndex.get(mcid) == null) {
                paytmIndex.put(mcid, 0L);
            }
            int listIndex = paytmIndex.get(mcid).intValue();

            Vector<PaytmClient> clients = paytmMaps.get(mcid);
            if (clients == null || clients.isEmpty()) {
                return null;
            }

            if (listIndex >= clients.size()) {
                listIndex = 0;
            }
            //获取可用的数组下标
            boolean flag = false;
            PaytmClient paytmClient = null;
            for (int i = listIndex; i < clients.size(); i++) {
                paytmClient = clients.get(i);
                if (validPaytmClient(paytmClient, amount)) {
                    listIndex = i + 1;
                    flag = true;
                    return paytmClient;
                }
            }
            if (!flag) {
                for (int i = 0; i < listIndex; i++) {
                    paytmClient = clients.get(i);
                    if (validPaytmClient(paytmClient, amount)) {
                        listIndex = i + 1;
                        return paytmClient;
                    }
                }
            }
            paytmIndex.put(mcid, Long.valueOf(listIndex));
        }
        return null;
    }

    /**
     * 获取一个有效的paytm客户对象
     *
     * @param pc
     * @return
     * @author:nb
     */
    public boolean validPaytmClient(PaytmClient pc, BigDecimal amount) {
        //判断通道是否开启
        if (pc.getMinTime() != null && pc.getMaxTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            Long time = Long.parseLong(sdf.format(new Date()));
            if (time < pc.getMinTime().longValue() || time > pc.getMaxTime().longValue()) {//不在开启时间段内
                return false;
            }
        }

        Integer type = pc.getType();
        //通道模式
        if (type == null || type.intValue() == 0) {
            if (pc.getMinMoney() != null && pc.getMaxMoney() != null) {
                if (amount.compareTo(pc.getMinMoney()) < 0 || amount.compareTo(pc.getMaxMoney()) > 0) {
                    return false;
                }
            }

        } else {
            boolean flag = false;
            for (BigDecimal m : pc.getMoneyList()) {
                if (m.compareTo(amount) == 0) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }

        return true;
    }

    //下单请求
    public OrderRequestInfo orderRequest(String sn, BigDecimal amount, String domain, String mid, String mkey) {
        try {
            JsonObject paytmParams = new JsonObject();

            JsonObject body = new JsonObject();
            body.addProperty("requestType", "Payment");
            body.addProperty("mid", mid);
            body.addProperty("websiteName", WEBSITE);
            body.addProperty("orderId", sn);
            body.addProperty("callbackUrl", domain + "/api/paytm/notify");

            JsonObject txnAmount = new JsonObject();
            txnAmount.addProperty("value", amount);
            txnAmount.addProperty("currency", "INR");

            JsonObject userInfo = new JsonObject();
            //Unique reference ID for every customer which is generated by merchant. Special characters allowed in CustId are @, ! ,_ ,$, .
            //商家生成的每个客户的唯一参考ID。 CustId中允许的特殊字符是@ ! _ $ .
            userInfo.addProperty("custId", "CUST_001");

            body.add("txnAmount", txnAmount);
            body.add("userInfo", userInfo);

            /*
             * Generate checksum by parameters we have in body
             * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
             * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
             */
            String checksum = PaytmChecksum.generateSignature(body.toString(), mkey);

            JsonObject head = new JsonObject();
            head.addProperty("signature", checksum);
            head.addProperty("version", "V1");
            head.addProperty("channelId", "WAP");
            head.addProperty("requestTimestamp", System.currentTimeMillis() / 1000);

            paytmParams.add("body", body);
            paytmParams.add("head", head);
            //请求数据
            String post_data = paytmParams.toString();
            System.out.println("请求数据：" + post_data);

            //数据请求
            OkHttpClient okhttp = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).connectionPool(ApplicationData.getConnectionPool()).build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody reqbody = RequestBody.create(mediaType, post_data);
            Request request = new Request.Builder()
                    .url(ORDER_URL + "?mid=" + mid + "&orderId=" + sn)
                    .post(reqbody)
                    .addHeader("content-type", "application/json")
                    .build();

            OrderRequestInfo ori = new OrderRequestInfo();
            ori.setMid(mid);
            ori.setMkey(mkey);

            String log = "";
            Response response = okhttp.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                String data = response.body().string();
                System.out.println("返回的数据：" + data);
                response.close();

                ori.setData(data);

                String txnToken = getTxnToken(data);
                if (StringUtils.isBlank(txnToken)) {
                    ori.setStatus(false);
                    log = ",请求成功,下单失败|" + data;
                } else {
                    ori.setStatus(true);
                    ori.setTxnToken(txnToken);

                    log = ",请求成功,下单成功|" + data;
                }

            } else {
                ori.setStatus(false);
                log = ",请求失败,响应码:" + response.code();
            }
            ori.setDesc("商户ID:" + mid + log);
            return ori;
        } catch (Exception e) {
            e.printStackTrace();
            return new OrderRequestInfo(false, mid, mkey, "程序异常：" + e.getLocalizedMessage());
        }
    }


    /**
     * 获取txnToken
     *
     * @param data
     * @return
     */
    public static String getTxnToken(String data) {
        try {
            JsonParser jp = new JsonParser();
            JsonObject jobj = jp.parse(data).getAsJsonObject();
            jobj = jobj.get("body").getAsJsonObject();
            return jobj.get("txnToken").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 查询订单支付状态
     *
     * @param sn
     * @author:nb
     */
    public Map<String, Object> queryOrderStatus(String mid, String key, String sn) {
        try {
            /* initialize an object */
            JsonObject paytmParams = new JsonObject();

            /* body parameters */
            JsonObject body = new JsonObject();

            /* Find your MID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
            body.addProperty("mid", mid);

            /* Enter your order id which needs to be check status for */
            body.addProperty("orderId", sn);

            /**
             * Generate checksum by parameters we have in body
             * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
             * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys 
             */
            String checksum = PaytmChecksum.generateSignature(body.toString(), key);
            /* head parameters */
            JsonObject head = new JsonObject();

            /* put generated checksum value here */
            head.addProperty("signature", checksum);

            /* prepare JSON string for request */
            paytmParams.add("body", body);
            paytmParams.add("head", head);
            String post_data = paytmParams.toString();
            System.out.println("查询订单状态:" + post_data);

            //数据请求
            OkHttpClient okhttp = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).connectionPool(ApplicationData.getConnectionPool()).build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody reqbody = RequestBody.create(mediaType, post_data);
            Request request = new Request.Builder()
                    .url(QUERY_ORDER_STATUS_URL)
                    .post(reqbody)
                    .addHeader("content-type", "application/json")
                    .build();

            Response response = okhttp.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                String data = response.body().string();
                System.out.println("返回的数据：" + data);
                response.close();
                return getOrderStatus(data);
            } else {
                System.out.println("false");
                if (response != null) response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>();
    }

    public static Map<String, Object> getOrderStatus(String data) {
        Map<String, Object> datas = new HashMap<String, Object>();
        try {
            JsonParser jp = new JsonParser();
            JsonObject jobj = jp.parse(data).getAsJsonObject();
            jobj = jobj.get("body").getAsJsonObject();

            datas.put("txnId", getJsonStr(jobj.get("txnId")));

            datas.put("amount", getJsonStr(jobj.get("txnAmount")));

            jobj = jobj.get("resultInfo").getAsJsonObject();

            datas.put("code", getJsonStr(jobj.get("resultCode")));
            datas.put("msg", getJsonStr(jobj.get("resultMsg")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static String getJsonStr(JsonElement je) {
        return je == null ? "" : je.getAsString();
    }


//    public static void processTransaction(String mid,String orderId,String txnToken) {
//        try {
//        
//            String url=PROCESS_TRANSACTION_ORDER_URL+"mid="+mid+"&orderId="+orderId;
//            
//            JsonObject paytmParams = new JsonObject();
//    
//            JsonObject body = new JsonObject();
//            body.addProperty("requestType", "NATIVE");
//            body.addProperty("mid", mid);
//            body.addProperty("orderId", orderId);
//            body.addProperty("paymentMode", "CREDIT_CARD");
//            body.addProperty("cardInfo", "|4111111111111111|111|122032");
//            body.addProperty("authMode", "otp");
//    
//            JsonObject head = new JsonObject();
//            head.addProperty("txnToken", txnToken);
//    
//            paytmParams.add("body", body);
//            paytmParams.add("head", head);
//    
//            String post_data = paytmParams.toString();
//    
//            OkHttpClient okhttp=new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
//                    .writeTimeout(20, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS).build();
//            MediaType mediaType = MediaType.parse("application/json");
//            RequestBody reqbody = RequestBody.create(mediaType, post_data);
//            Request request=new Request.Builder()
//                    .url(ORDER_URL+"?mid="+mid+"&orderId="+sn)
//                    .post(reqbody)
//                    .addHeader("content-type", "application/json")
//                    .build();
//            
//            OrderRequestInfo ori=new OrderRequestInfo();
//            ori.setMid(mid);
//            ori.setMkey(mkey);
//            
//            String log="";
//            Response response = okhttp.newCall(request).execute();
//            if(response!=null && response.isSuccessful()){
//                String data=response.body().string();
//                System.out.println("返回的数据："+data);
//                response.close();
//                
//                ori.setData(data);
//                
//                String txnToken=getTxnToken(data);
//                if(StringUtils.isBlank(txnToken)) {
//                    ori.setStatus(false);
//                    log=",请求成功,下单失败|"+data;
//                }else {
//                    ori.setStatus(true);
//                    ori.setTxnToken(txnToken);
//                    
//                    log=",请求成功,下单成功|"+data;
//                }
//                
//            }else {
//                ori.setStatus(false);
//                log=",请求失败,响应码:"+response.code();
//            }
//            ori.setDesc("商户ID:"+mid+log);
//        
//        }catch(Exception e) {
//            
//        }
//        
//    }
}
