package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.*;
import com.city.city_collector.channel.util.Constants;
import com.city.city_collector.channel.util.HttpUtil;
import com.city.city_collector.channel.util.SignUtil;
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 卓合
 */
public class ShanDianPayClientTest extends ClientTest {

    public ShanDianPayClientTest() {
        merchantNo = "10079";
        merchantMy = "6x5vl3hcx53ybabegekge5s8nuor5xcf";
        payUrl = "http://www.hivezb.com/Pay_Index.html";
        cashUrl = "";
        channelParams = new HashMap<String, Object>();
        channelParams.put("pay_bankcode", "920");

//        {"pay_bankcode":"920"}
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("pay_memberid", merchantNo);
        params.put("pay_orderid", sn);
        params.put("pay_applydate", sdf.format(new Date()));
        params.put("pay_notifyurl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        params.put("pay_callbackurl", "http://google.com");

        params.put("pay_amount", amount);

        if (channelParams != null) params.putAll(channelParams);

        String signStr = "pay_amount=" + params.get("pay_amount") + "&pay_applydate=" + params.get("pay_applydate") + "&pay_bankcode=" + params.get("pay_bankcode") + "&pay_callbackurl=" + params.get("pay_callbackurl") + "&pay_memberid=" + params.get("pay_memberid") + "&pay_notifyurl=" + params.get("pay_notifyurl") + "&pay_orderid=" + params.get("pay_orderid") + "&key=" + merchantMy;
//        System.out.println(signStr);
        params.put("pay_md5sign", MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase());

//        params.put("pay_format", "json");
        params.put("pay_client", sn);

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
//        System.out.println("dealOrder: "+data);

        JsonParser jp = new JsonParser();

        JsonObject jobj = jp.parse(data).getAsJsonObject();
        if (!"1".equals(jobj.get("status").getAsString())) {
            return null;
        }

        jobj = jobj.getAsJsonObject("msg");

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);

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

//            OK

            if (!"00".equals(params.get("returncode"))) {
                return null;
            }

            String signStr = "amount=" + params.get("amount") + "&memberid=" + params.get("memberid") + "&orderid=" + params.get("orderid") + "&returncode=" + params.get("returncode") + "&transaction_id=" + params.get("transaction_id") + "&key=" + merchantMy;
//            System.out.println(signStr);
            String sign = MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase();
//            System.out.println(sign);
            if (sign.equals(params.get("sign"))) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("orderid"));
                ni.setClientSn("");
                ni.setStatus("success");
                ni.setAmount(new BigDecimal(params.get("amount").toString()));
                ni.setActualAmount(new BigDecimal(params.get("amount").toString()));
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("pay_memberid", merchantNo);
        params.put("pay_orderid", sn);
        params.put("pay_applydate", sdf.format(new Date()));
        params.put("pay_notifyurl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        params.put("pay_callbackurl", "http://google.com");

        params.put("pay_amount", amount);

        if (channelParams != null) params.putAll(channelParams);

        String signStr = "pay_amount=" + params.get("pay_amount") + "&pay_applydate=" + params.get("pay_applydate") + "&pay_bankcode=" + params.get("pay_bankcode") + "&pay_callbackurl=" + params.get("pay_callbackurl") + "&pay_memberid=" + params.get("pay_memberid") + "&pay_notifyurl=" + params.get("pay_notifyurl") + "&pay_orderid=" + params.get("pay_orderid") + "&key=" + merchantMy;
//        System.out.println(signStr);
        params.put("pay_md5sign", MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase());

//        params.put("pay_format", "json");
        params.put("pay_client", sn);


        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(signStr);

        apiInfo.setSignStr(params.get("pay_md5sign").toString());

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
