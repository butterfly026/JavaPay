package com.city.city_collector.common.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PageResult implements Serializable {
    public static final int PAGE_SUCCESS = 0;

    public static final int PAGE_ERROR = 1;

    private static final long serialVersionUID = 2677854988090057211L;

    private Integer code = Integer.valueOf(0);

    private String hint;

    private Long count;

    private List<Map<String, Object>> data;

    public PageResult() {
    }

    public PageResult(Integer code, String hint, Long count, List<Map<String, Object>> data) {
        this.code = code;
        this.hint = hint;
        this.count = count;
        this.data = data;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getHint() {
        return this.hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Long getCount() {
        return this.count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Map<String, Object>> getData() {
        return this.data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
