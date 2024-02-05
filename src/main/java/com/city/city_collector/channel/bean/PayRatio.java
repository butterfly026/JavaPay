
package com.city.city_collector.channel.bean;

import java.math.BigDecimal;

/**
 * @Description:费率
 */
public class PayRatio {

    /**
     * 固定手续费
     **/
    private BigDecimal commission;

    /**
     * 百分比手续费
     **/
    private BigDecimal ratioCommission;

    /**
     * 代理收取-固定手续费
     **/
    private BigDecimal proxyCommission;

    /**
     * 代理收取-百分比手续费
     **/
    private BigDecimal proxyRatioCommission;

    /**
     *
     */
    public PayRatio() {
        super();
    }

    /**
     * @param commission
     * @param ratioCommission
     * @param proxyCommission
     * @param proxyRatioCommission
     */
    public PayRatio(BigDecimal commission, BigDecimal ratioCommission, BigDecimal proxyCommission,
                    BigDecimal proxyRatioCommission) {
        super();
        this.commission = commission;
        this.ratioCommission = ratioCommission;
        this.proxyCommission = proxyCommission;
        this.proxyRatioCommission = proxyRatioCommission;
    }

    /**
     * commission
     *
     * @return commission
     */
    public BigDecimal getCommission() {
        return commission == null ? BigDecimal.ZERO : commission;
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
     * ratioCommission
     *
     * @return ratioCommission
     */
    public BigDecimal getRatioCommission() {
        return ratioCommission == null ? BigDecimal.ZERO : ratioCommission;
    }

    /**
     * 设置 ratioCommission
     *
     * @param ratioCommission ratioCommission
     */
    public void setRatioCommission(BigDecimal ratioCommission) {
        this.ratioCommission = ratioCommission;
    }

    /**
     * proxyCommission
     *
     * @return proxyCommission
     */
    public BigDecimal getProxyCommission() {
        return proxyCommission == null ? BigDecimal.ZERO : proxyCommission;
    }

    /**
     * 设置 proxyCommission
     *
     * @param proxyCommission proxyCommission
     */
    public void setProxyCommission(BigDecimal proxyCommission) {
        this.proxyCommission = proxyCommission;
    }

    /**
     * proxyRatioCommission
     *
     * @return proxyRatioCommission
     */
    public BigDecimal getProxyRatioCommission() {
        return proxyRatioCommission == null ? BigDecimal.ZERO : proxyRatioCommission;
    }

    /**
     * 设置 proxyRatioCommission
     *
     * @param proxyRatioCommission proxyRatioCommission
     */
    public void setProxyRatioCommission(BigDecimal proxyRatioCommission) {
        this.proxyRatioCommission = proxyRatioCommission;
    }
}
