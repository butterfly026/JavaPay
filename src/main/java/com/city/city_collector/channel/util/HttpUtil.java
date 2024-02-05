
package com.city.city_collector.channel.util;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.city.util.Logger;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.CashInfo;
import com.city.city_collector.channel.bean.OrderInfo;
import com.city.city_collector.channel.bean.OrderPreInfo;
import com.city.city_collector.channel.bean.QueryOrderResult;
import com.city.city_collector.paylog.PayLogManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import groovy.lang.Binding;
import groovy.lang.Script;
import okhttp3.ConnectionPool;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author nb
 * @Description:
 */
public class HttpUtil {

    public static MediaType mediaType = MediaType.parse("application/json");

    public enum RequestType {
        GET,
        POST
    }

    /**
     * 通用数据请求
     *
     * @param url
     * @param requestType
     * @param params
     * @param requestProperty
     * @param charset
     * @return
     * @author:nb
     */
    public static OrderInfo requestData(String url, RequestType requestType, Map<String, Object> params, Map<String, String> headers, Map<String, String> requestProperty, String charset
            , Long merchantId, String sn, int reqType, Long id, Long clientId, Script script, String clientNo, String clientName, Map<String, Object> channelParams,int useProxy) {
        long start = System.currentTimeMillis();
        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"request url: " + url + " sn: " + sn);
        Response response = null;
        try {
            Proxy proxy=null;
            
           
            String getProxyHostBlock = ApplicationData.getInstance().getConfig().getProxyInfoBlock();
            //String getProxyHostBlock="192.168.3.1:1080:s:|192.168.3.2:1081:s1:b1|";
            String[] block = getProxyHostBlock.split("\\|");
            Random rand=new Random();
            int idx=rand.nextInt(block.length);

            if(block.length>0 && !block[0].isEmpty()){
                //for(int cnt=0;cnt<block.length;cnt++){
                    String[] info= block[idx].split(":");
                    if(info.length==4)
                    {   //"20.255.53.100", 1080,"",""
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"拉单使用代理: " + info[0] + " - " + info[1]+ " - " + info[2] + " - " + info[3]);
                        proxy = HttpUtil.CreateProxy(info[0],Integer.parseInt(info[1]),info[2],info[3]);
                    }else if(info.length==3){
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"拉单使用代理: " + info[0] + " - " + info[1] + " - " + info[2]);
                        proxy = HttpUtil.CreateProxy(info[0],Integer.parseInt(info[1]),info[2],"");
                    }else{
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"拉单使用代理: " + info[0] + " - " + info[1]);
                        proxy = HttpUtil.CreateProxy(info[0],Integer.parseInt(info[1]),"","");
                    }
                    
               // }
            }
		//mod by astro
	 if(useProxy == 0){
		proxy=null;
		//不使用全局代理
		} 


           
            Long timeout_sec = 30L;//设置超时时间
            OkHttpClient okhttp = null;
            if(proxy!=null){
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"进入代理拉单流程!");
                okhttp = new OkHttpClient.Builder()
                            .proxy(proxy)
                            .connectTimeout(timeout_sec, TimeUnit.SECONDS)
                            .writeTimeout(timeout_sec, TimeUnit.SECONDS)
                            .readTimeout(timeout_sec, TimeUnit.SECONDS)
            //                    .retryOnConnectionFailure(false)
                            .connectionPool(ApplicationData.getConnectionPool()).build();
            }else{
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"进入非代理拉单流程!");
                okhttp = new OkHttpClient.Builder()
                            .connectTimeout(timeout_sec, TimeUnit.SECONDS)
                            .writeTimeout(timeout_sec, TimeUnit.SECONDS)
                            .readTimeout(timeout_sec, TimeUnit.SECONDS)
            //                    .retryOnConnectionFailure(false)
                            .connectionPool(ApplicationData.getConnectionPool()).build();
            }

            Request.Builder request = null;
            if (requestType == RequestType.POST) {
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
                if (reqType == 1) {//json模式
                    RequestBody body = RequestBody.create(mediaType, new Gson().toJson(params));
                    request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .addHeader("content-type", "application/json");
                } else {
                    request = new Request.Builder()
                            .url(url)
                            .post(formBuilder.build());
                    //                      .addHeader("Content-Type", "application/json")
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

            if (headers != null && headers.size() > 0 && request != null) {
                Request.Builder finalRequest = request;
                headers.forEach((key, value) -> {
                    finalRequest.header(key, value);
                });
            }
            Request build = request.build();

            response = okhttp.newCall(build).execute();
            if (response != null && response.isSuccessful()) {
                String data = response.body().string();
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"接口返回的数据：" + "sn->" + sn + " : " + data );
                response.close();

                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "上游响应成功:" + url, new Gson().toJson(params), data,
                        clientId, id, merchantId, null);


                Binding binding = new Binding();
                binding.setProperty("data", data);
                binding.setProperty("clientId", clientId);
                binding.setProperty("clientNo", clientNo);
                binding.setProperty("clientName", clientName);
                binding.setProperty("clientChannelId", id);

                binding.setProperty("url", url);
                binding.setProperty("sn", sn);
                binding.setProperty("channelParams", channelParams);

                long end = System.currentTimeMillis();
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"request url: " + url + " sn: " + sn + " useTime: " + (end - start));
                //执行脚本返回oi
                return (OrderInfo) GroovyUtil.runScript(script, binding);

            } else {
                if (response != null) {
                    response.close();
                }
                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "上游响应失败", new Gson().toJson(params), response == null ? "" : response.code() + "," + response.message(),
                        clientId, id, merchantId, null);
                return null;
            }

        } catch (Exception e) {
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"上游响应超时：" + "sn->" + sn+ ",response:" +response == null ? "" : response.code() + "," + response.message()+ ",url:" +url+ ",merchantId:" + merchantId+ ",id:" + id+ ",clientId:" +clientId+ ",clientNo:" +clientNo+ ",clientName:" +clientName+ ",channelParams:" +channelParams + ",params:" +  new Gson().toJson(params) );
            e.printStackTrace();
            if (response != null) response.close();
            return null;
        }
    }
        //"20.255.53.100", 1080
        public  static Proxy CreateProxy(String host,int port,String username,String password){
        
            InetSocketAddress addr = new InetSocketAddress(host, port);
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr); // http 代理
            Authenticator.setDefault(new ProxyAuthenticator(username,password));// 设置代理的用户和密码
            /*
            Request request = new Request.Builder()
                                .url("https://www.baidu.com")
                                .build();
    
            return new OkHttpClient.Builder()
                .proxy(proxy)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
              */       
            return proxy;               
    
        }
        static class ProxyAuthenticator extends Authenticator {
            private String user = "";
            private String password = "";
     
            public ProxyAuthenticator(String user, String password) {
                this.user = user;
                this.password = password;
            }
     
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password.toCharArray());
            }
        }
    /**
     * 提现请求
     *
     * @param url
     * @param requestType
     * @param params
     * @param requestProperty
     * @param charset
     * @param sn
     * @param reqType
     * @param script
     * @return
     * @author:nb
     */
    public static CashInfo requestData_Cash(String url, RequestType requestType, Map<String, Object> params, Map<String, String> requestProperty, String charset
            , String sn, int reqType, Script script) {
        try {
            System.out.println("issued 3-1: 请求代付上游 sn: " + sn + " url: " + url + " param: " + new Gson().toJson(params));
            OkHttpClient okhttp = new OkHttpClient.Builder().connectTimeout(60L, TimeUnit.SECONDS)
                    .writeTimeout(60L, TimeUnit.SECONDS).readTimeout(60L, TimeUnit.SECONDS)
                    .connectionPool(ApplicationData.getConnectionPool())
//                    .retryOnConnectionFailure(false)
                    .build();
            Request request = null;
            if (requestType == RequestType.POST) {

                Map<String, String> headers = null;
                Object headersParam = params.get("headers");
                if (headersParam != null) {
                    headers = (Map<String, String>) headersParam;
                    params.remove("headers");
                }

                if (reqType == 1) {//json模式
                    String bodyStr = (String) params.get("customBody");
                    if (bodyStr == null) {
                        bodyStr = new Gson().toJson(params);
                    }
//                    System.out.println(bodyStr);
                    RequestBody body = RequestBody.create(mediaType, bodyStr);
                    Request.Builder builder = new Request.Builder()
                            .url(url)
                            .post(body);
                    if (headers != null) {
                        for (Map.Entry<String, String> entry : headers.entrySet()) {
                            builder.addHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    request = builder
                            .addHeader("content-type", "application/json")
                            .build();
                } else {
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
                            .url(url)
                            .post(formBuilder.build());
                    if (headers != null) {
                        for (Map.Entry<String, String> entry : headers.entrySet()) {
                            builder.addHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    request = builder
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
                }
                if (StringUtils.isNotBlank(pstr)) {
                    url += "?" + pstr;
                }

                request = new Request.Builder()
                        .url(url)
                        .get()
//                      .addHeader("Content-Type", "application/json")
                        .build();

            }

            Response response = okhttp.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                String data = response.body().string();
                System.out.println("issued 3-2: 代付接口返回结果 : sn: " + sn + " 数据: " + data);
                response.close();

                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "代付上游响应成功:" + url, new Gson().toJson(params), data,
                        null, null, null, null);

                Binding binding = new Binding();

                binding.setProperty("data", data);
                binding.setProperty("sn", sn);
                binding.setProperty("reqParams", params);

                Object obj = GroovyUtil.runScript(script, binding);
                CashInfo cinfo = (obj == null) ? null : (CashInfo) obj;
                return cinfo;
            } else {

                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "代付上游响应失败", new Gson().toJson(params), response == null ? "" : response.code() + "," + response.message(),
                        null, null, null, null);
                if (response != null) response.close();
                return new CashInfo(false, "API请求失败:" + response.code());
            }
        } catch (Exception e) {

            e.printStackTrace();
            return new CashInfo(false, "API请求失败:" + e.getMessage());
        }
    }

    public static QueryOrderResult requestData_QueryOrder(String url, RequestType requestType, Map<String, Object> params, Map<String, String> requestProperty, String charset, int reqType, Script script, PayOrder order, PayClientChannel pcc, SysUser client, Map<String, Object> channelParams) {
        try {
            OkHttpClient okhttp = new OkHttpClient.Builder().connectTimeout(15L, TimeUnit.SECONDS)
                    .writeTimeout(15L, TimeUnit.SECONDS)
                    .readTimeout(15L, TimeUnit.SECONDS).connectionPool(ApplicationData.getConnectionPool())
                    .build();
            Request request = null;
            if (requestType == RequestType.POST) {
                FormBody.Builder formBuilder = new FormBody.Builder();

                if ((params != null) && (!params.isEmpty())) {
                    Set keys = params.keySet();
                    Iterator it = keys.iterator();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        if (params.get(key) != null) {
                            formBuilder.add(key, params.get(key) + "");
                        }
                    }
                }
                if (reqType == 1) {
                    RequestBody body = RequestBody.create(mediaType, new Gson().toJson(params));

                    request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .addHeader("content-type", "application/json")
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(url)
                            .post(formBuilder
                                    .build())
                            .build();
                }
            } else {
                String pstr = "";
                if ((params != null) && (!params.isEmpty())) {
                    Set keys = params.keySet();
                    Iterator it = keys.iterator();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        if (params.get(key) != null) {
                            pstr = pstr + key + "=" + params.get(key) + "&";
                        }
                    }
                    if (!"".equals(pstr)) {
                        pstr = pstr.substring(0, pstr.length() - 1);
                    }
                }
                if (StringUtils.isNotBlank(pstr)) {
                    url += "?" + pstr;
                }
                System.out.println(url);

                request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
            }

            Response response = okhttp.newCall(request).execute();
            if ((response != null) && (response.isSuccessful())) {
                String data = response.body().string();
                System.out.println("接口返回的数据：" + data);
                response.close();

                PayLogManager.getInstance().createPayLogByOrderRequest(order.getSn(), "上游查单成功:" + url, new Gson().toJson(params), data, null, null, null, null);

                Binding binding = new Binding();
                binding.setProperty("data", data);
                binding.setProperty("order", order);
                binding.setProperty("clientChannel", pcc);
                binding.setProperty("client", client);
                binding.setProperty("channelParams", channelParams);
                Object obj = GroovyUtil.runScript(script, binding);
                QueryOrderResult qor = obj == null ? null : (QueryOrderResult) obj;
                return qor;
            }
            String data = response.body().string();

            PayLogManager.getInstance().createPayLogByOrderRequest(order.getSn(), "上游查单失败", new Gson().toJson(params), response.code() + "," + response.message(), null, null, null, null);

            if (response != null) response.close();
            return new QueryOrderResult(false, "API查单失败:" + data);
        } catch (Exception e) {
            e.printStackTrace();
            return new QueryOrderResult(false, "E-API查单失败:" + e.getMessage());
        }
    }

    public static String commonRequestDataXl(String url, Map<String, Object> params
            , String sn, Long id, long time) {
        Response response = null;
        try {
            OkHttpClient okhttp = new OkHttpClient.Builder().connectTimeout(time, TimeUnit.SECONDS)
                    .writeTimeout(time, TimeUnit.SECONDS).readTimeout(time, TimeUnit.SECONDS)
                    .connectionPool(ApplicationData.getConnectionPool())
//                    .retryOnConnectionFailure(false)
                    .build();
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
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBuilder.build())
                    .build();
            response = okhttp.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                String data = response.body().string();
                System.out.println("接口返回的数据：" + data);
                response.close();

                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "上游响应成功:" + data, new Gson().toJson(params), data, null, id, null, null);
                return data;
            } else {
                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "上游响应失败:" + response.body().string(), new Gson().toJson(params), response == null ? "" : response.code() + "," + response.message(), null, id, null, null);
                if (response != null) response.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (response != null) response.close();
            return null;
        }
    }
}
