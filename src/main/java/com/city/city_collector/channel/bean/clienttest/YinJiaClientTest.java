
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
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author nb
 * @Description:
 */
public class YinJiaClientTest extends ClientTest {

    /**
     *
     */
    public YinJiaClientTest() {
        merchantNo = "";
        merchantMy = "";

//        payUrl="https://pay.sepropay.com/sepro/pay/web";
        payUrl = "https://www.inrusdt.com/b/recharge";
        cashUrl = "https://www.inearning.com/api/df/add";
        queryUrl = "https://www.inrusdt.com/api/b/getRechargeStatus";

        channelParams = new HashMap<String, Object>();


    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {

        BigDecimal money = amount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
        String userId = SignUtil.genNonceStr();
        String notifyAddress = domain + Constants.ASYNC_URL + "_json/" + id;

        String payMethod = "inrpay";

        if (channelParams != null && !channelParams.isEmpty()) {
            if (channelParams.get("payMethod") != null) {
                payMethod = channelParams.get("payMethod").toString();
            }
        }

        String signStr = "bizNum=" + sn + "&merchantId=" + merchantNo + "&money=" + money + "&notifyAddress=" + notifyAddress + "&payMethod=" + payMethod + "&type=recharge&userId=" + userId + "&key=" + merchantMy;
        String sign = MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase();

        String payUrl = "?" + "bizNum=" + sn + "&merchantId=" + merchantNo + "&money=" + money + "&notifyAddress=" + URLEncoder.encode(notifyAddress, "UTF-8") + "&payMethod=" + payMethod + "&type=recharge&userId=" + userId + "&sign=" + sign;

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("payUrl", payUrl);
        params.put("data", "");

        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(params);
        opi.setReqType(0);

        opi.setIsOrderInfo(1);
        opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);

        return opi;
    }

    @Override
    public OrderInfo dealOrderData(String data, Long clientId, String clientNo, String clientName,
                                   Long clientChannelId, String url, String sn, Map<String, Object> channelParams) {
        //
        return null;
    }

    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        try {
            Integer status = (Integer) params.get("status");

            Integer money = (Integer) params.get("money");
            Integer usdtAmount = (Integer) params.get("usdtAmount");
            Integer merchantPrice = (Integer) params.get("merchantPrice");
            String merchantBizNum = (String) params.get("merchantBizNum");
            String merchantId = (String) params.get("merchantId");
            String sysBizNum = (String) params.get("sysBizNum");
            String sign = (String) params.get("sign");
            if (status.intValue() != 1) {
                return null;
            }
//            System.out.println(params);

            String signStr = "merchantBizNum=" + merchantBizNum + "&merchantId=" + merchantId + "&merchantPrice=" + merchantPrice + "&money=" + money + "&status=" + status + "&sysBizNum=" + sysBizNum + "&usdtAmount=" + usdtAmount + "&key=" + merchantMy;

            System.out.println(signStr);

            if (sign.equals(MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase())) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn(merchantBizNum);
                ni.setClientSn(sysBizNum);
                ni.setStatus("success");
                ni.setAmount(new BigDecimal(money));
                ni.setActualAmount(new BigDecimal(money));
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
        BigDecimal money = amount.multiply(new BigDecimal(100));
        String userId = SignUtil.genNonceStr();
        String notifyAddress = domain + Constants.ASYNC_URL + "_json/" + id;

        String payMethod = "inrpay";

        if (channelParams != null && !channelParams.isEmpty()) {
            if (channelParams.get("payMethod") != null) {
                payMethod = channelParams.get("payMethod").toString();
            }
        }

        String signStr = "bizNum=" + sn + "&merchantId=" + merchantNo + "&money=" + money + "&notifyAddress=" + notifyAddress + "&payMethod=" + payMethod + "&type=recharge&userId=" + userId + "&key=" + merchantMy;
        String sign = MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase();

        String payUrl = "?" + "bizNum=" + sn + "&merchantId=" + merchantNo + "&money=" + money + "&notifyAddress=" + URLEncoder.encode(notifyAddress, "UTF-8") + "&payMethod=" + payMethod + "&type=recharge&userId=" + userId + "&sign=" + sign;

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("payUrl", payUrl);
        params.put("data", "");

        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(signStr);

        apiInfo.setSignStr(signStr);

        apiInfo.setSign(sign);

        params.put("sign", sign);

        apiInfo.setRequstData(gson.toJson(params));
        apiInfo.setDatas(params);
        apiInfo.setGotype(1);
        apiInfo.setPayUrl(payUrl);
        return apiInfo;
    }

    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {
        BigDecimal money = cash.getClientMoney().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("merchantId", "5ffc3ad5a9afde0b62885bce");
        params.put("money", money);
        params.put("bizNum", cash.getSn());

        params.put("name", cash.getBankAccname());
        params.put("mobile", cash.getBankAccmobile());
        params.put("account", cash.getBankAccno());
        params.put("ifscCode", cash.getBankIfsc());
        params.put("notifyAddress", domain + "/api/pay/cashnotify/" + keyname + "_" + client.getId());

        String signStr = "account=" + params.get("account") + "&bizNum=" + params.get("bizNum") + "&ifscCode=" + params.get("ifscCode") + "&merchantId=" + params.get("merchantId") + "&mobile=" + params.get("mobile") + "&money=" + params.get("money") + "&name=" + params.get("name") + "&notifyAddress=" + params.get("notifyAddress") + "&key=0001DB4F7A6D8DAD249F2AF344D0F9D6";
        String sign = MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase();
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
        if (!jobj.get("success").getAsBoolean()) {
            return new CashInfo(false, "发起代付失败:" + jobj.get("msg"));
        }

        CashInfo ci = new CashInfo(true, "发起代付成功");

        jobj = jobj.get("data").getAsJsonObject();
        ci.setSn(jobj.get("sysBizNum").getAsString());

        return ci;
    }

    @Override
    public QueryOrderInfo createQueryOrder(PayOrder order, PayClientChannel clientChannel, SysUser client,
                                           Map<String, Object> channelParams) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("merchantId", client.getMerchantNo());
        params.put("bizNum", order.getSn());

        String signStr = "bizNum=" + params.get("bizNum") + "&merchantId=" + params.get("merchantId") + "&key=" + client.getMerchantMy();
        String sign = MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase();

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
            BigDecimal amount = order.getAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);

            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(data).getAsJsonObject();
            if (!jobj.get("success").getAsBoolean()) {
                return new QueryOrderResult(false, "查单失败:" + jobj.get("msg"));
            }
            QueryOrderResult qor = new QueryOrderResult();
            jobj = jobj.get("data").getAsJsonObject();
            int status = jobj.get("status").getAsInt();
            if (status != 1) {
                qor.setStatus(false);
            } else {
                long money = jobj.get("money").getAsLong();
                if (amount.equals(new BigDecimal(money))) {
                    qor.setStatus(true);
                }
            }

            return qor;
        } catch (Exception e) {
            return new QueryOrderResult(false, "查单失败:" + e.getMessage());
        }
    }

}
