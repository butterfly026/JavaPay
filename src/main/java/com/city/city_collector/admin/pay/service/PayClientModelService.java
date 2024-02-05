package com.city.city_collector.admin.pay.service;

import java.util.List;
import java.util.Map;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.entity.PayClientModel;

/**
 * Description:上游模块-service接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayClientModelService {
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
     * @param payClientModel
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void addSave(PayClientModel payClientModel);

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    public PayClientModel querySingle(Map<String, Object> params);

    public PayClientModel queryModelExists(Map<String, Object> params);

    public PayClientModel queryModelExistsEdit(Map<String, Object> params);

    public List<PayClientModel> queryList(Map<String, Object> params);

    public List<PayClientModel> queryListNoJson(Map<String, Object> params);

    /**
     * Description:更新记录
     *
     * @param payClientModel
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayClientModel payClientModel);

    /**
     * Description:删除指定记录
     *
     * @param ids ID数组
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void delete(Long[] ids);
}
