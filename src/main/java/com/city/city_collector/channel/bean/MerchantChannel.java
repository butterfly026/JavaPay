
package com.city.city_collector.channel.bean;

import java.util.Vector;

/**
 * @author nb
 * @Description:
 */
public class MerchantChannel {
    /**
     * 商户通道ID
     */
    private Long id;
    /**
     * 商户编号
     */
    private String no;

    /**
     * 通道名
     */
    private String name;
    /**
     * 支付类型
     */
    private Long payType;

    /**
     * 商户对象
     */
    private Merchant merchant;

    /**
     * 上游通道列表
     */
    private Vector<ClientChannel> clientChannels;

    /**
     * id
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * no
     *
     * @return no
     */
    public String getNo() {
        return no;
    }

    /**
     * 设置 no
     *
     * @param no no
     */
    public void setNo(String no) {
        this.no = no;
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
     * payType
     *
     * @return payType
     */
    public Long getPayType() {
        return payType;
    }

    /**
     * 设置 payType
     *
     * @param payType payType
     */
    public void setPayType(Long payType) {
        this.payType = payType;
    }

    /**
     * merchant
     *
     * @return merchant
     */
    public Merchant getMerchant() {
        return merchant;
    }

    /**
     * 设置 merchant
     *
     * @param merchant merchant
     */
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    /**
     * clientChannels
     *
     * @return clientChannels
     */
    public Vector<ClientChannel> getClientChannels() {
        return clientChannels;
    }

    /**
     * 设置 clientChannels
     *
     * @param clientChannels clientChannels
     */
    public void setClientChannels(Vector<ClientChannel> clientChannels) {
        this.clientChannels = clientChannels;
    }
}
