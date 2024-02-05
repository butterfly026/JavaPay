package com.city.city_collector.admin.pay.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.pay.entity.PayCash;

/**
 * Description:商户提现-Mapper接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayCashDao {
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
    public void addSave(PayCash payCash);

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    public PayCash querySingle(Map<String, Object> params);

    /**
     * Description:更新记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayCash payCash);

    /**
     * Description:删除指定记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void delete(String ids);

    /**
     * 更新提现状态
     *
     * @param payCash
     * @author:nb
     */
    public void updateCashStatus(PayCash payCash);

    public void updateMerchantClientCashSubmit(PayCash payCash);

    public void updateNotifyCash(PayCash payCash);

    public void updateCashRemark(PayCash payCash);

    public List<PayCash> querySuccessCashOrder();

    public List<PayCash> queryNeedNotifyCashOrder();

    public void updateNotifyOrder(PayCash payCash);

    public List<Map<String, Object>> queryExportList(Map<String, Object> params);

    public List<Map<String, Object>> queryExportListMerchant(Map<String, Object> params);

    public Map<String, Object> queryCashDetail(Map<String, Object> params);

    public List<Map<String, Object>> queryClientInfoByModel();

    /**
     * 查询待处理的订单数
     **/
    public int queryWaitDealCount();

    public int updateAutoModel(Map<String, Object> params);

    public Map<String, Object> queryAutoPayModule();

    public int queryCashOrderCountByMerchant(Map<String, Object> params);

}
