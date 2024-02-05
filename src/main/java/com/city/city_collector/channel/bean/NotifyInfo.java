package com.city.city_collector.channel.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author nb
 * @Description:
 */
public class NotifyInfo {
    private int verifyAmount;
    /**
     * 平台订单号
     */
    private String sn;
    /**
     * 上游订单号
     */
    private String clientSn;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 订单金额
     */
    private BigDecimal actualAmount;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 支付状态
     */
    private String status;
    /**
     * 签名
     */
    private String sign;

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
     * actualAmount
     *
     * @return actualAmount
     */
    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    /**
     * 设置 actualAmount
     *
     * @param actualAmount actualAmount
     */
    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
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
    public String getStatus() {
        return status;
    }

    /**
     * 设置 status
     *
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * sign
     *
     * @return sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * 设置 sign
     *
     * @param sign sign
     */
    public void setSign(String sign) {
        this.sign = sign;
    }
    //kahn
    public void setVerifyAmount(int verifyAmount){
        this.verifyAmount = verifyAmount;
    }
    public int getVerifyAmount(){
        return this.verifyAmount;
    }
}
