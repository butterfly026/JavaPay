package com.city.adminpermission.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.city.adminpermission.bean.AdminPermissionRepertory;
import com.city.adminpermission.exception.ScannerPackageNotFoundException;
import com.city.adminpermission.scanner.AnnotationScanner;

public class AdminPermissionServlet extends HttpServlet {
    private static final long serialVersionUID = 6717696078710434794L;

    public void init(ServletConfig config)
            throws ServletException {
        System.out.println("初始化权限配置...");
        AdminPermissionRepertory apr = AdminPermissionRepertory.getInstance();
        apr.setNoPermissionUrl(config.getInitParameter("noPermissionUrl"));
        apr.setNoUserUrl(config.getInitParameter("noUserUrl"));
        apr.setScannerPackage(config.getInitParameter("scannerPackage"));

        AnnotationScanner as = new AnnotationScanner();
        System.out.println("开始扫描权限注解...");
        try {
            as.scannerMain();
        } catch (ScannerPackageNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("注解扫描完成...");
    }
}
