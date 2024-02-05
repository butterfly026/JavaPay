package com.city.city_collector.admin.system.service;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysRole;
import com.city.city_collector.common.bean.Page;

public abstract interface SysRoleService {
    public abstract Page queryPage(Map<String, Object> paramMap, Page paramPage, String[] paramArrayOfString);

    public abstract void addSave(SysRole paramSysRole);

    public abstract void editSave(SysRole paramSysRole);

    public abstract void delete(Long[] paramArrayOfLong);

    public abstract SysRole querySysRoleById(Long paramLong);

    public abstract List<Map<String, Object>> queryAllRole();

    public abstract String queryRoleMenuIds(Long paramLong);

    public abstract void addSaveRoleMenu(Long paramLong, Long[] paramArrayOfLong);

    public abstract String queryRoleUserIds(Long paramLong);

    public abstract void addSaveRoleUser(Long paramLong, Long[] paramArrayOfLong);
}
