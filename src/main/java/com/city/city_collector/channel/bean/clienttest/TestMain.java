package com.city.city_collector.channel.bean.clienttest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import com.city.city_collector.channel.bean.*;
import com.city.city_collector.common.util.SnUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.ok2curl.logger.Loggable;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.util.HttpUtil.RequestType;
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

import javax.websocket.ClientEndpoint;

/**
 * @author nb
 * @Description:
 */
public class TestMain {

//    N1@ds#LAQ=-*d2s8j@?KVcW

    public static MediaType mediaType = MediaType.parse("application/json");


    private static void test() {
        String body = "{\"memberid\":\"13025\",\"orderid\":\"2021032718255226222\",\"transaction_id\":\"20210916195902545449\",\"amount\":\"100.0000\",\"datetime\":\"20210916200139\",\"returncode\":\"00\",\"sign\":\"168C4625AE7411B5DCA056EDE4C4FF13\",\"attach\":\"\"}";


        JsonParser jsonParser = new JsonParser();
        JsonObject asJsonObject = jsonParser.parse(body).getAsJsonObject();
        Set<String> strings = asJsonObject.keySet();
        for (String value : strings) {
            String asString = asJsonObject.get(value).getAsString();

//            repParams.put("amount","5000");
            System.out.println("repParams.put(\"" + value + "\",\"" + asString + "\");");
        }

    }

//    public static void main(String[] args) throws IOException {
////        test();
//
//        Config config = new Config();
//        config.setNotifyurl("http://112.213.98.220");
//        config.setPaynotifyurl("http://127.0.0.1:8081");
//        ApplicationData.getInstance().setConfig(config);
//
//        //支付测试
////        ClientTest clientTest = new NineNine();
////        ClientTest clientTest = new XiaoQiangDaiFuClientTest();
////        testCreateOrder(clientTest, new BigDecimal("100"), "2021032718255226229");
//
//        String body = "{\"merchantNo\":\"1546\",\"orderNo\":\"202103271825522622019\",\"settleAmt\":\"90.50\",\"sign\":\"c4e9e035a3527ccbe47335f35380dbfe\",\"status\":\"0000\",\"timestamp\":\"1630492565882\",\"trxAmt\":\"100.00\"}";
//
//
//        Map<String, Object> repParams = new HashMap<>();
//        repParams.put("memberid", "13025");
//        repParams.put("orderid", "2021032718255226222");
//        repParams.put("transaction_id", "20210916195902545449");
//        repParams.put("amount", "100.0000");
//        repParams.put("datetime", "20210916200139");
//        repParams.put("returncode", "00");
//        repParams.put("sign", "168C4625AE7411B5DCA056EDE4C4FF13");
//        repParams.put("attach", "");
//
//
////        NotifyInfo notifyInfo = clientTest.notifyData(repParams, clientTest.merchantNo, clientTest.merchantMy, null, body);
////        System.out.println(new Gson().toJson(notifyInfo));
//
//        //代付测试
////        ClientTest clientTest = new ExampleDaiFuClientTest();
////        ClientTest clientTest = new HuanYuanDaiFuClientTest();
////        ClientTest clientTest = new AnAnDaiFuClientTest();
//        ClientTest clientTest = new MiqiPayClientTest();
//        testCreateOrder(clientTest, new BigDecimal("100"), "210902150801520cc035");
////        NotifyInfo notifyInfo = clientTest.notifyData(repParams, clientTest.merchantNo, clientTest.merchantMy, null, body);
////        System.out.println(new Gson().toJson(notifyInfo));
//
////        System.out.println(compress("LNCA20210902150801520cc332"));
//        String sn = SnUtil.createSn(ApplicationData.getInstance().getConfig().getCashsnpre());
//        System.out.println(sn);
////        CA202 10911192312133a4faf
//        String a = "\"{\"head\":{\"trxCode\":\"P20045\",\"signedMsg\":\"\",\"retCode\":\"000000\",\"retMsg\":\"处理成功\",\"version\":\"01\",\"customerNo\":\"C202112130191\",\"reqSn\":\"C202112130191210902150801520cf035\",\"timestamp\":\"20211215205151\"},\"response\":{\"returnCode\":\"\",\"returnMsg\":\"\",\"orderNo\":\"20211215205151000163\",\"cusOrderNo\":\"210902150801520cf035\",\"extrasContent\":null,\"payAmt\":1000,\"customerNo\":\"C202112130191\",\"status\":5}}\"";
//        JsonParser jp = new JsonParser();
//        a = a.substring(1, a.length() - 1);
//        JsonObject jobj = jp.parse(a).getAsJsonObject();
//        System.out.println(jobj);

//        ClientTest clientTest = new VipDaiFuClientTest();
//        testCreateCash(clientTest, new BigDecimal("10"), "210902150801520co035", 1);
//    }


    public static void testCreateOrder(ClientTest clientTest, BigDecimal amount, String sn) {
        try {
            OrderPreInfo opi = clientTest.createReqParams(1L, clientTest.merchantNo, clientTest.merchantMy, clientTest.channelParams, sn, amount, clientTest.domain, 2);

            if (opi.getIsOrderInfo() == 1) {
                System.out.println(new Gson().toJson(opi));
                Map<String, Object> p = opi.getParams();

                String payUrl = (String) p.get("payUrl");
                if (!payUrl.startsWith("http")) {
                    payUrl = clientTest.payUrl + payUrl;
                }
                System.out.println(payUrl);
                return;
//            }else if(opi.getIsOrderInfo()==2) {
//                opi=ChannelManager1.getInstance().createSbOrder(1L, clientTest.merchantNo, clientTest.merchantMy,clientTest.channelParams, sn, amount, clientTest.domain,clientTest.payUrl);
//
//                System.out.println(new Gson().toJson(opi));
//                return;
            }

            System.out.println(new Gson().toJson(opi));
            System.out.println(opi.getParams());
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new CurlInterceptor(s -> System.out.println(s)));
            OkHttpClient okhttp = builder.connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
            Request.Builder request = null;
            if (opi.getRequestType() == RequestType.POST) {
                FormBody.Builder formBuilder = new FormBody.Builder();

                if (opi.getParams() != null && !opi.getParams().isEmpty()) {
                    Set<String> keys = opi.getParams().keySet();
                    Iterator<String> it = keys.iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (opi.getParams().get(key) != null) {
                            formBuilder.add(key, opi.getParams().get(key) + "");
                        }
                    }
                }
                if (opi.getReqType() == 1) {//json模式
//                    System.out.println(new GsonBuilder().disableHtmlEscaping().create().toJson(opi.getParams()));
                    RequestBody body = RequestBody.create(mediaType, new Gson().toJson(opi.getParams()));
                    request = new Request.Builder()
                            .url(clientTest.payUrl)
                            .post(body)
                            .addHeader("content-type", "application/json");
                } else {
                    request = new Request.Builder()
                            .url(clientTest.payUrl)
                            .post(formBuilder.build());
                    //                      .addHeader("Content-Type", "application/json")
                }
            } else {
                String url = clientTest.payUrl;
                String pstr = "";
                if (opi.getParams() != null && !opi.getParams().isEmpty()) {
                    Set<String> keys = opi.getParams().keySet();
                    Iterator<String> it = keys.iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (opi.getParams().get(key) != null) {
                            pstr += key + "=" + opi.getParams().get(key) + "&";
                        }
                    }
                }
                if (StringUtils.isNotBlank(pstr)) {
                    url += "?" + pstr;
                }

                System.out.println(url);
                request = new Request.Builder()
                        .url(url)
                        .get();
//                      .addHeader("Content-Type", "application/json")

            }
            System.out.println(clientTest.payUrl);

//            okhttp.newCall(request).enqueue(new Callback() {
//
//                @Override
//                public void onResponse(Call arg0, Response arg1) throws IOException {
//                    String data = arg1.body().string();
//                    System.out.println(data);
//                    if(arg1!=null) arg1.close();
//                }
//
//                @Override
//                public void onFailure(Call arg0, IOException arg1) {
//                    System.out.println(arg1.getMessage());
//                }
//            });
            Map<String, String> headers = opi.getHeaders();
            if (headers != null && headers.size() > 0 && request != null) {
                Request.Builder finalRequest = request;
                headers.forEach((key, value) -> {
                    finalRequest.header(key, value);
                });
            }
            Request build = request.build();
            Response response = okhttp.newCall(build).execute();
            if (response != null && response.isSuccessful()) {
                String data = response.body().string();
//                String data = "{\"code\":0,\"msg\":\"OK\",\"data\":{\"payWayID\":2,\"amount\":300.00,\"orderNumber\":\"202103271825522622015\",\"partner\":\"zhuohe\",\"type\":0,\"paymentInfo\":\"http://pay.usdotc.vip/#/?orderNumber=202108222059205795874535&timestamp=1629637160&sign=8b5e08fd8a789af708f73418a11dbb76\"}}";
                System.out.println("接口返回的数据：" + data);
                OrderInfo oinfo = clientTest.dealOrderData(data, 1L, "Test", "测试数据", 1L, clientTest.payUrl, sn, clientTest.channelParams);
                if (oinfo != null) {
                    System.out.println(new Gson().toJson(oinfo));
                    System.out.println(oinfo.getPayUrl());
                } else {
                    System.out.println("接口数据处理失败");
                }

            } else {
                System.out.println("请求上游失败：" + response.code() + "..." + response.body().string());

            }
            if (response != null) response.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    //    public static void main(String[] args) throws IOException{
//    ClientTest clientTest = new superPayClientTest();
//        Map<String, Object> params = new TreeMap();
//        params.put("amount", "200.00");
//        params.put("createTime", "2021-07-14 10:48:49");
//        params.put("merchantNo", "S100");
//        params.put("merchantSn", "LN202107141048498787f57f");
//        params.put("money", "200.00");
//        params.put("notifyStatus", "0");
//        params.put("orderStatus", "1");
//        params.put("payTime", "2021-07-14 10:49:23");
//        params.put("sign", "871C829FCF2BBD8968BB69FE3E8B9635");
//        params.put("sn", "LN2021071410484990088376");
//
//        NotifyInfo opi = clientTest.notifyData(params, "S100", "392254c255e4485e973d9fe69aaaf358", null, null);
//    }
    public static void testCreateCash(ClientTest clientTest, BigDecimal amount, String sn, int model) {
        try {

//            S003-20210728002436000368
//            {"bankAccountName":["王笑展"],
//            "bankCode":["ABC"],"
//            amount":["1000.00"],
//            "sign":["F7ED1ADEBB15C3D76E7965E764E751C0"],
//            "bankAccountNo":["6230520730024859779"],
//            "bankName":["农业银行"],"channelType":["1"]
//            ,"merchantSn":["20210728002436000368"],
//            "bankNameSub":["中国农业银行"],"signType":["md5"],
//            "notifyUrl":["http://newgateway.5hb7hn.cn/superpay-web/api/finotifyPayemnt/superB"],
//            "time":["1627403120"],"merchantNo":["S003"]}

            PayCash cash = new PayCash();
            cash.setAmount(amount);
            cash.setClientMoney(amount);
            cash.setSn(sn);
//            cash.setBankAccid("3623xxxx234325");
            cash.setBankAccno("6221803000013811505");
            cash.setBankAccname("韩瑞宝");
            cash.setBankName("中国邮政储蓄银行");
            cash.setBankCode("PSBC");
            cash.setBtype(0);
            cash.setBankIfsc("PYTM0123456");
            cash.setBankNation("中国");
            cash.setBankProvince("广东省");
            cash.setBankCity("广州市");
            cash.setBankSubname("农业银行");
            cash.setBankAccmobile("9811111111");
            cash.setNotifyUrl("http://haskfjowief.com");
//            cash.setChannelType(1);

            SysUser client = new SysUser();
            client.setId(1L);
            client.setMerchantNo(clientTest.merchantNo);
            client.setMerchantMy(clientTest.merchantMy);


            Map<String, Object> params = clientTest.createReqParams_Cash(cash, client, "http://localhost/", clientTest.cashUrl, "key");

            System.out.println(params);

            Map<String, String> headers = null;
            Object headersParam = params.get("headers");
            if (headersParam != null) {
                headers = (Map<String, String>) headersParam;
                params.remove("headers");
            }

            OkHttpClient.Builder builder1 = new OkHttpClient.Builder();
            builder1.addInterceptor(new CurlInterceptor(s -> System.out.println(s)));
            OkHttpClient okhttp = builder1.connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
            Request request = null;

            if (model == 0) {
                FormBody.Builder formBuilder = new FormBody.Builder();

                if (params != null && !params.isEmpty()) {
                    Set<String> keys = params.keySet();
                    Iterator<String> it = keys.iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (params.get(key) != null) {
                            formBuilder.add(key, params.get(key) + "");
                        }
                    }
                }
                Request.Builder builder = new Request.Builder()
                        .url(clientTest.cashUrl)
                        .post(formBuilder.build());
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                request = builder
//                      .addHeader("Content-Type", "application/json")
                        .build();
            } else {
                String bodyStr = (String) params.get("customBody");
                if (bodyStr == null) {
                    bodyStr = new Gson().toJson(params);
                }
                System.out.println(bodyStr);
                RequestBody body = RequestBody.create(mediaType, bodyStr);
                Request.Builder builder = new Request.Builder()
                        .url(clientTest.cashUrl)
                        .post(body);
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                request = builder
//                        .addHeader("content-type", "application/json")
                        .build();
            }

            System.out.println(clientTest.cashUrl);
            Response response = okhttp.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                String data = response.body().string();
                System.out.println("接口返回的数据：" + data);
                response.close();

//                data = "{\"returnCode\":\"0000\",\"returnMsg\":\"处理成功\",\"nonceStr\":\"e6d8b7e1993248f383870e9b83c074e2\",\"payResult\":\"03\",\"transactionNo\":\"44202108040735472112489\",\"amount\":2000,\"procedureFee\":102,\"actualFee\":2000,\"customerCode\":null}";
                CashInfo ci = clientTest.dealOrderData(data, sn, params);
                System.out.println(ci.getSn() + "--" + ci.getStatus() + "---" + ci.getMsg());
            } else {
                System.out.println("请求失败..." + response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryOrder(ClientTest clientTest, String sn, String clientSn, String money) throws IOException {
        PayOrder po = new PayOrder();
        po.setSn(sn);
        po.setClientSn(clientSn);
        po.setAmount(new BigDecimal(money));

        PayClientChannel pcc = new PayClientChannel();


        SysUser client = new SysUser();
        client.setMerchantNo(clientTest.merchantNo);
        client.setMerchantMy(clientTest.merchantMy);

        QueryOrderInfo qoi = clientTest.createQueryOrder(po, pcc, client, clientTest.channelParams);
        if (qoi.isNeedRequest()) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new CurlInterceptor(s -> System.out.println(s)));
            OkHttpClient okhttp = builder.connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
            Request request = null;
            Map<String, Object> params = qoi.getParams();

            System.out.println(new Gson().toJson(params));

            if (qoi.getRequestType() == RequestType.POST) {
                FormBody.Builder formBuilder = new FormBody.Builder();

                if (params != null && !params.isEmpty()) {
                    Set<String> keys = params.keySet();
                    Iterator<String> it = keys.iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (params.get(key) != null) {
                            formBuilder.add(key, params.get(key) + "");
                        }
                    }
                }
                if (qoi.getReqType() == 1) {//json模式

                    RequestBody body = RequestBody.create(mediaType, new Gson().toJson(params));
                    request = new Request.Builder()
                            .url(clientTest.queryUrl)
                            .post(body)
                            .addHeader("content-type", "application/json")
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(clientTest.queryUrl)
                            .post(formBuilder.build())
                            //                      .addHeader("Content-Type", "application/json")
                            .build();
                }
            } else {
                String pstr = "";
                if (params != null && !params.isEmpty()) {
                    Set<String> keys = params.keySet();
                    Iterator<String> it = keys.iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (params.get(key) != null) {
                            pstr += key + "=" + params.get(key) + "&";
                        }
                    }
                    if (!"".equals(pstr)) {
                        pstr = pstr.substring(0, pstr.length() - 1);
                    }
                }

                if (StringUtils.isNotBlank(pstr)) {
                    clientTest.queryUrl += "?" + pstr;
                }
                System.out.println(clientTest.queryUrl);

                request = new Request.Builder()
                        .url(clientTest.queryUrl)
                        .get()
//                      .addHeader("Content-Type", "application/json")
                        .build();

            }

            Response response = okhttp.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                String data = response.body().string();
                System.out.println("接口返回的数据：" + data);
                response.close();

                QueryOrderResult dor = clientTest.dealQueryResult(data, po, pcc, client, clientTest.channelParams);
                System.out.println(dor.isStatus() + ":" + dor.getMsg());
            } else {
                System.out.println("请求失败..." + response.body());
                if (response != null) response.close();
            }

        } else {
            System.out.println("订单支付状态：" + qoi.isOrderSuccess());
        }
    }
}
