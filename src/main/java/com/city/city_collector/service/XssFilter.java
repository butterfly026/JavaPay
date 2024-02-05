
package com.city.city_collector.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import com.city.city_collector.admin.city.util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;


/**
 * @author nb
 * @Description:
 */
@WebFilter(filterName = "securityRequestFilter", urlPatterns = {"/*"})
public class XssFilter implements Filter {

    private static String[] filterUrl = {"/api", "/payClientModel", "/exportExcel", "/exportExcelMerchant", "/payClientChannel","/payChannelType"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private static boolean isFilterUrl(String uri) {
        for(String str:filterUrl) {
            if(uri.indexOf(str)!=-1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("进入了过滤器....");
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        if (uri == null || isFilterUrl(uri)) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(new XssAndSqlHttpServletRequestWrapper(req), response);
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 过滤json类型的
     *
     * @param builder
     * @return
     */
    @Bean
    @Primary
    public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
        //解析器
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        //注册xss解析器
        SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer");
        xssModule.addSerializer(new XssStringJsonSerializer());
        objectMapper.registerModule(xssModule);
        //返回
        return objectMapper;
    }

}
