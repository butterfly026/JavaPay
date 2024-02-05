package com.city.city_collector.admin.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:支付订单
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayOrder {
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

    private String sn;

    /**
     * 上游id
     */
    private Long clientId;

    /**
     * 上游号
     */
    private String clientNo;


    /**
     * 上游订单号
     */
    private String clientSn;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 商户订单号
     */
    private String merchantNo;

    private String merchantSn;

    /**
     * 订单金额
     */
    private BigDecimal money;

    /**
     * 应付金额
     */
    private BigDecimal amount;

    private Date payTime;

    private Long channelTypeId;

    /**
     * -2 未获取链接
     *  -1 获取链接失败
     *  0 获取到支付链接, 未支付
     *  1 支付完成
     */
    private Integer orderStatus;

    private Integer notifyStatus;

    private String payUrl;

    private Integer payUrlCount;

    private String remark;

    private String clientInfo;

    private String notifyUrl;

    private String productName;

    /**
     * 上游通道id
     */
    private Long clientChannelId;

    private Long merchantChannelId;

    private Integer dealStatus;

    private Integer notifyCount;

    private Date notifyTime;

    private Integer notifyResult;

    private BigDecimal profit;

    private Integer mend;

    private String tname;

    private Long proxyId;

    private String proxyNo;

    private Integer orderType;

    private BigDecimal xnprofit;

    // 虚拟订单
    private Integer xnorder;

    private Integer platform;
    private BigDecimal merchantRatio;
    private BigDecimal proxyRatio;
    private BigDecimal clientRatio;
    
    public Integer getPlatform() {
        return platform;
    }

    /**
     * 设置 platform
     *
     * @param platform platform
     */
    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getPayUrlCount() {
        return payUrlCount;
    }

    public void setPayUrlCount(Integer payUrlCount) {
        this.payUrlCount = payUrlCount;
    }

    /**
     * xnorder
     *
     * @return xnorder
     */
    public Integer getXnorder() {
        return xnorder == null ? 0 : xnorder;
    }

    /**
     * 设置 xnorder
     *
     * @param xnorder xnorder
     */
    public void setXnorder(Integer xnorder) {
        this.xnorder = xnorder;
    }

    /**
     * xnprofit
     *
     * @return xnprofit
     */
    public BigDecimal getXnprofit() {
        return xnprofit;
    }

    /**
     * 设置 xnprofit
     *
     * @param xnprofit xnprofit
     */
    public void setXnprofit(BigDecimal xnprofit) {
        this.xnprofit = xnprofit;
    }

    /**
     * profit
     *
     * @return profit
     */
    public BigDecimal getProfit() {
        return profit;
    }

    /**
     * 设置 profit
     *
     * @param profit profit
     */
    public void setProfit(BigDecimal profit) {
        this.profit = profit;
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
     * merchantSn
     *
     * @return merchantSn
     */
    public String getMerchantSn() {
        return merchantSn;
    }

    /**
     * 设置 merchantSn
     *
     * @param merchantSn merchantSn
     */
    public void setMerchantSn(String merchantSn) {
        this.merchantSn = merchantSn;
    }

    /**
     * money
     *
     * @return money
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置 money
     *
     * @param money money
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * amount
     *
     * @return amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置 amount
     *
     * @param amount amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * payTime
     *
     * @return payTime
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * 设置 payTime
     *
     * @param payTime payTime
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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
     * notifyStatus
     *
     * @return notifyStatus
     */
    public Integer getNotifyStatus() {
        return notifyStatus;
    }

    /**
     * 设置 notifyStatus
     *
     * @param notifyStatus notifyStatus
     */
    public void setNotifyStatus(Integer notifyStatus) {
        this.notifyStatus = notifyStatus;
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
     * remark
     *
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 remark
     *
     * @param remark remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * clientInfo
     *
     * @return clientInfo
     */
    public String getClientInfo() {
        return clientInfo;
    }

    /**
     * 设置 clientInfo
     *
     * @param clientInfo clientInfo
     */
    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    /**
     * notifyUrl
     *
     * @return notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * 设置 notifyUrl
     *
     * @param notifyUrl notifyUrl
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    /**
     * productName
     *
     * @return productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置 productName
     *
     * @param productName productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
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

    /**
     * dealStatus
     *
     * @return dealStatus
     */
    public Integer getDealStatus() {
        return dealStatus;
    }

    /**
     * 设置 dealStatus
     *
     * @param dealStatus dealStatus
     */
    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    /**
     * notifyCount
     *
     * @return notifyCount
     */
    public Integer getNotifyCount() {
        return notifyCount;
    }

    /**
     * 设置 notifyCount
     *
     * @param notifyCount notifyCount
     */
    public void setNotifyCount(Integer notifyCount) {
        this.notifyCount = notifyCount;
    }

    /**
     * notifyTime
     *
     * @return notifyTime
     */
    public Date getNotifyTime() {
        return notifyTime;
    }

    /**
     * 设置 notifyTime
     *
     * @param notifyTime notifyTime
     */
    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    /**
     * notifyResult
     *
     * @return notifyResult
     */
    public Integer getNotifyResult() {
        return notifyResult;
    }

    /**
     * 设置 notifyResult
     *
     * @param notifyResult notifyResult
     */
    public void setNotifyResult(Integer notifyResult) {
        this.notifyResult = notifyResult;
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
     * clientRatio
     *
     * @return clientRatio
     */
    public BigDecimal getClientRatio() {
        return clientRatio;
    }

    /**
     * 设置 clientRatio
     *
     * @param clientRatio clientRatio
     */
    public void setClientRatio(BigDecimal clientRatio) {
        this.clientRatio = clientRatio;
    }

    /**
     * mend
     *
     * @return mend
     */
    public Integer getMend() {
        return mend;
    }

    /**
     * 设置 mend
     *
     * @param mend mend
     */
    public void setMend(Integer mend) {
        this.mend = mend;
    }

    /**
     * clientNo
     *
     * @return clientNo
     */
    public String getClientNo() {
        return clientNo;
    }

    /**
     * 设置 clientNo
     *
     * @param clientNo clientNo
     */
    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
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
     * tname
     *
     * @return tname
     */
    public String getTname() {
        return tname;
    }

    /**
     * 设置 tname
     *
     * @param tname tname
     */
    public void setTname(String tname) {
        this.tname = tname;
    }

    /**
     * proxyId
     *
     * @return proxyId
     */
    public Long getProxyId() {
        return proxyId;
    }

    /**
     * 设置 proxyId
     *
     * @param proxyId proxyId
     */
    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    /**
     * proxyNo
     *
     * @return proxyNo
     */
    public String getProxyNo() {
        return proxyNo;
    }

    /**
     * 设置 proxyNo
     *
     * @param proxyNo proxyNo
     */
    public void setProxyNo(String proxyNo) {
        this.proxyNo = proxyNo;
    }

    /**
     * orderType
     *
     * @return orderType
     */
    public Integer getOrderType() {
        return orderType;
    }

    /**
     * 设置 orderType
     *
     * @param orderType orderType
     */
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }
}
