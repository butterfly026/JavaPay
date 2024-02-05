package com.city.adminpermission.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.bean.AdminPermissionRepertory;
import com.city.adminpermission.exception.PermissionValidateFailException;
import com.city.adminpermission.exception.UserNotFoundException;

public class AdminPermissionInterceptor implements HandlerInterceptor {
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
            throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ((handler instanceof HandlerMethod)) {
            HandlerMethod hMethod = (HandlerMethod) handler;
            try {
                AdminPermissionManager.permissionValidate(hMethod.getBeanType(), hMethod.getMethod(), request);
            } catch (PermissionValidateFailException e) {
                response.sendRedirect(request.getAttribute("serverName")
                        + AdminPermissionRepertory.getInstance().getNoPermissionUrl());
                return false;
            } catch (UserNotFoundException e) {
                response.sendRedirect(
                        request.getAttribute("serverName") + AdminPermissionRepertory.getInstance().getNoUserUrl());
                return false;
            }
        }

        return true;
    }
}
