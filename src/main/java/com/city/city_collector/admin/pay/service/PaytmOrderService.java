package com.city.city_collector.admin.pay.service;

import java.util.List;
import java.util.Map;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.entity.PaytmOrder;

/**
 * Description:支付订单-service接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PaytmOrderService {
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
    public Page queryPage(Map<String, Object> params, Page page, String[] orders);

    /**
     * Description:保存记录
     *
     * @param payOrder
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void addSave(PaytmOrder paytmOrder);

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    public PaytmOrder querySingle(Map<String, Object> params);
}
