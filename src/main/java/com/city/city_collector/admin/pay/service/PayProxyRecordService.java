package com.city.city_collector.admin.pay.service;

import java.util.Map;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.entity.PayMemchantRecord;
import com.city.city_collector.admin.pay.entity.PayProxyRecord;
import com.city.city_collector.admin.system.entity.SysUser;

/**
 * Description:代理流水-service接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayProxyRecordService {
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
     * @param payProxyRecord
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void addSave(PayProxyRecord payProxyRecord);

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    public PayProxyRecord querySingle(Map<String, Object> params);

    /**
     * Description:更新记录
     *
     * @param payProxyRecord
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayProxyRecord payProxyRecord);

    /**
     * Description:删除指定记录
     *
     * @param ids ID数组
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void delete(Long[] ids);

    public void updateProxyMoney(PayProxyRecord ppr, SysUser proxy);

    public void updateProxyMoney_add(PayProxyRecord ppr, SysUser proxy);
}
