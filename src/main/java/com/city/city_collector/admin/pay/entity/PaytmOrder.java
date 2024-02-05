
package com.city.city_collector.admin.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author nb
 * @Description:
 */
public class PaytmOrder {

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

    private String mid;

    private String txntoken;

    private BigDecimal amount;

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
     * createTime
     *
     * @return createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 createTime
     *
     * @param createTime createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * updateTime
     *
     * @return updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 updateTime
     *
     * @param updateTime updateTime
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
     * mid
     *
     * @return mid
     */
    public String getMid() {
        return mid;
    }

    /**
     * 设置 mid
     *
     * @param mid mid
     */
    public void setMid(String mid) {
        this.mid = mid;
    }

    /**
     * txntoken
     *
     * @return txntoken
     */
    public String getTxntoken() {
        return txntoken;
    }

    /**
     * 设置 txntoken
     *
     * @param txntoken txntoken
     */
    public void setTxntoken(String txntoken) {
        this.txntoken = txntoken;
    }
}
