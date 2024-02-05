package com.city.city_collector.admin.system.service;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysMenu;
import com.city.city_collector.common.bean.Page;

public abstract interface SysMenuService {
    public abstract Page queryPage(Map<String, Object> paramMap, Page paramPage);

    public abstract void addSave(SysMenu paramSysMenu);

    public abstract void editSave(SysMenu paramSysMenu);

    public abstract List<Map<String, Object>> queryAllMenu();

    public abstract void delete(Long[] paramArrayOfLong);

    public abstract SysMenu querySysMenuById(Long paramLong);
}
