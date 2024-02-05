
package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
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
import com.city.city_collector.channel.util.HttpUtil;
import com.city.city_collector.channel.util.RSAUtils2;
import com.city.city_collector.channel.util.tianxia.LocalUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author nb
 * @Description:
 */
public class HaiShangClientTest extends ClientTest {

    public HaiShangClientTest() {
        merchantNo = "";
        merchantMy = "//+iOUHldAjRgHT95KFVT5Xyu7e3lvcE7pY3kV6tzV5OY5ttw2HrIw8wwqMPwPOADv++pubTSfp+S+YhL99MwV67MEwroKH1vBpTQrCrCXU9lXrgckZKZxYdyVXLDfFJdBauQB8inAgMBAAECgYAg4h8OtIhpwFJNzlqxdcJgsaospY9ZePdlvVPCQ7qkBX85ipPN80P72gs+WKpsZefVGI2ev0lSQwcGcofvdnHkXMmR/KzD4Sz4Jc35gQ5xInX80XQ6PJsechACZgSbcYypRklLVmJ4Sew3Rgn8RerQ0Z5PVsAAhvUPql/X0P3CGQJBAPm/UCudJhibdnskPL0BnMAII1KRTG8aMKINpXiUBbYYL8iP8uDu8XIHdUthnQuumAr3xZqNrkgm81+AtS+E9OsCQQCeemWJGX2qp9WVWg3aVgR6SDeJiedm2//eaRpQX7dzVWYBEcsQMehLmykf5c/u+1rcGk5udwbRzpLPd5axzjw1AkBgYtqBCpRiWNWALvsWAuJJJprjSdpS4qu0T0n2YEJYBbujTxLwhqc+V+yH3DNtB8P8L6XewSFrDUVOLXE/1rNjAkEAhAEVpC48c7LbAE2DrnLpxeVzhTf+LLphlaPcTEh+KS5gCdIFX9cCVD/I8I3fNoPd+epbnD2+y3IS7YcCWdacZQJAbTDzGmHj3WuxO6GANdAPz3r1FW8bsfNNs/MHGaPOegP/fMMKISmHLyUwKGTyiE4iquRcaG8iXBAXa8s+n5syMA==";

        payUrl = "http://47.100.184.138:32510/QRCodeSys/online.action";
        cashUrl = "http://47.100.184.138:25430/up_account/main.action";
        channelParams = new HashMap<String, Object>();
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        String agentnum = "104";
        Map<String, Object> totalParam = new HashMap<String, Object>();
        //公钥
        String merchantGy = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCam3u1WqfLaf1bkLZ6gf1dGMEtMXV3xM4KMBvojlB5XQI0YB0/eShVU+V8ru3t5b3BO6WN5Ferc1eTmObbcNh6yMPMMKjD8DzgA7/vqbm00n6fkvmIS/fTMFeuzBMK6Ch9bwaU0Kwqwl1PZV64HJGSmcWHclVyw3xSXQWrkAfIpwIDAQAB";

        totalParam.put("serviceCode", "A0480");

        //请求数据
        Map<String, Object> data = new TreeMap<String, Object>();

        data.put("amount", amount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP));
        //机构编号
        data.put("agentnum", agentnum);

        data.put("channelNum", "100036");
        data.put("payCode", "100201");
        data.put("payType", "");
        data.put("bankCode", "");
        data.put("accNoType", "");
        data.put("appOrderId", sn);
        data.put("callBackUrl", "http://103.68.61.81" + Constants.ASYNC_URL + "/" + id);
        data.put("frontUrl", "http://www.google.com");
        if (channelParams != null) data.putAll(channelParams);

        System.out.println(data);

        Map<String, Object> data1 = new HashMap<String, Object>();
        totalParam.put("request", data1);

        String dataStr = new GsonBuilder().disableHtmlEscaping().serializeNulls().create().toJson(data);
        dataStr = Base64.encodeBase64String(dataStr.getBytes());

        data1.put("account", merchantNo);
        //用私钥生成签名
        byte[] mySign = LocalUtil.sign(Base64.decodeBase64(merchantMy.getBytes()), dataStr);

        String reqSign = new String(mySign);

        data1.put("sign", reqSign);

        //通过公钥进行数据加密
        byte[] encodedData = RSAUtils2.encryptByPublicKey(dataStr.getBytes(), merchantGy);
        String reqData = Base64.encodeBase64String(encodedData);
        data1.put("data", reqData);

        Map<String, Object> params = new HashMap<String, Object>();


        String reqParam = new GsonBuilder().disableHtmlEscaping().serializeNulls().create().toJson(totalParam);
        params.put("reqParam", reqParam);

        String url = "http://47.100.184.138:32510/QRCodeSys/online.action";

        String responseData = HttpUtil.commonRequestDataXl(url, params, sn, id, 20);
        if (responseData != null) {
            if (StringUtils.isBlank(responseData)) {
                return null;
            }
            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(responseData).getAsJsonObject();
            jobj = jobj.get("response").getAsJsonObject();

            if (!"0000".equals(jobj.get("respCode").getAsString())) {
                return null;
            }

            Map<String, Object> datas = new HashMap<String, Object>();
            datas.put("data", jobj.get("resp").getAsString());
            datas.put("payUrl", "http://146.196.52.24/api/pay/formRequest?sn=" + sn);
            datas.put("sn", jobj.get("orderId").getAsString());

            OrderPreInfo opi = new OrderPreInfo();
            opi.setCharset("UTF-8");
            opi.setParams(datas);
            opi.setIsOrderInfo(1);
            opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);
            return opi;
        }
        return null;

//        OrderPreInfo opi=new OrderPreInfo();
//        opi.setCharset("UTF-8");
//        opi.setParams(params);
//        opi.setReqType(0);
//        opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);
//        return opi;
    }

    @Override
    public OrderInfo dealOrderData(String data, Long clientId, String clientNo, String clientName,
                                   Long clientChannelId, String url, String sn, Map<String, Object> channelParams) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JsonParser jp = new JsonParser();

        JsonObject jobj = jp.parse(data).getAsJsonObject();
        jobj = jobj.get("response").getAsJsonObject();

        if (!"0000".equals(jobj.get("respCode").getAsString())) {
            return null;
        }

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);

        oi.setSn(jobj.get("orderId").getAsString());

        oi.setPayUrl("/api/pay/formRequest?sn=");
        oi.setData(jobj.get("resp").getAsString());
        oi.setName("");
        oi.setCard("");
        oi.setBankName("");

        oi.setClientId(clientId);
        oi.setClientNo(clientNo);
        oi.setClientName(clientName);

        oi.setClientChannelId(clientChannelId);

        oi.setGtype(1);
        return oi;
    }

    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        try {
            System.out.println("====");
            body = URLDecoder.decode(body, "UTF-8");

            System.out.println(body);

            JsonParser jp = new JsonParser();
            JsonObject jobj = jp.parse(body).getAsJsonObject();
            jobj = jobj.get("response").getAsJsonObject();

            String code = jobj.get("respCode").getAsString();
            if (!"0000".equals(code)) {
                return null;
            }

            String sign = jobj.get("sign").getAsString();
            String sn = jobj.get("appOrderId").getAsString();
            String clientSn = jobj.get("orderId").getAsString();

            NotifyInfo ni = new NotifyInfo();
            ni.setSn(sn);
            ni.setClientSn(clientSn);
            ni.setStatus("success");
            ni.setAmount(jobj.get("amount").getAsBigDecimal());
            ni.setActualAmount(jobj.get("amount").getAsBigDecimal());
            ni.setSign(sign);
            ni.setPayTime(new Date());
            return ni;
//            String signData="{data=,orderId=,respCode=,respInfo=}";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ApiInfo createReqParams_Test(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain) throws Exception {
        String agentnum = "104";
        Map<String, Object> totalParam = new HashMap<String, Object>();
        String merchantGy = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCam3u1WqfLaf1bkLZ6gf1dGMEtMXV3xM4KMBvojlB5XQI0YB0/eShVU+V8ru3t5b3BO6WN5Ferc1eTmObbcNh6yMPMMKjD8DzgA7/vqbm00n6fkvmIS/fTMFeuzBMK6Ch9bwaU0Kwqwl1PZV64HJGSmcWHclVyw3xSXQWrkAfIpwIDAQAB";

        totalParam.put("serviceCode", "A0480");

        //请求数据
        Map<String, Object> data = new TreeMap<String, Object>();

        data.put("amount", amount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP));
        //机构编号
        data.put("agentnum", agentnum);

        data.put("channelNum", "100036");
        data.put("payCode", "100201");
        data.put("payType", "02");
        data.put("bankCode", "");
        data.put("accNoType", "");
        data.put("appOrderId", sn);
        data.put("callBackUrl", domain + Constants.ASYNC_URL + "/" + id);
        data.put("frontUrl", "http://www.google.com");
        if (channelParams != null) data.putAll(channelParams);

        Map<String, Object> data1 = new HashMap<String, Object>();
        totalParam.put("request", data1);

        String dataStr = new GsonBuilder().disableHtmlEscaping().serializeNulls().create().toJson(data);
        dataStr = Base64.encodeBase64String(dataStr.getBytes());

        data1.put("account", merchantNo);
        //用私钥生成签名
        byte[] mySign = LocalUtil.sign(Base64.decodeBase64(merchantMy.getBytes()), dataStr);

        String reqSign = new String(mySign);

        data1.put("sign", reqSign);

//                data1.put("data", AESUtil.encrypt(dataStr, merchantGy));
        //通过公钥进行数据加密
        byte[] encodedData = RSAUtils2.encryptByPublicKey(dataStr.getBytes(), merchantGy);
        String reqData = Base64.encodeBase64String(encodedData);
        data1.put("data", reqData);

        Map<String, Object> params = new HashMap<String, Object>();

//        String reqParam=new GsonBuilder().disableHtmlEscaping().serializeNulls().create().toJson(totalParam);
//        System.out.println("req:"+reqParam);
        params.put("reqParam", new GsonBuilder().disableHtmlEscaping().serializeNulls().create().toJson(totalParam));

        String url = "http://47.100.184.138:32510/QRCodeSys/online.action";

        Gson gson = new Gson();

        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setSignData(gson.toJson(data));

        apiInfo.setSignStr(gson.toJson(params));

        apiInfo.setSign(gson.toJson(params));

        apiInfo.setRequstData(gson.toJson(params));

        apiInfo.setGotype(1);

        String responseData = HttpUtil.commonRequestDataXl(url, params, sn, id, 20);
        if (responseData != null) {
            apiInfo.setResponseData(responseData);

            if (StringUtils.isBlank(responseData)) {
                return null;
            }
            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(responseData).getAsJsonObject();

            jobj = jobj.get("response").getAsJsonObject();

            if (!"0000".equals(jobj.get("respCode").getAsString())) {
                return null;
            }

            apiInfo.setPayUrl("http://146.196.52.24/api/pay/formRequest?sn=" + sn);
            apiInfo.setSavePayOrder(0);

            Map<String, Object> datas = new HashMap<String, Object>();
            datas.put("data", jobj.get("resp").getAsString());
            datas.put("payUrl", "http://146.196.52.24/api/pay/formRequest?sn=" + sn);
            datas.put("sn", jobj.get("orderId").getAsString());

            apiInfo.setDatas(datas);
        } else {
            apiInfo.setStatus(false);
            apiInfo.setResponseData(responseData);
        }
        return apiInfo;


//        Gson gson=new Gson();
//        ApiInfo apiInfo=new ApiInfo();
//
//        apiInfo.setSignData(gson.toJson(data));
//
//        apiInfo.setSignStr(gson.toJson(params));
//
//        apiInfo.setSign(gson.toJson(params));
//
//        apiInfo.setRequstData(gson.toJson(params));
//        apiInfo.setDatas(params);
//        apiInfo.setGotype(0);
//        return apiInfo;
    }

    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {
        String agentNum = "104";
        Map<String, Object> totalParam = new HashMap<String, Object>();
        //公钥
        String merchantGy = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCam3u1WqfLaf1bkLZ6gf1dGMEtMXV3xM4KMBvojlB5XQI0YB0/eShVU+V8ru3t5b3BO6WN5Ferc1eTmObbcNh6yMPMMKjD8DzgA7/vqbm00n6fkvmIS/fTMFeuzBMK6Ch9bwaU0Kwqwl1PZV64HJGSmcWHclVyw3xSXQWrkAfIpwIDAQAB";

        totalParam.put("code", "A0001");

        //请求数据
        Map<String, Object> data = new TreeMap<String, Object>();

        data.put("mercNum", client.getMerchantNo());

        data.put("amount", cash.getClientMoney().multiply(new BigDecimal(100)) + "");
        //结算账户名
        data.put("settlementName", cash.getBankAccname());
        //结算卡号
        data.put("cardNo", cash.getBankAccno());
        //身份证号
        data.put("crpIdNo", StringUtils.isBlank(cash.getBankAccid()) ? "ww" : cash.getBankAccid());
        //银行预留手机号
        data.put("phone", StringUtils.isBlank(cash.getBankAccmobile()) ? "13901391390" : cash.getBankAccmobile());
        //联行号
        data.put("banksysnumber", StringUtils.isBlank(cash.getBankId()) ? "ww" : cash.getBankId());
        //开户行省市
        String city = "";
        if (cash.getBankProvince() != null) {
            city = cash.getBankProvince();
            if (cash.getBankCity() != null) {
                city += "-" + cash.getBankCity();
            }
        } else {
            city = "ww-ww";
        }

        data.put("bankProvinceCity", city);
        //开户总行
        data.put("headBank", cash.getBankName());
        //开户支行
        data.put("branchBank", StringUtils.isBlank(cash.getBankSubname()) ? "ww" : cash.getBankSubname());

        //机构编号
        data.put("agentNum", agentNum);
        //订单号
        data.put("orderNo", cash.getSn());

        System.out.println(data);

        Map<String, Object> data1 = new HashMap<String, Object>();
        totalParam.put("request", data1);

        data1.put("agentNum", agentNum);

        String dataStr = new GsonBuilder().disableHtmlEscaping().serializeNulls().create().toJson(data);
        System.out.println(dataStr);
        //dataStr：{"agentnum":"108","amount":200000,"bankProvinceCity":"ww-ww","banksysnumber":"ww","branchBank":"ww","cardNo":"6214680220032614","crpIdNo":"ww","headBank":"北京银行","mercNum":"850100074020003","orderNo":"TestCAWESXDC10002","phone":"13655556666","settlementName":"陈焰"}
        dataStr = Base64.encodeBase64String(dataStr.getBytes());
        //用私钥生成签名
        byte[] mySign = LocalUtil.sign(Base64.decodeBase64(client.getMerchantMy().getBytes()), dataStr);

        String reqSign = new String(mySign);
//        System.out.println("sign:"+reqSign);
        data1.put("sign", reqSign);

        //通过公钥进行数据加密
        try {
            byte[] encodedData = RSAUtils2.encryptByPublicKey(dataStr.getBytes(), merchantGy);
            String reqData = Base64.encodeBase64String(encodedData);
//            System.out.println("data:"+reqData);
            data1.put("data", reqData);
        } catch (Exception e) {
        }

        Map<String, Object> params = new HashMap<String, Object>();

        String reqParam = new GsonBuilder().disableHtmlEscaping().serializeNulls().create().toJson(totalParam);
        System.out.println("req:" + reqParam);

        params.put("requestData", reqParam);
        params.put("unitType", "paid");
        return params;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        if (StringUtils.isBlank(data)) {
            return new CashInfo(false, "发起代付失败!");
        }
        JsonParser jp = new JsonParser();

        JsonObject jobj = jp.parse(data).getAsJsonObject();
        if (!"0000".equals(jobj.get("code").getAsString())) {
            return new CashInfo(false, "发起代付失败:" + jobj.get("msg"));
        }

        CashInfo ci = new CashInfo(true, "发起代付成功");
        ci.setSn("-");

        return ci;
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
