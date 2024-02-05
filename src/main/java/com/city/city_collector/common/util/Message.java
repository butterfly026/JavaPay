package com.city.city_collector.common.util;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = -5690781835233764327L;

    private int code;

    private String msg;

    private Object result;

    public Message() {
    }

    public Message(int code, String msg, Object result) {
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

    public Object getResult() {
        return this.result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public static Message success(String msg, Object obj) {
        return new Message(200, msg, obj);
    }

    public static Message success(String msg) {
        return success(msg, null);
    }

    public static Message success() {
        return success("操作成功", null);
    }

    public static Message error(String msg, Object obj) {
        return new Message(201, msg, obj);
    }

    public static Message error(String msg) {
        return error(msg, null);
    }

    public static Message error() {
        return error("程序异常,操作失败", null);
    }

    public static Message valfail(String msg, Object obj) {
        return new Message(202, msg, obj);
    }

    public static Message valfail(String msg) {
        return valfail(msg, null);
    }

    public static Message valfail() {
        return valfail("用户无权限", null);
    }
}