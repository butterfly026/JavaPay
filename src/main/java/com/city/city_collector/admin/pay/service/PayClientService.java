package com.city.city_collector.admin.pay.service;

import java.util.List;
import java.util.Map;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.entity.PayClient;
import com.city.city_collector.admin.system.entity.SysUser;

/**
 * Description:上游管理-service接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayClientService {
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
     * @param payClient
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void addSave(PayClient payClient);

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    public PayClient querySingle(Map<String, Object> params);

    /**
     * Description:更新记录
     *
     * @param payClient
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(SysUser sysUser);

    /**
     * Description:删除指定记录
     *
     * @param ids ID数组
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void delete(Long[] ids);

    public List<SysUser> querySelectList();

    public void updatePayword(SysUser sysUser);
}
