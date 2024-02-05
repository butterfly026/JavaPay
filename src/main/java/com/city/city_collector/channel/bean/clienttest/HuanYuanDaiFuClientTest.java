
package com.city.city_collector.channel.bean.clienttest;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayBankCode;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.*;
import com.city.city_collector.channel.util.taihe.Des;
import com.city.city_collector.common.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.filters.StringInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author nb
 * @Description:
 */
public class HuanYuanDaiFuClientTest extends ClientTest {

    public HuanYuanDaiFuClientTest() {
        merchantNo = "2131643";
        merchantMy = "2967C1E53D6D4A20BB7592B9==D026B1C9D74442BABA22A745";

        payUrl = "";
        queryUrl = "";
        cashUrl = "https://Pay.heepay.com/API/PayTransit/PayTransferWithSmallAll.aspx";

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

        ArrayList<PayBankCode> payBankCodes = new ArrayList<>();
        payBankCodes.add(new PayBankCode("4", "中国邮政储蓄银行", "PSBC"));
        payBankCodes.add(new PayBankCode("1", "工商银行", "ICBC"));
        payBankCodes.add(new PayBankCode("3", "农业银行", "ABC"));
        payBankCodes.add(new PayBankCode("5", "中国银行", "BOC"));
        payBankCodes.add(new PayBankCode("2", "建设银行", "CCB"));
        payBankCodes.add(new PayBankCode("6", "交通银行", "COMM"));
        payBankCodes.add(new PayBankCode("12", "中信银行", "CITIC"));
        payBankCodes.add(new PayBankCode("8", "光大银行", "CEB"));
        payBankCodes.add(new PayBankCode("10", "华夏银行", "HXBANK"));
        payBankCodes.add(new PayBankCode("14", "民生银行", "CMBC"));
//        payBankCodes.add(new PayBankCode("19", "广发银行", "GDB"));
//        payBankCodes.add(new PayBankCode("", "浦东发展银行", "SPDB"));
        payBankCodes.add(new PayBankCode("7", "招商银行", "CMB"));
        payBankCodes.add(new PayBankCode("13", "兴业银行", "CIB"));
        payBankCodes.add(new PayBankCode("63", "恒丰银行", "EGBANK"));
        payBankCodes.add(new PayBankCode("32", "浙商银行", "CZBANK"));
        payBankCodes.add(new PayBankCode("23", "渤海银行", "BOHAIB"));
        payBankCodes.add(new PayBankCode("18", "平安银行", "SPABANK"));
//        payBankCodes.add(new PayBankCode("", "企业银行", "DIYEBANK"));
        payBankCodes.add(new PayBankCode("16", "上海银行", "SHBANK"));
//        payBankCodes.add(new PayBankCode("", "厦门银行", "XMBANK"));
        payBankCodes.add(new PayBankCode("33", "北京银行", "BJBANK"));
        payBankCodes.add(new PayBankCode("143", "福建海峡银行", "FJHXBC"));
        payBankCodes.add(new PayBankCode("145", "吉林银行", "JLBANK"));
        payBankCodes.add(new PayBankCode("17", "宁波银行", "NBBANK"));
        payBankCodes.add(new PayBankCode("30", "温州银行", "WZCB"));
        payBankCodes.add(new PayBankCode("22", "广州银行", "GCB"));
        payBankCodes.add(new PayBankCode("41", "汉口银行", "HKB"));
        payBankCodes.add(new PayBankCode("109", "洛阳银行", "LYB"));
//        payBankCodes.add(new PayBankCode("", "大连银行", "DLB"));
        payBankCodes.add(new PayBankCode("93", "河北银行", "BHB"));
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
        payBankCodes.add(new PayBankCode("136", "重庆银行", "CQBANK"));
//        payBankCodes.add(new PayBankCode("", "哈尔滨银行", "HRBANK"));
//        payBankCodes.add(new PayBankCode("", "贵阳银行", "GYCB"));
        payBankCodes.add(new PayBankCode("123", "兰州银行", "LZYH"));
        payBankCodes.add(new PayBankCode("295", "南昌银行", "NCB"));
        payBankCodes.add(new PayBankCode("67", "青岛银行", "QDCCB"));
        payBankCodes.add(new PayBankCode("146", "青海银行", "BOQH"));
        payBankCodes.add(new PayBankCode("40", "台州银行", "TZCB"));
        payBankCodes.add(new PayBankCode("89", "长沙银行", "CSCB"));
        payBankCodes.add(new PayBankCode("121", "赣州银行", "GZB"));
        payBankCodes.add(new PayBankCode("94", "内蒙古银行", "H3CB"));
        payBankCodes.add(new PayBankCode("69", "包商银行", "BSB"));
        payBankCodes.add(new PayBankCode("92", "龙江银行", "DAQINGB"));
//        payBankCodes.add(new PayBankCode("", "上海农商银行", "SHRCB"));
//        payBankCodes.add(new PayBankCode("", "深圳农村商业银行", "SRCB"));
        payBankCodes.add(new PayBankCode("79", "广州农村商业银行", "GZRCU"));
        payBankCodes.add(new PayBankCode("127", "东莞农村商业银行", "DRCBCL"));
//        payBankCodes.add(new PayBankCode("", "北京农村商业银行", "BJRCB"));
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

        String merchantMy = client.getMerchantMy();
        String[] split = merchantMy.split("==");
        String key = split[1];
        String DesKey = split[0];


        String version = "3";               //当前接口版本号
        String agent_id = client.getMerchantNo();    //商户ID
        String batch_no = cash.getSn();    //批量付款订单号
        //上游只支持20位商户号 LNCA2021091123041186b3252
        batch_no = batch_no.replaceFirst("LNCA202", "");

        BigDecimal amount = cash.getClientMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
        String batch_amt = amount.toString();       //付款总金额
        String batch_num = "1";   //该次付款总笔数
        //String detail_data = "A123456^2^0^6217000010100164865^张三^0.01^商户付款^北京^北京市^建设银行";           //批付到银行帐户格式
        StringBuilder detailStr = new StringBuilder();
        detailStr.append(batch_no)
                .append("^")
                .append(payBankCode.getIdStr())
                .append("^0^")
                .append(cash.getBankAccno())
                .append("^")
                .append(cash.getBankAccname())
                .append("^")
                .append(amount.toString())
                .append("^工资代发");
        if (StringUtils.isBlank(cash.getBankProvince())) {
            cash.setBankProvince(payBankCode.getName());
        }
        detailStr.append("^").append(cash.getBankProvince());
        if (StringUtils.isBlank(cash.getBankCity())) {
            cash.setBankCity(payBankCode.getName());
        }
        detailStr.append("^").append(cash.getBankCity());
        if (StringUtils.isBlank(cash.getBankSubname())) {
            cash.setBankSubname(payBankCode.getName());
        }
        detailStr.append("^").append(cash.getBankSubname());


        String detail_data = detailStr.toString();
//        String detail_data = cash.getSn() + "^" + payBankCode.getIdStr() + "^0^" + cash.getBankAccno()
//                + "^" + cash.getBankAccname() + "^" + amount.toString() + "^工资代发^" + cash.getBankProvince() + "^" + cash.getBankCity() + "^" + cash.getBankSubname();
        System.out.println(detail_data);
        String notify_url = "";                //异步通知地址
        if (amount.compareTo(new BigDecimal(2000)) < 0) {
            String notifyurl = ApplicationData.getInstance().getConfig().getNotifyurl();
            notify_url = notifyurl + "/api/pay/cashnotify/" + keyname + "_" + client.getId();
        } else {
            notify_url = "https://google.com";
        }
        String ext_param1 = "123";                //商户自定义
        String sign = "";                   //签名结果

//        String version = "3";               //当前接口版本号
//        String agent_id = "2131643";    //商户ID
//        String batch_no = "20171010101010";    //批量付款订单号
//        String batch_amt = "0.01";       //付款总金额
//        String batch_num = "1";   //该次付款总笔数
//        //String detail_data = "A123456^2^0^6217000010100164865^张三^0.01^商户付款^北京^北京市^建设银行";           //批付到银行帐户格式
//        String detail_data = "A123456^2^0^6217000010100164865^张三^0.01^工资代发^北京^北京市^建设银行";           //批付到银行帐户格式
//        String notify_url = "http://www.hy.com";                //异步通知地址
//        String ext_param1 = "123";                //商户自定义
//        String DesKey = "2967C1E53D6D4A20BB7592B9";
//        String key = "D026B1C9D74442BABA22A745";               //签名密钥
//        String sign = "";                   //签名结果

        //组织签名串
        StringBuilder sign_sb = new StringBuilder();
        sign_sb.append("agent_id").append("=").append(agent_id).append("&")
                .append("batch_amt").append("=").append(batch_amt).append("&")
                .append("batch_no").append("=").append(batch_no).append("&")
                .append("batch_num").append("=").append(batch_num).append("&")
                .append("detail_data").append("=").append(detail_data).append("&")
                .append("ext_param1").append("=").append(ext_param1).append("&")
                .append("key").append("=").append(key).append("&")
                .append("notify_url").append("=").append(notify_url).append("&")
                .append("version").append("=").append(version);
//        System.out.println("签名参数：" + sign_sb.toString().toLowerCase());
        sign = MD5Util.MD5Encode(sign_sb.toString().toLowerCase(), "UTF-8");
//        System.out.println("签名结果：" + sign);
        //3DES加密detail_data
        detail_data = Des.Encrypt3Des(detail_data, DesKey, "ToHex16");
        //请求参数
        StringBuilder requestParams = new StringBuilder();
        requestParams.append("version").append("=").append(version).append("&")
                .append("agent_id").append("=").append(agent_id).append("&")
                .append("batch_no").append("=").append(batch_no).append("&")
                .append("batch_amt").append("=").append(batch_amt).append("&")
                .append("batch_num").append("=").append(batch_num).append("&")
                .append("detail_data").append("=").append(detail_data).append("&")
                .append("notify_url").append("=").append(notify_url).append("&")
                .append("ext_param1").append("=").append(ext_param1).append("&")
                .append("sign").append("=").append(sign);
//        System.out.println("请求参数：" + requestParams.toString());

//        params.put("customBody", requestParams.toString());
        params.put("version", version);
        params.put("agent_id", agent_id);
        params.put("batch_no", batch_no);
        params.put("batch_amt", batch_amt);
        params.put("batch_num", batch_num);
        params.put("detail_data", detail_data);
        params.put("notify_url", notify_url);
        params.put("ext_param1", ext_param1);
        params.put("sign", sign);

        return params;
    }

    @Override
    public CashInfo dealOrderData(String data, String sn, Map<String, Object> reqParams) {
        try {
            if (StringUtils.isBlank(data)) {
                return new CashInfo(false, "发起代付失败!");
            }


            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document parse = documentBuilder.parse(new StringInputStream(data));

            NodeList ret_code = parse.getElementsByTagName("ret_code");
            String textContent = ret_code.item(0).getTextContent();
//            System.out.println(textContent);

            NodeList ret_msg = parse.getElementsByTagName("ret_msg");
            String textContent1 = ret_msg.item(0).getTextContent();
//            System.out.println(textContent1);
            //<?xml version="1.0" encoding="utf-8"?><root><ret_code>0000</ret_code>
            // <ret_msg>创建单据成功</ret_msg><agent_id>2131643</agent_id>
            // <sign>5a2b2ed4bfc0eea9e5c86c19f46e5717</sign></root>
            //                    0000

            if (!textContent.equals("0000")) {
                return new CashInfo(false, "发起代付失败:" + textContent1);
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
//            成功响应ok失败响应error
            System.out.println("notify hua yuan start");

            //接收异步通知参数
            String ret_code = params.get("ret_code").toString();//返回码值0000 表示查询成功
            String ret_msg = params.get("ret_msg").toString();//返回码信息提示
            String agent_id = params.get("agent_id").toString();//商户ID
            String hy_bill_no = params.get("hy_bill_no").toString();//汇付宝订单号
            String status = params.get("status").toString();//-1=无效，0=未处理，1=成功
            String batch_no = params.get("batch_no").toString();//商户系统订单号
            String batch_amt = params.get("batch_amt").toString();//成功付款金额
            String batch_num = params.get("batch_num").toString();//成功付款数量
            String detail_data = params.get("detail_data").toString();//付款明细
            String ext_param1 = params.get("ext_param1").toString();//商户自定义参数，透传参数
            String sign = params.get("sign").toString();//签名

//            String[] split = merchantMy.split("==");
//            String key = split[0];
//            String DesKey = split[1];
//            String key = "D77686B728A6470E9E4E3F9A";
//            String DesKey = "588AA8D960FA4C4D8558BD72";
            //解密detail_data
//            detail_data = Des.Decrypt3Des(detail_data, DesKey, "ToHex16");
            //验签
//            String b_sign = "ret_code=" + ret_code +
//                    "&ret_msg=" + ret_msg +
//                    "&agent_id=" + agent_id +
//                    "&hy_bill_no=" + hy_bill_no +
//                    "&status=" + status +
//                    "&batch_no=" + batch_no +
//                    "&batch_amt=" + batch_amt +
//                    "&batch_num=" + batch_num +
//                    "&detail_data=" + detail_data +
//                    "&ext_param1=" + ext_param1 +
//                    "&key=" + key;

//            String signStr = MD5Util.MD5Encode(b_sign, "UTF-8").toLowerCase();
//            if (signStr.equals(sign)) {

            System.out.println(detail_data);
            NotifyInfo notifyInfo = new NotifyInfo();
            if ("0000".equalsIgnoreCase(ret_code) && StringUtils.isNotBlank(detail_data) && detail_data.endsWith("^S^")) {
                notifyInfo.setStatus("success");

                //上游只支付20位订单号。 LNCA2021091123041186b3252 前面加 LNCA202
                batch_no = "LNCA202" + batch_no;

                notifyInfo.setSn(batch_no);
                notifyInfo.setClientSn(hy_bill_no);
            } else {
                notifyInfo.setStatus("fail");
            }

            return notifyInfo;

//            }
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
