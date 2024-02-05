package com.city.city_collector.queryorder;

public class QueryOrderMsg {

    /**
     * 状态
     */
    private boolean status = false;

    /**
     * 消息
     */
    private String msg;

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

    public QueryOrderMsg() {
    }

    public QueryOrderMsg(boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
