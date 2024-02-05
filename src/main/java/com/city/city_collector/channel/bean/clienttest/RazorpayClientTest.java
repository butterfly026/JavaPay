
package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;

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
import com.google.gson.Gson;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

/**
 * @author nb
 * @Description:
 */
public class RazorpayClientTest extends ClientTest {

    public RazorpayClientTest() {
//        merchantNo="rzp_live_BMqWGz7SwCV5Eo";
//        merchantMy="R6ZmtBdprS6Yq9BWVjgzeq7F";

        merchantNo = "";
        merchantMy = "";

        payUrl = "";
        cashUrl = "";
        channelParams = new HashMap<String, Object>();
//       channelParams.put("ip", "127.0.0.1");
//       channelParams.put("payType", "BPP");
    }

//    public static void main(String[] args) throws RazorpayException {
//        String sn="SN1000000012";
//        String merchantNo="rzp_live_BMqWGz7SwCV5Eo";
//        String merchantKey="R6ZmtBdprS6Yq9BWVjgzeq7F";
//
//        int money=8000;
//        //初始化razorpay客户端
//        RazorpayClient razorpayClient = new RazorpayClient(merchantNo, merchantKey);
//        Map<String, String> headers = new HashMap<String, String>();
//        razorpayClient.addHeaders(headers);
//
//
//        JSONObject options = new JSONObject();
//        options.put("amount", money);
//        options.put("currency", "INR");
//        //创建订单
//        options.put("receipt", sn);
//        Order order = razorpayClient.Orders.create(options);
//
//        System.out.println(order.get("status"));
//        System.out.println(order.get("amount_due"));
//        System.out.println(order.get("id"));
//
//    }

    @Override
    public OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain, Integer platform) throws Exception {
        BigDecimal h = new BigDecimal(100);

        RazorpayClient razorpayClient = new RazorpayClient(merchantNo, merchantMy);
        Map<String, String> headers = new HashMap<String, String>();
        razorpayClient.addHeaders(headers);


        JSONObject options = new JSONObject();
        options.put("amount", amount.multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP));
        options.put("currency", "INR");
        //创建订单
        options.put("receipt", sn);
        Order order = razorpayClient.Orders.create(options);

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("payUrl", domain + "/api/pay/rzpay?sn=" + sn);
        params.put("data", "");
        params.put("sn", order.get("id"));

        OrderPreInfo opi = new OrderPreInfo();
        opi.setCharset("UTF-8");
        opi.setParams(params);
//        opi.setReqType(0);

        opi.setIsOrderInfo(1);
        opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);

        return opi;
    }

    @Override
    public OrderInfo dealOrderData(String data, Long clientId, String clientNo, String clientName,
                                   Long clientChannelId, String url, String sn, Map<String, Object> channelParams) {
        return null;
    }

    @Override
    public NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy,
                                 Map<String, Object> channelParams, String body) {
        //
        return null;
    }

    @Override
    public ApiInfo createReqParams_Test(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain) throws Exception {
        BigDecimal h = new BigDecimal(100);

        RazorpayClient razorpayClient = new RazorpayClient(merchantNo, merchantMy);
        Map<String, String> headers = new HashMap<String, String>();
        razorpayClient.addHeaders(headers);


        JSONObject options = new JSONObject();
        options.put("amount", amount.multiply(h).setScale(0, BigDecimal.ROUND_HALF_UP));
        options.put("currency", "INR");
        //创建订单
        options.put("receipt", sn);
        Order order = razorpayClient.Orders.create(options);

//        System.out.println(order.get("status"));
//        System.out.println(order.get("id"));

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("payUrl", domain + "/api/pay/rzpay?sn=" + sn);
        params.put("data", "");
        params.put("sn", order.get("id"));

        Gson gson = new Gson();
        ApiInfo apiInfo = new ApiInfo();

        apiInfo.setSignData("");

        apiInfo.setSignStr("");

        apiInfo.setSign("");

        params.put("sign", "");

        apiInfo.setRequstData(gson.toJson(params));
        apiInfo.setDatas(params);
        apiInfo.setGotype(1);
        apiInfo.setPayUrl(domain + "/api/pay/rzpay?sn=" + sn);
        apiInfo.setSavePayOrder(1);
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
        QueryOrderInfo qoi = new QueryOrderInfo();
        qoi.setNeedRequest(false);
        qoi.setOrderSuccess(false);
        try {
            RazorpayClient razorpayClient = new RazorpayClient(client.getMerchantNo(), client.getMerchantMy());
            Map<String, String> headers = new HashMap<String, String>();
            razorpayClient.addHeaders(headers);
            Order od = razorpayClient.Orders.fetch(order.getClientSn());
            String status = od.get("status");

            if ("paid".equals(status)) {
                qoi.setOrderSuccess(true);
            }
        } catch (RazorpayException e) {

            e.printStackTrace();
        }
        return qoi;
    }

    @Override
    public QueryOrderResult dealQueryResult(String data, PayOrder order, PayClientChannel clientChannel, SysUser client,
                                            Map<String, Object> channelParams) {

        return null;
    }
}
