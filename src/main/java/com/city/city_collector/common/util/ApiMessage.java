package com.city.city_collector.common.util;

import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.city.util.ApplicationData;

public class ApiMessage {
    private int code;

    private String msg;

    private Object data;

    public ApiMessage() {
    }

    public ApiMessage(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    /**
     * data
     *
     * @return data
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data data
     */
    public void setData(Object data) {
        this.data = data;
    }

    public static ApiMessage success(String msg, Object data) {
        Config config = ApplicationData.getInstance().getConfig();
        int code = 0;
        if (config.getCashMode().intValue() == 1) {
            code = 200;
        }
        return new ApiMessage(code, msg, data);
    }

    public static ApiMessage success(String msg) {
        return success(msg, null);
    }

    public static ApiMessage success() {
        return success("操作成功", null);
    }

    public static ApiMessage error(String msg, Object data) {
        Config config = ApplicationData.getInstance().getConfig();
        int code = 1;
        if (config.getCashMode().intValue() == 1) {
            code = -1;
        }
        return new ApiMessage(code, msg, data);
    }

    public static ApiMessage error(String msg) {
        return error(msg, null);
    }

    public static ApiMessage error() {
        return error("程序异常,操作失败", null);
    }
}
