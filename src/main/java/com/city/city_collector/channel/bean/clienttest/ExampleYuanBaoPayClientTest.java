package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.*;
import com.city.city_collector.channel.util.Constants;
import com.city.city_collector.channel.util.SignUtil;
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 对接上游示例
 */
public class ExampleYuanBaoPayClientTest extends ClientTest {

    //1. 商户资料
    public ExampleYuanBaoPayClientTest() {
        /**
         * 商户号
         */
        merchantNo = "13025";
        /**
         * 商户秘钥
         */
        merchantMy = "i6ulylt42xlln8gd19fhdtamc084507g";
        /**
         * 下单url
         */
        payUrl = "http://sc-wsjs.com/Pay_Index.html";
        //???
        cashUrl = "";
        /**
         * 通道参数: 上游多通道使用
         */
        channelParams = new HashMap<String, Object>();
        channelParams.put("pay_bankcode", "904");

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("pay_memberid", merchantNo);
        params.put("pay_orderid", sn);
        params.put("pay_applydate", sdf.format(new Date()));
        params.put("pay_notifyurl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        params.put("pay_callbackurl", "http://google.com");

        params.put("pay_amount", amount);
        params.put("pay_productname", "g1");

        if (channelParams != null) {
            params.putAll(channelParams);
        }

        String signStr = "pay_amount=" + params.get("pay_amount") + "&pay_applydate=" + params.get("pay_applydate") + "&pay_bankcode=" + params.get("pay_bankcode") + "&pay_callbackurl=" + params.get("pay_callbackurl") + "&pay_memberid=" + params.get("pay_memberid") + "&pay_notifyurl=" + params.get("pay_notifyurl") + "&pay_orderid=" + params.get("pay_orderid") + "&key=" + merchantMy;
//        System.out.println(signStr);
        params.put("pay_md5sign", MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase());
//        params.put("pay_md5sign", SignUtil.getMD5Sign(params, merchantMy).toUpperCase());

//        System.out.println(params);
        params.put("pay_format", "json");


        //5. 网络请求参数生成和方式
        //上游网络请求参数:
        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(params);
        opi.setReqType(0);
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
        if (StringUtils.isBlank(data)) {
            return null;
        }
//        System.out.println("dealOrder: "+data);

        JsonParser jp = new JsonParser();

        JsonObject jobj = jp.parse(data).getAsJsonObject();
        if (!"200".equals(jobj.get("status").getAsString())) {
            return null;
        }

        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);

        oi.setSn("");
        oi.setPayUrl(jobj.get("payurl").getAsString());
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

//            ok

            if (!"00".equals(params.get("returncode"))) {
                return null;
            }

            String signStr = "amount=" + params.get("amount") + "&datetime=" + params.get("datetime") + "&memberid=" + params.get("memberid") + "&orderid=" + params.get("orderid") + "&returncode=" + params.get("returncode") + "&transaction_id=" + params.get("transaction_id") + "&key=" + merchantMy;
//            System.out.println(signStr);
            String sign = MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase();
//            System.out.println(sign);
            if (sign.equals(params.get("sign"))) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("orderid"));
                ni.setClientSn((String) params.get("transaction_id"));
                ni.setStatus("success");
                ni.setAmount(new BigDecimal(params.get("amount").toString()));
                ni.setActualAmount(new BigDecimal(params.get("amount").toString()));
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
        //10. 测试下单参数生成 .
        //2. 请求参数核对
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("pay_memberid", merchantNo);
        params.put("pay_orderid", sn);
        params.put("pay_applydate", sdf.format(new Date()));
        params.put("pay_notifyurl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        params.put("pay_callbackurl", "http://google.com");

        params.put("pay_amount", amount);
        params.put("pay_productname", "g1");

        if (channelParams != null) {
            params.putAll(channelParams);
        }

        String signStr = "pay_amount=" + params.get("pay_amount") + "&pay_applydate=" + params.get("pay_applydate") + "&pay_bankcode=" + params.get("pay_bankcode") + "&pay_callbackurl=" + params.get("pay_callbackurl") + "&pay_memberid=" + params.get("pay_memberid") + "&pay_notifyurl=" + params.get("pay_notifyurl") + "&pay_orderid=" + params.get("pay_orderid") + "&key=" + merchantMy;
//        System.out.println(signStr);
        params.put("pay_md5sign", MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase());
//        params.put("pay_md5sign", SignUtil.getMD5Sign(params, merchantMy).toUpperCase());

//        System.out.println(params);
        params.put("pay_format", "json");



        //11. 测试下单请求数据
        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(signStr);

        apiInfo.setSignStr(params.get("pay_md5sign").toString());

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
