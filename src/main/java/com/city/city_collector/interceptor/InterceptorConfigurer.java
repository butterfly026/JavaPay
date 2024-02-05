package com.city.city_collector.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.city.adminpermission.interceptor.AdminPermissionInterceptor;

@Configuration
public class InterceptorConfigurer implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePathList = new ArrayList<String>();
        excludePathList.add("/");
        excludePathList.add("/test");
        excludePathList.add("/login/**");
        excludePathList.add("/error/**");
        excludePathList.add("/admin/**");
        excludePathList.add("/css/**");
        excludePathList.add("/js/**");
        excludePathList.add("/layui/**");
        excludePathList.add("/lib/**");
        registry.addInterceptor(new CommonInterceptor()).addPathPatterns(new String[]{"/**"});
        registry.addInterceptor(new UserValidInterceptor()).addPathPatterns(new String[]{"/system/**"});
//                .excludePathPatterns(excludePathList);
        registry.addInterceptor(new AdminPermissionInterceptor()).addPathPatterns(new String[]{"/system/**"});
//                .excludePathPatterns(excludePathList);
    }
}
