package com.city.city_collector.admin.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:商户流水
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayMemchantRecord {
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

    private String no;

    private Long merchantId;

    private String merchantNo;

    private String merchantName;

    private String orderSn;

    private Integer type;

    private BigDecimal money;

    private BigDecimal balance;

    private String remark;

    private String reason;

    private BigDecimal ratio;

    private String remark1;

    private String key;


    /**
     * remark1
     *
     * @return remark1
     */
    public String getRemark1() {
        return remark1;
    }

    /**
     * 设置 remark1
     *
     * @param remark1 remark1
     */
    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    /**
     * ratio
     *
     * @return ratio
     */
    public BigDecimal getRatio() {
        return ratio;
    }

    /**
     * 设置 ratio
     *
     * @param ratio ratio
     */
    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
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

    private BigDecimal commission;

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
     * orderSn
     *
     * @return orderSn
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置 orderSn
     *
     * @param orderSn orderSn
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
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
     * balance
     *
     * @return balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 设置 balance
     *
     * @param balance balance
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
     * reason
     *
     * @return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置 reason
     *
     * @param reason reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {this.key = key;}
}
