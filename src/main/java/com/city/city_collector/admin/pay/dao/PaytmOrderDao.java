package com.city.city_collector.admin.pay.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.entity.PaytmOrder;

/**
 * Description:支付订单-Mapper接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PaytmOrderDao {
    /**
     * Description:查询记录总条数
     *
     * @param params 参数键值对象
     * @return int
     * @author:demo
     * @since 2020-6-29
     */
    public int queryCount(Map<String, Object> params);

    /**
     * Description:查询分页数据
     *
     * @param params 参数键值对象
     * @return List<Map>
     * @author:demo
     * @since 2020-6-29
     */
    public List<Map<String, Object>> queryPage(Map<String, Object> params);

    /***
     * Description:保存记录
     * @author:demo
     * @since 2020-6-29
     * @param params 参数键值对象
     * @return void
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
