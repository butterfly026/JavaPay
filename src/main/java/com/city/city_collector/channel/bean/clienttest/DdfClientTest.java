
package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
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
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author nb
 * @Description:
 */
public class DdfClientTest extends ClientTest {

    public DdfClientTest() {

//        merchantNo="";
//        merchantMy="";
//
//        payUrl="http://8.135.110.47:8124/bc/order/orderCheck";
//        cashUrl="http://8.135.110.47:8124/bc/order/orderCheck";

        merchantNo = "";
        merchantMy = "";

        payUrl = "http://8.135.106.161:8124/bc/order/orderCheck";
        cashUrl = "http://8.135.106.161:8124/bc/order/orderCheck";

        channelParams = new HashMap<String, Object>();
        channelParams.put("payType", "1");
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("merchantId", merchantNo);
        params.put("tradeNoThird", sn);
        params.put("notifyUrl", domain + Constants.ASYNC_URL + "/" + id);
        params.put("successUrl", "http://www.google.com");
        params.put("errorUrl", "http://www.google.com");

        params.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP));

        params.put("businessType", "1");

        if (channelParams != null) params.putAll(channelParams);

        String signStr = "" + params.get("merchantId") + params.get("tradeNoThird") + params.get("notifyUrl") + params.get("successUrl") + params.get("errorUrl") + params.get("amount") + params.get("payType") + params.get("businessType") + merchantMy;
        params.put("signstr", MD5Util.MD5Encode(signStr, "UTF-8"));

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
        jobj = jobj.get("body").getAsJsonObject();
        oi.setSn("");
        oi.setPayUrl(jobj.get("payLink").getAsString());
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
            String code = params.get("code") == null ? "" : (String) params.get("code");
            String msg = params.get("msg") == null ? "" : (String) params.get("msg");
            String tradeNoThird = params.get("tradeNoThird") == null ? "" : (String) params.get("tradeNoThird");
            String tradeNo = params.get("tradeNo") == null ? "" : (String) params.get("tradeNo");

            String signstr = params.get("signstr") == null ? "" : (String) params.get("signstr");


            if (signstr.equalsIgnoreCase(MD5Util.MD5Encode(code + tradeNoThird + msg + tradeNo + merchantMy, "utf-8"))) {//配对成功

                NotifyInfo ni = new NotifyInfo();
                ni.setSn(tradeNoThird);
                ni.setClientSn(tradeNo);
                ni.setStatus("success");

                ni.setAmount(BigDecimal.ZERO);
                ni.setActualAmount(BigDecimal.ZERO);
                ni.setSign(signstr);
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
        params.put("merchantId", merchantNo);
        params.put("tradeNoThird", sn);
        params.put("notifyUrl", domain + Constants.ASYNC_URL + "/" + id);
        params.put("successUrl", "http://www.google.com");
        params.put("errorUrl", "http://www.google.com");

        params.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP));

        params.put("businessType", "1");

        if (channelParams != null) params.putAll(channelParams);

        String signStr = "" + params.get("merchantId") + params.get("tradeNoThird") + params.get("notifyUrl") + params.get("successUrl") + params.get("errorUrl") + params.get("amount") + params.get("payType") + params.get("businessType") + merchantMy;
        String sign = MD5Util.MD5Encode(signStr, "UTF-8");
        params.put("signstr", sign);

        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(params));

        apiInfo.setSignStr(signStr);

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
