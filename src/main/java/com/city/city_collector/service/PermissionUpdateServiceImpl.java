package com.city.city_collector.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.city.adminpermission.bean.AdminUser;
import com.city.adminpermission.iface.PermissionUpdateService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.google.gson.Gson;

@Service("permissionUpdateService")
public class PermissionUpdateServiceImpl implements PermissionUpdateService {

    @Autowired
    SysUserService sysUserService;

    public String[] getUserAllPermission(AdminUser adUser) {
        String[] pms = null;
        if ((adUser == null) || (adUser.getUser() == null)) {
            return null;
        }
        SysUser sysUser = adUser.getUser();
        List<String> permissions = this.sysUserService.queryUserPermission(sysUser.getId());
        System.out.println("permissions:" + new Gson().toJson(permissions));
        if ((permissions != null) && (!permissions.isEmpty())) {
            for (int i = 0; i < permissions.size(); i++) {
                if ((null == permissions.get(i)) || ("".equals(permissions.get(i)))) {
                    permissions.remove(i);
                    i--;
                }
            }
            pms = permissions.toArray(new String[]{});
        }
        System.out.println("pms:" + new Gson().toJson(pms));
        return pms;
    }
}
