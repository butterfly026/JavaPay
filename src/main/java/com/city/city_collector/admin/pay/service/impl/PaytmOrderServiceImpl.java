package com.city.city_collector.admin.pay.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.city.city_collector.admin.pay.dao.PayOrderDao;
import com.city.city_collector.admin.pay.dao.PaytmOrderDao;
import com.city.city_collector.admin.pay.entity.PayClientBalance;
import com.city.city_collector.admin.pay.entity.PayMemchantRecord;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.entity.PayProxyRecord;
import com.city.city_collector.admin.pay.entity.PaytmOrder;
import com.city.city_collector.admin.pay.service.PayClientBalanceService;
import com.city.city_collector.admin.pay.service.PayMemchantRecordService;
import com.city.city_collector.admin.pay.service.PayOrderService;
import com.city.city_collector.admin.pay.service.PayProxyRecordService;
import com.city.city_collector.admin.pay.service.PaytmOrderService;
import com.city.city_collector.admin.system.dao.SysUserDao;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.util.SignUtil;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.util.MD5Util;
import com.city.city_collector.common.util.SnUtil;
import com.city.city_collector.paylog.PayLogManager;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:支付订单-service实现类
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Service
public class PaytmOrderServiceImpl implements PaytmOrderService {

    @Autowired
    PaytmOrderDao paytmOrderDao;

    /**
     * Description:分页查询
     *
     * @param params 参数键值对象
     * @param page   分页对象
     * @param orders 排序
     * @return Page
     * @author:demo
     * @since 2020-6-29
     */
    public Page queryPage(Map<String, Object> params, Page page, String[] orders) {
        return page;
    }

    /**
     * Description:保存记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    @Override
    public void addSave(PaytmOrder paytmOrder) {
        paytmOrderDao.addSave(paytmOrder);
    }

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    @Override
    public PaytmOrder querySingle(Map<String, Object> params) {
        return paytmOrderDao.querySingle(params);
    }
}
 
 
