package com.city.city_collector.admin.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:上游代付
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayClientCash {
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

    private Long clientId;

    private String cashSn;

    private BigDecimal money;

    private String bankAccno;

    private String bankAccname;

    private String bankName;

    private String bankSubname;

    private String bankId;

    private String bankAccid;

    private String bankAccmobile;

    private String bankCode;

    private String notifyUrl;

    private String remark;

    private Date payTime;

    private Integer status;

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
     * cashSn
     *
     * @return cashSn
     */
    public String getCashSn() {
        return cashSn;
    }

    /**
     * 设置 cashSn
     *
     * @param cashSn cashSn
     */
    public void setCashSn(String cashSn) {
        this.cashSn = cashSn;
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
}
