package com.city.city_collector.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
//import org.springframework.context.ApplicationContext;
//import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
//
//import com.apay.city_collector.service.RedisService;
//import com.city.adminpermission.bean.AdminPermissionRepertory;
import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.city.util.ApplicationData;

public class CommonInterceptor implements HandlerInterceptor {
//    RedisService redisService;

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
            throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("go web...");
        String realServerName = request.getHeader("realServerName");
        if (StringUtils.isBlank(realServerName)) {
            realServerName = request.getScheme() + "://" + request.getServerName();
            if (!"80".equals(request.getServerPort() + "")) {
                realServerName = realServerName + ":" + request.getServerPort();
            }
        }
        request.setAttribute("context", "");
        if (StringUtils.isNotBlank(request.getContextPath())) {
            String contextPath = request.getContextPath();
            if (!contextPath.startsWith("/")) {
                contextPath = "/" + contextPath;
            }
            realServerName = realServerName + contextPath;
            request.setAttribute("context", contextPath);
        }
//        System.out.println(request.getRequestURL().toString());
        System.out.println("pre：" + realServerName + "|" + request.getRequestURI() + "|" + getIpAddr(request));
        request.setAttribute("serverName", realServerName);

        Config config = ApplicationData.getInstance().getConfig();
        if (config != null && StringUtils.isNotBlank(config.getCreq())) {//需要做规则验证
//            System.out.println(realServerName+request.getRequestURI()+"---"+config.getCreq());
            if (!(realServerName + request.getRequestURI()).matches(config.getCreq())) {
                System.out.println("valid error!");
                response.sendRedirect("https://www.google.com.hk/search?safe=strict&hl=zh-CN&sxsrf=ALeKk02I0HhjkGMk69kSknLs5Q9Z-Yg6Vg%3A1599449060126&source=hp&ei=5KdVX7ytBZDi-AaQ5pSYCg&q=fuck&oq=fuck&gs_lcp=CgZwc3ktYWIQAzICCAAyAggAMgIIADICCAAyAggAMgIIADICCAAyAggAMgIIADICCABQ-QdYtg5g7RBoAHAAeACAAWaIAcoCkgEDMy4xmAEAoAEBqgEHZ3dzLXdpeg&sclient=psy-ab&ved=0ahUKEwi8r8CIjNbrAhUQMd4KHRAzBaMQ4dUDCAc&uact=5");
                return false;
            }
        }
        //静态资源通用版本号
        request.setAttribute("s_vs", 16);

        request.setAttribute("sysconfig", config);

        return true;
    }

    /**
     * 获取远程IP
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "X-Real-IP".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
