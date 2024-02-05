package com.city.city_collector.admin.system.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysRole;

public abstract interface SysRoleDao {
    public abstract int queryCount(Map<String, Object> paramMap);

    public abstract List<Map<String, Object>> queryPage(Map<String, Object> paramMap);

    public abstract void addSave(SysRole paramSysRole);

    public abstract SysRole querySingle(Map<String, Object> paramMap);

    public abstract void editSave(SysRole paramSysRole);

    public abstract void delete(String paramString);

    public abstract List<Map<String, Object>> queryAllRole();

    public abstract String queryRoleMenuIds(Long paramLong);

    public abstract void deleteRoleMenu(String paramString);

    public abstract void addSaveRoleMenu(List<Map<String, Long>> paramList);

    public abstract String queryRoleUserIds(Long paramLong);

    public abstract void deleteRoleUser(String paramString);

    public abstract void addSaveRoleUser(List<Map<String, Long>> paramList);
}
