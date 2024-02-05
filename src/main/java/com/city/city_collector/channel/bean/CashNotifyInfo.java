
package com.city.city_collector.channel.bean;

import java.math.BigDecimal;

/**
 * @author nb
 * @Description:
 */
public class CashNotifyInfo {

    private Boolean status;

    private String sn;

    private String clientSn;

    private BigDecimal amount;

    private Boolean dealStatus;

    /**
     * dealStatus
     *
     * @return dealStatus
     */
    public Boolean getDealStatus() {
        return dealStatus;
    }

    /**
     * 设置 dealStatus
     *
     * @param dealStatus dealStatus
     */
    public void setDealStatus(Boolean dealStatus) {
        this.dealStatus = dealStatus;
    }

    /**
     * status
     *
     * @return status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置 status
     *
     * @param status status
     */
    public void setStatus(Boolean status) {
        this.status = status;
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
     * @param status
     */
    public CashNotifyInfo(Boolean status) {
        super();
        this.status = status;
    }

    public CashNotifyInfo() {
    }
}
