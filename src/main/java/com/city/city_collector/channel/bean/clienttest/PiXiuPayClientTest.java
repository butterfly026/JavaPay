package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.*;
import com.city.city_collector.channel.util.Constants;
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class PiXiuPayClientTest extends ClientTest {

    public PiXiuPayClientTest() {
        merchantNo = "";
        merchantMy = "";
        payUrl = "http://link.hua-du.xyz/api/Pay/Create";
        cashUrl = "";
        channelParams = new HashMap<String, Object>();
//        channelParams.put("appId", "");
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        Map<String, Object> params = new TreeMap<String, Object>();

        params.put("Amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP));

        if (channelParams != null) {
//            params.put("CategoryCode", "138");
            params.putAll(channelParams);
        }
        params.put("ClientIp", "127.0.0.1");

        params.put("MchId", merchantNo);
        params.put("MchOrderNo", sn);

        params.put("NotifyUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("RequestTime", sdf.format(new Date()));
        params.put("ReturnUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);


        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Map.Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue())) && !et.getValue().equals("null")) {
                sbuf.append(et.getKey().toLowerCase());
                sbuf.append("=");
                sbuf.append(String.valueOf(et.getValue()));
                sbuf.append("&");
            }
        }
        sbuf.append("key=" + merchantMy);
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
        params.put("Sign", sign);

        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(params);
        opi.setReqType(1);
        opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);

        return opi;
    }

    @Override
    public OrderInfo dealOrderData(String data, Long clientId, String clientNo, String clientName,
                                   Long clientChannelId, String url, String sn, Map<String, Object> channelParams) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JsonParser jp = new JsonParser();

        JsonObject jobj = jp.parse(data).getAsJsonObject();
        if (!"0".equals(jobj.get("ErrCode").getAsString())) {
            return null;
        }

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);
        jobj = jobj.get("ResData").getAsJsonObject();
        oi.setSn(jobj.get("MchOrderNo").getAsString());
        oi.setPayUrl(jobj.get("CallBackUrl").getAsString());
        oi.setName("");
        oi.setCard("");
        oi.setBankName("");
        oi.setClientId(clientId);
        oi.setClientNo(clientNo);
        oi.setClientName(clientName);
        oi.setClientChannelId(clientChannelId);
        return oi;
    }


    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        try {
            JsonParser jp = new JsonParser();
            JsonObject jobj = jp.parse(body).getAsJsonObject();
            params.put("MchId", jobj.get("MchId").getAsString());
            params.put("OrderNo", jobj.get("OrderNo").getAsString());
            params.put("MchOrderNo", jobj.get("MchOrderNo").getAsString());
            params.put("Amount", jobj.get("Amount").getAsString());
            params.put("Sign", jobj.get("Sign").getAsString());


            String sign = (String) params.get("Sign");
            if (StringUtils.isBlank(sign)) {
                return null;
            }

            Map<String, Object> signParams = new TreeMap<String, Object>();
            signParams.putAll(params);
            signParams.remove("Sign");
            signParams.remove("OrderNo");


            Iterator<Map.Entry<String, Object>> it = signParams.entrySet().iterator();
            StringBuffer sbuf = new StringBuffer("");
            while (it.hasNext()) {
                Map.Entry<String, Object> et = it.next();
                if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                    sbuf.append(et.getKey().toLowerCase());
                    sbuf.append("=");
                    sbuf.append(String.valueOf(et.getValue()));
                    sbuf.append("&");
                }
            }

            sbuf.append("key=" + merchantMy);

            String anObject = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
            if (sign.equals(anObject)) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("MchOrderNo"));
                ni.setClientSn((String) params.get("OrderNo"));
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

        params.put("Amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP));

        if (channelParams != null) {
//            params.put("CategoryCode", "138");
            params.putAll(channelParams);
        }
        params.put("ClientIp", "127.0.0.1");

        params.put("MchId", merchantNo);
        params.put("MchOrderNo", sn);

        params.put("NotifyUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("RequestTime", sdf.format(new Date()));
        params.put("ReturnUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);


        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Map.Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                sbuf.append(et.getKey().toLowerCase());
                sbuf.append("=");
                sbuf.append(String.valueOf(et.getValue()));
                sbuf.append("&");
            }
        }
        sbuf.append("key=" + merchantMy);
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
        params.put("Sign", sign);


        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(params));

        apiInfo.setSignStr(sbuf.toString());

        apiInfo.setSign(sign);

        apiInfo.setRequstData(gson.toJson(params));
        apiInfo.setDatas(params);
        apiInfo.setGotype(0);
        apiInfo.setReqType(1);

        return apiInfo;
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
        return null;
    }

    @Override
    public QueryOrderResult dealQueryResult(String data, PayOrder order, PayClientChannel clientChannel, SysUser client,
                                            Map<String, Object> channelParams) {
        return null;
    }
}
