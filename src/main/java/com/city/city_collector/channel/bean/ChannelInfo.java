
package com.city.city_collector.channel.bean;

import java.math.BigDecimal;

/**
 * @author nb
 * @Description:
 */
public class ChannelInfo {

    private String merchantNo;

    private Long merchantId;

    private String merchantName;

    private Long mcId;

    private String mcName;

    private Long chanelTypeId;

    private Long ccId;

    private BigDecimal minMoney;

    private BigDecimal maxMoney;

    private String startTime;

    private String endTime;

    private Integer mtype;

    private String moneyStr;

    private Long clientId;

    private String clientNo;

    private String clientName;

    private String ccName;

    private String keyname;

    private Integer gtype;

    private String clientMerchantNo;

    private String clientMerchantMy;

    private String urlpay;

    private String merchantIp;

    private String params;

    private String paytmid;

    private String paytmkey;

    private String paytmuid;

    private String paytmmd5;

    private Integer priority;


    private Integer retryNumber;

    public Integer getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(Integer retryNumber) {
        this.retryNumber = retryNumber;
    }

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
        return priority;
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
     * paytmid
     *
     * @return paytmid
     */
    public String getPaytmid() {
        return paytmid;
    }

    /**
     * 设置 paytmid
     *
     * @param paytmid paytmid
     */
    public void setPaytmid(String paytmid) {
        this.paytmid = paytmid;
    }

    /**
     * paytmkey
     *
     * @return paytmkey
     */
    public String getPaytmkey() {
        return paytmkey;
    }

    /**
     * 设置 paytmkey
     *
     * @param paytmkey paytmkey
     */
    public void setPaytmkey(String paytmkey) {
        this.paytmkey = paytmkey;
    }

    /**
     * paytmuid
     *
     * @return paytmuid
     */
    public String getPaytmuid() {
        return paytmuid;
    }

    /**
     * 设置 paytmuid
     *
     * @param paytmuid paytmuid
     */
    public void setPaytmuid(String paytmuid) {
        this.paytmuid = paytmuid;
    }

    /**
     * paytmmd5
     *
     * @return paytmmd5
     */
    public String getPaytmmd5() {
        return paytmmd5;
    }

    /**
     * 设置 paytmmd5
     *
     * @param paytmmd5 paytmmd5
     */
    public void setPaytmmd5(String paytmmd5) {
        this.paytmmd5 = paytmmd5;
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
     * merchantId
     *
     * @return merchantId
     */
    public Long getMerchantId() {
        return merchantId;
    }

    /**
     * 设置 merchantId
     *
     * @param merchantId merchantId
     */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * merchantName
     *
     * @return merchantName
     */
    public String getMerchantName() {
        return merchantName;
    }

    /**
     * 设置 merchantName
     *
     * @param merchantName merchantName
     */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /**
     * mcId
     *
     * @return mcId
     */
    public Long getMcId() {
        return mcId;
    }

    /**
     * 设置 mcId
     *
     * @param mcId mcId
     */
    public void setMcId(Long mcId) {
        this.mcId = mcId;
    }

    /**
     * mcName
     *
     * @return mcName
     */
    public String getMcName() {
        return mcName;
    }

    /**
     * 设置 mcName
     *
     * @param mcName mcName
     */
    public void setMcName(String mcName) {
        this.mcName = mcName;
    }

    /**
     * chanelTypeId
     *
     * @return chanelTypeId
     */
    public Long getChanelTypeId() {
        return chanelTypeId;
    }

    /**
     * 设置 chanelTypeId
     *
     * @param chanelTypeId chanelTypeId
     */
    public void setChanelTypeId(Long chanelTypeId) {
        this.chanelTypeId = chanelTypeId;
    }

    /**
     * ccId
     *
     * @return ccId
     */
    public Long getCcId() {
        return ccId;
    }

    /**
     * 设置 ccId
     *
     * @param ccId ccId
     */
    public void setCcId(Long ccId) {
        this.ccId = ccId;
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
     * mtype
     *
     * @return mtype
     */
    public Integer getMtype() {
        return mtype;
    }

    /**
     * 设置 mtype
     *
     * @param mtype mtype
     */
    public void setMtype(Integer mtype) {
        this.mtype = mtype;
    }

    /**
     * moneyStr
     *
     * @return moneyStr
     */
    public String getMoneyStr() {
        return moneyStr;
    }

    /**
     * 设置 moneyStr
     *
     * @param moneyStr moneyStr
     */
    public void setMoneyStr(String moneyStr) {
        this.moneyStr = moneyStr;
    }

    /**
     * clientId
     *
     * @return clientId
     */
    public Long getClientId() {
        return clientId;
    }

    /**
     * 设置 clientId
     *
     * @param clientId clientId
     */
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    /**
     * clientNo
     *
     * @return clientNo
     */
    public String getClientNo() {
        return clientNo;
    }

    /**
     * 设置 clientNo
     *
     * @param clientNo clientNo
     */
    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    /**
     * clientName
     *
     * @return clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 设置 clientName
     *
     * @param clientName clientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * ccName
     *
     * @return ccName
     */
    public String getCcName() {
        return ccName;
    }

    /**
     * 设置 ccName
     *
     * @param ccName ccName
     */
    public void setCcName(String ccName) {
        this.ccName = ccName;
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

    /**
     * clientMerchantNo
     *
     * @return clientMerchantNo
     */
    public String getClientMerchantNo() {
        return clientMerchantNo;
    }

    /**
     * 设置 clientMerchantNo
     *
     * @param clientMerchantNo clientMerchantNo
     */
    public void setClientMerchantNo(String clientMerchantNo) {
        this.clientMerchantNo = clientMerchantNo;
    }

    /**
     * clientMerchantMy
     *
     * @return clientMerchantMy
     */
    public String getClientMerchantMy() {
        return clientMerchantMy;
    }

    /**
     * 设置 clientMerchantMy
     *
     * @param clientMerchantMy clientMerchantMy
     */
    public void setClientMerchantMy(String clientMerchantMy) {
        this.clientMerchantMy = clientMerchantMy;
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

    /**
     * merchantIp
     *
     * @return merchantIp
     */
    public String getMerchantIp() {
        return merchantIp;
    }

    /**
     * 设置 merchantIp
     *
     * @param merchantIp merchantIp
     */
    public void setMerchantIp(String merchantIp) {
        this.merchantIp = merchantIp;
    }

    /**
     * params
     *
     * @return params
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置 params
     *
     * @param params params
     */
    public void setParams(String params) {
        this.params = params;
    }
}
