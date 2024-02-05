package com.city.city_collector.admin.pay.entity;

import java.util.Date;

/**
 * Description:系统日志
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayLog {
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

    private Integer type;

    private Long userId;

    private String userName;

    private String sn;

    private Integer snType;

    private String descript;

    private String requestData;

    private String responseData;

    private Long clientId;

    private Long clientChannelId;

    private Long merchantId;

    private Long merchantChannelId;

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
     * type
     *
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置 type
     *
     * @param type type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * userId
     *
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置 userId
     *
     * @param userId userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * userName
     *
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置 userName
     *
     * @param userName userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
     * snType
     *
     * @return snType
     */
    public Integer getSnType() {
        return snType;
    }

    /**
     * 设置 snType
     *
     * @param snType snType
     */
    public void setSnType(Integer snType) {
        this.snType = snType;
    }

    /**
     * descript
     *
     * @return descript
     */
    public String getDescript() {
        return descript;
    }

    /**
     * 设置 descript
     *
     * @param descript descript
     */
    public void setDescript(String descript) {
        this.descript = descript;
    }

    /**
     * requestData
     *
     * @return requestData
     */
    public String getRequestData() {
        return requestData;
    }

    /**
     * 设置 requestData
     *
     * @param requestData requestData
     */
    public void setRequestData(String requestData) {
        this.requestData = requestData;
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

    /**
     * clientId
     *
     * @return clientId
     */
    public Long getClientId() {
        return clientId;
    }

    /**
     * 设置 clientId
     *
     * @param clientId clientId
     */
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    /**
     * clientChannelId
     *
     * @return clientChannelId
     */
    public Long getClientChannelId() {
        return clientChannelId;
    }

    /**
     * 设置 clientChannelId
     *
     * @param clientChannelId clientChannelId
     */
    public void setClientChannelId(Long clientChannelId) {
        this.clientChannelId = clientChannelId;
    }

    /**
     * merchantId
     *
     * @return merchantId
     */
    public Long getMerchantId() {
        return merchantId;
    }

    /**
     * 设置 merchantId
     *
     * @param merchantId merchantId
     */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * merchantChannelId
     *
     * @return merchantChannelId
     */
    public Long getMerchantChannelId() {
        return merchantChannelId;
    }

    /**
     * 设置 merchantChannelId
     *
     * @param merchantChannelId merchantChannelId
     */
    public void setMerchantChannelId(Long merchantChannelId) {
        this.merchantChannelId = merchantChannelId;
    }
}
