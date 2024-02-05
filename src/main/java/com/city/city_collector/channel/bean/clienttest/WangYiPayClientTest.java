package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.*;
import com.city.city_collector.channel.util.Constants;
import com.city.city_collector.channel.util.HttpUtil;
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 卓合
 */
public class WangYiPayClientTest extends ClientTest {

    public WangYiPayClientTest() {
        merchantNo = "";
        merchantMy = "";
        payUrl = "http://api.wyuuutt.top/api";
        cashUrl = "";
        channelParams = new HashMap<String, Object>();
//        channelParams.put("appId", "");
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        Map<String, Object> params = new TreeMap<String, Object>();

        params.put("appid", merchantNo);
        params.put("error_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("inlet", "wyy");
        params.put("mode", "1");
        params.put("money", amount.setScale(2, BigDecimal.ROUND_HALF_UP));
        params.put("notify_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("out_order_no", sn);
        params.put("pay_type", "alipay");
        params.put("success_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        if (channelParams != null) {
            params.putAll(channelParams);
        }

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
        sbuf.append("key=" + merchantMy);
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8").toUpperCase();
        params.put("sign", sign);

        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(params);
        opi.setReqType(0);
        opi.setRequestType(HttpUtil.RequestType.POST);

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
        if (!"1".equals(jobj.get("status").getAsString())) {
            return null;
        }

        JsonObject dataInn = jobj.getAsJsonObject("result");

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);
        oi.setSn(sn);
        oi.setPayUrl(dataInn.get("weburl").getAsString());
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
            if (StringUtils.isBlank(sign)) {
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
            sbuf.append("key=" + merchantMy);

            String anObject = MD5Util.MD5Encode(sbuf.toString(), "UTF-8").toUpperCase();
            if (sign.equals(anObject)) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("out_order_no"));
                ni.setClientSn((String) params.get("order_no"));
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

        params.put("appid", merchantNo);
        params.put("error_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("inlet", "wyy");
        params.put("mode", "1");
        params.put("money", amount.setScale(2, BigDecimal.ROUND_HALF_UP));
        params.put("notify_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("out_order_no", sn);
        params.put("pay_type", "alipay");
        params.put("success_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        if (channelParams != null) {
//            params.put("ptype", 1);
            params.putAll(channelParams);
        }

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
        sbuf.append("key=" + merchantMy);
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8").toUpperCase();
        params.put("sign", sign);
        //0240BC8A016D9A4044F9290C839CD7D4

        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(params);
        opi.setReqType(0);
        opi.setRequestType(HttpUtil.RequestType.POST);

        Gson gson = new Gson();

        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(params));
        apiInfo.setSignStr(sbuf.toString());
        apiInfo.setSign(sign);

        apiInfo.setDatas(params);
        apiInfo.setGotype(0);
        apiInfo.setReqType(0);

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
