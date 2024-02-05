package com.city.city_collector.admin.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:商户通道
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayMerchantChannel {
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

    private Long merchantId;

    private String merchantNo;

    private String merchantName;

    private Long channelTypeId;

    private BigDecimal merchantRatio;

    private BigDecimal proxyRatio;

    private BigDecimal jrcg;

    private BigDecimal jrcgl;

    private BigDecimal lscg;

    private BigDecimal lscgl;

    private Integer status;

    private Integer del;

    private Integer useChannelPayurl;
        /**
     * useChannelPayurl
     *
     * @return useChannelPayurl
     */
    public Integer getUseChannelPayurl() {
        return useChannelPayurl;
    }

    /**
     * 设置 useChannelPayurl
     *
     * @param useChannelPayurl useChannelPayurl
     */
    public void setUseChannelPayurl(Integer use_channel_payurl) {
        this.useChannelPayurl = use_channel_payurl;
    }


    /**
     * del
     *
     * @return del
     */
    public Integer getDel() {
        return del;
    }

    /**
     * 设置 del
     *
     * @param del del
     */
    public void setDel(Integer del) {
        this.del = del;
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
     * merchantNo
     *
     * @return merchantNo
     */
    public String getMerchantNo() {
        return merchantNo;
    }

    /**
     * 设置 merchantNo
     *
     * @param merchantNo merchantNo
     */
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    /**
     * merchantName
     *
     * @return merchantName
     */
    public String getMerchantName() {
        return merchantName;
    }

    /**
     * 设置 merchantName
     *
     * @param merchantName merchantName
     */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /**
     * channelTypeId
     *
     * @return channelTypeId
     */
    public Long getChannelTypeId() {
        return channelTypeId;
    }

    /**
     * 设置 channelTypeId
     *
     * @param channelTypeId channelTypeId
     */
    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    /**
     * merchantRatio
     *
     * @return merchantRatio
     */
    public BigDecimal getMerchantRatio() {
        return merchantRatio;
    }

    /**
     * 设置 merchantRatio
     *
     * @param merchantRatio merchantRatio
     */
    public void setMerchantRatio(BigDecimal merchantRatio) {
        this.merchantRatio = merchantRatio;
    }

    /**
     * proxyRatio
     *
     * @return proxyRatio
     */
    public BigDecimal getProxyRatio() {
        return proxyRatio;
    }

    /**
     * 设置 proxyRatio
     *
     * @param proxyRatio proxyRatio
     */
    public void setProxyRatio(BigDecimal proxyRatio) {
        this.proxyRatio = proxyRatio;
    }

    /**
     * jrcg
     *
     * @return jrcg
     */
    public BigDecimal getJrcg() {
        return jrcg;
    }

    /**
     * 设置 jrcg
     *
     * @param jrcg jrcg
     */
    public void setJrcg(BigDecimal jrcg) {
        this.jrcg = jrcg;
    }

    /**
     * jrcgl
     *
     * @return jrcgl
     */
    public BigDecimal getJrcgl() {
        return jrcgl;
    }

    /**
     * 设置 jrcgl
     *
     * @param jrcgl jrcgl
     */
    public void setJrcgl(BigDecimal jrcgl) {
        this.jrcgl = jrcgl;
    }

    /**
     * lscg
     *
     * @return lscg
     */
    public BigDecimal getLscg() {
        return lscg;
    }

    /**
     * 设置 lscg
     *
     * @param lscg lscg
     */
    public void setLscg(BigDecimal lscg) {
        this.lscg = lscg;
    }

    /**
     * lscgl
     *
     * @return lscgl
     */
    public BigDecimal getLscgl() {
        return lscgl;
    }

    /**
     * 设置 lscgl
     *
     * @param lscgl lscgl
     */
    public void setLscgl(BigDecimal lscgl) {
        this.lscgl = lscgl;
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
}
