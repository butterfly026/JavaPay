package com.city.city_collector.admin.pay.entity;

import java.util.Date;

public class PayRateStatistics {

    private int amount;

    private int count;

    private int failCount;

    private int merchantId;

    private int clientChannelId;

    private int orderStatus;

    private String merchantName;

    private String merchantNickname;

    private String channelName;

    private String mcname;
    private String tname;
    private Integer mcid;


    private Date begin;

    private Date end;

    public String getMCName() {
        return mcname;
    }

    public void setMCName(String mcname) {
        this.mcname = mcname;
    }

    public String getTName() {
        return tname;
    }

    public void setTName(String tname) {
        this.tname = tname;
    }

    public Integer getMCId() {
        return mcid;
    }

    public void setMCId(Integer mcid) {
        this.mcid = mcid;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public int getClientChannelId() {
        return clientChannelId;
    }

    public void setClientChannelId(int clientChannelId) {
        this.clientChannelId = clientChannelId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getmerchantNickname() {
        return merchantNickname;
    }

    public void setmerchantNickname(String merchantNickname) {
        this.merchantNickname = merchantNickname;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
