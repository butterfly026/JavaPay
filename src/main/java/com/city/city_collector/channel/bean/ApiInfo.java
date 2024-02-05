
package com.city.city_collector.channel.bean;

import java.util.Map;

/**
 * @author nb
 * @Description:
 */
public class ApiInfo {
    //参与签名的数据
    private String signData;
    //用于签名的字符串
    private String signStr;
    /**
     * 签名
     */
    private String sign;
    /**
     * 请求的数据json
     */
    private String requstData;
    /**
     * 用于请求的数据
     */
    private Map<String, Object> datas;

    private Map<String,String> headers;
    /**
     * 接口返回的数据
     */
    private String responseData;
    /**
     * 响应的错误信息
     */
    private String responseError;
    /**
     * 处理结果
     */
    private String dealResult;
    /**
     * 结果
     */
    private String result;

    private String extraStr;
    /**
     * 跳转模式
     */
    private Integer gotype;
    /**
     * 支付链接
     */
    private String payUrl;
    /**
     * 请求类型，默认0，表单请求。1：json请求
     */
    private Integer reqType = 0;
    /**
     *是否使用全局代理,默认1，使用,0跳过
     *
     */
    private Integer useProxy = 1;
    /**
     * 下单地址
     */
    private String orderUrl;

    private Integer savePayOrder = 0;

    private boolean status = true;

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
     * savePayOrder
     *
     * @return savePayOrder
     */
    public Integer getSavePayOrder() {
        return savePayOrder;
    }

    /**
     * 设置 savePayOrder
     *
     * @param savePayOrder savePayOrder
     */
    public void setSavePayOrder(Integer savePayOrder) {
        this.savePayOrder = savePayOrder;
    }

    /**
     * orderUrl
     *
     * @return orderUrl
     */
    public String getOrderUrl() {
        return orderUrl;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * 设置 orderUrl
     *
     * @param orderUrl orderUrl
     */
    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    /**
     * reqType
     *
     * @return reqType
     */
    public Integer getReqType() {
        return reqType;
    }

    /**
     * 设置 reqType
     *
     * @param reqType reqType
     */
    public void setReqType(Integer reqType) {
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
     * payUrl
     *
     * @return payUrl
     */
    public String getPayUrl() {
        return payUrl;
    }

    /**
     * 设置 payUrl
     *
     * @param payUrl payUrl
     */
    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    /**
     * gotype
     *
     * @return gotype
     */
    public Integer getGotype() {
        return gotype;
    }

    /**
     * 设置 gotype
     *
     * @param gotype gotype
     */
    public void setGotype(Integer gotype) {
        this.gotype = gotype;
    }

    /**
     * extraStr
     *
     * @return extraStr
     */
    public String getExtraStr() {
        return extraStr;
    }

    /**
     * 设置 extraStr
     *
     * @param extraStr extraStr
     */
    public void setExtraStr(String extraStr) {
        this.extraStr = extraStr;
    }

    /**
     * result
     *
     * @return result
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置 result
     *
     * @param result result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * dealResult
     *
     * @return dealResult
     */
    public String getDealResult() {
        return dealResult;
    }

    /**
     * 设置 dealResult
     *
     * @param dealResult dealResult
     */
    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    /**
     * responseError
     *
     * @return responseError
     */
    public String getResponseError() {
        return responseError;
    }

    /**
     * 设置 responseError
     *
     * @param responseError responseError
     */
    public void setResponseError(String responseError) {
        this.responseError = responseError;
    }

    /**
     * signData
     *
     * @return signData
     */
    public String getSignData() {
        return signData;
    }

    /**
     * 设置 signData
     *
     * @param signData signData
     */
    public void setSignData(String signData) {
        this.signData = signData;
    }

    /**
     * signStr
     *
     * @return signStr
     */
    public String getSignStr() {
        return signStr;
    }

    /**
     * 设置 signStr
     *
     * @param signStr signStr
     */
    public void setSignStr(String signStr) {
        this.signStr = signStr;
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

    /**
     * requstData
     *
     * @return requstData
     */
    public String getRequstData() {
        return requstData;
    }

    /**
     * 设置 requstData
     *
     * @param requstData requstData
     */
    public void setRequstData(String requstData) {
        this.requstData = requstData;
    }

    /**
     * datas
     *
     * @return datas
     */
    public Map<String, Object> getDatas() {
        return datas;
    }

    /**
     * 设置 datas
     *
     * @param datas datas
     */
    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }

    /**
     * responseData
     *
     * @return responseData
     */
    public String getResponseData() {
        return responseData;
    }

    /**
     * 设置 responseData
     *
     * @param responseData responseData
     */
    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
}
