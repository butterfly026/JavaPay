package com.city.city_collector.admin.pay.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.entity.PayMerchantChannel;

/**
 * Description:商户通道-service接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayMerchantChannelService {
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
     * @param payMerchantChannel
     * @return void
     * @author:demo
     * @since 2020-6-29
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

    /**
     * Description:更新记录
     *
     * @param payMerchantChannel
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayMerchantChannel payMerchantChannel);

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

    public String queryMerchantClientIds(Long id);

    public List<Map<String, Object>> queryClientChannelList(Long id);

    //    public void addSaveClientChannel(Long id, Long[] channelIds);
    public void addSaveClientChannel(Long id, List<Map<String, Long>> list);

    public PayMerchantChannel querySingleByMerchantId(Map<String, Object> params);

    public List<Map<String, Object>> querySelectList();


    public void updateChannelData(Long[] ids, Integer status);


    List<PayMerchantChannel> queryPayMerchantByIds(String ids);
}
