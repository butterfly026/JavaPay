
package com.city.city_collector.channel.bean;

import groovy.lang.Script;

/**
 * @author nb
 * @Description:
 */
public class GroovyScript {
    /**
     * 下单参数生成脚本
     */
    private Script orderParams;
    /**
     * 下单数据处理脚本
     */
    private Script dealOrderData;
    /**
     * 下单通知回调处理脚本
     */
    private Script notifyData;
    /**
     * 测试-下单参数生成脚本
     */
    private Script testOrderParams;

    /**
     * 代付参数生成脚本
     */
    private Script payOrderParams;
    /**
     * 代付数据处理脚本
     */
    private Script dealPayOrderData;

    /**
     * 查询订单脚本
     */
    private Script queryOrderData;
    /**
     * 处理查询订单数据脚本
     */
    private Script dealQueryOrderData;

    /**
     * 版本号
     */
    private Long version;

    /**
     * queryOrderData
     *
     * @return queryOrderData
     */
    public Script getQueryOrderData() {
        return queryOrderData;
    }

    /**
     * 设置 queryOrderData
     *
     * @param queryOrderData queryOrderData
     */
    public void setQueryOrderData(Script queryOrderData) {
        this.queryOrderData = queryOrderData;
    }

    /**
     * dealQueryOrderData
     *
     * @return dealQueryOrderData
     */
    public Script getDealQueryOrderData() {
        return dealQueryOrderData;
    }

    /**
     * 设置 dealQueryOrderData
     *
     * @param dealQueryOrderData dealQueryOrderData
     */
    public void setDealQueryOrderData(Script dealQueryOrderData) {
        this.dealQueryOrderData = dealQueryOrderData;
    }

    /**
     * 版本号
     *
     * @return version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * 设置 版本号
     *
     * @param version version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * 下单参数生成脚本
     *
     * @return orderParams
     */
    public Script getOrderParams() {
        return orderParams;
    }

    /**
     * 设置 下单参数生成脚本
     *
     * @param orderParams orderParams
     */
    public void setOrderParams(Script orderParams) {
        this.orderParams = orderParams;
    }

    /**
     * 下单数据处理脚本
     *
     * @return dealOrderData
     */
    public Script getDealOrderData() {
        return dealOrderData;
    }

    /**
     * 设置 下单数据处理脚本
     *
     * @param dealOrderData dealOrderData
     */
    public void setDealOrderData(Script dealOrderData) {
        this.dealOrderData = dealOrderData;
    }

    /**
     * 下单通知回调处理脚本
     *
     * @return notifyData
     */
    public Script getNotifyData() {
        return notifyData;
    }

    /**
     * 设置 下单通知回调处理脚本
     *
     * @param notifyData notifyData
     */
    public void setNotifyData(Script notifyData) {
        this.notifyData = notifyData;
    }

    /**
     * 测试-下单参数生成脚本
     *
     * @return testOrderParams
     */
    public Script getTestOrderParams() {
        return testOrderParams;
    }

    /**
     * 设置 测试-下单参数生成脚本
     *
     * @param testOrderParams testOrderParams
     */
    public void setTestOrderParams(Script testOrderParams) {
        this.testOrderParams = testOrderParams;
    }

    /**
     * 代付参数生成脚本
     *
     * @return payOrderParams
     */
    public Script getPayOrderParams() {
        return payOrderParams;
    }

    /**
     * 设置 代付参数生成脚本
     *
     * @param payOrderParams payOrderParams
     */
    public void setPayOrderParams(Script payOrderParams) {
        this.payOrderParams = payOrderParams;
    }

    /**
     * 代付数据处理脚本
     *
     * @return dealPayOrderData
     */
    public Script getDealPayOrderData() {
        return dealPayOrderData;
    }

    /**
     * 设置 代付数据处理脚本
     *
     * @param dealPayOrderData dealPayOrderData
     */
    public void setDealPayOrderData(Script dealPayOrderData) {
        this.dealPayOrderData = dealPayOrderData;
    }
}
