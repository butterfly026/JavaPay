package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.*;
import com.city.city_collector.channel.util.Constants;
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 对接上游示例
 */
public class UUppPayClientTest extends ClientTest {

    //1. 商户资料
    public UUppPayClientTest() {
        /**
         * 商户号
         */
        merchantNo = "";
        /**
         * 商户秘钥
         */
        merchantMy = "";
        /**
         * 下单url
         */
        payUrl = "https://sfapi.usdotc.vip/api/gw";
        //???
        cashUrl = "";
        /**
         * 通道参数: 上游多通道使用
         */
        channelParams = new HashMap<String, Object>();
        channelParams.put("payWayID","2");

    }

    /**
     * 上游参数创建
     *
     * @param id            上游id. 回调地址使用
     * @param merchantNo    商户号. 请求上游参数
     * @param merchantMy    商户秘钥. 加密参数
     * @param channelParams 上游通道参数
     * @param sn            系统订单号
     * @param amount        下单金额
     * @param domain        访问域名,尽量不要使用
     * @param platform
     * @return 上游网络请求
     * OrderPreInfo 网络请求参数
     * setParams 请求内容
     * setRequestType 请求类型. Get, Post
     * setReqType post请求体类型  0. form-data 1. application/json
     * @see com.city.city_collector.channel.ChannelManager1#createOrder(ClientChannel, String, String, BigDecimal, Long, MerchantChannel, Integer)
     */
    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        //2. 请求参数核对
        Map<String, Object> params = new TreeMap<String, Object>();

        String amountValue = amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        params.put("amount", amountValue);

        if (channelParams != null) {
            params.putAll(channelParams);
        }

        params.put("partner", merchantNo);
        params.put("orderNumber", sn);
        params.put("type","0");


        String notifyUrl = ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id;
        params.put("callbackUrl", notifyUrl);

        params.put("timestamp", System.currentTimeMillis()+"");


        //3. 签名生成
        //示例md5加密: key=val&key=val&key=signKey&sign=sign
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Map.Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue())) && !et.getValue().equals("null")) {
                sbuf.append(et.getKey());
                sbuf.append("=");
                sbuf.append(et.getValue());
                sbuf.append("&");
            }
        }
        //1. 接接key
        //2. 最后key直接拼接
        sbuf.deleteCharAt(sbuf.length() - 1);
        sbuf.append(merchantMy);

        String signStr = sbuf.toString();
        System.out.println(signStr);
        //4. 签名核对
        //--- md5 是否大小写
        String sign = MD5Util.MD5Encode(signStr, "UTF-8").toLowerCase();
        System.out.println(sign);
        params.put("sign", sign);


        HashMap<String, Object> sendParam = new HashMap<>();
        sendParam.put("method", "recharge");
        sendParam.put("param", params);

        //5. 网络请求参数生成和方式
        //上游网络请求参数:
        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(sendParam);
        opi.setReqType(1);
        opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);

        return opi;
    }

    /**
     * 处理上游下单 有网络请求时返回数据进行处理,保存支付链接和上游订单号
     *
     * @param data            body体内容
     * @param clientId        上游ID
     * @param clientNo        上游编号
     * @param clientName      上游名
     * @param clientChannelId 上游通道id
     * @param url             上游请求地址
     * @param sn              平台订单号
     * @param channelParams   上游通道参数
     * @return 上游返回支付链接后,
     * 获取上游支付链接 (赋值给 PayUrl 字段)
     * 和
     * 上游订单号 (赋值给sn字段)
     * 进行保存.
     * 其余参数使用默认值
     * @see com.city.city_collector.channel.util.HttpUtil#requestData(String, com.city.city_collector.channel.util.HttpUtil.RequestType, Map, Map, String, Long, String, int, Long, Long, groovy.lang.Script, String, String, Map)
     */
    @Override
    public OrderInfo dealOrderData(String data, Long clientId, String clientNo, String clientName,
                                   Long clientChannelId, String url, String sn, Map<String, Object> channelParams) {
        //6. 请求订单返回数据
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JsonParser jp = new JsonParser();

        JsonObject jobj = jp.parse(data).getAsJsonObject();
        if (!"0".equals(jobj.get("code").getAsString())) {
            return null;
        }

        //7. 获取支付链接
        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);
        jobj = jobj.get("data").getAsJsonObject();
        oi.setSn(sn);
        oi.setPayUrl(jobj.get("paymentInfo").getAsString());
        oi.setName("");
        oi.setCard("");
        oi.setBankName("");
        oi.setClientId(clientId);
        oi.setClientNo(clientNo);
        oi.setClientName(clientName);
        oi.setClientChannelId(clientChannelId);
        return oi;
    }


    /**
     * 上游通知订单处理状态:
     * 服务器日志获取上游请求参数
     * cat /data/log/app/http/http.log | grep LN123432143214
     *
     * @param params        上游post表单请求时表单参数.
     * @param merchantNo    商户ID
     * @param merchantMy    商户秘钥. 验证签名使用
     * @param channelParams 上游通道参数
     * @param body          上游其它post请求时请求体内容
     * @return 验证上游签名是否正确, 签名正确只用获取上游返回的平台订单号.
     * NotifyInfo  sn参数: 平台订单号,查询此单是否成功
     * @see com.city.city_collector.web.controller.PayOrderController#notify(Long, Map, String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        //8. 上游回调通知
        try {
            JsonParser jp = new JsonParser();
            JsonObject jobj = jp.parse(body).getAsJsonObject();
            params.put("orderNumber", jobj.get("orderNumber").getAsString());
            params.put("amount", jobj.get("amount").getAsString());
            params.put("timestamp", jobj.get("timestamp").getAsString());
            params.put("status", jobj.get("status").getAsString());
            params.put("sign", jobj.get("sign").getAsString());


            String sign = (String) params.get("sign");
            if (StringUtils.isBlank(sign)) {
                return null;
            }

            if (!"2".equals(params.get("status").toString())) {
                return null;
            }

            Map<String, Object> signParams = new TreeMap<String, Object>();
            signParams.putAll(params);
            signParams.remove("sign");

            //9. 回调数据签名

            Iterator<Map.Entry<String, Object>> it = signParams.entrySet().iterator();
            StringBuffer sbuf = new StringBuffer("");
            while (it.hasNext()) {
                Map.Entry<String, Object> et = it.next();
                if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                    sbuf.append(et.getKey());
                    sbuf.append("=");
                    sbuf.append(et.getValue());
                    sbuf.append("&");
                }
            }

            sbuf.deleteCharAt(sbuf.length() - 1);
            sbuf.append(merchantMy);

            String anObject = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
            if (sign.equals(anObject)) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("orderNumber"));
                ni.setClientSn("");
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

    /**
     * 测试上游请求时使用
     * 直接复制请求参数方法即可.
     * <p>
     * ApiInfo setReqType 设置请求方式.
     * 0. post form
     * 1. application/json
     * 2. get请求
     *
     * @see com.city.city_collector.admin.pay.controller.ClientTestController#testApi(Long, BigDecimal, String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ApiInfo createReqParams_Test(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain) throws Exception {
        //2. 请求参数核对
        Map<String, Object> params = new TreeMap<String, Object>();

        String amountValue = amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        params.put("amount", amountValue);

        if (channelParams != null) {
            params.putAll(channelParams);
        }

        params.put("partner", merchantNo);
        params.put("orderNumber", sn);
        params.put("type","0");


        String notifyUrl = ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id;
        params.put("callbackUrl", notifyUrl);

        params.put("timestamp", System.currentTimeMillis()+"");


        //3. 签名生成
        //示例md5加密: key=val&key=val&key=signKey&sign=sign
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Map.Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue())) && !et.getValue().equals("null")) {
                sbuf.append(et.getKey());
                sbuf.append("=");
                sbuf.append(et.getValue());
                sbuf.append("&");
            }
        }
        //1. 接接key
        //2. 最后key直接拼接
        sbuf.deleteCharAt(sbuf.length() - 1);
        sbuf.append(merchantMy);

        String signStr = sbuf.toString();
        System.out.println(signStr);
        //4. 签名核对
        //--- md5 是否大小写
        String sign = MD5Util.MD5Encode(signStr, "UTF-8").toLowerCase();
        System.out.println(sign);
        params.put("sign", sign);


        HashMap<String, Object> sendParam = new HashMap<>();
        sendParam.put("method", "recharge");
        sendParam.put("param", params);

        //11. 测试下单请求数据
        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(sendParam));

        apiInfo.setSignStr(sbuf.toString());

        apiInfo.setSign(sign);

        apiInfo.setRequstData(gson.toJson(sendParam));
        apiInfo.setDatas(sendParam);
        apiInfo.setGotype(0);
        apiInfo.setReqType(1);

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
