package com.city.city_collector.admin.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.city.adminpermission.AdminPermissionManager;
import com.city.city_collector.admin.system.dao.SysRoleDao;
import com.city.city_collector.admin.system.entity.SysRole;
import com.city.city_collector.admin.system.service.SysRoleService;
import com.city.city_collector.common.bean.Page;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    SysRoleDao sysRoleMapper;

    public Page queryPage(Map<String, Object> params, Page page, String[] orders) {
        int total = this.sysRoleMapper.queryCount(params);
        page.setTotal(total);
        params.put("start", Integer.valueOf(page.getStartRow()));
        params.put("end", page.getPageSize());

        if ((orders != null) && (orders.length > 0)) {
            String orderby = "";
            for (String str : orders) {
                String[] arr1 = str.split(" ");
                if (arr1.length == 2) {
                    orderby = orderby + "`" + arr1[0] + "` " + arr1[1] + ",";
                }
            }
            if (orderby.endsWith(",")) {
                orderby = orderby.substring(0, orderby.length() - 1);
            }
            params.put("orderby", orderby);
        }

        page.setResults(this.sysRoleMapper.queryPage(params));

        return page;
    }

    public void addSave(SysRole sysRole) {
        this.sysRoleMapper.addSave(sysRole);
    }

    public void editSave(SysRole sysRole) {
        this.sysRoleMapper.editSave(sysRole);
    }

    public void delete(Long[] ids) {
        String ids_str = "";
        for (int i = 0; i < ids.length; i++) {
            ids_str = ids_str + ids[i] + ",";
        }
        if (ids_str.endsWith(",")) {
            ids_str = ids_str.substring(0, ids_str.length() - 1);
        }
        this.sysRoleMapper.delete(ids_str);
        AdminPermissionManager.updateSystemRoleVersion();
    }

    public SysRole querySysRoleById(Long id) {
        Map params = new HashMap();
        params.put("id", id);
        return this.sysRoleMapper.querySingle(params);
    }

    public List<Map<String, Object>> queryAllRole() {
        return this.sysRoleMapper.queryAllRole();
    }

    public String queryRoleMenuIds(Long roleId) {
        return this.sysRoleMapper.queryRoleMenuIds(roleId);
    }

    @Transactional
    public void addSaveRoleMenu(Long roleId, Long[] menuIds) {
        this.sysRoleMapper.deleteRoleMenu(roleId + "");
        if (menuIds != null) {
            List list = new ArrayList();

            for (int i = 0; i < menuIds.length; i++) {
                Map map = new HashMap();
                map.put("role_id", roleId);
                map.put("menu_id", menuIds[i]);
                list.add(map);

                if ((i > 0) && (i % 5000 == 0)) {
                    this.sysRoleMapper.addSaveRoleMenu(list);
                    list = new ArrayList();
                }
            }

            if (!list.isEmpty())
                this.sysRoleMapper.addSaveRoleMenu(list);
        }
        AdminPermissionManager.updateSystemRoleVersion();
    }

    public String queryRoleUserIds(Long roleId) {
        return this.sysRoleMapper.queryRoleUserIds(roleId);
    }

    @Transactional
    public void addSaveRoleUser(Long roleId, Long[] userIds) {
        this.sysRoleMapper.deleteRoleUser(roleId + "");
        if (userIds != null) {
            List list = new ArrayList();

            for (int i = 0; i < userIds.length; i++) {
                Map map = new HashMap();
                map.put("role_id", roleId);
                map.put("user_id", userIds[i]);
                list.add(map);

                if ((i > 0) && (i % 5000 == 0)) {
                    this.sysRoleMapper.addSaveRoleUser(list);
                    list = new ArrayList();
                }
            }

            if (!list.isEmpty())
                this.sysRoleMapper.addSaveRoleUser(list);
        }
        AdminPermissionManager.updateSystemRoleVersion();
    }
}
