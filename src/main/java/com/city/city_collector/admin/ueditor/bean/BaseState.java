package com.city.city_collector.admin.ueditor.bean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BaseState implements State {
    private boolean state = false;

    private String info = null;

    private Map<String, String> infoMap = new HashMap();

    public BaseState() {
        this.state = true;
    }

    public BaseState(boolean state) {
        setState(state);
    }

    public BaseState(boolean state, String info) {
        setState(state);
        this.info = info;
    }

    public BaseState(boolean state, int infoCode) {
        setState(state);
        this.info = AppInfo.getStateInfo(infoCode);
    }

    public boolean isSuccess() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setInfo(int infoCode) {
        this.info = AppInfo.getStateInfo(infoCode);
    }

    public String toJSONString() {
        return toString();
    }

    public String toString() {
        String key = null;
        String stateVal = isSuccess() ? AppInfo.getStateInfo(0) : this.info;

        StringBuilder builder = new StringBuilder();

        builder.append(new StringBuilder().append("{\"state\": \"").append(stateVal).append("\"").toString());

        Iterator iterator = this.infoMap.keySet().iterator();

        while (iterator.hasNext()) {
            key = (String) iterator.next();

            builder.append(new StringBuilder().append(",\"").append(key).append("\": \"")
                    .append((String) this.infoMap.get(key)).append("\"").toString());
        }

        builder.append("}");

        return Encoder.toUnicode(builder.toString());
    }

    public void putInfo(String name, String val) {
        this.infoMap.put(name, val);
    }

    public void putInfo(String name, long val) {
        putInfo(name, new StringBuilder().append(val).append("").toString());
    }
}
