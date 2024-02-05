package com.city.city_collector.admin.pay.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.pay.entity.PayClientChannel;

/**
 * Description:上游通道-Mapper接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayClientChannelDao {
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
    public void addSave(PayClientChannel payClientChannel);

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    public PayClientChannel querySingle(Map<String, Object> params);

    /**
     * Description:更新记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayClientChannel payClientChannel);

    /**
     * Description:删除指定记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void delete(String ids);

    public void updateStatus(Map<String, Object> params);

    public List<Map<String, Object>> querySelectList();

    public List<PayClientChannel>  querySelectListByIds(String ids);

    public List<Map<String, Object>> querySelectListTest();

    public Map<String, Object> queryDetail(Map<String, Object> params);

    public List<Map<String, Object>> queryIndexEcharts();

    // paytm
    public List<Map<String, Object>> querySelectListTestPaytm();

    public void updateAlarmStatus(Map<String, Object> params);

    /**
     * 批量更新通道状态
     *
     * @param params
     */
    public void updateChannelDataStatus(Map<String, Object> params);

    public void editYjSave(PayClientChannel payClientChannel);

    /**
     * 获取已经选择的商户ID
     *
     * @param id
     * @return
     */
    public String queryClientMerchantIds(Long id);

    /**
     * 查询商户通道列表
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> queryMerchantChannelList(Long id);

    public void deleteClientChannel(Long id);

    public void addSaveClientChannel(List<Map<String, Long>> paramList);

    public List<PayClientChannel> queryAllList(Map<String, Object> params);
}
