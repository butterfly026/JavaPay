package com.city.city_collector.common.util;


public class CashMessage {

    public boolean status;

    public String msg;

    public String sn;

    /**
     * status
     *
     * @return status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * 设置 status
     *
     * @param status status
     */
    public void setStatus(boolean status) {
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

    public CashMessage(boolean status, String msg, String sn) {
        super();
        this.status = status;
        this.msg = msg;
        this.sn = sn;
    }

    public CashMessage() {
        super();
    }


}
