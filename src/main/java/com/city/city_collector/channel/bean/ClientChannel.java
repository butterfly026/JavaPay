
package com.city.city_collector.channel.bean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.paylog.PayLogManager;
import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author nb
 * @Description:
 */
public class ClientChannel implements Cloneable, Comparable<ClientChannel>{

//    public static String asyncUrl = "/api/pay/notify";
//
//    public static String returnUrl = "/api/pay/notify";
//
//    public static MediaType mediaType = MediaType.parse("application/json");

    /**
     * id
     */
    private Long id;
    /**
     * 编号
     */
    private String no;
    /**
     * 名称
     */
    private String name;
    /**
     * 开启时间
     */
    private String startTime;
    /**
     * 关闭时间
     */
    private String endTime;
    /**
     * 最小时间
     */
    private Long minTime;
    /**
     * 最大时间
     */
    private Long maxTime;

    /**
     * 模式：0,最大最小金额。1:固定金额
     */
    private Integer type;
    /**
     * 最小金额
     */
    private BigDecimal minMoney;
    /**
     * 最大金额
     */
    private BigDecimal maxMoney;
    /**
     * 固定金额列表
     */
    private List<BigDecimal> moneyList;
    /**
     * 支付类型
     */
    private Long payType;

    /**
     * 客户
     */
    private Client client;
    /**
     * 跳转方式
     */
    private Integer gtype;

    /**
     * 上游商户秘钥
     */
    private String merchantMy;

    /**
     * 上游商户号
     */
    private String merchantNo;

    /**
     * 固定参数
     */
    private Map<String, Object> params = new HashMap<String, Object>();

    /**
     * 下单地址
     */
    private String urlpay;

    /**
     * 商户回调IP
     */
    private String merchatnIp;

    /**
     * 键名
     */
    private String keyname;

    /**
     * 优先级
     */
    private Integer priority;


    private Integer retryNumber;

    public Integer getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(Integer retryNumber) {
        this.retryNumber = retryNumber;
    }


    /**
     * 优先处理平台. 1. ios 2. android 3. other
     * 默认值 0
     */
    private Integer primaryPlatform;

    public Integer getPrimaryPlatform() {
        return primaryPlatform;
    }

    public void setPrimaryPlatform(Integer primaryPlatform) {
        this.primaryPlatform = primaryPlatform;
    }

    /**
     * priority
     *
     * @return priority
     */
    public Integer getPriority() {
        return priority == null ? 0 : priority;
    }

    /**
     * 设置 priority
     *
     * @param priority priority
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * keyname
     *
     * @return keyname
     */
    public String getKeyname() {
        return keyname;
    }

    /**
     * 设置 keyname
     *
     * @param keyname keyname
     */
    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * id
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * merchatnIp
     *
     * @return merchatnIp
     */
    public String getMerchatnIp() {
        return merchatnIp;
    }

    /**
     * 设置 merchatnIp
     *
     * @param merchatnIp merchatnIp
     */
    public void setMerchatnIp(String merchatnIp) {
        this.merchatnIp = merchatnIp;
    }

    /**
     * no
     *
     * @return no
     */
    public String getNo() {
        return no;
    }

    /**
     * 设置 no
     *
     * @param no no
     */
    public void setNo(String no) {
        this.no = no;
    }

    /**
     * name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * asyncUrl
     *
     * @return asyncUrl
     */
//    public static String getAsyncUrl() {
//        return asyncUrl;
//    }
//
//    /**
//     * 设置 asyncUrl
//     *
//     * @param asyncUrl asyncUrl
//     */
//    public static void setAsyncUrl(String asyncUrl) {
//        ClientChannel.asyncUrl = asyncUrl;
//    }
//
//    /**
//     * returnUrl
//     *
//     * @return returnUrl
//     */
//    public static String getReturnUrl() {
//        return returnUrl;
//    }
//
//    /**
//     * 设置 returnUrl
//     *
//     * @param returnUrl returnUrl
//     */
//    public static void setReturnUrl(String returnUrl) {
//        ClientChannel.returnUrl = returnUrl;
//    }

    /**
     * startTime
     *
     * @return startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 设置 startTime
     *
     * @param startTime startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * endTime
     *
     * @return endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 设置 endTime
     *
     * @param endTime endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * type
     *
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置 type
     *
     * @param type type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * minMoney
     *
     * @return minMoney
     */
    public BigDecimal getMinMoney() {
        return minMoney;
    }

    /**
     * 设置 minMoney
     *
     * @param minMoney minMoney
     */
    public void setMinMoney(BigDecimal minMoney) {
        this.minMoney = minMoney;
    }

    /**
     * maxMoney
     *
     * @return maxMoney
     */
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }

    /**
     * 设置 maxMoney
     *
     * @param maxMoney maxMoney
     */
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }

    /**
     * moneyList
     *
     * @return moneyList
     */
    public List<BigDecimal> getMoneyList() {
        return moneyList;
    }

    /**
     * 设置 moneyList
     *
     * @param moneyList moneyList
     */
    public void setMoneyList(List<BigDecimal> moneyList) {
        this.moneyList = moneyList;
    }

    /**
     * payType
     *
     * @return payType
     */
    public Long getPayType() {
        return payType;
    }

    /**
     * 设置 payType
     *
     * @param payType payType
     */
    public void setPayType(Long payType) {
        this.payType = payType;
    }

    /**
     * minTime
     *
     * @return minTime
     */
    public Long getMinTime() {
        return minTime;
    }

    /**
     * 设置 minTime
     *
     * @param minTime minTime
     */
    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }

    /**
     * maxTime
     *
     * @return maxTime
     */
    public Long getMaxTime() {
        return maxTime;
    }

    /**
     * 设置 maxTime
     *
     * @param maxTime maxTime
     */
    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * client
     *
     * @return client
     */
    public Client getClient() {
        return client;
    }

    /**
     * 设置 client
     *
     * @param client client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * gtype
     *
     * @return gtype
     */
    public Integer getGtype() {
        return gtype;
    }

    /**
     * 设置 gtype
     *
     * @param gtype gtype
     */
    public void setGtype(Integer gtype) {
        this.gtype = gtype;
    }

    /**
     * merchantMy
     *
     * @return merchantMy
     */
    public String getMerchantMy() {
        return merchantMy;
    }

    /**
     * 设置 merchantMy
     *
     * @param merchantMy merchantMy
     */
    public void setMerchantMy(String merchantMy) {
        this.merchantMy = merchantMy;
    }

    /**
     * merchantNo
     *
     * @return merchantNo
     */
    public String getMerchantNo() {
        return merchantNo;
    }

    /**
     * 设置 merchantNo
     *
     * @param merchantNo merchantNo
     */
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    /**
     * params
     *
     * @return params
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * 设置 params
     *
     * @param params params
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * urlpay
     *
     * @return urlpay
     */
    public String getUrlpay() {
        return urlpay;
    }

    /**
     * 设置 urlpay
     *
     * @param urlpay urlpay
     */
    public void setUrlpay(String urlpay) {
        this.urlpay = urlpay;
    }


    @Override
    public int compareTo(ClientChannel p) {
        return p.getPriority() - this.getPriority();
    }

}
