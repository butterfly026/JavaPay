package com.city.adminpermission.bean;

import com.city.city_collector.admin.system.entity.SysUser;

public class AdminUser {
    private int permissionVersion = 0;
    private SysUser user;
    private String[] permissions;

    public AdminUser(SysUser user, String[] permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public int getPermissionVersion() {
        return this.permissionVersion;
    }

    public void setPermissionVersion(int permissionVersion) {
        this.permissionVersion = permissionVersion;
    }

    public SysUser getUser() {
        return this.user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public String[] getPermissions() {
        return this.permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
}
