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
import java.net.URLEncoder;
import java.util.*;

/**
 * 卓合
 */
public class YiShengPayClientTest extends ClientTest {

    public YiShengPayClientTest() {
        merchantNo = "29188426";
        merchantMy = "076324ca2c784ca1959c4b8128bd7464";
        payUrl = "http://payxiong1234.made777.com:83/order/place";
        cashUrl = "";
        channelParams = new HashMap<String, Object>();
        channelParams.put("ptype", "16");
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        Map<String, Object> params = new TreeMap<String, Object>();

        params.put("client_ip", "127.0.0.1");
        params.put("format", "json");
        params.put("goods_desc", "test");
        params.put("mch_id", merchantNo);
        params.put("money", amount.setScale(0, BigDecimal.ROUND_HALF_UP));
        params.put("notify_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("order_sn", sn);

        if (channelParams != null) {
//            params.put("ptype", 1);
            params.putAll(channelParams);
        }

        String timeMill = String.valueOf(System.currentTimeMillis());
        params.put("time", timeMill.substring(0,timeMill.length()-3));


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
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
        params.put("sign", sign);

        String json = new Gson().toJson(params);
        HashMap<String, Object> paramsJson = new HashMap<>();
        paramsJson.put("json", URLEncoder.encode(json,"UTF-8"));

        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(paramsJson);
        opi.setReqType(0);
        opi.setRequestType(HttpUtil.RequestType.GET);

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
        if (!"1".equals(jobj.get("code").getAsString())) {
            return null;
        }

        JsonObject dataInn = jobj.getAsJsonObject("data");

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);
        oi.setSn(sn);
        oi.setPayUrl(dataInn.get("url").getAsString());
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
            String status = (String) String.valueOf(params.get("state"));
            if (StringUtils.isBlank(sign) || !"4".equals(status)) {
                return null;
            }

//            {"mchNo":"180430","msg":"支付成功",
//            "orderCode":"LN20210715171643472808b8",
//            "platSign":"72f248a2ff5313d116c909a5998c271b",
//            "price":"50.00","signType":"MD5",
//            "status":"SUCCESS","ts":"1626340718470"}
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

            String anObject = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
            if (sign.equals(anObject)) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("sh_order"));
                ni.setClientSn((String) params.get("pt_order"));
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

        params.put("client_ip", "127.0.0.1");
        params.put("format", "json");
        params.put("goods_desc", "test");
        params.put("mch_id", merchantNo);
        params.put("money", amount.setScale(0, BigDecimal.ROUND_HALF_UP));
        params.put("notify_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("order_sn", sn);

        if (channelParams != null) {
//            params.put("ptype", 1);
            params.putAll(channelParams);
        }

        params.put("time", System.currentTimeMillis());


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
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
        params.put("sign", sign);

        Gson gson = new Gson();
        String json = gson.toJson(params);
//        HashMap<String, Object> paramsJson = new HashMap<>();
//        paramsJson.put("json", URLEncoder.encode(json,"UTF-8"));


        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(params));

        apiInfo.setSignStr(sbuf.toString());

        apiInfo.setSign(sign);

        apiInfo.setRequstData("json="+URLEncoder.encode(json,"UTF-8"));
        apiInfo.setDatas(params);
        apiInfo.setGotype(0);
        apiInfo.setReqType(2);

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
