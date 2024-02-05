package com.city.city_collector.admin.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:商户提现
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayCash {
    /**
     * ID
     */
    private Long id;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新日期
     */
    private Date updateTime;

    private Long merchantId;

    private String merchantNo;

    private String merchantName;

    private Long proxyId;

    private String proxyNo;

    private String proxyName;

    private String sn;

    private String merchantSn;

    private Integer cashType;

    private BigDecimal amount;

    private BigDecimal commission;

    private BigDecimal realMoney;

    private String bankAccno;

    private String bankAccname;

    private String bankName;

    private String bankSubname;

    private String bankId;

    private String bankAccid;

    private String bankAccmobile;

    private String notifyUrl;

    private String remark;

    private Date payTime;

    private Integer status;

    /**
     * 0 初始化
     * 1 失败
     * 2 拒绝
     * 3 审核通过
     * 4 提交上游
     * 5 成功
     */
    private Integer payStatus;

    private Integer notifyStatus;

    private Long clientId;

    private String clientNo;

    private String clientName;

    private String bankCode;

    private String clientSn;

    private BigDecimal clientMoney;

    private BigDecimal clientCommission;

    private BigDecimal clientRealMoney;

    private Integer payType;

    private Integer dealStatus;

    private Integer notifyCount;

    private Date notifyTime;

    private Integer notifyResult;

    private String apiremark;

    private String bankProvince;

    private String bankCity;

    private String bankIfsc;

    private String bankNation;

    private Integer btype;

    private BigDecimal proxyMoney;
    //默认为商户
    private Integer userType = 0;
    //通道类型
    private Integer channelType = 0;

    /**
     * channelType
     *
     * @return channelType
     */
    public Integer getChannelType() {
        return channelType;
    }

    /**
     * 设置 channelType
     *
     * @param channelType channelType
     */
    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    /**
     * userType
     *
     * @return userType
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * 设置 userType
     *
     * @param userType userType
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    /**
     * proxyMoney
     *
     * @return proxyMoney
     */
    public BigDecimal getProxyMoney() {
        return proxyMoney;
    }

    /**
     * 设置 proxyMoney
     *
     * @param proxyMoney proxyMoney
     */
    public void setProxyMoney(BigDecimal proxyMoney) {
        this.proxyMoney = proxyMoney;
    }

    /**
     * bankIfsc
     *
     * @return bankIfsc
     */
    public String getBankIfsc() {
        return bankIfsc;
    }

    /**
     * 设置 bankIfsc
     *
     * @param bankIfsc bankIfsc
     */
    public void setBankIfsc(String bankIfsc) {
        this.bankIfsc = bankIfsc;
    }

    /**
     * bankNation
     *
     * @return bankNation
     */
    public String getBankNation() {
        return bankNation;
    }

    /**
     * 设置 bankNation
     *
     * @param bankNation bankNation
     */
    public void setBankNation(String bankNation) {
        this.bankNation = bankNation;
    }

    /**
     * btype
     *
     * @return btype
     */
    public Integer getBtype() {
        return btype;
    }

    /**
     * 设置 btype
     *
     * @param btype btype
     */
    public void setBtype(Integer btype) {
        this.btype = btype;
    }

    /**
     * bankProvince
     *
     * @return bankProvince
     */
    public String getBankProvince() {
        return bankProvince;
    }

    /**
     * 设置 bankProvince
     *
     * @param bankProvince bankProvince
     */
    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    /**
     * bankCity
     *
     * @return bankCity
     */
    public String getBankCity() {
        return bankCity;
    }

    /**
     * 设置 bankCity
     *
     * @param bankCity bankCity
     */
    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    /**
     * apiremark
     *
     * @return apiremark
     */
    public String getApiremark() {
        return apiremark;
    }

    /**
     * 设置 apiremark
     *
     * @param apiremark apiremark
     */
    public void setApiremark(String apiremark) {
        this.apiremark = apiremark;
    }

    /**
     * dealStatus
     *
     * @return dealStatus
     */
    public Integer getDealStatus() {
        return dealStatus;
    }

    /**
     * 设置 dealStatus
     *
     * @param dealStatus dealStatus
     */
    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    /**
     * notifyCount
     *
     * @return notifyCount
     */
    public Integer getNotifyCount() {
        return notifyCount;
    }

    /**
     * 设置 notifyCount
     *
     * @param notifyCount notifyCount
     */
    public void setNotifyCount(Integer notifyCount) {
        this.notifyCount = notifyCount;
    }

    /**
     * notifyTime
     *
     * @return notifyTime
     */
    public Date getNotifyTime() {
        return notifyTime;
    }

    /**
     * 设置 notifyTime
     *
     * @param notifyTime notifyTime
     */
    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    /**
     * notifyResult
     *
     * @return notifyResult
     */
    public Integer getNotifyResult() {
        return notifyResult;
    }

    /**
     * 设置 notifyResult
     *
     * @param notifyResult notifyResult
     */
    public void setNotifyResult(Integer notifyResult) {
        this.notifyResult = notifyResult;
    }

    /**
     * payType
     *
     * @return payType
     */
    public Integer getPayType() {
        return payType;
    }

    /**
     * 设置 payType
     *
     * @param payType payType
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * clientSn
     *
     * @return clientSn
     */
    public String getClientSn() {
        return clientSn;
    }

    /**
     * 设置 clientSn
     *
     * @param clientSn clientSn
     */
    public void setClientSn(String clientSn) {
        this.clientSn = clientSn;
    }

    /**
     * bankCode
     *
     * @return bankCode
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * 设置 bankCode
     *
     * @param bankCode bankCode
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * 获取ID
     *
     * @return id ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建日期
     *
     * @return createTime 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新日期
     *
     * @return updateTime 更新日期
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新日期
     *
     * @param updateTime 更新日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
     * proxyId
     *
     * @return proxyId
     */
    public Long getProxyId() {
        return proxyId;
    }

    /**
     * 设置 proxyId
     *
     * @param proxyId proxyId
     */
    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    /**
     * proxyNo
     *
     * @return proxyNo
     */
    public String getProxyNo() {
        return proxyNo;
    }

    /**
     * 设置 proxyNo
     *
     * @param proxyNo proxyNo
     */
    public void setProxyNo(String proxyNo) {
        this.proxyNo = proxyNo;
    }

    /**
     * proxyName
     *
     * @return proxyName
     */
    public String getProxyName() {
        return proxyName;
    }

    /**
     * 设置 proxyName
     *
     * @param proxyName proxyName
     */
    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    /**
     * sn
     *
     * @return sn
     */
    public String getSn() {
        return sn;
    }

    /**
     * 设置 sn
     *
     * @param sn sn
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * merchantSn
     *
     * @return merchantSn
     */
    public String getMerchantSn() {
        return merchantSn;
    }

    /**
     * 设置 merchantSn
     *
     * @param merchantSn merchantSn
     */
    public void setMerchantSn(String merchantSn) {
        this.merchantSn = merchantSn;
    }

    /**
     * cashType
     *
     * @return cashType
     */
    public Integer getCashType() {
        return cashType;
    }

    /**
     * 设置 cashType
     *
     * @param cashType cashType
     */
    public void setCashType(Integer cashType) {
        this.cashType = cashType;
    }

    /**
     * amount
     *
     * @return amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置 amount
     *
     * @param amount amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * commission
     *
     * @return commission
     */
    public BigDecimal getCommission() {
        return commission;
    }

    /**
     * 设置 commission
     *
     * @param commission commission
     */
    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    /**
     * bankAccno
     *
     * @return bankAccno
     */
    public String getBankAccno() {
        return bankAccno;
    }

    /**
     * 设置 bankAccno
     *
     * @param bankAccno bankAccno
     */
    public void setBankAccno(String bankAccno) {
        this.bankAccno = bankAccno;
    }

    /**
     * bankAccname
     *
     * @return bankAccname
     */
    public String getBankAccname() {
        return bankAccname;
    }

    /**
     * 设置 bankAccname
     *
     * @param bankAccname bankAccname
     */
    public void setBankAccname(String bankAccname) {
        this.bankAccname = bankAccname;
    }

    /**
     * bankName
     *
     * @return bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置 bankName
     *
     * @param bankName bankName
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * bankSubname
     *
     * @return bankSubname
     */
    public String getBankSubname() {
        return bankSubname;
    }

    /**
     * 设置 bankSubname
     *
     * @param bankSubname bankSubname
     */
    public void setBankSubname(String bankSubname) {
        this.bankSubname = bankSubname;
    }

    /**
     * bankId
     *
     * @return bankId
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * 设置 bankId
     *
     * @param bankId bankId
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * bankAccid
     *
     * @return bankAccid
     */
    public String getBankAccid() {
        return bankAccid;
    }

    /**
     * 设置 bankAccid
     *
     * @param bankAccid bankAccid
     */
    public void setBankAccid(String bankAccid) {
        this.bankAccid = bankAccid;
    }

    /**
     * bankAccmobile
     *
     * @return bankAccmobile
     */
    public String getBankAccmobile() {
        return bankAccmobile;
    }

    /**
     * 设置 bankAccmobile
     *
     * @param bankAccmobile bankAccmobile
     */
    public void setBankAccmobile(String bankAccmobile) {
        this.bankAccmobile = bankAccmobile;
    }

    /**
     * notifyUrl
     *
     * @return notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * 设置 notifyUrl
     *
     * @param notifyUrl notifyUrl
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    /**
     * remark
     *
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 remark
     *
     * @param remark remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * payTime
     *
     * @return payTime
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * 设置 payTime
     *
     * @param payTime payTime
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * status
     *
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置 status
     *
     * @param status status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * payStatus
     *
     * @return payStatus
     */
    public Integer getPayStatus() {
        return payStatus;
    }

    /**
     * 设置 payStatus
     *
     * @param payStatus payStatus
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * notifyStatus
     *
     * @return notifyStatus
     */
    public Integer getNotifyStatus() {
        return notifyStatus;
    }

    /**
     * 设置 notifyStatus
     *
     * @param notifyStatus notifyStatus
     */
    public void setNotifyStatus(Integer notifyStatus) {
        this.notifyStatus = notifyStatus;
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
     * realMoney
     *
     * @return realMoney
     */
    public BigDecimal getRealMoney() {
        return realMoney;
    }

    /**
     * 设置 realMoney
     *
     * @param realMoney realMoney
     */
    public void setRealMoney(BigDecimal realMoney) {
        this.realMoney = realMoney;
    }

    /**
     * clientMoney
     *
     * @return clientMoney
     */
    public BigDecimal getClientMoney() {
        return clientMoney;
    }

    /**
     * 设置 clientMoney
     *
     * @param clientMoney clientMoney
     */
    public void setClientMoney(BigDecimal clientMoney) {
        this.clientMoney = clientMoney;
    }

    /**
     * clientCommission
     *
     * @return clientCommission
     */
    public BigDecimal getClientCommission() {
        return clientCommission;
    }

    /**
     * 设置 clientCommission
     *
     * @param clientCommission clientCommission
     */
    public void setClientCommission(BigDecimal clientCommission) {
        this.clientCommission = clientCommission;
    }

    /**
     * clientRealMoney
     *
     * @return clientRealMoney
     */
    public BigDecimal getClientRealMoney() {
        return clientRealMoney;
    }

    /**
     * 设置 clientRealMoney
     *
     * @param clientRealMoney clientRealMoney
     */
    public void setClientRealMoney(BigDecimal clientRealMoney) {
        this.clientRealMoney = clientRealMoney;
    }
}
