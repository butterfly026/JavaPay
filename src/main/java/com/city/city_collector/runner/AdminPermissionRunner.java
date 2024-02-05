package com.city.city_collector.runner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.city.adminpermission.bean.AdminPermissionRepertory;
import com.city.adminpermission.exception.ScannerPackageNotFoundException;
import com.city.adminpermission.scanner.AnnotationScanner;
import com.city.city_collector.admin.city.service.ConfigService;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayClient;
import com.city.city_collector.admin.pay.entity.PayClientModel;
import com.city.city_collector.admin.pay.service.PayClientModelService;
import com.city.city_collector.admin.pay.service.PayClientService;
import com.city.city_collector.admin.system.service.SysImageService;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.util.GroovyUtil;
import com.city.city_collector.common.util.OrderDealThread;
import com.city.city_collector.configuration.SpringAnnotationScanner;

@Component
public class AdminPermissionRunner implements ApplicationRunner {

    @Value("${permission.noPermissionUrl}")
    String noPermissionUrl;

    @Value("${permission.noUserUrl}")
    String noUserUrl;

    @Value("${permission.scannerPkgName}")
    String scannerPkgName;

    @Autowired
    ConfigService configService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    PayClientModelService payClientModelService;

    @Autowired
    SysImageService sysImageService;

    public void run(ApplicationArguments args) throws Exception {
        AdminPermissionRepertory apr = AdminPermissionRepertory.getInstance();
        apr.setNoPermissionUrl(this.noPermissionUrl);
        apr.setNoUserUrl(this.noUserUrl);
        apr.setScannerPackage(this.scannerPkgName);

//        AnnotationScanner as = new AnnotationScanner();
//        System.out.println("开始扫描权限注解...");
//        try {
//            as.scannerMain();
//        }
//        catch (ScannerPackageNotFoundException e) {
//            e.printStackTrace();
//        }
        System.out.println("开始扫描注解");
        new SpringAnnotationScanner().scannerMain();
        System.out.println("注解扫描完成...");

        //加载系统配置
        ApplicationData.getInstance().setConfig(configService.querySingle(new HashMap<String, Object>()));

        //初始化通道数据
        sysUserService.updateChannelData();

        //初始化脚本
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", 1);
        List<PayClientModel> modelList = payClientModelService.queryList(params);
        try {
            GroovyUtil.initScripts(modelList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("加载首页图片");
        try {
            ApplicationData.getInstance().updateImgList(sysImageService.queryIndexImgList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(new OrderDealThread()).start();
        System.out.println("容器启动：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
