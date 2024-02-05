package com.city.city_collector.admin.pay.entity;

import java.util.Date;

public class PayInComeOrder {

    private int amount;

    private int count;

    private int merchantId;

    private int clientChannelId;

    private String merchantName;

    private String channelName;

    private Date begin;

    private Date end;

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
