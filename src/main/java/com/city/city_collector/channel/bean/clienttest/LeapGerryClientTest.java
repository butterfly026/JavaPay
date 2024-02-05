package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
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
import com.city.city_collector.channel.util.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author nb
 * @Description:
 */
public class LeapGerryClientTest extends ClientTest {

    /**
     *
     */
    public LeapGerryClientTest() {
        merchantNo = "";
        merchantMy = "";

        payUrl = "http://paygerry.payto89.com/order/getUrl";
        cashUrl = "http://tgerry.payto89.com:82/order/cashout";
        channelParams = new HashMap<String, Object>();
        channelParams.put("ptype", "3");
    }


    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setReqType(0);
        opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.GET);

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("mch_id", merchantNo);
        params.put("ptype", 1);
        params.put("order_sn", sn);
        params.put("money", amount);
        params.put("goods_desc", sn);
        params.put("client_ip", "127.0.0.1");

        params.put("format", "json");

        params.put("notify_url", domain + Constants.RETURN_URL + "/" + id);
        params.put("time", TimeUtil.getSystemTimeSecond());


        if (channelParams != null) params.putAll(channelParams);
        String sign = SignUtil.getMD5Sign(params, merchantMy).toLowerCase();

        params.put("sign", sign);

        Map<String, Object> p = new HashMap<String, Object>();
        Gson gson = new Gson();
        p.put("json", URLEncoder.encode(gson.toJson(params), "utf-8"));
        opi.setParams(p);
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

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);
        jobj = jobj.get("data").getAsJsonObject();
        oi.setSn("");
        oi.setPayUrl(jobj.get("url").getAsString());
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
            if (!"1".equals(params.get("state"))) {
                return null;
            }
            String sn = params.get("sh_order") == null ? "" : (String) params.get("sh_order");
            String clientSn = params.get("pt_order") == null ? "" : (String) params.get("pt_order");

            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
            if (StringUtils.isBlank(sn) || StringUtils.isBlank(sign)) {
                return null;
            }

            Map<String, Object> map = new TreeMap<String, Object>();
            map.put("sh_order", params.get("sh_order"));
            map.put("pt_order", params.get("pt_order"));
            map.put("money", params.get("money"));
            map.put("time", params.get("time"));
            map.put("state", params.get("state"));
            map.put("goods_desc", params.get("goods_desc"));

            if (sign.equals(SignUtil.getMD5Sign(map, merchantMy))) {//配对成功

                NotifyInfo ni = new NotifyInfo();
                ni.setSn(sn);
                ni.setClientSn(clientSn);
                ni.setStatus("success");
                ni.setAmount(new BigDecimal((String) params.get("money")));
                ni.setActualAmount(new BigDecimal((String) params.get("money")));
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
        params.put("ptype", 1);
        params.put("order_sn", sn);
        params.put("money", amount);
        params.put("goods_desc", sn);
        params.put("client_ip", "127.0.0.1");

        params.put("format", "json");

        params.put("notify_url", domain + Constants.RETURN_URL + "/" + id);
        params.put("time", "1605338564");


        if (channelParams != null) params.putAll(channelParams);

        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(params));

        apiInfo.setSignStr(SignUtil.joinKeyValue(new TreeMap<String, Object>(params), null, "&key=" + merchantMy, "&", true, "sign_type", "sign"));

        String sign = SignUtil.getMD5Sign(params, merchantMy).toLowerCase();
        apiInfo.setSign(sign);

        params.put("sign", sign);

        apiInfo.setRequstData("json=" + URLEncoder.encode(gson.toJson(params), "utf-8"));
        apiInfo.setDatas(params);
        apiInfo.setGotype(0);
        apiInfo.setReqType(2);
        return apiInfo;
    }

    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {

        return null;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {

        return null;
    }


    @Override
    public QueryOrderInfo createQueryOrder(PayOrder order, PayClientChannel clientChannel, SysUser client,
                                           Map<String, Object> channelParams) {
        //
        return null;
    }


    @Override
    public QueryOrderResult dealQueryResult(String data, PayOrder order, PayClientChannel clientChannel, SysUser client,
                                            Map<String, Object> channelParams) {
        //
        return null;
    }

}
