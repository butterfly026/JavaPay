
package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.ApiInfo;
import com.city.city_collector.channel.bean.CashInfo;
import com.city.city_collector.channel.bean.NotifyInfo;
import com.city.city_collector.channel.bean.OrderInfo;
import com.city.city_collector.channel.bean.OrderPreInfo;
import com.city.city_collector.channel.bean.QueryOrderInfo;
import com.city.city_collector.channel.bean.QueryOrderResult;
import com.city.city_collector.channel.util.Constants;
import com.city.city_collector.channel.util.SignUtil;
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author nb
 * @Description:
 */
public class ChengGongClientTest extends ClientTest {

    public ChengGongClientTest() {
        merchantNo = "";
        merchantMy = "";

        payUrl = "http://eb.gateway.725989.com:30001/api/v1/lang_order_post.do";
        queryUrl = "http://eb.gateway.725989.com:30001/api/v1/lang_order_query.do";
        cashUrl = "http://eb.gateway.725989.com:30001/";

        channelParams = new HashMap<String, Object>();
        // 测试  s96224946704
        // 正式  s47680356103
        channelParams.put("upuid", "auto");
        channelParams.put("pt", "wx");
        channelParams.put("type", "1");

//        channelParams.put("url", "http://eb.gateway.725989.com:30001/api/v1/lang_order_post.do");
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
//        OrderPreInfo opi=new OrderPreInfo();
//        opi.setIsOrderInfo(2);
//        return opi;
        //**********************************************//
//        String url="http://eb.gateway.725989.com:30001/api/v1/lang_order_post.do";
//        String pt=(String)channelParams.get("pt");

        Map<String, Object> params = new TreeMap<String, Object>();

        params.put("order", sn);
        params.put("price", amount);

        params.put("uid", merchantNo);

        params.put("notify_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);


        params.put("upuid", channelParams.get("upuid"));


        String signStr = "key=" + merchantMy + "&" + "notify_url=" + URLEncoder.encode(ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id, "UTF-8") + "&order=" + params.get("order") + "&price=" + params.get("price") + "&uid=" + params.get("uid") + "&upuid=" + params.get("upuid");

        String sign = MD5Util.MD5Encode(signStr, "UTF-8").toLowerCase();

        params.put("sign", sign);

        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(params);
        opi.setReqType(0);
        opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);

        return opi;

//		domain="http://203.86.232.123";
//
//		String data=HttpUtil.commonRequestDataXl(url, params, sn, id,5);
//		if(data!=null) {
//		    if(StringUtils.isBlank(data)) {
//                return null;
//            }
//            JsonParser jp=new JsonParser();
//
//            JsonObject jobj=jp.parse(data).getAsJsonObject();
//            if(!"109".equals(jobj.get("retcode").getAsString()) || !"wait".equals(jobj.get("pay_status").getAsString()) ) {
//                return null;
//            }
//
//            String requestSign="";
//            String requestParam="";
//            if("wx".equals(pt)) {//wx
//                String arr=URLDecoder.decode(jobj.get("pay_url").getAsString(),"UTF-8");
//                String[] arrdata=arr.split("~");
//                requestSign=arrdata[0].replace("\"", "&quot;");
//                requestParam=arrdata[1].replace("\"", "&quot;");
//            }else {//zfb
//                String arr=URLDecoder.decode(jobj.get("pay_url_ali").getAsString(),"UTF-8");
//                String[] arrdata=arr.split("~");
//                requestSign=arrdata[0].replace("\"", "&quot;");
//                requestParam=arrdata[1].replace("\"", "&quot;");
//            }
//            String sbuf="<form name='postSubmit' method='post' action='https://pay.it.10086.cn/payprod-format/h5/dup_submit' >";
//            sbuf+="<input type='hidden' name='sign' value='"+requestSign+"' />";
//            sbuf+="<input type='hidden' name='request_params' value='"+requestParam+"' />";
//            sbuf+="</form> <script>document.postSubmit.submit();</script>";
//
//            Map < String,Object > datas=new HashMap<String,Object>();
//            datas.put("data", sbuf.toString());
//            datas.put("payUrl", domain+"/api/pay/formRequest?sn="+sn);
//            datas.put("sn", jobj.get("bank_order").getAsString());
//
//            OrderPreInfo opi=new OrderPreInfo();
//            opi.setCharset("UTF-8");
//            opi.setParams(datas);
////            opi.setReqType(0);
//            opi.setIsOrderInfo(1);
//            opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);
//
//            sbuf=null;
//            return opi;
//		}
//		return null;

        //**********************************************//

//        OkHttpClient okhttp=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS).readTimeout(10,TimeUnit.SECONDS).build();
//        FormBody.Builder formBuilder = new FormBody.Builder();
//
//        if(params!=null && !params.isEmpty()) {
//            Set < String > keys = params.keySet();
//            Iterator < String > it = keys.iterator();
//            while(it.hasNext()) {
//                String key=it.next();
//                if(params.get(key)!=null) {
//                    formBuilder.add(key,params.get(key)+"");
//                }
//            }
//        }
//        Request request=new Request.Builder()
//                .url(url)
//                .post(formBuilder.build())
//                .build();
//        Response response = okhttp.newCall(request).execute();
//        if(response!=null && response.isSuccessful()){
//            String data=response.body().string();
//            System.out.println("接口返回的数据："+data);
//            response.close();
//
//            PayLogManager.getInstance().createPayLogByOrderRequest(sn, "上游响应成功:"+data, new Gson().toJson(params), data,null, id, null, null);
//            if(StringUtils.isBlank(data)) {
//                return null;
//            }
//            JsonParser jp=new JsonParser();
//
//            JsonObject jobj=jp.parse(data).getAsJsonObject();
//            if(!"109".equals(jobj.get("retcode").getAsString()) || !"wait".equals(jobj.get("pay_status").getAsString()) ) {
//                return null;
//            }
//
//            String requestSign="";
//            String requestParam="";
//            if("wx".equals(pt)) {//wx
//                String arr=URLDecoder.decode(jobj.get("pay_url").getAsString(),"UTF-8");
//                String[] arrdata=arr.split("~");
//                requestSign=arrdata[0].replace("\"", "&quot;");
//                requestParam=arrdata[1].replace("\"", "&quot;");
//            }else {//zfb
//                String arr=URLDecoder.decode(jobj.get("pay_url_ali").getAsString(),"UTF-8");
//                String[] arrdata=arr.split("~");
//                requestSign=arrdata[0].replace("\"", "&quot;");
//                requestParam=arrdata[1].replace("\"", "&quot;");
//            }
//            String sbuf="<form name='postSubmit' method='post' action='https://pay.it.10086.cn/payprod-format/h5/dup_submit' >";
//            sbuf+="<input type='hidden' name='sign' value='"+requestSign+"' />";
//            sbuf+="<input type='hidden' name='request_params' value='"+requestParam+"' />";
//            sbuf+="</form> <script>document.postSubmit.submit();</script>";
//
//            Map < String,Object > datas=new HashMap<String,Object>();
//            datas.put("data", sbuf.toString());
//            datas.put("payUrl", domain+"/api/pay/formRequest?sn="+sn);
//            datas.put("sn", jobj.get("bank_order").getAsString());
//
//            OrderPreInfo opi=new OrderPreInfo();
//            opi.setCharset("UTF-8");
//            opi.setParams(datas);
////            opi.setReqType(0);
//            opi.setIsOrderInfo(1);
//            opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);
//
//            sbuf=null;
//            return opi;
//        }else {
//            PayLogManager.getInstance().createPayLogByOrderRequest(sn, "上游响应失败:"+response.body().string(), new Gson().toJson(params), response==null?"":response.code()+","+response.message(), null, id, null, null);
//            if(response!=null) response.close();
//            return null;
//        }
    }

//    public static void main(String[] args) {
//        String data="{\"retcode\":109,\"operator\":1,\"order\":\"LN20210312102943320e4710\",\"bank_order\":\"508242556008964646\",\"pay_status\":\"wait\",\"order_amount\":100,\"fact_amount\":99.8,\"create_time\":\"2021-03-12 10:29:17\",\"pay_url\":\"%7B%22CerID%22%3A%22120a050%22%2C%22SignValue%22%3A%22S13DF0KhK9ZcU0GKaNYdZ0KiFwUtnrMX784Zo6Su%2BmAv9Ptfd8nQSkenhzH40IFfmJrSEyBY5mY4fCYQxTDHsyTvmjo%2BnyFKmTx69ShZLJ9fP990D3t1nQX1p1uNzhlICXtFH0wzy5c6wDHCBwfhompJNLOJT1TCdeHUI9TlOHg%3D%22%7D%7E%7B%22MerActivityID%22%3A%22%22%2C%22ProductName%22%3A%22%E4%B8%BA136%2A%2A%2A%2A3699%E8%AF%9D%E8%B4%B9%E5%85%85%E5%80%BC%22%2C%22MchId%22%3A%22N00550001%22%2C%22CustomParam%22%3A%22508242556008964646%7C20%7C0003xWAP%22%2C%22OrderNo%22%3A%22161551615742217270397381200056%22%2C%22ReturnURL%22%3A%22https%3A%2F%2Fpay.shop.10086.cn%2Fiphone%5Fpaygw%2FunipayCallback2%22%2C%22ReqChannel%22%3A%224010%22%2C%22IDValue%22%3A%22%22%2C%22IDType%22%3A%22%22%2C%22OrderDate%22%3A%2220210312%22%2C%22BusiType%22%3A%221012%22%2C%22Gift%22%3A%2220%22%2C%22Payment%22%3A%229980%22%2C%22TimeoutExpress%22%3A%2230m%22%2C%22ExtraParams%22%3A%7B%22ClientIP%22%3A%22116.236.182.187%22%7D%2C%22Version%22%3A%221.0%22%2C%22NotifyURL%22%3A%22https%3A%2F%2Fpay.shop.10086.cn%2Fiphone%5Fpaygw%2FunipayNotify2%22%2C%22PaymentType%22%3A%22WEIXIN-WAP%22%2C%22ProductDesc%22%3A%22%E8%AF%9D%E8%B4%B9%E5%85%85%E5%80%BC%22%2C%22ReqDateTime%22%3A%2220210312102917%22%2C%22ReqSys%22%3A%220055%22%7D\",\"pay_voucher\":\"https://touch.10086.cn/i/mobile/stc-recharge-result.html?orderId=508242556008964646&requestId=20210312102917397573255061163101&hmac=356d7ac407ca0e28a581bcd968bd10a7&reserved2=credit&reserved1=0003xWAP&status=1&ts=20210312102917\"}";
//
//        Map <String,Object> cc=new HashMap<String,Object>();
//        cc.put("pt", "wx");
//
//        ClientTest clientTest=new ChengGongClientTest();
//        OrderInfo oi=clientTest.dealOrderData(data, 1L, "LLL", "LLL1", 2L, "", "S11111", cc);
//        System.out.println(new Gson().toJson(oi));
//    }

    @Override
    public OrderInfo dealOrderData(String data, Long clientId, String clientNo, String clientName,
                                   Long clientChannelId, String url, String sn, Map<String, Object> channelParams) {
        // retcode  109
        // pay_status  wait
        try {
//            domain=ApplicationData.getInstance().getConfig().getPaynotifyurl();
            //Lion
//            domain="http://106.75.189.7";
//            //tiantian
//            domain="http://106.75.172.109:50000/";
//            //ksf
            domain = "http://106.75.172.109:50002/";

            if (StringUtils.isBlank(data)) {
                return null;
            }
            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(data).getAsJsonObject();
//              System.out.println("zzzz11-"+jobj.get("retcode"));
            if (!"109".equals(jobj.get("retcode").getAsString()) || !"wait".equals(jobj.get("pay_status").getAsString())) {
                return null;
            }

            String requestSign = "";
            String requestParam = "";
            String pt = (String) channelParams.get("pt");
//              System.out.println("zzzz-"+pt);
            if ("wx".equals(pt)) {//wx
//                  System.out.println("zzzz-wx");
                String arr = URLDecoder.decode(jobj.get("pay_url").getAsString(), "UTF-8");
                String[] arrdata = arr.split("~");
                requestSign = arrdata[0].replace("\"", "&quot;");
                requestParam = arrdata[1].replace("\"", "&quot;");
            } else {//zfb
//                  System.out.println("zzzz-zfb");
                String arr = URLDecoder.decode(jobj.get("pay_url_ali").getAsString(), "UTF-8");
                String[] arrdata = arr.split("~");
                requestSign = arrdata[0].replace("\"", "&quot;");
                requestParam = arrdata[1].replace("\"", "&quot;");
            }
            String sbuf = "<form name='postSubmit' method='post' action='https://pay.it.10086.cn/payprod-format/h5/dup_submit' >";
            sbuf += "<input type='hidden' name='sign' value='" + requestSign + "' />";
            sbuf += "<input type='hidden' name='request_params' value='" + requestParam + "' />";
            sbuf += "</form> <script>document.postSubmit.submit();</script>";

            OrderInfo oi = new OrderInfo();
            oi.setFlag(true);
            oi.setData(sbuf.toString());

            oi.setSn(jobj.get("bank_order").getAsString());
            oi.setPayUrl(domain + "/api/pay/formRequest?sn=" + sn);
            oi.setName("");
            oi.setCard("");
            oi.setBankName("");

            oi.setClientId(clientId);
            oi.setClientNo(clientNo);
            oi.setClientName(clientName);

            oi.setClientChannelId(clientChannelId);

            return oi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        Map < String, Object > param=new HashMap<String,Object>();
////        {"bank_order":"507717015051167909","cancel":"0","order":"LN20210306083018904448e6","pay_status":"success","pay_voucher":"https://pay.shop.10086.cn/paygw/fnpay/507717015051167909-20.html","price":"100","retcode":"115","sign":"2886e98a1afe9af22f2cc1450231cb92"}
//        param.put("bank_order", "507717015051167909");
//        param.put("cancel", "0");
//        param.put("order", "LN20210306083018904448e6");
//        param.put("pay_status", "success");
//        param.put("pay_voucher", "https%3A%2F%2Fpay.shop.10086.cn%2Fpaygw%2Ffnpay%2F507717015051167909-20.html");
//        param.put("price", 50);
//        param.put("retcode", "115");
//        param.put("sign", "2886e98a1afe9af22f2cc1450231cb92");
//        ClientTest client=new ChengGongClientTest();
//        NotifyInfo ni=client.notifyData(param,client.merchantNo, client.merchantMy, null, "");
//        System.out.println(new Gson().toJson(ni));
//    }


    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        try {
//            String retCode=(String)params.get("retcode");
            String payStatus = (String) params.get("pay_status");
            String url = (String) params.get("pay_voucher");
            url = URLEncoder.encode(url, "UTF-8");
//            url=url.replace(".", "%2E").replace("-", "%2D");

            String sign = (String) params.get("sign");
            if (!"success".equals(payStatus)) {
                return null;
            }

            String signStr = "key=" + merchantMy + "&bank_order=" + params.get("bank_order") + "&cancel=" + params.get("cancel") + "&order=" + params.get("order") + "&pay_status=" + params.get("pay_status") + "&pay_voucher=" + url + "&price=" + params.get("price") + "&retcode=" + params.get("retcode");
//            System.out.println(signStr);
            String sign1 = MD5Util.MD5Encode(signStr, "UTF-8").toLowerCase();
//            System.out.println(sign1);
            if (sign.equalsIgnoreCase(sign1)) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("order"));
                ni.setClientSn((String) params.get("bank_order"));
                ni.setStatus("success");
                ni.setAmount(BigDecimal.ZERO);
                ni.setActualAmount(BigDecimal.ZERO);
                ni.setSign(sign);
                ni.setPayTime(new Date());
                return ni;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ApiInfo createReqParams_Test(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain) throws Exception {
        Map<String, Object> params = new TreeMap<String, Object>();

        params.put("order", sn);
        params.put("price", amount);

        params.put("uid", merchantNo);

        params.put("notify_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);


        params.put("upuid", channelParams.get("upuid"));


        String signStr = "key=" + merchantMy + "&" + "notify_url=" + URLEncoder.encode(ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id, "UTF-8") + "&order=" + params.get("order") + "&price=" + params.get("price") + "&uid=" + params.get("uid") + "&upuid=" + params.get("upuid");

        String sign = MD5Util.MD5Encode(signStr, "UTF-8").toLowerCase();

        params.put("sign", sign);

        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(params));

        apiInfo.setSignStr(SignUtil.joinKeyValue(new TreeMap<String, Object>(params), null, "&key=" + merchantMy, "&", true, "sign_type", "sign"));

        apiInfo.setSign(sign);

        params.put("sign", sign);

        apiInfo.setRequstData(gson.toJson(params));
        apiInfo.setDatas(params);
        apiInfo.setGotype(0);
        return apiInfo;
//        String url="http://eb.gateway.725989.com:30001/api/v1/lang_order_post.do";
//        Map < String,Object> params=new TreeMap<String,Object>();
//
//        params.put("order", sn);
//        params.put("price", amount);
//
//        params.put("uid", merchantNo);
//
//        params.put("notify_url", domain+Constants.ASYNC_URL+"/"+id);
//
//        String pt=(String)channelParams.get("pt");
//
//        params.putAll(channelParams);
//
//        String signStr="key="+merchantMy+"&"+"notify_url="+URLEncoder.encode(domain+Constants.ASYNC_URL+"/"+id,"UTF-8")+"&order="+params.get("order")+"&price="+params.get("price")+"&uid="+params.get("uid")+"&upuid="+params.get("upuid");
//
//        String sign=MD5Util.MD5Encode(signStr, "UTF-8").toLowerCase();
//
//        params.put("sign", sign);
//
//		domain="http://203.86.232.123";
//
//		String data=HttpUtil.commonRequestDataXl(url, params, sn, id,20);
//		Gson gson=new Gson();
//        ApiInfo apiInfo=new ApiInfo();
//        apiInfo.setSignData(signStr);
//
//        apiInfo.setSignStr(signStr);
//
//        apiInfo.setSign(sign);
//
//        apiInfo.setRequstData(gson.toJson(params));
//
//        apiInfo.setGotype(1);
//		if(data!=null) {
//		    apiInfo.setResponseData(data);
//
//            if(StringUtils.isBlank(data)) {
//                return null;
//            }
//            JsonParser jp=new JsonParser();
//
//            JsonObject jobj=jp.parse(data).getAsJsonObject();
//            if(!"109".equals(jobj.get("retcode").getAsString()) || !"wait".equals(jobj.get("pay_status").getAsString()) ) {
//                apiInfo.setStatus(false);
//                apiInfo.setResponseData(data);
//                return apiInfo;
//            }
//
//            String requestSign="";
//            String requestParam="";
//            if("wx".equals(pt)) {//wx
//                String arr=URLDecoder.decode(jobj.get("pay_url").getAsString(),"UTF-8");
//                String[] arrdata=arr.split("~");
//                requestSign=arrdata[0].replace("\"", "&quot;");
//                requestParam=arrdata[1].replace("\"", "&quot;");
//            }else {//zfb
//                String arr=URLDecoder.decode(jobj.get("pay_url_ali").getAsString(),"UTF-8");
//                String[] arrdata=arr.split("~");
//                requestSign=arrdata[0].replace("\"", "&quot;");
//                requestParam=arrdata[1].replace("\"", "&quot;");
//            }
//            String sbuf="<form name='postSubmit' method='post' action='https://pay.it.10086.cn/payprod-format/h5/dup_submit' >";
//            sbuf+="<input type='hidden' name='sign' value='"+requestSign+"' />";
//            sbuf+="<input type='hidden' name='request_params' value='"+requestParam+"' />";
//            sbuf+="</form> <script>document.postSubmit.submit();</script>";
//
//            apiInfo.setPayUrl(domain+"/api/pay/formRequest?sn="+sn);
//            apiInfo.setSavePayOrder(0);
//
//            Map < String, Object > datas=new HashMap<String,Object>();
//            datas.put("sn", jobj.get("bank_order").getAsString());
//            datas.put("data", sbuf);
//
//            apiInfo.setDatas(datas);
//            sbuf=null;
//		}else {
//            apiInfo.setStatus(false);
//            apiInfo.setResponseData(data);
//		}
//		return apiInfo;

//        OkHttpClient okhttp=new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS).build();
//        FormBody.Builder formBuilder = new FormBody.Builder();
//
//        if(params!=null && !params.isEmpty()) {
//            Set < String > keys = params.keySet();
//            Iterator < String > it = keys.iterator();
//            while(it.hasNext()) {
//                String key=it.next();
//                if(params.get(key)!=null) {
//                    formBuilder.add(key,params.get(key)+"");
//                }
//            }
//        }
//        Request request=new Request.Builder()
//                .url(url)
//                .post(formBuilder.build())
//                .build();
//        Response response = okhttp.newCall(request).execute();
//
//        Gson gson=new Gson();
//        ApiInfo apiInfo=new ApiInfo();
//        apiInfo.setSignData(signStr);
//
//        apiInfo.setSignStr(signStr);
//
//        apiInfo.setSign(sign);
//
//        apiInfo.setRequstData(gson.toJson(params));
//
//        apiInfo.setGotype(1);
//
//        if(response!=null && response.isSuccessful()){
//            String data=response.body().string();
////            System.out.println("接口返回的数据："+data);
//            response.close();
//
//            PayLogManager.getInstance().createPayLogByOrderRequest(sn, "上游响应成功:"+data, new Gson().toJson(params), data,
//                    null, id, null, null);
//            apiInfo.setResponseData(data);
//
//            if(StringUtils.isBlank(data)) {
//                return null;
//            }
//            JsonParser jp=new JsonParser();
//
//            JsonObject jobj=jp.parse(data).getAsJsonObject();
//            if(!"109".equals(jobj.get("retcode").getAsString()) || !"wait".equals(jobj.get("pay_status").getAsString()) ) {
//                apiInfo.setStatus(false);
//                apiInfo.setResponseData(data);
//                return apiInfo;
//            }
//
//            String requestSign="";
//            String requestParam="";
//            if("wx".equals(pt)) {//wx
//                String arr=URLDecoder.decode(jobj.get("pay_url").getAsString(),"UTF-8");
//                String[] arrdata=arr.split("~");
//                requestSign=arrdata[0].replace("\"", "&quot;");
//                requestParam=arrdata[1].replace("\"", "&quot;");
//            }else {//zfb
//                String arr=URLDecoder.decode(jobj.get("pay_url_ali").getAsString(),"UTF-8");
//                String[] arrdata=arr.split("~");
//                requestSign=arrdata[0].replace("\"", "&quot;");
//                requestParam=arrdata[1].replace("\"", "&quot;");
//            }
//            String sbuf="<form name='postSubmit' method='post' action='https://pay.it.10086.cn/payprod-format/h5/dup_submit' >";
//            sbuf+="<input type='hidden' name='sign' value='"+requestSign+"' />";
//            sbuf+="<input type='hidden' name='request_params' value='"+requestParam+"' />";
//            sbuf+="</form> <script>document.postSubmit.submit();</script>";
//
//            apiInfo.setPayUrl(domain+"/api/pay/formRequest?sn="+sn);
//            apiInfo.setSavePayOrder(0);
//
//            Map < String, Object > datas=new HashMap<String,Object>();
//            datas.put("sn", jobj.get("bank_order").getAsString());
//            datas.put("data", sbuf);
//
//            apiInfo.setDatas(datas);
//            sbuf=null;
//            return apiInfo;
//        }else {
//            String data=response.body().string();
//            PayLogManager.getInstance().createPayLogByOrderRequest(sn, "上游响应失败:"+data, new Gson().toJson(params), response==null?"":response.code()+","+response.message(),
//                    null, id, null, null);
//            if(response!=null) response.close();
//            apiInfo.setStatus(false);
//            apiInfo.setResponseData(data);
//            return apiInfo;
//        }


    }

    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {
        //
        return null;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        //
        return null;
    }

    @Override
    public QueryOrderInfo createQueryOrder(PayOrder order, PayClientChannel clientChannel, SysUser client,
                                           Map<String, Object> channelParams) {
        Map<String, Object> params = new TreeMap<String, Object>();

        params.put("order", order.getSn());
        params.put("uid", client.getMerchantNo());

        String signStr = "key=" + client.getMerchantMy() + "&" + "order=" + params.get("order") + "&uid=" + params.get("uid");
        String sign = MD5Util.MD5Encode(signStr, "UTF-8").toLowerCase();

        params.put("sign", sign);

        QueryOrderInfo qoi = new QueryOrderInfo();
        qoi.setNeedRequest(true);
        qoi.setReqType(0);
        qoi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);
        qoi.setCharset("UTF-8");
        qoi.setParams(params);
        return qoi;
    }

    @Override
    public QueryOrderResult dealQueryResult(String data, PayOrder order, PayClientChannel clientChannel, SysUser client,
                                            Map<String, Object> channelParams) {
        try {
            if (StringUtils.isBlank(data)) {
                return new QueryOrderResult(false, "查单失败!");
            }

            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(data).getAsJsonObject();
            if (!"112".equals(jobj.get("retcode").getAsString()) && !"115".equals(jobj.get("retcode").getAsString())) {
                return new QueryOrderResult(false, "查单失败:" + jobj.get("pay_status"));
            }

            QueryOrderResult qor = new QueryOrderResult();
            if (!"success".equals(jobj.get("pay_status").getAsString())) {
                qor.setMsg("查单失败:" + jobj.get("pay_status"));
                qor.setStatus(false);
            } else {
                qor.setStatus(true);
            }
            return qor;
        } catch (Exception e) {
            return new QueryOrderResult(false, "查单失败:" + e.getMessage());
        }
    }

}
