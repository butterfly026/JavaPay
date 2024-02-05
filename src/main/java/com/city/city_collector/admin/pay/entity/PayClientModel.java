package com.city.city_collector.admin.pay.entity;

import java.util.Date;

/**
 * Description:上游模块
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayClientModel {
    /**
     * ID
     */
    private Long id;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新日期
     */
    private Date updateTime;

    private String name;

    private String keyname;

    private String filepath;

    private Integer status;

    private Integer orderStatus;

    private Integer payStatus;

    private Integer reqType;

    private Long version;

    private Integer testStatus;

    private String json;

    private String notifystr;

    private Integer cdStatus;

    /**
     * cdStatus
     *
     * @return cdStatus
     */
    public Integer getCdStatus() {
        return cdStatus;
    }

    /**
     * 设置 cdStatus
     *
     * @param cdStatus cdStatus
     */
    public void setCdStatus(Integer cdStatus) {
        this.cdStatus = cdStatus;
    }

    /**
     * notifystr
     *
     * @return notifystr
     */
    public String getNotifystr() {
        return notifystr;
    }

    /**
     * 设置 notifystr
     *
     * @param notifystr notifystr
     */
    public void setNotifystr(String notifystr) {
        this.notifystr = notifystr;
    }

    /**
     * json
     *
     * @return json
     */
    public String getJson() {
        return json;
    }

    /**
     * 设置 json
     *
     * @param json json
     */
    public void setJson(String json) {
        this.json = json;
    }

    /**
     * testStatus
     *
     * @return testStatus
     */
    public Integer getTestStatus() {
        return testStatus;
    }

    /**
     * 设置 testStatus
     *
     * @param testStatus testStatus
     */
    public void setTestStatus(Integer testStatus) {
        this.testStatus = testStatus;
    }

    /**
     * 获取ID
     *
     * @return id ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建日期
     *
     * @return createTime 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新日期
     *
     * @return updateTime 更新日期
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新日期
     *
     * @param updateTime 更新日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * keyname
     *
     * @return keyname
     */
    public String getKeyname() {
        return keyname;
    }

    /**
     * 设置 keyname
     *
     * @param keyname keyname
     */
    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    /**
     * filepath
     *
     * @return filepath
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * 设置 filepath
     *
     * @param filepath filepath
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * status
     *
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置 status
     *
     * @param status status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * orderStatus
     *
     * @return orderStatus
     */
    public Integer getOrderStatus() {
        return orderStatus;
    }

    /**
     * 设置 orderStatus
     *
     * @param orderStatus orderStatus
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * payStatus
     *
     * @return payStatus
     */
    public Integer getPayStatus() {
        return payStatus;
    }

    /**
     * 设置 payStatus
     *
     * @param payStatus payStatus
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
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
     * version
     *
     * @return version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * 设置 version
     *
     * @param version version
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}
