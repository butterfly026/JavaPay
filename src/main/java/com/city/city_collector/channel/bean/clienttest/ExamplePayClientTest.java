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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 对接上游示例
 */
public class ExamplePayClientTest extends ClientTest {

    //1. 商户资料
    public ExamplePayClientTest() {
        /**
         * 商户号
         */
        merchantNo = "" ;
        /**
         * 商户秘钥
         */
        merchantMy = "" ;
        /**
         * 下单url
         */
        payUrl = "" ;
        //???
        cashUrl = "" ;
        /**
         * 通道参数: 上游多通道使用
         */
        channelParams = new HashMap<String, Object>();
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

        //通用参数示例
        //1. 32位随机数
        String random32Str = UUID.randomUUID().toString().replaceAll("-", "");

        //2. 请求金额.
        // 单位元,保留2小数点.
        BigDecimal amoutYuan = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        //单位 分,是否保留小数点
        BigDecimal h = new BigDecimal(100);
        BigDecimal amoutFen = amount.multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP);

        //3. 请求日期指定格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String requestDate = dateFormat.format(new Date());

        //时间戳 , 注意长度
        String requestTime10Length = (System.currentTimeMillis() / 1000 + "").substring(0, 10);
        String requestTime13Length = System.currentTimeMillis() + "" ;


        //参数排序:
        // TreeMap 默认排序规则：按照key的字典顺序来排序（升序）
        //降序
        TreeMap<String, String> map = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });


        //2. 请求参数核对
        Map<String, Object> params = new TreeMap<String, Object>();

        String amountValue = amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        params.put("Amount", amountValue);

        if (channelParams != null) {
//            params.put("CategoryCode", "138");
            params.putAll(channelParams);
        }
        params.put("ClientIp", "127.0.0.1");

        params.put("MchId", merchantNo);
        params.put("MchOrderNo", sn);

        String notifyUrl = ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id;
        params.put("NotifyUrl", notifyUrl);
        String returnUrl = "https://google.com" ;
        params.put("ReturnUrl", returnUrl);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("RequestTime", sdf.format(new Date()));


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
        sbuf.append("key=" + merchantMy);
        //2. 最后key直接拼接
//        sbuf.deleteCharAt(sbuf.length() - 1);
//        sbuf.append(merchantMy);

        String signStr = sbuf.toString();
        System.out.println(signStr);
        //4. 签名核对
        //--- md5 是否大小写
        String sign = MD5Util.MD5Encode(signStr, "UTF-8").toLowerCase();
        System.out.println(sign);
        params.put("sign", sign);


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
     * @see com.city.city_collector.channel.util.HttpUtil#requestData(java.lang.String, com.city.city_collector.channel.util.HttpUtil.RequestType, java.util.Map, java.util.Map, java.lang.String, java.lang.Long, java.lang.String, int, java.lang.Long, java.lang.Long, groovy.lang.Script, java.lang.String, java.lang.String, java.util.Map)
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
        if (!"0".equals(jobj.get("ErrCode").getAsString())) {
            return null;
        }

        //7. 获取支付链接
        OrderInfo oi = new OrderInfo();
        oi.setFlag(true);
        oi.setData(data);
        jobj = jobj.get("ResData").getAsJsonObject();
        oi.setSn(sn);
        oi.setPayUrl(jobj.get("CallBackUrl").getAsString());
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
     * @see com.city.city_collector.web.controller.PayOrderController#notify(java.lang.Long, java.util.Map, java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        //8. 上游回调通知
        try {
            JsonParser jp = new JsonParser();
            JsonObject jobj = jp.parse(body).getAsJsonObject();
            params.put("MchId", jobj.get("MchId").getAsString());
            params.put("OrderNo", jobj.get("OrderNo").getAsString());
            params.put("MchOrderNo", jobj.get("MchOrderNo").getAsString());
            params.put("Amount", jobj.get("Amount").getAsString());
            params.put("Sign", jobj.get("Sign").getAsString());


            String sign = (String) params.get("Sign");
            if (StringUtils.isBlank(sign)) {
                return null;
            }

            if(!"1".equals(params.get("state").toString())){
                return null;
            }

            Map<String, Object> signParams = new TreeMap<String, Object>();
            signParams.putAll(params);
            signParams.remove("Sign");

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

            sbuf.append("key=" + merchantMy);

            String anObject = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
            if (sign.equals(anObject)) {
                NotifyInfo ni = new NotifyInfo();
                ni.setSn((String) params.get("MchOrderNo"));
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
     * @see com.city.city_collector.admin.pay.controller.ClientTestController#testApi(java.lang.Long, java.math.BigDecimal, java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ApiInfo createReqParams_Test(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain) throws Exception {
        //10. 测试下单参数生成 .
        Map<String, Object> params = new TreeMap<String, Object>();

        params.put("Amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP));

        if (channelParams != null) {
//            params.put("CategoryCode", "138");
            params.putAll(channelParams);
        }
        params.put("ClientIp", "127.0.0.1");

        params.put("MchId", merchantNo);
        params.put("MchOrderNo", sn);

        params.put("NotifyUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("RequestTime", sdf.format(new Date()));
        params.put("ReturnUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);


        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Map.Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                sbuf.append(et.getKey().toLowerCase());
                sbuf.append("=");
                sbuf.append(String.valueOf(et.getValue()));
                sbuf.append("&");
            }
        }
        sbuf.append("key=" + merchantMy);
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8");
        params.put("Sign", sign);


        //11. 测试下单请求数据
        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData(gson.toJson(params));

        apiInfo.setSignStr(sbuf.toString());

        apiInfo.setSign(sign);

        apiInfo.setRequstData(gson.toJson(params));
        apiInfo.setDatas(params);
        apiInfo.setGotype(0);
        apiInfo.setReqType(0);

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
