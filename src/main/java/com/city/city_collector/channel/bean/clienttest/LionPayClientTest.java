
package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * @author nb
 * @Description:
 */
public class LionPayClientTest extends ClientTest {

    /**
     *
     */
    public LionPayClientTest() {
        merchantNo = "";
        merchantMy = "";

        payUrl = "http://103.68.61.129/api/pay/order";
        queryUrl = "http://103.68.61.129/api/pay/orderInfo";
        cashUrl = "http://103.68.61.129/api/pay/issued";
        channelParams = new HashMap<String, Object>();

        channelParams.put("channel", "alipay");
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("merchantNo", merchantNo);
        params.put("merchantSn", sn);
        params.put("amount", amount);
        params.put("notifyUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        params.put("time", System.currentTimeMillis() + "");

        params.put("signType", "md5");

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
        if (!"200".equals(jobj.get("code").getAsString())) {
            return null;
        }

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);
        jobj = jobj.get("data").getAsJsonObject();
        oi.setSn(jobj.get("sn").getAsString());
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

//    public static void main(String[] args) {
////        {"amount":"50.00","createTime":"2021-02-07 14:33:31","merchantNo":"LON1825","merchantSn":"MS202102071433312934c5c2","money":"50.00","notifyStatus":"0","orderStatus":"1","payTime":"2021-02-07 14:33:48","sign":"D0CAC88ECE32A476F3021EE5C5726229","sn":"LN2021020714333131439b5d"}
//        Map < String, Object > params=new HashMap<String,Object>();
//        params.put("amount", "50.00");
//        params.put("createTime", "2021-02-07 14:33:31");
//        params.put("merchantNo", "LON1825");
//        params.put("merchantSn", "MS202102071433312934c5c2");
//        params.put("money", "50.00");
//        params.put("notifyStatus", "0");
//        params.put("orderStatus", "1");
//        params.put("payTime", "2021-02-07 14:33:48");
//        params.put("orderStatus", "1");
//        params.put("sign", "D0CAC88ECE32A476F3021EE5C5726229");
//        params.put("sn", "LN2021020714333131439b5d");
//
//        ClientTest clientTest=new LionPayClientTest();
//        NotifyInfo ni=clientTest.notifyData(params, clientTest.merchantNo, clientTest.merchantMy,new HashMap<String,Object>(), "");
//        System.out.println(new Gson().toJson(ni));
//    }

    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        try {
            String orderStatus = params.get("orderStatus") == null ? "" : (String) params.get("orderStatus");
            String sign = params.get("sign") == null ? "" : (String) params.get("sign");
            if (!"1".equals(orderStatus)) {
                return null;
            }

            Map<String, Object> map = new TreeMap<String, Object>();
            map.put("merchantNo", (String) params.get("merchantNo"));
            map.put("sn", (String) params.get("sn"));
            map.put("merchantSn", (String) params.get("merchantSn"));
            map.put("orderStatus", (String) params.get("orderStatus"));
            map.put("notifyStatus", (String) params.get("notifyStatus"));
            map.put("payTime", (String) params.get("payTime"));
            map.put("createTime", (String) params.get("createTime"));
            map.put("money", (String) params.get("money"));
            map.put("amount", (String) params.get("amount"));
            map.put("remark", (String) params.get("remark"));

            if (sign.equals(SignUtil.getMD5Sign(map, merchantMy))) {//配对成功

                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("merchantSn"));
                ni.setClientSn((String) params.get("sn"));
                ni.setStatus("success");
                ni.setAmount(new BigDecimal(0));
                ni.setActualAmount(new BigDecimal(0));
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
        params.put("merchantNo", merchantNo);
        params.put("merchantSn", sn);
        params.put("amount", amount);
        params.put("notifyUrl", domain + Constants.ASYNC_URL + "/" + id);

        params.put("time", System.currentTimeMillis() + "");

        params.put("signType", "md5");


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
        params.put("merchantNo", client.getMerchantNo());
        params.put("merchantSn", cash.getSn());
        params.put("amount", cash.getClientMoney());

        params.put("notifyUrl", domain + "/api/pay/cashnotify/" + keyname + "_" + client.getId());


        params.put("bankAccountNo", cash.getBankAccno());
        params.put("bankAccountName", cash.getBankAccname());
        params.put("bankName", cash.getBankName() == null ? "-" : cash.getBankName());

        params.put("bankCode", cash.getBankCode());

        params.put("bankNameSub", cash.getBankSubname());

        params.put("remark", "");

        params.put("time", System.currentTimeMillis() + "");
        params.put("signType", "md5");
        params.put("channelType", cash.getChannelType() == null ? 0 : cash.getChannelType());
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
        if (!"200".equals(jobj.get("code").getAsString())) {
            return new CashInfo(false, "发起代付失败:" + jobj.get("msg"));
        }

        CashInfo ci = new CashInfo(true, "发起代付成功");

        jobj = jobj.get("data").getAsJsonObject();
        ci.setSn(jobj.get("sn").getAsString());

        return ci;
    }

    @Override
    public QueryOrderInfo createQueryOrder(PayOrder order, PayClientChannel clientChannel, SysUser client,
                                           Map<String, Object> channelParams) {
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("merchantSn", order.getSn());
        params.put("time", System.currentTimeMillis() + "");

        params.put("signType", "md5");

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
            String status = jobj.get("orderStatus").getAsString();
            if (!"1".equals(status)) {
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
