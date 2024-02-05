package com.city.city_collector.admin.ueditor.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiState implements State {
    private boolean state = false;

    private String info = null;

    private Map<String, Long> intMap = new HashMap();

    private Map<String, String> infoMap = new HashMap();

    private List<String> stateList = new ArrayList();

    public MultiState(boolean state) {
        this.state = state;
    }

    public MultiState(boolean state, String info) {
        this.state = state;
        this.info = info;
    }

    public MultiState(boolean state, int infoKey) {
        this.state = state;
        this.info = AppInfo.getStateInfo(infoKey);
    }

    public boolean isSuccess() {
        return this.state;
    }

    public void addState(State state) {
        this.stateList.add(state.toJSONString());
    }

    public void putInfo(String name, String val) {
        this.infoMap.put(name, val);
    }

    public String toJSONString() {
        String stateVal = isSuccess() ? AppInfo.getStateInfo(0) : this.info;

        StringBuilder builder = new StringBuilder();

        builder.append(new StringBuilder().append("{\"state\": \"").append(stateVal).append("\"").toString());

        Iterator iterator = this.intMap.keySet().iterator();

        while (iterator.hasNext()) {
            stateVal = (String) iterator.next();

            builder.append(new StringBuilder().append(",\"").append(stateVal).append("\": ")
                    .append(this.intMap.get(stateVal)).toString());
        }

        iterator = this.infoMap.keySet().iterator();

        while (iterator.hasNext()) {
            stateVal = (String) iterator.next();

            builder.append(new StringBuilder().append(",\"").append(stateVal).append("\": \"")
                    .append((String) this.infoMap.get(stateVal)).append("\"").toString());
        }

        builder.append(", list: [");

        iterator = this.stateList.iterator();

        while (iterator.hasNext()) {
            builder.append(new StringBuilder().append((String) iterator.next()).append(",").toString());
        }

        if (this.stateList.size() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append(" ]}");

        return Encoder.toUnicode(builder.toString());
    }

    public void putInfo(String name, long val) {
        this.intMap.put(name, Long.valueOf(val));
    }
}
