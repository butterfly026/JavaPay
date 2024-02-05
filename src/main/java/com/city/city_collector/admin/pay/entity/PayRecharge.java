package com.city.city_collector.admin.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:商户充值
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayRecharge {
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

    private String sn;

    private Long merchantId;

    private String merchantNo;

    private String merchantName;

    private BigDecimal money;

    private Integer accType;

    private String cardNo;

    private String cardName;

    private String bankName;

    private String bankIfsc;

    private String bankNation;

    private Integer status;

    private String remark;

    private Integer smodel;

    /**
     * smodel
     *
     * @return smodel
     */
    public Integer getSmodel() {
        return smodel;
    }

    /**
     * 设置 smodel
     *
     * @param smodel smodel
     */
    public void setSmodel(Integer smodel) {
        this.smodel = smodel;
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
     * money
     *
     * @return money
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置 money
     *
     * @param money money
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * accType
     *
     * @return accType
     */
    public Integer getAccType() {
        return accType;
    }

    /**
     * 设置 accType
     *
     * @param accType accType
     */
    public void setAccType(Integer accType) {
        this.accType = accType;
    }

    /**
     * cardNo
     *
     * @return cardNo
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * 设置 cardNo
     *
     * @param cardNo cardNo
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * cardName
     *
     * @return cardName
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * 设置 cardName
     *
     * @param cardName cardName
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
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
}
