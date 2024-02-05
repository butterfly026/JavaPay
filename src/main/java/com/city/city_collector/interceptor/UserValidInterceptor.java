package com.city.city_collector.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.bean.AdminPermissionRepertory;
import com.city.adminpermission.exception.PermissionValidateFailException;
import com.city.adminpermission.exception.UserNotFoundException;
import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.common.bean.SystemConfig;
import com.city.city_collector.common.util.CommonUtil;
import com.city.city_collector.common.util.SingleLoginUtil;
import com.city.city_collector.configuration.SpringUtil;
import com.city.city_collector.paylog.PayLogManager;
import com.google.gson.Gson;

public class UserValidInterceptor implements HandlerInterceptor {
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
            throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        try {
            Config config = ApplicationData.getInstance().getConfig();
            if (config != null && StringUtils.isNotBlank(config.getCadmin())) {//需要做规则验证
                String sn = request.getAttribute("serverName") == null ? "" : request.getAttribute("serverName").toString();
                if (!sn.matches(config.getCadmin())) {
                    System.out.println("valid error!");
                    response.sendRedirect("https://www.google.com.hk/search?safe=strict&hl=zh-CN&sxsrf=ALeKk02I0HhjkGMk69kSknLs5Q9Z-Yg6Vg%3A1599449060126&source=hp&ei=5KdVX7ytBZDi-AaQ5pSYCg&q=fuck&oq=fuck&gs_lcp=CgZwc3ktYWIQAzICCAAyAggAMgIIADICCAAyAggAMgIIADICCAAyAggAMgIIADICCABQ-QdYtg5g7RBoAHAAeACAAWaIAcoCkgEDMy4xmAEAoAEBqgEHZ3dzLXdpeg&sclient=psy-ab&ved=0ahUKEwi8r8CIjNbrAhUQMd4KHRAzBaMQ4dUDCAc&uact=5");
                    return false;
                }
            }

            SystemConfig sysConfig = SystemConfig.getInstance();

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);

            //录入系统日志
            //请求的方法    请求参数
            PayLogManager.getInstance().createOperationLog(sysUser.getId(), sysUser.getUsername(), "请求URL:" + request.getRequestURL().toString() + ",IP:" + CommonUtil.getIp(request), new Gson().toJson(request.getParameterMap()));

//            System.out.println("userType:"+sysUser.getType());
            if (sysUser.getType() != null && sysUser.getType() == 2) {
                SysUserService sysUserService = (SysUserService) SpringUtil.getBean("sysUserService");
                SysUser user = sysUserService.querySysUserById(sysUser.getId());
                if (StringUtils.isNotBlank(user.getAdminip())) {
                    String[] ips = user.getAdminip().trim().split(",");
                    String uip = CommonUtil.getIp(request);
                    boolean flag = false;
                    for (int i = 0; i < ips.length; i++) {
                        if (ips[i].equals(uip)) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        throw new PermissionValidateFailException("无权访问");
                    }
                }
            }

            if ((sysConfig.isSingleUser())
                    && (SingleLoginUtil.validUserLoginException(request, sysUser.getId(), sysUser.getVersion()))) {
                response.sendRedirect(request.getAttribute("serverName") + sysConfig.getSingleUserUrl());
                return false;
            }
        } catch (UserNotFoundException e) {
            response.sendRedirect(
                    request.getAttribute("serverName") + AdminPermissionRepertory.getInstance().getNoUserUrl());
            return false;
        } catch (PermissionValidateFailException e) {
            response.sendRedirect(request.getAttribute("serverName")
                    + AdminPermissionRepertory.getInstance().getNoPermissionUrl());
            return false;
        }
        return true;
    }
}
