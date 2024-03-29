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
import java.util.*;

public class TianHongPayClientTest extends ClientTest {

    public TianHongPayClientTest() {
        merchantNo = "";
        merchantMy = "";
//        merchantMy = "";
        payUrl = "https://www.myyhpp.buzz/api/pay1/create_order";
        queryUrl = "https://www.myyhpp.buzz/api/pay1/query_order";
        cashUrl = "";
//        channelParams = new HashMap<String, Object>();
//        channelParams.put("appId", "");
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        Map<String, Object> params = new TreeMap<String, Object>();

        params.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP));
        params.put("clientIp", "127.0.0.1");

        params.put("mchId", merchantNo);
        params.put("mchOrderNo", sn);

        params.put("notifyUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("productId", "203");
        params.put("returnUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        if (channelParams != null)
            params.putAll(channelParams);

        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Map.Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                sbuf.append(et.getKey());
                sbuf.append("=");
                sbuf.append(String.valueOf(et.getValue()));
                sbuf.append("&");
            }
        }
        sbuf.deleteCharAt(sbuf.length()-1);
        sbuf.append(merchantMy);
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
        params.put("sign", sign);

        params.put("backType","1");

        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(params);
        opi.setReqType(0);
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
        if (!"SUCCESS".equals(jobj.get("retCode").getAsString())) {
            return null;
        }

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);
        oi.setSn(sn);
        jobj = jobj.get("payParams").getAsJsonObject();
        oi.setPayUrl(jobj.get("payUrl").getAsString());
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
            String sign = (String) params.get("sign");
            String status = (String) params.get("status");
            if (StringUtils.isBlank(sign) || !"2".equals(status)) {
                return null;
            }

            Map<String, Object> signParams = new TreeMap<String, Object>();
            signParams.putAll(params);
            signParams.remove("sign");

            Iterator<Map.Entry<String, Object>> it = signParams.entrySet().iterator();
            StringBuffer sbuf = new StringBuffer("");
            while (it.hasNext()) {
                Map.Entry<String, Object> et = it.next();
                if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                    sbuf.append(et.getKey());
                    sbuf.append("=");
                    sbuf.append(String.valueOf(et.getValue()));
                    sbuf.append("&");
                }
            }

            sbuf.deleteCharAt(sbuf.length()-1);
            sbuf.append(merchantMy);

            String anObject = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
            if (sign.equals(anObject)) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("mchOrderNo"));
                ni.setClientSn((String) params.get("payOrderId"));
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

        params.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP));
        params.put("clientIp", "127.0.0.1");

        params.put("mchId", merchantNo);
        params.put("mchOrderNo", sn);

        params.put("notifyUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("productId", "203");
        params.put("returnUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        if (channelParams != null)
            params.putAll(channelParams);

        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Map.Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                sbuf.append(et.getKey());
                sbuf.append("=");
                sbuf.append(String.valueOf(et.getValue()));
                sbuf.append("&");
            }
        }
        sbuf.deleteCharAt(sbuf.length()-1);
        sbuf.append(merchantMy);
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
        params.put("sign", sign);

        params.put("backType","1");


        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(params));

        apiInfo.setSignStr(sbuf.toString());

        apiInfo.setSign(sign);

        apiInfo.setRequstData(gson.toJson(params));
        apiInfo.setDatas(params);
        apiInfo.setGotype(0);
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
