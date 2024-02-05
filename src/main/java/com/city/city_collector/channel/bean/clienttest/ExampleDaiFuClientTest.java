
package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.*;
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * @author nb
 * @Description:
 */
public class ExampleDaiFuClientTest extends ClientTest {

    public ExampleDaiFuClientTest() {
        merchantNo = "chaojifu";
        merchantMy = "af50aa681ab94b39a15cab05569e9fea";

        payUrl = "";
        queryUrl = "";
        cashUrl = "http://16.163.4.68/api/pay/issued";

    }


    /**
     * @param cash
     * @param client
     * @param domain
     * @param urlcash
     * @param keyname
     * @return 1.  表示 失败   params.put("dferror", "此代付通道不支持该银行编码:" + cash.getBankCode());
     * 2.
     * @see com.city.city_collector.admin.pay.controller.PayCashController#merchantClientCashSubmit(Long, Integer, Long, BigDecimal, BigDecimal, BigDecimal, Long, String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {

        Map<String, Object> params = new TreeMap<String, Object>();


        params.put("merchantNo", client.getMerchantNo());

        BigDecimal amount = cash.getClientMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
        params.put("amount", amount);
        params.put("merchantSn", cash.getSn());

        //小于2000元自动回调
        if (amount.compareTo(new BigDecimal(2000)) < 0) {
            String notifyurl = ApplicationData.getInstance().getConfig().getNotifyurl();
            params.put("notifyUrl", notifyurl + "/api/pay/cashnotify/" + keyname + "_" + client.getId());
        } else {
            params.put("notifyUrl", "https://google.com");
        }
        params.put("channelType", "1");
        params.put("bankAccountNo", cash.getBankAccno().trim());
        params.put("bankAccountName", cash.getBankAccname().trim());
        params.put("bankName", cash.getBankName().trim());
        params.put("bankCode", cash.getBankCode());
        params.put("bankNameSub", cash.getBankSubname());
        params.put("time", System.currentTimeMillis());
        params.put("signType", "md5");

        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                sbuf.append(et.getKey());
                sbuf.append("=");
                sbuf.append(et.getValue().toString());
                sbuf.append("&");
            }
        }
        sbuf.append("key=" + client.getMerchantMy());
        System.out.println(sbuf.toString());
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8").toUpperCase();
        params.put("sign", sign);
        System.out.println(sign);

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

            if (!"200".equals(jobj.get("code").getAsString())) {
                return new CashInfo(false, "发起代付失败:" + jobj.get("msg"));
            }

            return new CashInfo(true, "发起代付成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CashInfo(false, "发起代付失败:" + e.getMessage());
        }
    }


    /**
     * @param params
     * @param merchantNo
     * @param merchantMy
     * @param channelParams
     * @param body
     * @return
     * @see com.city.city_collector.web.controller.PayOrderController#cashnotify(String, Long, Map, javax.servlet.http.HttpServletRequest, String)
     */
    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        try {
            NotifyInfo notifyInfo = new NotifyInfo();
            if ("5".equalsIgnoreCase(params.get("payStatus").toString())) {
                notifyInfo.setStatus("success");
                notifyInfo.setSn(params.get("merchantSn").toString());
                notifyInfo.setClientSn(params.get("sn").toString());
            } else {
                notifyInfo.setStatus("fail");
            }
            return notifyInfo;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public ApiInfo createReqParams_Test(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain) throws Exception {

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
