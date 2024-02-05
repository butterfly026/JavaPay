
package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.pay.entity.PayBankCode;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * @author nb
 * @Description:
 */
public class GongFuDaiFuClientTest extends ClientTest {

    public GongFuDaiFuClientTest() {
        merchantNo = "5110187";
        merchantMy = "9fe740b80e7c6636b77db81d06ea3d0e";

        payUrl = "";
        queryUrl = "";
        cashUrl = "https://jy.kofoo.cc/v1/api/transfer/5110187";

//        206.119.88.246

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
        //银行编码整理
        ArrayList<PayBankCode> payBankCodes = new ArrayList<>();
        payBankCodes.add(new PayBankCode(1L, "中国邮政储蓄银行", "PSBC"));
        payBankCodes.add(new PayBankCode(2L, "中国工商银行", "ICBC"));
        payBankCodes.add(new PayBankCode(3L, "中国农业银行", "ABC"));
        payBankCodes.add(new PayBankCode(4L, "中国银行", "BOC"));
        payBankCodes.add(new PayBankCode(5L, "中国建设银行", "CCB"));
        payBankCodes.add(new PayBankCode(7L, "交通银行", "COMM"));
        payBankCodes.add(new PayBankCode(8L, "中信银行", "CITIC"));
        payBankCodes.add(new PayBankCode(9L, "中国光大银行", "CEB"));
        payBankCodes.add(new PayBankCode(10L, "华夏银行", "HXBANK"));
        payBankCodes.add(new PayBankCode(11L, "中国民生银行", "CMBC"));
        payBankCodes.add(new PayBankCode(12L, "广发银行", "GDB"));
        payBankCodes.add(new PayBankCode(13L, "平安银行", "SPABANK"));
        payBankCodes.add(new PayBankCode(14L, "招商银行", "CMB"));
        payBankCodes.add(new PayBankCode(15L, "兴业银行", "CIB"));
        payBankCodes.add(new PayBankCode(16L, "上海浦东发展银行", "SPDB"));
        payBankCodes.add(new PayBankCode(17L, "恒丰银行", "EGBANK"));
        payBankCodes.add(new PayBankCode(18L, "浙商银行", "CZBANK"));
        payBankCodes.add(new PayBankCode(19L, "渤海银行", "BOHAIB"));
//        payBankCodes.add(new PayBankCode(21L,"东亚银行",""));
        payBankCodes.add(new PayBankCode(48L, "上海银行", "SHBANK"));
        payBankCodes.add(new PayBankCode(50L, "北京银行", "BJBANK"));
        payBankCodes.add(new PayBankCode(56L, "宁波银行", "NBBANK"));
        payBankCodes.add(new PayBankCode(61L, "汉口银行", "HKB"));
//        payBankCodes.add(new PayBankCode(69L,"杭州银行",""));
        payBankCodes.add(new PayBankCode(70L, "南京银行", "NJCB"));
        payBankCodes.add(new PayBankCode(88L, "徽商银行", "HSBANK"));
        payBankCodes.add(new PayBankCode(109L, "长沙银行", "CSCB"));
//        payBankCodes.add(new PayBankCode(143L,"晋城银行",""));
//        payBankCodes.add(new PayBankCode(155L,"浙江稠州商业银行",""));
        payBankCodes.add(new PayBankCode(213L, "上海农商银行", "SHRCB"));
//        payBankCodes.add(new PayBankCode(219L,"顺德农村商业银行",""));
        payBankCodes.add(new PayBankCode(232L, "北京农村商业银行", "BJRCB"));
//        payBankCodes.add(new PayBankCode(1358L,"中国银联",""));

        String bankCode = cash.getBankCode();
        PayBankCode payBankCode = null;
        for (int i = 0; i < payBankCodes.size(); i++) {
            PayBankCode item = payBankCodes.get(i);
            if (bankCode.equals(item.getCode())) {
                payBankCode = item;
                break;
            }
        }

        Map<String, Object> params = new TreeMap<String, Object>();
        if (payBankCode == null) {
            params.put("dferror", "银行不支持");
            return params;
        }

        params.put("merchant_no", client.getMerchantNo());

        BigDecimal amount = cash.getClientMoney().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
        params.put("amount", amount);
        params.put("order_no", cash.getSn());

        params.put("bank_id", payBankCode.getId());
        params.put("payee_name", cash.getBankAccname().trim());

        params.put("bank_name", payBankCode.getName().trim());
        params.put("bank_account", cash.getBankAccno().trim());
        params.put("bank_branch_name", cash.getBankSubname() == null || cash.getBankSubname().length() == 0 ? "xxx支行" : cash.getBankSubname().trim());

        params.put("sign_type", "MD5");
        params.put("sign_ts", (System.currentTimeMillis() / 1000 + "").substring(0, 10));

        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                sbuf.append(et.getKey());
                sbuf.append("=");
                sbuf.append(URLEncoder.encode(et.getValue().toString()));
                sbuf.append("&");
            }
        }
        sbuf.deleteCharAt(sbuf.length() - 1);
        sbuf.append(client.getMerchantMy());
        System.out.println(sbuf.toString());
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8").toLowerCase();
        params.put("sign", sign);
        System.out.println(sign);

        return params;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        try {
//{"http_status":"BAD_REQUEST","err_code":4000001,"error_msg":"CA20210130165035135c02 订单已存在",
//"detail_msg":"","request_uri":"/v1/api/transfer/5110187","http_code":400}

            if (StringUtils.isBlank(data)) {
                return new CashInfo(false, "发起代付失败!");
            }
            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(data).getAsJsonObject();
            if (!"WAITING".equals(jobj.get("state").getAsString())) {
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
            if ("SUCCESSFUL".equalsIgnoreCase(params.get("state").toString())) {
                notifyInfo.setStatus("success");
                notifyInfo.setSn(params.get("order_no").toString());
                notifyInfo.setClientSn(params.get("id").toString());
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
