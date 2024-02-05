
package com.city.city_collector.channel.bean;

import java.util.Map;

import com.city.city_collector.channel.util.HttpUtil.RequestType;

/**
 * @author nb
 * @Description:
 */
public class QueryOrderInfo {

    /**
     * 需要发起请求
     */
    private boolean needRequest = true;
    /**
     * 请求方式：GET,POST
     */
    private RequestType requestType;
    /**
     * 请求类型：普通表单请求,json数据
     */
    private int reqType;
    /**
     * 字符串
     */
    private String charset;
    /**
     * 请求参数
     */
    private Map<String, Object> params;

    /**
     * 不需要发起请求用这个状态来判断订单是否成功
     */
    private boolean orderSuccess = false;

    /**
     * 上游单号
     */
    private String clientSn = null;

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
     * orderSuccess
     *
     * @return orderSuccess
     */
    public boolean isOrderSuccess() {
        return orderSuccess;
    }

    /**
     * 设置 orderSuccess
     *
     * @param orderSuccess orderSuccess
     */
    public void setOrderSuccess(boolean orderSuccess) {
        this.orderSuccess = orderSuccess;
    }

    /**
     * needRequest
     *
     * @return needRequest
     */
    public boolean isNeedRequest() {
        return needRequest;
    }

    /**
     * 设置 needRequest
     *
     * @param needRequest needRequest
     */
    public void setNeedRequest(boolean needRequest) {
        this.needRequest = needRequest;
    }

    /**
     * requestType
     *
     * @return requestType
     */
    public RequestType getRequestType() {
        return requestType;
    }

    /**
     * 设置 requestType
     *
     * @param requestType requestType
     */
    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * reqType
     *
     * @return reqType
     */
    public int getReqType() {
        return reqType;
    }

    /**
     * 设置 reqType
     *
     * @param reqType reqType
     */
    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    /**
     * charset
     *
     * @return charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * 设置 charset
     *
     * @param charset charset
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * params
     *
     * @return params
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * 设置 params
     *
     * @param params params
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
