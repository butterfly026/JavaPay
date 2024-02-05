
package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.city.util.ApplicationData;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * @author nb
 * @Description:
 */
public class XiaoQiangDaiFuClientTest extends ClientTest {

    public XiaoQiangDaiFuClientTest() {
        merchantNo = "";
        merchantMy = "";

        payUrl = "";
        queryUrl = "";
        cashUrl = "http://t.hqb72168.com:93/cashout";

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
     * @see com.city.city_collector.admin.pay.controller.PayCashController#merchantClientCashSubmit(java.lang.Long, java.lang.Integer, java.lang.Long, java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal, java.lang.Long, java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {

        ArrayList<PayBankCode> payBankCodes = new ArrayList<>();
        payBankCodes.add(new PayBankCode("4", "中国邮政储蓄银行", "PSBC"));
        payBankCodes.add(new PayBankCode("1", "中国工商银行", "ICBC"));
        payBankCodes.add(new PayBankCode("3", "中国农业银行", "ABC"));
        payBankCodes.add(new PayBankCode("5", "中国银行", "BOC"));
        payBankCodes.add(new PayBankCode("2", "中国建设银行", "CCB"));
        payBankCodes.add(new PayBankCode("6", "交通银行", "COMM"));
        payBankCodes.add(new PayBankCode("12", "中信银行", "CITIC"));
        payBankCodes.add(new PayBankCode("8", "中国光大银行", "CEB"));
        payBankCodes.add(new PayBankCode("10", "华夏银行", "HXBANK"));
        payBankCodes.add(new PayBankCode("14", "中国民生银行", "CMBC"));
//        payBankCodes.add(new PayBankCode("19", "广发银行", "GDB"));
        payBankCodes.add(new PayBankCode("", "上海浦东发展银行", "SPDB"));
        payBankCodes.add(new PayBankCode("7", "招商银行", "CMB"));
        payBankCodes.add(new PayBankCode("13", "兴业银行", "CIB"));
        payBankCodes.add(new PayBankCode("63", "恒丰银行", "EGBANK"));
        payBankCodes.add(new PayBankCode("32", "浙商银行", "CZBANK"));
        payBankCodes.add(new PayBankCode("23", "渤海银行", "BOHAIB"));
        payBankCodes.add(new PayBankCode("18", "平安银行（原深圳发展银行）", "SPABANK"));
        payBankCodes.add(new PayBankCode("", "企业银行", "DIYEBANK"));
        payBankCodes.add(new PayBankCode("16", "上海银行", "SHBANK"));
        payBankCodes.add(new PayBankCode("", "厦门银行", "XMBANK"));
        payBankCodes.add(new PayBankCode("33", "北京银行", "BJBANK"));
        payBankCodes.add(new PayBankCode("143", "福建海峡银行", "FJHXBC"));
        payBankCodes.add(new PayBankCode("145", "吉林银行", "JLBANK"));
        payBankCodes.add(new PayBankCode("17", "宁波银行", "NBBANK"));
        payBankCodes.add(new PayBankCode("30", "温州银行", "WZCB"));
        payBankCodes.add(new PayBankCode("22", "广州银行", "GCB"));
        payBankCodes.add(new PayBankCode("41", "汉口银行", "HKB"));
        payBankCodes.add(new PayBankCode("109", "洛阳银行", "LYB"));
        payBankCodes.add(new PayBankCode("", "大连银行", "DLB"));
        payBankCodes.add(new PayBankCode("93", "河北银行股份有限公司", "BHB"));
//        payBankCodes.add(new PayBankCode("", "杭州商业银行", "HZCB"));
        payBankCodes.add(new PayBankCode("21", "南京银行", "NJCB"));
        payBankCodes.add(new PayBankCode("128", "乌鲁木齐市商业银行", "URMQCCB"));
        payBankCodes.add(new PayBankCode("65", "绍兴银行", "SXCB"));
//        payBankCodes.add(new PayBankCode("", "葫芦岛市商业银行", "HLDCCB"));
        payBankCodes.add(new PayBankCode("43", "郑州银行", "ZZBANK"));
        payBankCodes.add(new PayBankCode("137", "宁夏银行", "NXBANK"));
        payBankCodes.add(new PayBankCode("51", "齐商银行", "QSBANK"));
        payBankCodes.add(new PayBankCode("124", "锦州银行", "BOJZ"));
        payBankCodes.add(new PayBankCode("25", "徽商银行", "HSBANK"));
        payBankCodes.add(new PayBankCode("136", "重庆银行股份有限公司", "CQBANK"));
        payBankCodes.add(new PayBankCode("", "哈尔滨银行结算中心", "HRBANK"));
        payBankCodes.add(new PayBankCode("", "贵阳银行", "GYCB"));
        payBankCodes.add(new PayBankCode("123", "兰州银行股份有限公司", "LZYH"));
        payBankCodes.add(new PayBankCode("295", "南昌银行", "NCB"));
        payBankCodes.add(new PayBankCode("67", "青岛银行", "QDCCB"));
        payBankCodes.add(new PayBankCode("146", "青海银行", "BOQH"));
        payBankCodes.add(new PayBankCode("40", "台州银行", "TZCB"));
        payBankCodes.add(new PayBankCode("89", "长沙银行", "CSCB"));
        payBankCodes.add(new PayBankCode("121", "赣州银行", "GZB"));
        payBankCodes.add(new PayBankCode("94", "内蒙古银行", "H3CB"));
        payBankCodes.add(new PayBankCode("69", "包商银行股份有限公司", "BSB"));
        payBankCodes.add(new PayBankCode("92", "龙江银行", "DAQINGB"));
        payBankCodes.add(new PayBankCode("", "上海农商银行", "SHRCB"));
//        payBankCodes.add(new PayBankCode("", "深圳农村商业银行", "SRCB"));
        payBankCodes.add(new PayBankCode("79", "广州农村商业银行", "GZRCU"));
        payBankCodes.add(new PayBankCode("127", "东莞农村商业银行", "DRCBCL"));
        payBankCodes.add(new PayBankCode("", "北京农村商业银行", "BJRCB"));
//        payBankCodes.add(new PayBankCode("", "天津农村商业银行", "TRCB"));
        payBankCodes.add(new PayBankCode("104", "江苏省农村信用社联合社", "JSRCU"));
        payBankCodes.add(new PayBankCode("38", "浙江泰隆商业银行", "ZJQL"));
//        payBankCodes.add(new PayBankCode("", "USDT", "USDT"));

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


        params.put("bank_card", cash.getBankAccno().trim());
        params.put("bank_name", cash.getBankAccname().trim());
        params.put("bank_opening", cash.getBankSubname() == null || cash.getBankSubname().length() == 0 ? "xxx支行" : cash.getBankSubname().trim());
        params.put("bank_type_name", payBankCode.getName());
        params.put("client_ip", "127.0.0.1");
        params.put("code", "xin");
        params.put("goods_desc", "goodshoping");
        params.put("mch_id", client.getMerchantNo());

        BigDecimal amount = cash.getClientMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
        params.put("money", amount);

        //小于2000元自动回调
        if (amount.compareTo(new BigDecimal(2000)) < 0) {
            String notifyurl = ApplicationData.getInstance().getConfig().getNotifyurl();
            params.put("notify_url", notifyurl + "/api/pay/cashnotify/" + keyname + "_" + client.getId());
        } else {
            params.put("notify_url", "https://google.com");
        }

        params.put("order_sn", cash.getSn());
        params.put("time", System.currentTimeMillis() + "");

        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        StringBuffer sbuf = new StringBuffer("");
        while (it.hasNext()) {
            Entry<String, Object> et = it.next();
            if (et.getValue() != null && StringUtils.isNotBlank(String.valueOf(et.getValue()))) {
                sbuf.append(et.getKey());
                sbuf.append("=");
                sbuf.append(String.valueOf(et.getValue()));
                sbuf.append("&");
            }
        }
        sbuf.append("key=");
        sbuf.append(client.getMerchantMy());
//        System.out.println(sbuf.toString());
        String sign = MD5Util.MD5Encode(sbuf.toString(), "UTF-8").toLowerCase();
        params.put("sign", sign);
//        System.out.println(sign);

        return params;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        try {
            if (StringUtils.isBlank(data)) {
                return new CashInfo(false, "发起代付失败!");
            }
            JsonParser jp = new JsonParser();

            JsonObject jobj = jp.parse(data).getAsJsonObject();
            if (!"1".equals(jobj.get("code").getAsString())) {
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
            if (params.get("state").toString().equalsIgnoreCase("4")) {
                notifyInfo.setStatus("success");
//                notifyInfo.setActualAmount(new BigDecimal(params.get("actual_price").toString()));
//                notifyInfo.setAmount(new BigDecimal(params.get("money").toString()));
            } else {
                notifyInfo.setStatus("fail");
            }

            notifyInfo.setSn(params.get("sh_order").toString());
            notifyInfo.setClientSn(params.get("pt_order").toString());
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
