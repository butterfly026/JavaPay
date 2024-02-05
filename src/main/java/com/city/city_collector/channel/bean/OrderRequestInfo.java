
package com.city.city_collector.channel.bean;

/**
 * 下单请求数据
 *
 * @Description:
 */
public class OrderRequestInfo {
    /**
     * 状态
     */
    private Boolean status;
    /**
     * 使用的mid
     */
    private String mid;
    /**
     * 使用的mkey
     */
    private String mkey;
    /**
     * 具体描述
     */
    private String desc;
    /**
     * 响应数据
     */
    private String data;
    /**
     * 支付token
     */
    private String txnToken;

    /**
     * 支付URL
     */
    private String payUrl;

    /**
     * 上游ID
     */
    private Long clientId;
    /**
     * 上游编号
     */
    private String clientNo;
    /**
     * 上游名
     */
    private String clientName;

    private Long clientChannelId;

    private Long merchantChannelId;

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
     * clientName
     *
     * @return clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 设置 clientName
     *
     * @param clientName clientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
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
     * data
     *
     * @return data
     */
    public String getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * txnToken
     *
     * @return txnToken
     */
    public String getTxnToken() {
        return txnToken;
    }

    /**
     * 设置 txnToken
     *
     * @param txnToken txnToken
     */
    public void setTxnToken(String txnToken) {
        this.txnToken = txnToken;
    }

    /**
     * status
     *
     * @return status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置 status
     *
     * @param status status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * mid
     *
     * @return mid
     */
    public String getMid() {
        return mid;
    }

    /**
     * 设置 mid
     *
     * @param mid mid
     */
    public void setMid(String mid) {
        this.mid = mid;
    }

    /**
     * mkey
     *
     * @return mkey
     */
    public String getMkey() {
        return mkey;
    }

    /**
     * 设置 mkey
     *
     * @param mkey mkey
     */
    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    /**
     * desc
     *
     * @return desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置 desc
     *
     * @param desc desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @param status
     * @param mid
     * @param mkey
     * @param desc
     */
    public OrderRequestInfo(Boolean status, String mid, String mkey, String desc) {
        super();
        this.status = status;
        this.mid = mid;
        this.mkey = mkey;
        this.desc = desc;
    }

    /**
     *
     */
    public OrderRequestInfo() {
        super();
    }

    /**
     * @param status
     * @param mid
     * @param mkey
     * @param desc
     * @param data
     * @param txnToken
     */
    public OrderRequestInfo(Boolean status, String mid, String mkey, String desc, String data, String txnToken) {
        super();
        this.status = status;
        this.mid = mid;
        this.mkey = mkey;
        this.desc = desc;
        this.data = data;
        this.txnToken = txnToken;
    }
}
