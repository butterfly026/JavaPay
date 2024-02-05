package com.city.adminpermission.iface;

import com.city.adminpermission.bean.AdminUser;

public abstract interface PermissionUpdateService {
    public abstract String[] getUserAllPermission(AdminUser paramAdminUser);
}
