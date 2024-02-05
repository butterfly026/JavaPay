
package com.city.city_collector.channel.bean;

import java.math.BigDecimal;

/**
 * @author nb
 * @Description: 代付对象
 */
public class CashInfo {

    private Boolean status;

    private String msg;

    private String sn;

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
     * msg
     *
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置 msg
     *
     * @param msg msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
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

    public CashInfo() {
    }

    /**
     * @param status
     * @param msg
     */
    public CashInfo(Boolean status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
    }
}
