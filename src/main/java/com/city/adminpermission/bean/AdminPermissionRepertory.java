package com.city.adminpermission.bean;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AdminPermissionRepertory {
    private static final AdminPermissionRepertory apRepertory = new AdminPermissionRepertory();
    private String scannerPackage;
    private Map<Class<?>, Map<Method, String[]>> classAnnos = new HashMap<Class<?>, Map<Method, String[]>>();

    private String noPermissionUrl = "";

    private String noUserUrl = "";

    private int sysPermissionVersion = 0;

    public static AdminPermissionRepertory getInstance() {
        return apRepertory;
    }

    public String getNoUserUrl() {
        return this.noUserUrl;
    }

    public void setNoUserUrl(String noUserUrl) {
        this.noUserUrl = noUserUrl;
    }

    public int getSysPermissionVersion() {
        return this.sysPermissionVersion;
    }

    public void setSysPermissionVersion(int sysPermissionVersion) {
        this.sysPermissionVersion = sysPermissionVersion;
    }

    public void updateSysPermissionVersion() {
        this.sysPermissionVersion += 1;
    }

    public String getNoPermissionUrl() {
        return this.noPermissionUrl;
    }

    public void setNoPermissionUrl(String noPermissionUrl) {
        this.noPermissionUrl = noPermissionUrl;
    }

    public Map<Class<?>, Map<Method, String[]>> getClassAnnos() {
        return this.classAnnos;
    }

    public void setClassAnnos(Map<Class<?>, Map<Method, String[]>> classAnnos) {
        this.classAnnos = classAnnos;
    }

    public String getScannerPackage() {
        return this.scannerPackage;
    }

    public void setScannerPackage(String scannerPackage) {
        this.scannerPackage = scannerPackage;
    }
}
