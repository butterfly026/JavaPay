package com.city.city_collector.common.util;

import java.util.HashMap;
import java.util.Map;

public class AppMessage {
    private int code;

    private String msg;

    private Map<String, Object> result;

    public AppMessage() {
    }

    public AppMessage(int code, String msg, Map<String, Object> result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getResult() {
        return this.result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public static AppMessage success(String msg, Map<String, Object> map) {
        return new AppMessage(200, msg, map);
    }

    public static AppMessage success(String msg) {
        return success(msg, new HashMap());
    }

    public static AppMessage success() {
        return success("操作成功", new HashMap());
    }

    public static AppMessage error(String msg, Map<String, Object> map) {
        return new AppMessage(202, msg, map);
    }

    public static AppMessage error(String msg) {
        return error(msg, new HashMap());
    }

    public static AppMessage error() {
        return error("程序错误,操作失败", new HashMap());
    }

    public static AppMessage valfail(String msg, Map<String, Object> map) {
        return new AppMessage(201, msg, map);
    }

    public static AppMessage valfail(String msg) {
        return valfail(msg, new HashMap());
    }

    public static AppMessage valfail() {
        return valfail("用户识别失败", new HashMap());
    }
}
