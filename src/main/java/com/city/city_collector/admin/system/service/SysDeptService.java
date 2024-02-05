package com.city.city_collector.admin.system.service;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysDept;
import com.city.city_collector.common.bean.Page;

public abstract interface SysDeptService {
    public abstract Page queryPage(Map<String, Object> paramMap, Page paramPage);

    public abstract List<Map<String, Object>> queryAllDept();

    public abstract void addSave(SysDept paramSysDept);

    public abstract void editSave(SysDept paramSysDept);

    public abstract void delete(Long[] paramArrayOfLong);

    public abstract SysDept querySysDeptById(Long paramLong);

    public abstract void addSaveUserDept(Long paramLong, Long[] paramArrayOfLong);

    public abstract String queryDeptUserIds(Long paramLong);
}
