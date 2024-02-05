
package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.city.city_collector.admin.city.util.ApplicationData;
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
public class LecpClientTest extends ClientTest {

    /**
     *
     */
    public LecpClientTest() {
//        merchantNo="10671";
//        merchantMy="iu0xi19x2905h0fkac8gt715r7cn7cxf";
//
//        payUrl="http://sx.baianpay.cn/Pay_Index.html";
//        cashUrl="";
//        queryUrl="http://sx.baianpay.cn/Pay_Trade_query.html";
//       channelParams=new HashMap<String,Object>();
//       //支付宝  28   微信  27
//       //支付宝转卡
//       channelParams.put("pay_bankcode", "935");

        merchantNo = "" ;
        merchantMy = "" ;

        payUrl = "http://hr.amghesy.cn/Pay_Index.html" ;
        cashUrl = "" ;
        queryUrl = "" ;
        channelParams = new HashMap<String, Object>();
        //支付宝  901   微信  902
        channelParams.put("pay_bankcode", "937");
    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("pay_memberid", merchantNo);
        params.put("pay_orderid", sn);
        params.put("pay_applydate", sdf.format(new Date()));
        params.put("pay_notifyurl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);

        params.put("pay_callbackurl", "http://google.com");

        params.put("pay_amount", amount);
        params.put("pay_productname", "g1");
        params.put("pay_attach", "mc");

        if (channelParams != null) {
            params.putAll(channelParams);
        }

        String signStr = "pay_amount=" + params.get("pay_amount") + "&pay_applydate=" + params.get("pay_applydate") + "&pay_bankcode=" + params.get("pay_bankcode") + "&pay_callbackurl=" + params.get("pay_callbackurl") + "&pay_memberid=" + params.get("pay_memberid") + "&pay_notifyurl=" + params.get("pay_notifyurl") + "&pay_orderid=" + params.get("pay_orderid") + "&key=" + merchantMy;
//        System.out.println(signStr);
        params.put("pay_md5sign", MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase());
//        params.put("pay_md5sign", SignUtil.getMD5Sign(params, merchantMy).toUpperCase());

//        System.out.println(params);

        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(params);
        opi.setReqType(0);
        opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);

        return opi;
    }

    @Override
    public OrderInfo dealOrderData(String data, Long clientId, String clientNo, String clientName,
                                   Long clientChannelId, String url, String sn, Map<String, Object> channelParams) {
        if (StringUtils.isBlank(data)) {
            return null;
        }

//        <script>location.href='http://43.228.65.19:3302/Pay_request_Index.gt?orderid=PDFX0943B2HB0943555210xg&sign=A54532F228E368EA25A452287789296B';</script>

        String pattern = "(https?):\\/\\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]" ;
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(data);
        if (matcher.find()) {
            String payUrl = matcher.group(0);

            OrderInfo oi = new OrderInfo();
            oi.setFlag(true);
            oi.setData(data);

            oi.setSn("");
            oi.setPayUrl(payUrl);
            oi.setName("");
            oi.setCard("");
            oi.setBankName("");

            oi.setClientId(clientId);
            oi.setClientNo(clientNo);
            oi.setClientName(clientName);

            oi.setClientChannelId(clientChannelId);

            return oi;
        }
        return null;
    }


    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        try {
            if (!"00".equals(params.get("returncode"))) {
                return null;
            }

            String signStr = "amount=" + params.get("amount") + "&datetime=" + params.get("datetime") + "&memberid=" + params.get("memberid") + "&orderid=" + params.get("orderid") + "&returncode=" + params.get("returncode") + "&transaction_id=" + params.get("transaction_id") + "&key=" + merchantMy;
            String sign = MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase();
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

    @Override
    public ApiInfo createReqParams_Test(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("pay_memberid", merchantNo);
        params.put("pay_orderid", sn);
        params.put("pay_applydate", sdf.format(new Date()));
        params.put("pay_notifyurl", ApplicationData.getInstance().getConfig().getNotifyurl() + Constants.ASYNC_URL + "/" + id);
        params.put("pay_callbackurl", "http://google.com");

        params.put("pay_amount", amount);
        params.put("pay_productname", "g1");
        params.put("pay_attach", "mc");

        if (channelParams != null) {
            params.putAll(channelParams);
        }

        String signStr = "pay_amount=" + params.get("pay_amount") + "&pay_applydate=" + params.get("pay_applydate") + "&pay_bankcode=" + params.get("pay_bankcode") + "&pay_callbackurl=" + params.get("pay_callbackurl") + "&pay_memberid=" + params.get("pay_memberid") + "&pay_notifyurl=" + params.get("pay_notifyurl") + "&pay_orderid=" + params.get("pay_orderid") + "&key=" + merchantMy;
        params.put("pay_md5sign", MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase());

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
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("pay_memberid", client.getMerchantNo());
        params.put("pay_orderid", order.getSn());
        String signStr = "pay_memberid=" + params.get("pay_memberid") + "&pay_orderid=" + params.get("pay_orderid") + "&key=" + client.getMerchantMy();

        params.put("pay_md5sign", MD5Util.MD5Encode(signStr, "UTF-8").toUpperCase());

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

            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(data).getAsJsonObject();
            if (!"0".equals(jobj.get("status").getAsString())) {
                return new QueryOrderResult(false, "查单失败:" + jobj.get("trade_state"));
            }

            jobj = jobj.get("data").getAsJsonObject();
            QueryOrderResult qor = new QueryOrderResult();
            String status = jobj.get("trade_state").getAsString();
            if (!"SUCCESS".equals(status)) {
                qor.setStatus(false);
                qor.setMsg("订单未支付");
            } else {
                qor.setStatus(true);
            }

            return qor;
        } catch (Exception e) {
            e.printStackTrace();
            return new QueryOrderResult(false, "查单失败:" + e.getMessage());
        }
    }


}
