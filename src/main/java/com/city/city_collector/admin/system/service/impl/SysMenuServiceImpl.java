package com.city.city_collector.admin.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.city.adminpermission.AdminPermissionManager;
import com.city.city_collector.admin.system.dao.SysMenuDao;
import com.city.city_collector.admin.system.entity.SysMenu;
import com.city.city_collector.admin.system.service.SysMenuService;
import com.city.city_collector.common.bean.Page;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    SysMenuDao sysMenuMapper;

    public Page queryPage(Map<String, Object> params, Page page) {
        return page;
    }

    public void addSave(SysMenu sysMenu) {
        this.sysMenuMapper.addSave(sysMenu);
    }

    public SysMenu querySysMenuById(Long id) {
        Map params = new HashMap();
        params.put("id", id);
        return this.sysMenuMapper.querySingle(params);
    }

    public void editSave(SysMenu sysMenu) {
        this.sysMenuMapper.editSave(sysMenu);
    }

    public List<Map<String, Object>> queryAllMenu() {
        return this.sysMenuMapper.queryAllMenu();
    }

    public void delete(Long[] ids) {
        String ids_str = "";
        for (int i = 0; i < ids.length; i++) {
            ids_str = ids_str + ids[i] + ",";
        }
        if (ids_str.endsWith(",")) {
            ids_str = ids_str.substring(0, ids_str.length() - 1);
        }

        this.sysMenuMapper.delete(ids_str);
        AdminPermissionManager.updateSystemRoleVersion();
    }
}
