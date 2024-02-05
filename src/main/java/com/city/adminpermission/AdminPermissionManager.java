package com.city.adminpermission;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.city.adminpermission.bean.AdminPermissionRepertory;
import com.city.adminpermission.bean.AdminUser;
import com.city.adminpermission.exception.PermissionValidateFailException;
import com.city.adminpermission.exception.UserNotFoundException;
import com.city.adminpermission.iface.PermissionUpdateService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.google.gson.Gson;
import com.city.adminpermission.bean.Constants;

public class AdminPermissionManager {

    /**
     * Description: 添加用户以及权限信息
     *
     * @param user    用户自定义对象
     * @param roles   用户权限标识
     * @param request request对象
     * @return void
     * @throws UserNotFoundException 用户不存在或已失效异常
     * @author:nb
     * @since 2017-12-5
     */
    public static AdminUser putAdminUser(SysUser user, String[] permissions, HttpServletRequest request) throws UserNotFoundException {
        if (user == null) {
            throw new UserNotFoundException("用户不存在或已失效");
        }

        List<String> nPermissions = new ArrayList<String>();
        //去除null、空值
        if (permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                if (StringUtils.isNotBlank(permissions[i])) {
                    nPermissions.add(permissions[i]);
                }
            }
        }
        permissions = nPermissions.toArray(new String[]{});

        AdminUser adminUser = new AdminUser(user, permissions);

        //设置系统权限版本
        adminUser.setPermissionVersion(AdminPermissionRepertory.getInstance().getSysPermissionVersion());
        request.getSession().setAttribute(Constants.SESSION_NAME, adminUser);
        request.getSession().setMaxInactiveInterval(3600);//以秒为单位，即在没有活动120分钟后，session将失效
        return adminUser;
    }

    /**
     * Description:获取存储的用户信息
     *
     * @param request
     * @return Object
     * @throws UserNotFoundException
     * @author:nb
     * @since 2017-12-7
     */
    public static Object getUserObject(HttpServletRequest request) throws UserNotFoundException {
        AdminUser aduser = (AdminUser) (request.getSession().getAttribute(Constants.SESSION_NAME));
        if (aduser == null) {//用户不存在或已失效
            throw new UserNotFoundException("用户不存在或已失效");
        }
        return aduser.getUser();
    }

    /**
     * Description: 获取系统保存的AdminUser对象
     *
     * @param request
     * @return AdminUser
     * @author:nb
     * @since 2017-12-8
     */
    public static AdminUser getAdminUser(HttpServletRequest request) {
        return (AdminUser) (request.getSession().getAttribute(Constants.SESSION_NAME));
    }

    /**
     * Description: 移除AdminUser对象
     *
     * @param request
     * @return void
     * @author:nb
     * @since 2017-12-10
     */
    public static void removeAdminUser(HttpServletRequest request) {
        request.getSession().removeAttribute(Constants.SESSION_NAME);
    }

    /**
     * Description:更新用户的权限信息
     *
     * @param permissions
     * @param request
     * @return void
     * @throws UserNotFoundException 用户不存在或已失效异常
     * @author:nb
     * @since 2017-12-6
     */
    public static void updatePermission(String[] permissions, HttpServletRequest request) throws UserNotFoundException {
        AdminUser aduser = (AdminUser) (request.getSession().getAttribute(Constants.SESSION_NAME));
        if (aduser == null) {//用户不存在或已失效
            throw new UserNotFoundException("用户不存在或已失效");
        }

        List<String> nPermissions = new ArrayList<String>();
        //去除null、空值
        if (permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                if (StringUtils.isNotBlank(permissions[i])) {
                    nPermissions.add(permissions[i]);
                }
            }
        }
        permissions = nPermissions.toArray(new String[]{});

        aduser.setPermissions(permissions);
    }

    /***
     * Description:更新系统权限版本，调用后版本号+1
     * @author:nb
     * @since 2017-12-5
     * @return void
     */
    public static void updateSystemRoleVersion() {
        AdminPermissionRepertory.getInstance().updateSysPermissionVersion();
    }

    /**
     * Description:权限验证
     *
     * @param cls      访问的类
     * @param method   访问的方法
     * @param request  request
     * @param response response
     * @return void
     * @throws PermissionValidateFailException 权限验证失败异常
     * @throws UserNotFoundException           用户不存在或已失效异常
     * @author:nb
     * @since 2017-12-5
     */
    public static boolean permissionValidate(Class<?> cls, Method method, HttpServletRequest request) throws PermissionValidateFailException, UserNotFoundException {
        AdminUser aduser = (AdminUser) (request.getSession().getAttribute(Constants.SESSION_NAME));
        if (aduser == null || aduser.getUser() == null) {//用户不存在或已失效
            throw new UserNotFoundException("用户不存在或已失效");
        }
        //如果用户权限版本与系统权限版本不一致，重新获取用户权限
        if (aduser.getPermissionVersion() != AdminPermissionRepertory.getInstance().getSysPermissionVersion()) {
            ApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
            PermissionUpdateService permissionUpdateService = (PermissionUpdateService) app.getBean(Constants.PERMISSIONUPDATESERVICE);
            if (permissionUpdateService != null) {//如果有注入接口
                aduser = putAdminUser(aduser.getUser(), permissionUpdateService.getUserAllPermission(aduser), request);
            }
        }

        if (cls == null || method == null) {
            return true;
        }

        AdminPermissionRepertory apr = AdminPermissionRepertory.getInstance();
        Map<Class<?>, Map<Method, String[]>> annos = apr.getClassAnnos();
        if (annos.get(cls) == null) {//如果这个类没有权限限制
            return true;
        }

        Map<Method, String[]> methods = annos.get(cls);
        String[] permissions = methods.get(method);//方法包含的权限字符串
        if (permissions == null || permissions.length == 0) {//如果这个方法没有权限控制
            return true;
        }

        //获取用户权限
        String[] userPermissions = aduser.getPermissions();
        if (userPermissions == null || userPermissions.length == 0) {//用户没有权限
            throw new PermissionValidateFailException("权限验证失败!");
        }
        //权限比较
        for (int i = 0; i < userPermissions.length; i++) {
            for (int j = 0; j < permissions.length; j++) {
                if (userPermissions[i].equals(permissions[j])) {//如果存在相同的权限
                    return true;
                }
            }
        }
        throw new PermissionValidateFailException("权限验证失败!");
    }

    /**
     * Description: 获取版本号
     *
     * @return String
     * @author:nb
     * @since 2017-12-8
     */
    public String getVersion() {
        return Constants.VERSION;
    }
}
