package com.city.city_collector.admin.system.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysImage;

public abstract interface SysImageDao {
    public abstract int queryCount(Map<String, Object> paramMap);

    public abstract List<Map<String, Object>> queryPage(Map<String, Object> paramMap);

    public abstract void addSave(SysImage paramSysImage);

    public abstract SysImage querySingle(Map<String, Object> paramMap);

    public abstract void editSave(SysImage paramSysImage);

    public abstract void delete(String paramString);

    public abstract void updateStatusByFolder(SysImage paramSysImage);

    public abstract void updateStatusByIds(Map<String, Object> paramMap);

    public List<String> queryIndexImgList();
}
