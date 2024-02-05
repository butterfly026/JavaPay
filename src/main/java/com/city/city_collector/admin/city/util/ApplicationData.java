package com.city.city_collector.admin.city.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.pay.entity.PayOrder;

import okhttp3.ConnectionPool;

/**
 * @Description:
 */
public class ApplicationData {

    private static final ApplicationData data = new ApplicationData();

    private static ConnectionPool connectionPool;

    private ApplicationData() {
        connectionPool = new ConnectionPool(40000, 10, TimeUnit.SECONDS);
    }

    public static ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static ApplicationData getInstance() {
        return data;
    }

    private Config config;

    private List<String> imgList = new ArrayList<String>();

    public void updateImgList(List<String> imgList) {
        this.imgList = imgList;
        this.imgList.add("/admin/login/bg.gif");
    }

    // 获取首页图片链接地址
    public String getIndexImgUrl() {
        try {
            if (imgList != null && !imgList.isEmpty()) {
                Random rd = new Random();
                int cu = rd.nextInt(imgList.size());

                return imgList.get(cu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/admin/login/bg.gif";
    }

    /**
     * config
     *
     * @return config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * 设置 config
     *
     * @param config config
     */
    public void setConfig(Config config) {
        this.config = config;
    }


    public boolean isNeedOutOrder(PayOrder po) {
        return false;
    }
}
