
package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author nb
 * @Description:
 */
public class AkClientTest extends ClientTest {

    /**
     *
     */
    public AkClientTest() {
        merchantNo = "";
        merchantMy = "";

        payUrl = "http://api.2020pay.info/api/v1/pay";
        cashUrl = "http://api.2020pay.info/api/v1/issued";
        queryUrl = "http://api.2020pay.info/api/v2/orderInfo";

        channelParams = new HashMap<String, Object>();
        channelParams.put("ip", "127.0.0.1");
        channelParams.put("payType", "BPP");
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("appId", merchantNo);
        params.put("outTradeNo", sn);
        params.put("amount", amount);
        params.put("nonceStr", System.currentTimeMillis() + "");
        params.put("asyncUrl", domain + Constants.ASYNC_URL + "/" + id);
        params.put("returnUrl", domain + Constants.RETURN_URL + "/" + id);

        if (channelParams != null) params.putAll(channelParams);

        params.put("sign", SignUtil.getMD5Sign(params, merchantMy));

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
        boolean flag = jobj.get("success").getAsBoolean();
        if (!flag) return null;

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);
        jobj = jobj.get("data").getAsJsonObject();
        oi.setSn(jobj.get("sn").getAsString());
        oi.setPayUrl(jobj.get("payUrl").getAsString());
        oi.setName(jobj.get("name").getAsString());
        oi.setCard(jobj.get("card").getAsString());
        oi.setBankName(jobj.get("bankName").getAsString());

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
            body = params.get("body") == null ? "" : (String) params.get("body");
            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
            if (StringUtils.isBlank(body) || StringUtils.isBlank(sign)) {
                return null;
            }
            Map<String, Object> map = new TreeMap<String, Object>();
            map.put("body", body);
            if (sign.equals(SignUtil.getMD5Sign(map, merchantMy))) {//配对成功

//                return new Gson().fromJson(body, NotifyInfo.class);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JsonParser jp = new JsonParser();
                JsonObject jobj = jp.parse(body).getAsJsonObject();
                NotifyInfo ni = new NotifyInfo();
                ni.setSn(jobj.get("outTradeNo").getAsString());
                ni.setClientSn(jobj.get("sn").getAsString());
                ni.setStatus(jobj.get("status").getAsString());
                ni.setAmount(jobj.get("amount").getAsBigDecimal());
                ni.setActualAmount(jobj.get("actualAmount").getAsBigDecimal());
                ni.setSign(sign);
                ni.setPayTime(sdf.parse(jobj.get("payTime").getAsString()));
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
        params.put("appId", merchantNo);
        params.put("outTradeNo", sn);
        params.put("amount", amount);
//                params.put("payType", "CP");
        params.put("nonceStr", System.currentTimeMillis() + "");
        params.put("asyncUrl", domain + Constants.ASYNC_URL + "/" + id);
        params.put("returnUrl", domain + Constants.RETURN_URL + "/" + id);
//                params.put("ip", "127.0.0.1");

        if (channelParams != null) params.putAll(channelParams);

        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(params));

        apiInfo.setSignStr(SignUtil.joinKeyValue(new TreeMap<String, Object>(params), null, "&key=" + merchantMy, "&", true, "sign_type", "sign"));

        String sign = SignUtil.getMD5Sign(params, merchantMy);
        apiInfo.setSign(sign);

        params.put("sign", sign);

        apiInfo.setRequstData(gson.toJson(params));
        apiInfo.setDatas(params);
        apiInfo.setGotype(0);
        return apiInfo;
    }

    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("appId", client.getMerchantNo());
        params.put("outTradeNo", cash.getSn());
        params.put("password", "");
//        params.put("payType", "CP");
        params.put("amount", cash.getClientMoney());

        params.put("name", cash.getBankAccname());
        params.put("card", cash.getBankAccno());
        params.put("bankBranch", cash.getBankName() == null ? "-" : cash.getBankName());
        params.put("bankCode", cash.getBankCode());

        params.put("bankIfsc", cash.getBankIfsc());
        params.put("bankNation", cash.getBankNation());
        params.put("type", cash.getBtype());

        params.put("asyncUrl", domain + "/api/pay/cashnotify/" + keyname + "_" + client.getId());
        params.put("returnUrl", domain + "/api/pay/cashnotify/" + keyname + "_" + client.getId());
        params.put("nonceStr", System.currentTimeMillis() + "");

        params.put("sign", SignUtil.getMD5Sign(params, client.getMerchantMy()));

        return params;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        if (StringUtils.isBlank(data)) {
            return new CashInfo(false, "发起代付失败!");
        }
        JsonParser jp = new JsonParser();

        JsonObject jobj = jp.parse(data).getAsJsonObject();
        boolean flag = jobj.get("success").getAsBoolean();
        if (!flag) return new CashInfo(false, "发起代付失败:" + jobj.get("message"));

        CashInfo ci = new CashInfo(true, "发起代付成功");

        jobj = jobj.get("data").getAsJsonObject();
        ci.setSn(jobj.get("sn").getAsString());

        return ci;
    }

    @Override
    public QueryOrderInfo createQueryOrder(PayOrder order, PayClientChannel clientChannel, SysUser client,
                                           Map<String, Object> channelParams) {
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("appId", client.getMerchantNo());
        params.put("outTradeNo", order.getSn());
        params.put("nonceStr", System.currentTimeMillis() + "");

        params.put("sign", SignUtil.getMD5Sign(params, client.getMerchantMy()));

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
            if (!"200".equals(jobj.get("code").getAsString())) {
                return new QueryOrderResult(false, "查单失败:" + jobj.get("info"));
            }

            QueryOrderResult qor = new QueryOrderResult();
            jobj = jobj.get("data").getAsJsonObject();
            String status = jobj.get("status").getAsString();
            if (!"success".equals(status)) {
                qor.setStatus(false);
                qor.setMsg("订单未支付");
            } else {
                qor.setStatus(true);
            }

            return qor;
        } catch (Exception e) {
            return new QueryOrderResult(false, "查单失败:" + e.getMessage());
        }
    }

}
