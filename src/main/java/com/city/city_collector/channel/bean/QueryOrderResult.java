
package com.city.city_collector.channel.bean;

/**
 * @author nb
 * @Description:
 */
public class QueryOrderResult {
    /**
     * 订单状态
     */
    private boolean status;
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

    /**
     * @param status
     * @param msg
     */
    public QueryOrderResult(boolean status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
    }

    /**
     *
     */
    public QueryOrderResult() {
        super();
    }
}
