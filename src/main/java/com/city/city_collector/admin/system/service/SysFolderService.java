package com.city.city_collector.admin.system.service;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysFolder;

public abstract interface SysFolderService {
    public abstract void addSave(SysFolder paramSysFolder);

    public abstract void editSave(SysFolder paramSysFolder);

    public abstract void delete(Long[] paramArrayOfLong);

    public abstract SysFolder querySysFolderById(Long paramLong);

    public abstract List<Map<String, Object>> queryList(Map<String, Object> paramMap);
}
