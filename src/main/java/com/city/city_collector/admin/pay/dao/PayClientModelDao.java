package com.city.city_collector.admin.pay.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.pay.entity.PayClientModel;

/**
 * Description:上游模块-Mapper接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayClientModelDao {
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
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayClientModel payClientModel);

    /**
     * Description:删除指定记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void delete(String ids);
}
