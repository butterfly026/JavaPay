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
import com.city.city_collector.channel.util.SignUtil;
import com.city.city_collector.channel.util.bmw.DesHelper;
import com.city.city_collector.channel.util.bmw.SignHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @Description:
 */
public class BwmClientTest extends ClientTest {

    /**
     *
     */
    public BwmClientTest() {
        merchantNo = "";
        merchantMy = "";

        payUrl = "https://doc.4pppsss.com/Doc/PayOrder";
        queryUrl = "https://doc.4pppsss.com/Doc/PayQuery";
        cashUrl = "https://doc.4pppsss.com/Doc/DaiFuOrder";

        channelParams = new HashMap<String, Object>();
//       channelParams.put("appid", "20210223172302f4c09a");
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        BigDecimal h = new BigDecimal(100);

        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("appid", merchantNo);
        params.put("order_trano_in", sn);// 商户单号
        params.put("order_goods", "pay");// 商品名称
        params.put("order_amount", amount.multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP));// 订单金额，单位分 (不能小于100)
        params.put("order_extend", "1");// 扩展参数，最大长度64位
        params.put("order_ip", "127.0.0.1");// 客户端真实ip
        params.put("order_return_url", "https://www.baidu.com");// 成功后同步地址
        params.put("order_notify_url", domain + Constants.ASYNC_URL + "/" + id);// 异步通知地址

//        if(channelParams!=null) params.putAll(channelParams);

        String jsonString = new Gson().toJson(params);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String value = entry.getValue().toString();
            if (!value.isEmpty()) {
                sb.append(String.format("%s%s", entry.getKey(), value));
            }
        }
        String sortStr = sb.toString();
        System.out.println(sortStr);

        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = SignHelper.genNonceStr();
        String signDesKey = SignHelper.MD5(timestamp + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgurvfDpqTObCiTG4JMIEBkroEUm6o35CmlStbRP1c5PHFOFSueuraWNRZap4wcJw5Wzd6y7WL2qLS+SqPiQlTM2+qrTqF8G8Lm7+1SBbv7uH+aUVSKCDCop63+1CQbiSht07RiIuAx6zF4kc79NG3q7D19HpQYpUsCs6/3X5kVdDiaqfhkRm5xAs2qgiE4OnIq3CmtIRWma2/aP0JRmdcKISiL3zq/+ugekAHbXR7kcF/lkN5Dh+StMIDcDiKmE8txxcWHrhJd+WIEPPlhEU7bdYSOGOclM8dPyzFEewRWWUhUx0GFFcy4UKa9B9ODOc1KW4bSDvlYFcMgWz5WFuGQIDAQAB" + nonce).substring(0, 8);
        String signtype = "MD5";

        // 公钥加密
        String data = DesHelper.encrypt(jsonString, signDesKey);
        // 私钥签名
        String sign = SignHelper.MD5(timestamp + nonce + sortStr + merchantMy);

        Map<String, Object> signmap = new HashMap<String, Object>();
        signmap.put("data", data);
        signmap.put("sign", sign);
        signmap.put("timestamp", timestamp);
        signmap.put("nonce", nonce);
        signmap.put("signtype", signtype);


        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(signmap);
        opi.setReqType(1);
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
        if (!"1".equals(jobj.get("code").getAsString())) {
            return null;
        }

        jobj = jobj.get("data").getAsJsonObject();

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);

        oi.setSn(jobj.get("order_number").getAsString());

        oi.setPayUrl(jobj.get("order_pay_url").getAsString());
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
//        //"{\"order_trano_in\":\"test_20190308111156419\",\"order_number\":\"20190308111156e38613\",\"order_pay\":2,\"order_state\":1,\"order_goods\":\"测试\",\"order_amount\":100,\"order_amount_real\":100,\"order_extend\":\"\",\"order_time\":\"2019-03-08 11:12:20\",\"signature\":\"995391B55B9378B5674FD9124E293AE3\"}";
//        String body="{\"order_trano_in\":\"LN202102242219194949724b\",\"order_number\":\"202102242219206bb13a\",\"order_state\":1,\"order_goods\":\"pay\",\"order_amount\":30000,\"order_amount_real\":30000,\"order_extend\":\"1\",\"order_time\":\"2021-02-24 22:28:05\",\"signature\":\"A19DA30A33DDE43AA7E262AA87D00AB2\"}";
//        BwmClientTest clientTest=new BwmClientTest();
//        NotifyInfo ni=clientTest.notifyData(new HashMap<String,Object>(), clientTest.merchantNo, clientTest.merchantMy, new HashMap<String,Object>(), body);
//        System.out.println(new Gson().toJson(ni));
//    }

    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        try {
            JsonParser jp = new JsonParser();
            JsonObject jobj = jp.parse(body).getAsJsonObject();
            String status = jobj.get("order_state").getAsString();
            String sign = jobj.get("signature").getAsString();

            if (!"1".equals(status)) {
                return null;
            }

            TreeMap<String, String> map = new TreeMap<String, String>();
            map.put("order_trano_in", jobj.get("order_trano_in").getAsString());
            map.put("order_number", jobj.get("order_number").getAsString());
            map.put("order_state", jobj.get("order_state").getAsString());

            map.put("order_goods", jobj.get("order_goods").getAsString());
            map.put("order_amount", jobj.get("order_amount").getAsString());
            map.put("order_amount_real", jobj.get("order_amount_real").getAsString());
            map.put("order_extend", jobj.get("order_extend").getAsString());
            map.put("order_time", jobj.get("order_time").getAsString());

            String signStr = SignHelper.sortSign(map) + merchantMy; //应用密钥
            System.out.println("排序后拼接Key的字符串:" + signStr);
            String md5 = SignHelper.MD5(signStr);

            if (sign.equalsIgnoreCase(md5)) {//配对成功
                System.out.println("sign ok!");

                NotifyInfo ni = new NotifyInfo();
                ni.setSn(jobj.get("order_trano_in").getAsString());
                ni.setClientSn(jobj.get("order_number").getAsString());
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
        BigDecimal h = new BigDecimal(100);

        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("appid", merchantNo);
        params.put("order_trano_in", sn);// 商户单号
        params.put("order_goods", "pay");// 商品名称
        params.put("order_amount", amount.multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP));// 订单金额，单位分 (不能小于100)
        params.put("order_extend", "1");// 扩展参数，最大长度64位
        params.put("order_ip", "127.0.0.1");// 客户端真实ip
        params.put("order_return_url", "https://www.baidu.com");// 成功后同步地址
        params.put("order_notify_url", domain + Constants.ASYNC_URL + "/" + id);// 异步通知地址

//        if(channelParams!=null) params.putAll(channelParams);

        String jsonString = new Gson().toJson(params);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String value = entry.getValue().toString();
            if (!value.isEmpty()) {
                sb.append(String.format("%s%s", entry.getKey(), value));
            }
        }
        String sortStr = sb.toString();
        System.out.println(sortStr);

        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = SignHelper.genNonceStr();
        String signDesKey = SignHelper.MD5(timestamp + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgurvfDpqTObCiTG4JMIEBkroEUm6o35CmlStbRP1c5PHFOFSueuraWNRZap4wcJw5Wzd6y7WL2qLS+SqPiQlTM2+qrTqF8G8Lm7+1SBbv7uH+aUVSKCDCop63+1CQbiSht07RiIuAx6zF4kc79NG3q7D19HpQYpUsCs6/3X5kVdDiaqfhkRm5xAs2qgiE4OnIq3CmtIRWma2/aP0JRmdcKISiL3zq/+ugekAHbXR7kcF/lkN5Dh+StMIDcDiKmE8txxcWHrhJd+WIEPPlhEU7bdYSOGOclM8dPyzFEewRWWUhUx0GFFcy4UKa9B9ODOc1KW4bSDvlYFcMgWz5WFuGQIDAQAB" + nonce).substring(0, 8);
        String signtype = "MD5";

        // 公钥加密
        String data = DesHelper.encrypt(jsonString, signDesKey);
        // 私钥签名
        String sign = SignHelper.MD5(timestamp + nonce + sortStr + merchantMy);

        Map<String, Object> signmap = new HashMap<String, Object>();
        signmap.put("data", data);
        signmap.put("sign", sign);
        signmap.put("timestamp", timestamp);
        signmap.put("nonce", nonce);
        signmap.put("signtype", signtype);


        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(signmap));

        apiInfo.setSignStr(jsonString);

        apiInfo.setSign(sign);

        apiInfo.setRequstData(gson.toJson(signmap));
        apiInfo.setDatas(signmap);
        apiInfo.setGotype(0);
        apiInfo.setReqType(1);
        return apiInfo;
    }

    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {
        BigDecimal h = new BigDecimal(100);
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("appId", client.getMerchantNo());
        params.put("order_trano_in", cash.getSn());

        params.put("order_account_name", cash.getBankAccname());
        params.put("order_account_no", cash.getBankAccno());
        params.put("order_bank_name", cash.getBankName());
        params.put("order_bank_code", cash.getBankCode());
        params.put("order_bank_mobile", cash.getBankAccmobile());
        params.put("order_bank_idcard", "");
        params.put("order_bank_branch", cash.getBankSubname());
        params.put("order_bank_firm_no", "123456");
        params.put("order_bank_city", cash.getBankCity());
        params.put("order_bank_province", cash.getBankProvince());

        params.put("order_amount", cash.getClientMoney().multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP));

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

        String jsonString = new Gson().toJson(params);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String value = entry.getValue().toString();
            if (!value.isEmpty()) {
                sb.append(String.format("%s%s", entry.getKey(), value));
            }
        }
        String sortStr = sb.toString();

        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = SignHelper.genNonceStr();
        String signDesKey = SignHelper.MD5(timestamp + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgurvfDpqTObCiTG4JMIEBkroEUm6o35CmlStbRP1c5PHFOFSueuraWNRZap4wcJw5Wzd6y7WL2qLS+SqPiQlTM2+qrTqF8G8Lm7+1SBbv7uH+aUVSKCDCop63+1CQbiSht07RiIuAx6zF4kc79NG3q7D19HpQYpUsCs6/3X5kVdDiaqfhkRm5xAs2qgiE4OnIq3CmtIRWma2/aP0JRmdcKISiL3zq/+ugekAHbXR7kcF/lkN5Dh+StMIDcDiKmE8txxcWHrhJd+WIEPPlhEU7bdYSOGOclM8dPyzFEewRWWUhUx0GFFcy4UKa9B9ODOc1KW4bSDvlYFcMgWz5WFuGQIDAQAB" + nonce).substring(0, 8);
        String signtype = "MD5";

        // 公钥加密
        String data = DesHelper.encrypt(jsonString, signDesKey);
        // 私钥签名
        String sign = SignHelper.MD5(timestamp + nonce + sortStr + merchantMy);

        Map<String, Object> signmap = new HashMap<String, Object>();
        signmap.put("data", data);
        signmap.put("sign", sign);
        signmap.put("timestamp", timestamp);
        signmap.put("nonce", nonce);
        signmap.put("signtype", signtype);

        return signmap;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        if (StringUtils.isBlank(data)) {
            return new CashInfo(false, "发起代付失败!");
        }
        JsonParser jp = new JsonParser();

        JsonObject jobj = jp.parse(data).getAsJsonObject();
        if (!"1".equals(jobj.get("code").getAsString())) {
            return new CashInfo(false, "发起代付失败:" + jobj.get("msg"));
        }

        CashInfo ci = new CashInfo(true, "发起代付成功");

        jobj = jobj.get("data").getAsJsonObject();
        ci.setSn(jobj.get("order_number").getAsString());

        return ci;
    }

    @Override
    public QueryOrderInfo createQueryOrder(PayOrder order, PayClientChannel clientChannel, SysUser client,
                                           Map<String, Object> channelParams) {
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("appid", "20210223172302f4c09a");
        params.put("order_trano_in", order.getSn());// 商户单号
        params.put("order_number", order.getClientSn());// 商品名称

//        if(channelParams!=null) params.putAll(channelParams);

        String jsonString = new Gson().toJson(params);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String value = entry.getValue().toString();
            if (!value.isEmpty()) {
                sb.append(String.format("%s%s", entry.getKey(), value));
            }
        }
        String sortStr = sb.toString();
//        System.out.println(sortStr);

        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = SignHelper.genNonceStr();
        String signDesKey = SignHelper.MD5(timestamp + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgurvfDpqTObCiTG4JMIEBkroEUm6o35CmlStbRP1c5PHFOFSueuraWNRZap4wcJw5Wzd6y7WL2qLS+SqPiQlTM2+qrTqF8G8Lm7+1SBbv7uH+aUVSKCDCop63+1CQbiSht07RiIuAx6zF4kc79NG3q7D19HpQYpUsCs6/3X5kVdDiaqfhkRm5xAs2qgiE4OnIq3CmtIRWma2/aP0JRmdcKISiL3zq/+ugekAHbXR7kcF/lkN5Dh+StMIDcDiKmE8txxcWHrhJd+WIEPPlhEU7bdYSOGOclM8dPyzFEewRWWUhUx0GFFcy4UKa9B9ODOc1KW4bSDvlYFcMgWz5WFuGQIDAQAB" + nonce).substring(0, 8);
        String signtype = "MD5";

        // 公钥加密
        String data = DesHelper.encrypt(jsonString, signDesKey);
        // 私钥签名
        String sign = SignHelper.MD5(timestamp + nonce + sortStr + client.getMerchantMy());

        Map<String, Object> signmap = new HashMap<String, Object>();
        signmap.put("data", data);
        signmap.put("sign", sign);
        signmap.put("timestamp", timestamp);
        signmap.put("nonce", nonce);
        signmap.put("signtype", signtype);

        QueryOrderInfo qoi = new QueryOrderInfo();
        qoi.setNeedRequest(true);
        qoi.setReqType(1);
        qoi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);
        qoi.setCharset("UTF-8");
        qoi.setParams(signmap);
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

            if (!"1".equals(jobj.get("code").getAsString())) {
                return new QueryOrderResult(false, "查单失败:" + jobj.get("msg"));
            }

            jobj = jobj.get("data").getAsJsonObject();

            if (!"1".equals(jobj.get("order_state").getAsString())) {
                return new QueryOrderResult(false, "查单失败:" + jobj.get("order_state"));
            }

            return new QueryOrderResult(true, "");
        } catch (Exception e) {
            return new QueryOrderResult(false, "查单失败:" + e.getMessage());
        }
    }

}
