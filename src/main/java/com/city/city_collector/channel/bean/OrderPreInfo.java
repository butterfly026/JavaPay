
package com.city.city_collector.channel.bean;

import java.util.Map;

import com.city.city_collector.channel.util.HttpUtil.RequestType;

/**
 * @author nb
 * @Description:
 */
public class OrderPreInfo {

    private RequestType requestType;

    /**
     * 请求体类型
     * 0. form-data
     * 1. application/json
     *
     */
    private int reqType;

    private String charset;

    private Map<String, Object> params;

    private Map<String,String> headers;
    private Integer useProxy = 1;

    //是否是订单信息，返回1表示此订单不需要发起请求，请求的链接在params的payurl中
    private int isOrderInfo = 0;

    /**
     * isOrderInfo
     *
     * @return isOrderInfo
     */
    public int getIsOrderInfo() {
        return isOrderInfo;
    }

    /**
     * 设置 isOrderInfo
     *
     * @param isOrderInfo isOrderInfo
     */
    public void setIsOrderInfo(int isOrderInfo) {
        this.isOrderInfo = isOrderInfo;
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
     * useProxy
     *
     * @return useProxy
     */
    public Integer getuseProxy() {
        return useProxy;
    }

    /**
     * 设置 useProxy
     *
     * @param useProxy 
     */
    public void setuseProxy(Integer useProxy) {
        this.useProxy = useProxy;
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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
