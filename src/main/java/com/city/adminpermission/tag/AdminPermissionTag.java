package com.city.adminpermission.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.bean.AdminUser;

public class AdminPermissionTag extends TagSupport {
    private static final long serialVersionUID = 7001156996601063440L;
    private String permission;

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int doStartTag()
            throws JspException {
        if (StringUtils.isBlank(this.permission)) {
            return 0;
        }
        String[] pagePermissions = this.permission.split(";");
        if ((pagePermissions == null) || (pagePermissions.length == 0)) {
            return 0;
        }

        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();

        AdminUser adminUser = AdminPermissionManager.getAdminUser(request);
        if (adminUser == null) {
            return 0;
        }

        String[] permissions = adminUser.getPermissions();
        if ((permissions == null) || (permissions.length == 0)) {
            return 0;
        }
        for (int i = 0; i < permissions.length; i++) {
            for (int j = 0; j < pagePermissions.length; j++) {
                if (pagePermissions[j].equals(permissions[i])) {
                    return 1;
                }
            }
        }
        return 0;
    }
}
