package com.city.city_collector.admin.system.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysDept;

public abstract interface SysDeptDao {
    public abstract int queryCount(Map<String, Object> paramMap);

    public abstract List<Map<String, Object>> queryPage(Map<String, Object> paramMap);

    public abstract List<Map<String, Object>> queryAllDept();

    public abstract void addSave(SysDept paramSysDept);

    public abstract SysDept querySingle(Map<String, Object> paramMap);

    public abstract void editSave(SysDept paramSysDept);

    public abstract void delete(String paramString);

    public abstract void deleteUserDept(String paramString);

    public abstract void addSaveUserDept(List<Map<String, Long>> paramList);

    public abstract String queryDeptUserIds(Long paramLong);
}
