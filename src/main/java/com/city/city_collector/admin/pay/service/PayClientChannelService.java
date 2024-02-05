package com.city.city_collector.admin.pay.service;

import java.util.List;
import java.util.Map;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.entity.PayClientChannel;

/**
 * Description:上游通道-service接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayClientChannelService {
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
     * @param payClientChannel
     * @return void
     * @author:demo
     * @since 2020-6-29
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
     * @param payClientChannel
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayClientChannel payClientChannel);

    /**
     * Description:删除指定记录
     *
     * @param ids ID数组
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void delete(Long[] ids);

    public void updateStatus(Long id, Integer status);

    public List<Map<String, Object>> querySelectList();

    List<PayClientChannel>  querySelectListByIds(String ids);

    public List<Map<String, Object>> querySelectListTest();

    /**
     * 查询通道详情
     *
     * @param params
     * @return
     * @author:nb
     */
    public Map<String, Object> queryDetail(Map<String, Object> params);

    public List<Map<String, Object>> queryIndexEcharts();

    public List<Map<String, Object>> querySelectListTestPaytm();

    public void updateAlarmStatus(Long id, Integer status);

    public void updateChannelData(Long[] ids, Integer status);

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

    public void addSaveMerchantChannel(Long id, List<Map<String, Long>> dataList);

    public List<PayClientChannel> queryAllList(Map<String, Object> params);
}
