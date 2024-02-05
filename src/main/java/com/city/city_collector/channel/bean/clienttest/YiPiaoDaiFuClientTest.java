
package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.*;
import com.city.city_collector.channel.util.yipiao.RsaUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author nb
 * @Description:
 */
public class YiPiaoDaiFuClientTest extends ClientTest {

    public YiPiaoDaiFuClientTest() {
        merchantNo = "";
        merchantMy = "";

        payUrl = "";
        queryUrl = "";
        cashUrl = "https://efps.epaylinks.cn/api/txs/pay/withdrawalToCard";
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

    /**
     * @param cash
     * @param client
     * @param domain
     * @param urlcash
     * @param keyname
     * @return
     * @see com.city.city_collector.admin.pay.controller.PayCashController#merchantClientCashSubmit(java.lang.Long, java.lang.Integer, java.lang.Long, java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal, java.lang.Long, java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {

        try {
            //check param
            if (StringUtils.isEmpty(cash.getBankAccno())) {
                HashMap<String, Object> error = new HashMap<>();
                error.put("dferror", "bankAccno 银行卡号不能为空");
                return error;
            }
            if (StringUtils.isEmpty(cash.getBankAccname())) {
                HashMap<String, Object> error = new HashMap<>();
                error.put("dferror", "bankAccname 开户人姓名不能为空");
                return error;
            }


            String publicKey = "MIIEIjCCAwqgAwIBAgIUJciVbai01yHdyTuMJ3UAOf6uWPQwDQYJKoZIhvcNAQELBQAwgZYxCzAJBgNVBAYTAkNOMTkwNwYDVQQKDDDljJfkuqzlpKnlqIHor5rkv6HnlLXlrZDllYbliqHmnI3liqHmnInpmZDlhazlj7gxFTATBgNVBAsMDOS8geS4muivgeS5pjE1MDMGA1UEAwws5aSp5aiB6K+a5L+h5pWw5a2X6K6k6K+B5Lit5b+D5LyB5Lia6K+B5LmmQ0EwHhcNMTgwMTExMDgwNjE4WhcNMTkwMTExMDgwNjE4WjCBjDE5MDcGA1UECgww5YyX5Lqs5aSp5aiB6K+a5L+h55S15a2Q5ZWG5Yqh5pyN5Yqh5pyJ6ZmQ5YWs5Y+4MRgwFgYDVQQLDA/ov5Dnu7TmlK/mjIHpg6gxNTAzBgNVBAMMLOaYk+elqOiBlOaUr+S7mOaciemZkOWFrOWPuC1FRlBT5ZWG5oi36Zeo5oi3MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAptTghT1nY9twX4VKrvJigGB/klcsP8tH+9aV5TofKskh8PQg8neTSprPMmEsidFGMJ26a4x/E88nJ6wMHAZqeWP0x64/GLtIff8r+WZjqBYP9WNiW71NcAsoJEBrlLlLrc5W/9mC+3z0oWYiz+LB9E+uMeCV3ocHP42Z6oGeCV6jSe1Sx2UBsCEROYh1nTnZQ13pHIghqO1Fc5MZTcTW5k3zviuFL7IJYUW79AIzh9SttcMbUZqGWkf808Ux3BhZzgUTL0g76fpCRjPilmlCJ/NdyGJ48E0IfMXHeTq3uiEjnO2nzsEV4zvhNoii0F2iH+fgMXBDwGd9O3mLv23gIQIDAQABo3AwbjAJBgNVHRMEAjAAMAsGA1UdDwQEAwIFoDBUBgNVHR8ETTBLMEmgR6BFhkNodHRwOi8vdG9wY2EuaXRydXMuY29tLmNuL3B1YmxpYy9pdHJ1c2NybD9DQT0kIAoKJENBX1NFUklBTE5VTUJFUiQkMA0GCSqGSIb3DQEBCwUAA4IBAQCUEFIx+vOeOQi2ZWR8TnADKYYTNsqv52e9WBz8VKfjbzIDA/hXqJUvtylyyEL4pBn82xG0WMk9UaAEEGrkpdA0figlbUInyXZE8WZYcE/7Nlr4aupH+JETp1OqvAsS0l5M2mH4OdVMDA/KpdwTMBa0it7f3QA8k2lXalHlifi35jrKj2q3DFKvy2n9pcwblPX2jpC7pZ1Y66tq7SapaGyCo12Q6o0vSCgGodFkZsczYmGdp4ZbphUbACVQ3Lhw/moqB3PadT6V0jB64BDDHnkDkaoUI5XUr1BMmTAyy+chBLHzk83Dx9ioNHdF0rHpEyUD73GfV11R7BIyzl5g8FpF";

            String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC5oeJ3WR+IYXJ8CQY07TfUK5/uTcTufSHgCLp++f9IAA/o0z/4MThxzEj3HvrYKr7M2ll41lSg392P1zo7Ro/0kCLnbn+/vKu0ma3IhQ6p5NdHVX+cqYRiku/vaWNEHOWdS2cR3khfogr6wYuJUNLcP3eCSy+yUg4KkVfK4LB130kDt8tKrYFj44siIw6QXcReOjUeul0/Udr9Xsq1FIi7F4YsMaFhFhoNQRwKJ5f8R4JXlT790N76IBI80o2+63HnAqg6Znx5Lven2yNfGnaE5qQI4LlHi0DZh9z69IhhzxXrNOoARJj6iXYkaCBqJgcpcJbTRBsZhRaBxiIkrVBPAgMBAAECggEBAK1EB/DkNAWFB2n0tmFiZbDZQz8YQc9k5uCI5SIt0HRw8mib/WJ49rlpN1XKCdMJGux0hfy++WDrc4+WEdlQBH/+pUWlIwJhZVd99fJrF4PGgJYpnKIqq9GiWuf5U1k5K60Eu2C3PJBe3KavTsHNufMwHil0akGkS0zb2rkXvj1VoXCWKQcT6Otnaedtng81pKjgmL3iOQp6fVPkrtpwcfpRgUfeAISfrvqLC8y+0dzbK6wU+gNNx4CO6R5BlXJlqN6taOHZUUgBWncuqKNxX42Qc066Wc1aR5ej1r/Ht6csNUiaOyc7F3m6BWBwPVEO6/eExfIKbT5IWPgQuGp+EiECgYEA9p9BD+ZpLt39e6EF9FqS7A5U4JvLY9Ufh8nWFdbg+22R22RQJYr17zRfiosg/VQ4S8dENqujZkST3UXKu6Honm8J2d/iUT0IKJ64Cg7iq4XFLhjmMbhODmGiAF+omB5yErExOAOsLzpV5Hs6fCCwxfmkru6TlKTzcPngkqKDglECgYEAwLDs2+/1Od9EiiNEbTuQkLrXl6dGre5xuFR5WUIO48QEogyhcHE4dFwZ84a5POSlN7r0ZbmAb4FFcKixZub/xinEDUt0I8wA91Fy8wDnaoMN2sQAAFO3MapIisu9eKcEUohwYWYLJEFvn4cqD3er/5XMYHWfX7qKG78oGIDRYJ8CgYBRLmkXV8xF7jMTsBSqs+nrJGiSKxaPfUdMxobfV4VlnlLF+kZbpVuBaycMDvJEEaYaY7SznlpCPs7BpsiubQSKqph+jLEN9t4pqN2CChZZScZ3fyC/OyEOoNrX7qUrtdHtD0DZZHQYc8Z0WJAO7h/ie2PXZZdU9dtg1BA1lKNfkQKBgBHbE0faN5YyS06lr/yTTXUr6ziBZrpNYzpakZgk1+5hRrChjQA3bazSsBDyBg5PfcPpTiZzByTODNpAt5e6hi8dxa4ZtNxwVhKU+JruFYO4OztveDSPjWVLy0cZVChvCaaAQDG2XAfzDF3zD4G5bDXV8sdpfuhHB+9ESnnizgQvAoGBAO4aCvW37RmmlDQfPiNkrpWX9FdZqZa8LrcU5VrJv4RPkafDHHuahgLSJHYh2qvRs0jdfhlFAYOoamBM0/udPGSGrMprKEZZunsL6C947Afhu61OTD0I0s+HKyzdWv3Degwr9kHwO5MZBAv3Bu3Y9BnJiWKm90YpAurcCTccBKCo";

            Map<String, Object> params = new TreeMap<String, Object>();

            BigDecimal h = new BigDecimal(100);
            params.put("amount", cash.getClientMoney().multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP));
            params.put("bankAccountType", "2");//1：对公，2：对私，5：存折
            params.put("bankCardNo", RsaUtils.encryptByPublicKey(cash.getBankAccno().trim(), publicKey));
            params.put("bankCity", cash.getBankCity() == null ? "" : cash.getBankCity().trim());
            params.put("bankName", cash.getBankName().trim());
            params.put("bankProvince", cash.getBankProvince() == null ? "" : cash.getBankProvince().trim());
            params.put("bankSub", cash.getBankSubname() == null ? "" : cash.getBankSubname().trim());

            if (!StringUtils.isEmpty(cash.getBankAccid())) {
                params.put("bankUserCert", RsaUtils.encryptByPublicKey(cash.getBankAccid().trim(), publicKey));
            }
            params.put("bankUserName", RsaUtils.encryptByPublicKey(cash.getBankAccname().trim(), publicKey));
            params.put("customerCode", client.getMerchantNo());
            params.put("isFullAmount", 0);
            params.put("nonceStr", UUID.randomUUID().toString().replaceAll("-", ""));

            if (cash.getClientMoney().intValue() < 2000) {
                String notifyurl = ApplicationData.getInstance().getConfig().getNotifyurl();
                params.put("notifyUrl", notifyurl + "/api/pay/cashnotify/" + keyname + "_" + client.getId());
            }

            params.put("outTradeNo", cash.getSn());
            params.put("payCurrency", "CNY");

            String bodyStr = new Gson().toJson(params);

            Map<String, Object> result = new TreeMap<String, Object>();
            result.put("customBody", bodyStr);

            String sign = RsaUtils.sign(privateKey, bodyStr);

            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            HashMap<String, String> headers = new HashMap<>();
            headers.put("x-efps-sign-no", client.getMerchantMy());
            headers.put("x-efps-sign-type", "SHA256withRSA");
            headers.put("x-efps-sign", sign);
            headers.put("x-efps-timestamp", df.format(new Date()));
            result.put("headers", headers);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        try {
            if (StringUtils.isBlank(data)) {
                return new CashInfo(false, "发起代付失败!");
            }
            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(data).getAsJsonObject();
            System.out.println(new Gson().toJson(jobj));
            if (!"0000".equals(jobj.get("returnCode").getAsString())) {
                return new CashInfo(false, "发起代付失败:" + jobj.get("returnMsg"));
            }

//            if (!"0".equals(jobj.get("resultCode").getAsString())) {
//                return new CashInfo(false, "发起代付失败:" + jobj.get("errMsg"));
//            }

            CashInfo ci = new CashInfo(true, "发起代付成功:");
            ci.setSn(sn);
            return ci;
        } catch (Exception e) {
            e.printStackTrace();
            return new CashInfo(false, "发起代付失败:" + e.getMessage());
        }
    }


    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        try {
            if (StringUtils.isBlank(body)) {
                return null;
            }
            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(body).getAsJsonObject();

            String sn = jobj.get("outTradeNo").getAsString();
            NotifyInfo notifyInfo = new NotifyInfo();
            notifyInfo.setSn(sn);
            notifyInfo.setStatus("success");
            return notifyInfo;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

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
