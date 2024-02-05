
package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayBankCode;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.*;
import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.text.SimpleDateFormat;



/**
 * @author nb
 * @Description:
 */
public class VipDaiFuClientTest extends ClientTest {

    public VipDaiFuClientTest() {
        merchantNo = "C202112130191";
        merchantMy = "4cd9f57a2c6948d9abed866f17381952";

        payUrl = "";
        queryUrl = "";
        cashUrl = "https://2pfront.xxqbfoahaa.com/pfront/pay/process";
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
            if (StringUtils.isBlank(body)) {
                return null;
            }
            Gson gson = new Gson();
            JsonElement data1 = gson.fromJson(body, JsonElement.class);

            String jsonString = data1.getAsJsonPrimitive().getAsString();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);

            NotifyInfo notifyInfo = new NotifyInfo();
            if (!"100".equals(jobj.get("status").getAsString())) {
                notifyInfo.setStatus("fail");
            } else {
                notifyInfo.setStatus("success");
            }
            String sn = jobj.get("cusOrderNo").getAsString();
            notifyInfo.setSn(sn);
            return notifyInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ApiInfo createReqParams_Test(Long id, String merchantNo, String merchantMy,
                                        Map<String, Object> channelParams, String sn, BigDecimal amount, String domain) throws Exception {

        return null;
    }

    @Override
    public Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash,
                                                    String keyname) {
        ArrayList<PayBankCode> payBankCodes = new ArrayList<>();
        payBankCodes.add(new PayBankCode("403100000004",     "中国邮政储蓄银行",           "PSBC"));
        payBankCodes.add(new PayBankCode("102100099996",     "中国工商银行",               "ICBC"));
        payBankCodes.add(new PayBankCode("103100000026",     "中国农业银行",               "ABC"));
        payBankCodes.add(new PayBankCode("104100000004",     "中国银行",                   "BOC"));
        payBankCodes.add(new PayBankCode("105100000017",     "中国建设银行",               "CCB"));
        payBankCodes.add(new PayBankCode("301290000007",     "交通银行",                   "COMM"));
        payBankCodes.add(new PayBankCode("302100011000",     "中信银行",                   "CITIC"));
        payBankCodes.add(new PayBankCode("303100000006",     "中国光大银行",               "CEB"));
        payBankCodes.add(new PayBankCode("304100040000",     "华夏银行",                   "HXBANK"));
        payBankCodes.add(new PayBankCode("305100000013",     "中国民生银行",               "CMBC"));
        payBankCodes.add(new PayBankCode("310290000013",     "上海浦东发展银行",           "SPDB"));
        payBankCodes.add(new PayBankCode("308584000013",     "招商银行",                   "CMB"));
        payBankCodes.add(new PayBankCode("309391000011",     "兴业银行",                   "CIB"));
        payBankCodes.add(new PayBankCode("315456000105",     "恒丰银行",                   "EGBANK"));
        payBankCodes.add(new PayBankCode("316331000018",     "浙商银行",                   "CZBANK"));
        payBankCodes.add(new PayBankCode("318110000014",     "渤海银行",                   "BOHAIB"));
        payBankCodes.add(new PayBankCode("307584007998",     "平安银行（原深圳发展银行）", "SPABANK"));
        payBankCodes.add(new PayBankCode("596110000013",     "企业银行",                   "DIYEBANK"));
        payBankCodes.add(new PayBankCode("325290000012",     "上海银行",                   "SHBANK"));
        payBankCodes.add(new PayBankCode("313393080005",     "厦门银行",                   "XMBANK"));
        payBankCodes.add(new PayBankCode("313100000013",     "北京银行",                   "BJBANK"));
        payBankCodes.add(new PayBankCode("313391080007",     "福建海峡银行",               "FJHXBC"));
        payBankCodes.add(new PayBankCode("313241066661",     "吉林银行",                   "JLBANK"));
        payBankCodes.add(new PayBankCode("313332082914",     "宁波银行",                   "NBBANK"));
        payBankCodes.add(new PayBankCode("313333007331",     "温州银行",                   "WZCB"));
        payBankCodes.add(new PayBankCode("313581003284",     "广州银行",                   "GCB"));
        payBankCodes.add(new PayBankCode("313521000011",     "汉口银行",                   "HKB"));
        payBankCodes.add(new PayBankCode("313493080539",      "洛阳银行",                   "LYB"));
        payBankCodes.add(new PayBankCode("313222080002",     "大连银行",                   "DLB"));
        payBankCodes.add(new PayBankCode("313121006888",     "河北银行股份有限公司",       "BHB"));
        payBankCodes.add(new PayBankCode("313301008887",     "南京银行",                   "NJCB"));
        payBankCodes.add(new PayBankCode("313881000002",     "乌鲁木齐市商业银行",         "URMQCCB"));
        payBankCodes.add(new PayBankCode("313337009004",     "绍兴银行",                   "SXCB"));
        payBankCodes.add(new PayBankCode("313491000232",     "郑州银行",                   "ZZBANK"));
        payBankCodes.add(new PayBankCode("313871000007",     "宁夏银行",                   "NXBANK"));
        payBankCodes.add(new PayBankCode("313453001017",     "齐商银行",                   "QSBANK"));
        payBankCodes.add(new PayBankCode("313227000012",     "锦州银行",                   "BOJZ"));
        payBankCodes.add(new PayBankCode("319361000013",     "徽商银行",                   "HSBANK"));
        payBankCodes.add(new PayBankCode("313653000013",     "重庆银行股份有限公司",       "CQBANK"));
        payBankCodes.add(new PayBankCode("313261000018",     "哈尔滨银行结算中心",         "HRBANK"));
        payBankCodes.add(new PayBankCode("313701098010",     "贵阳银行",                   "GYCB"));
        payBankCodes.add(new PayBankCode("313821001016",     "兰州银行股份有限公司",       "LZYH"));
        payBankCodes.add(new PayBankCode("313421087506",     "南昌银行",                   "NCB"));
        payBankCodes.add(new PayBankCode("313452060150",     "青岛银行",                   "QDCCB"));
        payBankCodes.add(new PayBankCode("313851000018",     "青海银行",                   "BOQH"));
        payBankCodes.add(new PayBankCode("313345001665",     "台州银行",                   "TZCB"));
        payBankCodes.add(new PayBankCode("313551088886",     "长沙银行",                   "CSCB"));
        payBankCodes.add(new PayBankCode("313428076517",     "赣州银行",                   "GZB"));
        payBankCodes.add(new PayBankCode("313191000011",     "内蒙古银行",                 "H3CB"));
        payBankCodes.add(new PayBankCode("313192000013",     "包商银行股份有限公司",       "BSB"));
        payBankCodes.add(new PayBankCode("313261099913",     "龙江银行",                   "DAQINGB"));
        payBankCodes.add(new PayBankCode("322290000011",     "上海农商银行",               "SHRCB"));
        payBankCodes.add(new PayBankCode("314581000011",     "广州农村商业银行",           "GZRCU"));
        payBankCodes.add(new PayBankCode("402602000018",     "东莞农村商业银行",           "DRCBCL"));
        payBankCodes.add(new PayBankCode("402100000018",     "北京农村商业银行",           "BJRCB"));
        payBankCodes.add(new PayBankCode("402301099998",     "江苏省农村信用社联合社",     "JSRCU"));
        payBankCodes.add(new PayBankCode("313345010019",     "浙江泰隆商业银行",           "ZJQL"));

        String bankCode = cash.getBankCode();
        PayBankCode payBankCode = null;
        for (int i = 0; i < payBankCodes.size(); i++) {
            PayBankCode item = payBankCodes.get(i);
            if (bankCode.equals(item.getCode())) {
                payBankCode = item;
                break;
            }
        }

        Map<String, Object> request = new TreeMap<String, Object>();
        if (payBankCode == null) {
            request.put("dferror", "银行不支持");
            return null;
        }

        request.put("customerNo", client.getMerchantNo());
        request.put("cusOrderNo", cash.getSn());
        request.put("modeCode", "2800000011");
        request.put("payAmt", cash.getClientMoney().multiply(new BigDecimal(100)));
        request.put("recAcctType", 1);


        request.put("recAcctNo", cash.getBankAccno().trim());
        request.put("recAcctName", cash.getBankAccname().trim());

        request.put("recBankCode", payBankCode.getIdStr());
        request.put("recBankName", payBankCode.getName());

        //String notifyurl = ApplicationData.getInstance().getConfig().getNotifyurl();
        String notifyurl = "http://18.162.213.217:8091/api/pay/cashnotify/vip_620";
        request.put("notifyUrl", notifyurl);
        request.put("userIp", "127.0.0.1");
        request.put("userNo", cash.getBankAccname().trim());
        request.put("userName", "cat");
        request.put("level", 1);

        Iterator<Entry<String, Object>> it = request.entrySet().iterator();
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
        String signStr = sbuf.toString().substring(0, sbuf.length() - 1);
        signStr = signStr + client.getMerchantMy();
        System.out.println("signstr-" + signStr);
        String sign = MD5Util.MD5Encode(signStr, "UTF-8");

        Map<String, Object> header = new TreeMap<String, Object>();
        header.put("trxCode", "P20045");
        header.put("version", "01");
        header.put("customerNo", client.getMerchantNo());
        header.put("reqSn", "C202112130191" + cash.getSn());
        Date d = new Date();
        SimpleDateFormat sbf = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(sbf.format(d));
        header.put("timestamp",  sbf.format(d));
        header.put("signedMsg",  sign);

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("head", header);
        params.put("request", request);

        Map<String, Object> heads = new TreeMap<String, Object>();
        heads.put("customerNo", client.getMerchantNo());
        heads.put("Content-Type", "text/xml; charset=utf-8;");
        params.put("headers", heads);
        return params;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        try {
            if (StringUtils.isBlank(data)) {
                return new CashInfo(false, "发起代付失败!");
            }
            System.out.println("vipproxy-" + data);
            Gson gson = new Gson();
            JsonElement data1 = gson.fromJson(data, JsonElement.class);

            String jsonString = data1.getAsJsonPrimitive().getAsString();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);


            if (!"000000".equals(jobj.get("head").getAsJsonObject().get("retCode").getAsString())) {
                return new CashInfo(false, "发起代付失败:" + jobj.get("head").getAsJsonObject().get("retMsg").getAsString());
            }

//            if (!"000000".equals(jobj.get("response").getAsJsonObject().get("returnCode").getAsString())) {
//                return new CashInfo(false, "发起代付失败:" + jobj.get("head").getAsJsonObject().get("status").getAsString());
//            }

            if (!"处理成功".equals(jobj.get("head").getAsJsonObject().get("retMsg").getAsString())) {
                return new CashInfo(false, "发起代付失败:" + jobj.get("head").getAsJsonObject().get("status").getAsString());
            }

            CashInfo ci = new CashInfo(true, "发起代付成功:" + jobj.get("response").getAsJsonObject().get("cusOrderNo").getAsString() + jobj.get("response").getAsJsonObject().get("status").getAsString());

            ci.setSn(jobj.get("response").getAsJsonObject().get("orderNo").getAsString());
            return ci;
        } catch (Exception e) {
            e.printStackTrace();
            return new CashInfo(false, "发起代付失败:" + e.getMessage());
        }
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
