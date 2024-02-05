package com.city.city_collector.admin.pay.service;

import java.util.List;
import java.util.Map;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.entity.PaytmOrder;

/**
 * Description:支付订单-service接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayOrderService {
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
     * @param payOrder
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void addSave(PayOrder payOrder);

    /**
     * 更新订单状态
     * @param payOrder
     */
    public void updateOrderStatus(PayOrder payOrder);

    /**
     * 更新订单状态
     * @param payOrder
     */
    public void updateOrderPlat(PayOrder payOrder);

    /**
     * 更新订单获取链接成功
     * @param payOrder
     */
    public void updateOrderSuccess(PayOrder payOrder);

    public void addSaveByPaytm(PayOrder payOrder, PaytmOrder paytmOrder);

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    public PayOrder querySingle(Map<String, Object> params);

    public PayOrder querySingle1(Map<String, Object> params);

    /**
     * Description:更新记录
     *
     * @param payOrder
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayOrder payOrder);

    /**
     * Description:删除指定记录
     *
     * @param ids ID数组
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void delete(Long[] ids);

    /**
     * 更新订单状态
     *
     * @param payOrder
     * @author:nb
     */
    public void updateOrderPayStatus(PayOrder payOrder);

//    public void orderSuccess();

    public void orderSuccessSingle(PayOrder po);

    /**
     * 查询已成功且通知状态为未通知的订单
     *
     * @return
     * @author:nb
     */
    public List<PayOrder> querySuccessOrder();

    /**
     * 查询需要通知的订单
     *
     * @return
     * @author:nb
     */
    public List<PayOrder> queryNeedNotifyOrder();

    /**
     * 发送订单通知
     *
     * @param po
     * @author:nb
     */
    public void sendOrderNotify(PayOrder po);

    /**
     * 查询订单详情 - 未支付
     *
     * @param params
     * @return
     * @author:nb
     */
    public Map<String, Object> queryOrderDetailNoPay(Long id, String sn);

    public Map<String, Object> queryOrderDetailSuccess(Long id, String sn);

//    public void mendOrderStatus(PayOrder po);

    public PayOrder queryMendOrder(Map<String, Object> params);

    public PayOrder queryOrderInfo(Map<String, Object> params);

    public List<Map<String, Object>> queryExportList(Map<String, Object> params, String[] orders);

    public List<Map<String, Object>> queryExportListMerchant(Map<String, Object> params, String[] orders);


    public Map<String, Object> queryTotalAdmin(Map<String, Object> params);

    public Map<String, Object> queryTotalMerchant(Map<String, Object> params);

    public void updateOrderMendStatus(PayOrder po);

    /**
     * 查询支持查单的上游渠道
     *
     * @return
     */
    public List<Map<String, Object>> queryClientModelList();

    /**
     * 查询需要查单的订单列表
     **/
    public List<PayOrder> queryNeedQueryOrdersList(Map<String, Object> params);

    public void batchNotifyMerchant(Map<String, Object> map);

    public void orderSuccess(int i);
//    //获取分页ID
//    public List<Long> queryPageIds(Map<String,Object> params);
//    //获取分页数据
//    public List<Map<String,Object>> queryPageNew(String ids);
}
