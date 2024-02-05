package com.city.city_collector.channel.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author nb
 * @Description: paytm列表
 */
public class PaytmClient {

    /**
     * id
     */
    private Long id;
    /**
     * 编号
     */
    private String no;
    /**
     * 名称
     */
    private String name;
    /**
     * 开启时间
     */
    private String startTime;
    /**
     * 关闭时间
     */
    private String endTime;
    /**
     * 最小时间
     */
    private Long minTime;
    /**
     * 最大时间
     */
    private Long maxTime;

    /**
     * 模式：0,最大最小金额。1:固定金额
     */
    private Integer type;
    /**
     * 最小金额
     */
    private BigDecimal minMoney;
    /**
     * 最大金额
     */
    private BigDecimal maxMoney;
    /**
     * 固定金额列表
     */
    private List<BigDecimal> moneyList;

    /**
     * 商户ID
     **/
    private String mid;
    /**
     * 商户key
     */
    private String mkey;

    /**
     * 上游ID
     */
    private Long clientId;
    /**
     * 上游编号
     */
    private String clientNo;

    private Long merchantChannelId;


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
     * startTime
     *
     * @return startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 设置 startTime
     *
     * @param startTime startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * endTime
     *
     * @return endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 设置 endTime
     *
     * @param endTime endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * minTime
     *
     * @return minTime
     */
    public Long getMinTime() {
        return minTime;
    }

    /**
     * 设置 minTime
     *
     * @param minTime minTime
     */
    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }

    /**
     * maxTime
     *
     * @return maxTime
     */
    public Long getMaxTime() {
        return maxTime;
    }

    /**
     * 设置 maxTime
     *
     * @param maxTime maxTime
     */
    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
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
     * minMoney
     *
     * @return minMoney
     */
    public BigDecimal getMinMoney() {
        return minMoney;
    }

    /**
     * 设置 minMoney
     *
     * @param minMoney minMoney
     */
    public void setMinMoney(BigDecimal minMoney) {
        this.minMoney = minMoney;
    }

    /**
     * maxMoney
     *
     * @return maxMoney
     */
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }

    /**
     * 设置 maxMoney
     *
     * @param maxMoney maxMoney
     */
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }

    /**
     * moneyList
     *
     * @return moneyList
     */
    public List<BigDecimal> getMoneyList() {
        return moneyList;
    }

    /**
     * 设置 moneyList
     *
     * @param moneyList moneyList
     */
    public void setMoneyList(List<BigDecimal> moneyList) {
        this.moneyList = moneyList;
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
}
