package com.city.city_collector.common.util;

import java.util.Vector;

public class SnCashCacheManager {

    private Vector<String> snCache = new Vector<String>();

    private Vector<String> merchantSnCache = new Vector<String>();

    public static SnCashCacheManager mn = null;

    private SnCashCacheManager() {
    }

    public static SnCashCacheManager getInstance() {
        if (mn == null) {
            mn = new SnCashCacheManager();
        }
        return mn;
    }

    private static int count = 200;

    /**
     * 订单号是否重复
     *
     * @param sn
     * @return
     */
    public synchronized boolean isValidSn(String sn) {
        if (snCache.contains(sn)) {
            return false;
        }
        if (snCache.size() > count) {
            snCache.remove(0);
        }
        snCache.add(sn);
        return true;
    }

    /**
     * 商户订单号是否重复
     *
     * @param sn
     * @return
     */
    public synchronized boolean isValidMerchantSn(String sn) {
        if (merchantSnCache.contains(sn)) {
            return false;
        }
        if (merchantSnCache.size() > count) {
            merchantSnCache.remove(0);
        }
        merchantSnCache.add(sn);
        return true;
    }
}
