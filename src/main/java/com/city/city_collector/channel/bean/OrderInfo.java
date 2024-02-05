
package com.city.city_collector.channel.bean;

/**
 * @author nb
 * @Description:
 */
public class OrderInfo {
    /**
     * 标志位
     */
    private Boolean flag;
    /**
     * 支付URL
     */
    private String payUrl;
    /**
     * 编号
     */
    private String sn;
    /**
     * 银行名
     */
    private String bankName;
    /**
     * 卡号
     */
    private String card;
    /**
     * 开户名
     */
    private String name;
    /**
     * 整个返回数据
     */
    private String data;
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

    private Integer gtype;

    private String merchantNo;

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
     * bankName
     *
     * @return bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置 bankName
     *
     * @param bankName bankName
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * card
     *
     * @return card
     */
    public String getCard() {
        return card;
    }

    /**
     * 设置 card
     *
     * @param card card
     */
    public void setCard(String card) {
        this.card = card;
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
     * flag
     *
     * @return flag
     */
    public Boolean getFlag() {
        return flag;
    }

    /**
     * 设置 flag
     *
     * @param flag flag
     */
    public void setFlag(Boolean flag) {
        this.flag = flag;
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
     * gtype
     *
     * @return gtype
     */
    public Integer getGtype() {
        return gtype;
    }

    /**
     * 设置 gtype
     *
     * @param gtype gtype
     */
    public void setGtype(Integer gtype) {
        this.gtype = gtype;
    }
}
