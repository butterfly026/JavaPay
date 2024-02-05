package com.city.city_collector.common.util;

import java.util.Vector;

public class PayOrderRequestCacheManager {

    private static Vector<String> snCache = new Vector<String>();

    public static PayOrderRequestCacheManager mn = null;

    private final Object lock = new Object();

    private PayOrderRequestCacheManager() {
    }

    public static PayOrderRequestCacheManager getInstance() {
        if (mn == null) {
            mn = new PayOrderRequestCacheManager();
        }
        return mn;
    }

    /**
     * 订单号是否重复
     *
     * @param sn
     * @return
     */
    public boolean exists(String sn) {
        synchronized (lock) {
            if (snCache.contains(sn)) {
                return true;
            }
            int count = 500;
            if (snCache.size() > count) {
                snCache.remove(0);
            }
            return false;
        }
    }

    public void addSn(String sn) {
        synchronized (lock) {
            snCache.add(sn);
        }
    }

    public void removeSn(String sn) {
        synchronized (lock) {
            snCache.remove(sn);
        }
    }

}
