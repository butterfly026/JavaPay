package com.city.city_collector.admin.system.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysMenu;

public abstract interface SysMenuDao {
    public abstract int queryCount(Map<String, Object> paramMap);

    public abstract List<Map<String, Object>> queryPage(Map<String, Object> paramMap);

    public abstract void addSave(SysMenu paramSysMenu);

    public abstract SysMenu querySingle(Map<String, Object> paramMap);

    public abstract void editSave(SysMenu paramSysMenu);

    public abstract List<Map<String, Object>> queryAllMenu();

    public abstract void delete(String paramString);
}
