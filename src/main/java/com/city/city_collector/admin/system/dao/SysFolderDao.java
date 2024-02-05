package com.city.city_collector.admin.system.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysFolder;

public abstract interface SysFolderDao {
    public abstract int queryCount(Map<String, Object> paramMap);

    public abstract List<Map<String, Object>> queryPage(Map<String, Object> paramMap);

    public abstract void addSave(SysFolder paramSysFolder);

    public abstract SysFolder querySingle(Map<String, Object> paramMap);

    public abstract void editSave(SysFolder paramSysFolder);

    public abstract void delete(String paramString);

    public abstract List<Map<String, Object>> queryList(Map<String, Object> paramMap);
}
