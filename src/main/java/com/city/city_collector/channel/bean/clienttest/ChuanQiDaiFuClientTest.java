package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

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
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChuanQiDaiFuClientTest extends ClientTest {

    public ChuanQiDaiFuClientTest() {
        merchantNo = "";
        merchantMy = "";

        payUrl = "";
        cashUrl = "http://47.113.228.63:10186/Payment_Dfpay_add.html";
        queryUrl = "";

        channelParams = new HashMap<String, Object>();
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        return null;
    }

    @Override
    public OrderInfo dealOrderData(String data, Long clientId, String clientNo, String clientName,
                                   Long clientChannelId, String url, String sn, Map<String, Object> channelParams) {
        return null;
    }

    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        return null;
    }

    @Override
    public ApiInfo createReqParams_Test(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain) throws Exception {
        return null;
    }

    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("mchid", client.getMerchantNo());
        params.put("out_trade_no", cash.getSn());

        params.put("money", cash.getClientMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
        params.put("bankname", cash.getBankName() == null ? "-" : cash.getBankName());
        params.put("subbranch", cash.getBankSubname() == null ? "-" : cash.getBankSubname());

        params.put("accountname", cash.getBankAccname());
        params.put("cardnumber", cash.getBankAccno());

        params.put("bank_code", cash.getBankCode());

        params.put("province", cash.getBankProvince() == null ? "-" : cash.getBankProvince());
        params.put("city", cash.getBankCity() == null ? "-" : cash.getBankCity());

        params.put("notifyurl", domain + "/api/pay/cashnotify/" + keyname + "_" + client.getId());

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

        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8").toUpperCase();

        params.put("pay_md5sign", sign);

        return params;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        if (StringUtils.isBlank(data)) {
            return new CashInfo(false, "发起代付失败!");
        }
        JsonParser jp = new JsonParser();

        JsonObject jobj = jp.parse(data).getAsJsonObject();
        String flag = jobj.get("status").getAsString();
        if (!"success".equals(flag)) {
            return new CashInfo(false, "发起代付失败:" + jobj.get("msg"));
        }

        CashInfo ci = new CashInfo(true, "发起代付成功");

        ci.setSn(jobj.get("transaction_id").getAsString());

        return ci;
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
