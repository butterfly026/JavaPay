package com.city.city_collector.admin.pay.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.pay.entity.PayMerchantChannel;
import com.city.city_collector.admin.system.entity.SysUser;

/**
 * Description:商户通道-Mapper接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayMerchantChannelDao {
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
    public void addSave(PayMerchantChannel payMerchantChannel);

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    public PayMerchantChannel querySingle(Map<String, Object> params);

    public PayMerchantChannel querySingleByMerchantId(Map<String, Object> params);

    /**
     * Description:更新记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayMerchantChannel payMerchantChannel);

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

    public String queryMerchantClientIds(Long id);

    public List<Map<String, Object>> queryClientChannelList(Long id);

    public void deleteMerchantChannel(Long id);

    public void addSaveMerchantChannel(List<Map<String, Long>> paramList);

    public List<Map<String, Object>> querySelectList();

    /**
     * 批量更新通道状态
     *
     * @param params
     */
    public void updateChannelDataStatus(Map<String, Object> params);

    List<PayMerchantChannel> queryPayMerchantChannelByIds(String ids);
}
