
package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
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

/**
 * @author nb
 * @Description:
 */
public class AnAnClientTest extends ClientTest {

    public AnAnClientTest() {
        merchantNo = "";
        merchantMy = "";

        payUrl = "http://39.108.74.99/gateway/api/trade";
        queryUrl = "http://39.108.74.99/gateway/api/trade";
        cashUrl = "http://39.108.74.99/gateway/api/trade";
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
        String banks = ",ICBC,ABC,BOC,CCB,BCOM,CMB,GDB,CITIC,CMBC,CEB,PABC,SPDB,PSBC,HXB,CIB,NBCB,BJBANK,";

        BigDecimal h = new BigDecimal(100);

        Map<String, Object> params = new TreeMap<String, Object>();
        // $dferror 表示 失败
        if ("SPABANK".equals(cash.getBankCode())) {
            cash.setBankCode("PABC");
        }
        if (banks.indexOf(cash.getBankCode()) == -1) {
            params.put("dferror", "此代付通道不支持该银行编码:" + cash.getBankCode());
            return params;
        }

        params.put("tradeType", "cs.df.submit");
        params.put("version", "2.0");

        params.put("mchNo", client.getMerchantNo());
        params.put("amount", cash.getClientMoney().multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP));
        params.put("mchOrderNo", cash.getSn());

        params.put("notifyUrl", domain + "/api/pay/cashnotify/" + keyname + "_" + client.getId());

        params.put("accountType", "0");

        params.put("accName", cash.getBankAccname().trim());
        params.put("bankNo", cash.getBankAccno().trim());
        params.put("remark", "tixian");
        params.put("bankSettleNo", "");

        params.put("bankFullName", cash.getBankSubname() == null ? "" : cash.getBankSubname().trim());

        params.put("accProvince", cash.getBankProvince() == null ? "" : cash.getBankProvince().trim());
        params.put("accCity", cash.getBankCity() == null ? "" : cash.getBankCity().trim());
        params.put("bankCode", cash.getBankCode() == null ? "" : cash.getBankCode().trim());
        params.put("bankName", cash.getBankName() == null ? "" : cash.getBankName().trim());

        params.put("phone", null);

        params.put("payType", "0");
        params.put("walletNo", null);

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
        sbuf.append("paySecret=");
        sbuf.append(client.getMerchantMy());
        System.out.println(sbuf.toString());

        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8").toUpperCase();
        params.put("sign", sign);


        return params;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        try {
            if (StringUtils.isBlank(data)) {
                return new CashInfo(false, "发起代付失败!");
            }
            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(data).getAsJsonObject();
            if (!"0".equals(jobj.get("status").getAsString())) {
                return new CashInfo(false, "发起代付失败:" + jobj.get("message"));
            }

            if (!"0".equals(jobj.get("resultCode").getAsString())) {
                return new CashInfo(false, "发起代付失败:" + jobj.get("errMsg"));
            }

            CashInfo ci = new CashInfo(true, "发起代付成功:" + jobj.get("payResult"));

            ci.setSn(jobj.get("cpOrderNo").getAsString());
            return ci;
        } catch (Exception e) {
            e.printStackTrace();
            return new CashInfo(false, "发起代付失败:" + e.getMessage());
        }
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
