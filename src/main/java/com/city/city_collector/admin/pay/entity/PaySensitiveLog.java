package com.city.city_collector.admin.pay.entity;

import java.util.Date;

public class PaySensitiveLog {

    private Long userId;

    private String username;

    private Date time;

    private String optType;

    private String optContent;

    private String linkContent;

    private String ip;

    public PaySensitiveLog() {
    }

    public PaySensitiveLog(Long userId, String username, Date time, String optType, String optContent, String linkContent, String ip) {
        this.userId = userId;
        this.username = username;
        this.time = time;
        this.optType = optType;
        this.optContent = optContent;
        this.linkContent = linkContent;
        this.ip = ip;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getOptContent() {
        return optContent;
    }

    public void setOptContent(String optContent) {
        this.optContent = optContent;
    }

    public String getLinkContent() {
        return linkContent;
    }

    public void setLinkContent(String linkContent) {
        this.linkContent = linkContent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
