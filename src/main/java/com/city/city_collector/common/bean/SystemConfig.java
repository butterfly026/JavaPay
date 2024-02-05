package com.city.city_collector.common.bean;

public class SystemConfig {
    private static final SystemConfig config = new SystemConfig();

    private boolean singleUser = false;

    private String singleUserUrl = "/admin/login/singleUserLogout.do";

    private boolean mergeRole = true;

    public static SystemConfig getInstance() {
        return config;
    }

    public boolean isSingleUser() {
        return this.singleUser;
    }

    public void setSingleUser(boolean singleUser) {
        this.singleUser = singleUser;
    }

    public String getSingleUserUrl() {
        return this.singleUserUrl;
    }

    public void setSingleUserUrl(String singleUserUrl) {
        this.singleUserUrl = singleUserUrl;
    }

    public boolean isMergeRole() {
        return this.mergeRole;
    }

    public void setMergeRole(boolean mergeRole) {
        this.mergeRole = mergeRole;
    }
}