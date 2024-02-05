package com.city.city_collector.channel.bean;

/**
 * @Description:提现配置
 */
public class CashConfig {
    /**
     * 商户银行卡下发
     */
    private PayRatio bankRatio;
    /**
     * 商户UPI下发
     */
    private PayRatio upiRatio;
    /**
     * 商户USDT下发
     */
    private PayRatio usdtRatio;

    /**
     * 商户银行卡代付
     */
    private PayRatio dfbankRatio;
    /**
     * 商户UPI代付
     */
    private PayRatio dfupiRatio;
    /**
     * 商户USDT代付
     */
    private PayRatio dfusdtRatio;

    /**
     * bankRatio
     *
     * @return bankRatio
     */
    public PayRatio getBankRatio() {
        return bankRatio == null ? new PayRatio() : bankRatio;
    }

    /**
     * 设置 bankRatio
     *
     * @param bankRatio bankRatio
     */
    public void setBankRatio(PayRatio bankRatio) {
        this.bankRatio = bankRatio;
    }

    /**
     * upiRatio
     *
     * @return upiRatio
     */
    public PayRatio getUpiRatio() {
        return upiRatio == null ? new PayRatio() : upiRatio;
    }

    /**
     * 设置 upiRatio
     *
     * @param upiRatio upiRatio
     */
    public void setUpiRatio(PayRatio upiRatio) {
        this.upiRatio = upiRatio;
    }

    /**
     * usdtRatio
     *
     * @return usdtRatio
     */
    public PayRatio getUsdtRatio() {
        return usdtRatio == null ? new PayRatio() : usdtRatio;
    }

    /**
     * 设置 usdtRatio
     *
     * @param usdtRatio usdtRatio
     */
    public void setUsdtRatio(PayRatio usdtRatio) {
        this.usdtRatio = usdtRatio;
    }

    /**
     * dfbankRatio
     *
     * @return dfbankRatio
     */
    public PayRatio getDfbankRatio() {
        return dfbankRatio == null ? new PayRatio() : dfbankRatio;
    }

    /**
     * 设置 dfbankRatio
     *
     * @param dfbankRatio dfbankRatio
     */
    public void setDfbankRatio(PayRatio dfbankRatio) {
        this.dfbankRatio = dfbankRatio;
    }

    /**
     * dfupiRatio
     *
     * @return dfupiRatio
     */
    public PayRatio getDfupiRatio() {
        return dfupiRatio == null ? new PayRatio() : dfupiRatio;
    }

    /**
     * 设置 dfupiRatio
     *
     * @param dfupiRatio dfupiRatio
     */
    public void setDfupiRatio(PayRatio dfupiRatio) {
        this.dfupiRatio = dfupiRatio;
    }

    /**
     * dfusdtRatio
     *
     * @return dfusdtRatio
     */
    public PayRatio getDfusdtRatio() {
        return dfusdtRatio == null ? new PayRatio() : dfusdtRatio;
    }

    /**
     * 设置 dfusdtRatio
     *
     * @param dfusdtRatio dfusdtRatio
     */
    public void setDfusdtRatio(PayRatio dfusdtRatio) {
        this.dfusdtRatio = dfusdtRatio;
    }
}
