package com.city.city_collector.configuration;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class UrlFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        System.out.println("requestURI:" + requestURI + " requestURL:" + requestURL + ">>" + httpServletRequest.getContextPath() + "   " + httpServletRequest.getServerName());
        Map<String, String[]> parameterMap = request.getParameterMap();

        Gson gson = new Gson();
        System.out.println("参数：" + gson.toJson(parameterMap));

        chain.doFilter(httpServletRequest, response);
    }

    public void destroy() {
    }
}
