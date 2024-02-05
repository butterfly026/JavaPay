
package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.city.city_collector.channel.util.HttpUtil;
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
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author nb
 * @Description:
 */
public class ZhaPayClientTest extends ClientTest {

    public ZhaPayClientTest() {

        merchantNo = "";
        merchantMy = "";

        payUrl = "http://94.74.106.25:8403/api/pay/create";
        queryUrl = "";
        cashUrl = "";
        channelParams = new HashMap<String, Object>();
        channelParams.put("product_id", "10001");
//        {"product_id":"10001"}
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        Map<String, Object> params = new TreeMap<String, Object>();

        params.put("mch_id", merchantNo);
        params.put("out_trade_no", sn);
        BigDecimal h = new BigDecimal(100);
        params.put("amount", amount.multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP));
        params.put("notify_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("return_url", "https://google.com");
        params.put("subject","tesshoppint");
        params.put("body","productdescript");

        if (channelParams != null){
            params.putAll(channelParams);
        }

        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Entry<String, Object> et = it.next();
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

        String paramJson = new Gson().toJson(params);
//        System.out.println(paramJson);
        TreeMap<String, Object> wrapParam = new TreeMap<>();
        wrapParam.put("params",paramJson);

        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(wrapParam);
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
        if (!"SUCCESS".equals(jobj.get("retCode").getAsString())) {
            return null;
        }

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);
        oi.setSn(sn);

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
            //success

            String sign = (String) params.get("sign");
            String status = (String) params.get("status");
            if (StringUtils.isBlank(sign) || !"2".equals(status)) {
                return null;
            }

            Map<String, Object> signParams = new TreeMap<String, Object>();
            signParams.putAll(params);
            signParams.remove("sign");

            Iterator<Entry<String, Object>> it = signParams.entrySet().iterator();
            StringBuffer sbuf = new StringBuffer("");
            while (it.hasNext()) {
                Entry<String, Object> et = it.next();
                if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                    sbuf.append(et.getKey());
                    sbuf.append("=");
                    sbuf.append(String.valueOf(et.getValue()));
                    sbuf.append("&");
                }
            }

            sbuf.append("key=" + merchantMy);

            if (sign.equals(MD5Util.MD5Encode(sbuf.toString(), "UTF-8").toUpperCase())) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("outTradeNo"));
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

        params.put("mch_id", merchantNo);
        params.put("out_trade_no", sn);
        BigDecimal h = new BigDecimal(100);
        params.put("amount", amount.multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP));
        params.put("notify_url", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("return_url", "https://google.com");
        params.put("subject","tesshoppint");
        params.put("body","productdescript");

        if (channelParams != null){
            params.putAll(channelParams);
        }

        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Entry<String, Object> et = it.next();
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

        String paramJson = new Gson().toJson(params);
//        System.out.println(paramJson);
        TreeMap<String, Object> wrapParam = new TreeMap<>();
        wrapParam.put("params",paramJson);

        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(wrapParam));

        apiInfo.setSignStr(sbuf.toString());

        apiInfo.setSign(sign);

        apiInfo.setRequstData(gson.toJson(wrapParam));
        apiInfo.setDatas(wrapParam);
        apiInfo.setGotype(0);
        return apiInfo;
    }

    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {
        BigDecimal h = new BigDecimal(100);
        Map<String, Object> params = new TreeMap<String, Object>();

        params.put("mchId", merchantNo);
        params.put("mchOrderNo", cash.getSn());

        params.put("amount", cash.getClientMoney().multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP));

        params.put("accountAttr", "0");

        params.put("accountName", cash.getBankAccname());
        params.put("accountNo", cash.getBankAccno());
        params.put("province", cash.getBankProvince());
        params.put("city", cash.getBankCity());
        params.put("bankName", cash.getBankName());
        params.put("bankNumber", "");

        params.put("notifyUrl", domain + "/api/pay/cashnotify/" + keyname + "_" + client.getId());

        params.put("remark", "trade");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        params.put("reqTime", sdf.format(new Date()));

        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Entry<String, Object> et = it.next();
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

        return params;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        if (StringUtils.isBlank(data)) {
            return new CashInfo(false, "发起代付失败!");
        }
        JsonParser jp = new JsonParser();

        JsonObject jobj = jp.parse(data).getAsJsonObject();
        if (!"SUCCESS".equals(jobj.get("retCode").getAsString())) {
            return new CashInfo(false, "发起代付失败:" + jobj.get("retMsg"));
        }

        CashInfo ci = new CashInfo(true, "发起代付成功");
        ci.setSn(jobj.get("agentpayOrderId").getAsString());

        return ci;
    }

    @Override
    public QueryOrderInfo createQueryOrder(PayOrder order, PayClientChannel clientChannel, SysUser client,
                                           Map<String, Object> channelParams) {
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("mchId", client.getMerchantNo());
        params.put("appId", channelParams.get("appId"));
        params.put("payOrderId", order.getClientSn());
        params.put("mchOrderNo", order.getSn());
        params.put("executeNotify", false);

        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                sbuf.append(et.getKey());
                sbuf.append("=");
                sbuf.append(String.valueOf(et.getValue()));
                sbuf.append("&");
            }
        }

        sbuf.append("key=" + client.getMerchantMy());

        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
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

            if (!"SUCCESS".equals(jobj.get("retCode").getAsString())) {
                return new QueryOrderResult(false, "查单失败:" + jobj.get("retMsg"));
            }

            if (!"2".equals(jobj.get("status").getAsString())) {
                return new QueryOrderResult(false, "查单失败:" + jobj.get("status"));
            }

            return new QueryOrderResult(true, "");
        } catch (Exception e) {
            return new QueryOrderResult(false, "查单失败:" + e.getMessage());
        }
    }


}
