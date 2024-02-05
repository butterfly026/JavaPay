package com.city.city_collector.admin.pay.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.pay.entity.PayInComeOrder;
import com.city.city_collector.admin.pay.entity.PayRateStatistics;
import org.apache.ibatis.annotations.Param;

import com.city.city_collector.admin.pay.entity.PayOrder;

/**
 * Description:支付订单-Mapper接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayOrderDao {
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
    public void addSave(PayOrder payOrder);


    public void updateOrderStatus(PayOrder payOrder);

    public void updateOrderPlat(PayOrder payOrder);

    public void updateOrderSuccess(PayOrder payOrder);
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
     * 查询支持查单的上游渠道
     *
     * @return
     */
    public List<Map<String, Object>> queryClientModelList();

    /**
     * Description:更新记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayOrder payOrder);

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
     * 更新订单状态
     *
     * @param payOrder
     * @author:nb
     */
    public void updateOrderPayStatus(PayOrder payOrder);

    /**
     * 查询已成功且通知状态为未通知的订单
     *
     * @return
     * @author:nb
     */
    public List<PayOrder> querySuccessOrder();

    public void dealSuccessOrder(PayOrder payOrder);

    /**
     * 查询需要通知的订单
     *
     * @return
     * @author:nb
     */
    public List<PayOrder> queryNeedNotifyOrder(Map<String, Object> params);

    /**
     * 更新订单通知消息
     *
     * @param payOrder
     * @author:nb
     */
    public void updateNotifyOrder(PayOrder payOrder);

    /**
     * 查询订单详情 - 未支付
     *
     * @param params
     * @return
     * @author:nb
     */
    public Map<String, Object> queryOrderDetailNoPay(Map<String, Object> params);

    public Map<String, Object> queryOrderDetailSuccess(Map<String, Object> params);

    public void mendOrderStatus(PayOrder po);

    public PayOrder queryMendOrder(Map<String, Object> params);

    public PayOrder queryOrderInfo(Map<String, Object> params);

    public List<Map<String, Object>> queryExportList(Map<String, Object> params);

    public List<Map<String, Object>> queryExportListMerchant(Map<String, Object> params);

    public Map<String, Object> queryTotalAdmin(Map<String, Object> params);

    public Map<String, Object> queryTotalMerchant(Map<String, Object> params);

    public void updateOrderMendStatus(PayOrder po);

    //获取分页ID
    public List<Long> queryPageIds(Map<String, Object> params);

    //获取分页数据
    public List<Map<String, Object>> queryPageNew(Map<String, Object> params);

    /**
     * 查询需要查单的订单列表
     **/
    public List<PayOrder> queryNeedQueryOrdersList(Map<String, Object> params);

    //获取需要处理的订单ID
    public List<Long> querySuccessOrderIdsNew(Map<String, Object> params);

    //获取订单数据
    public List<PayOrder> querySuccessOrderListNew(Map<String, Object> params);

    public void batchNotifyMerchant(Map<String, Object> map);

    /**
     * 最新的订单处理程序
     *
     * @param orderId
     * @param merchantId
     * @param proxyId
     * @param clientId
     * @param orderMoney
     * @param orderSn
     * @param merchantMoney
     * @param merchantRatio
     * @param merchantCommission
     * @param proxyMoney
     * @param proxyRatio
     * @param proxyCommission
     * @param clientMoney
     * @param clientRatio
     * @param clientCommission
     * @param profit
     * @param xnprofit
     * @param otherId
     * @param otherMoney
     * @param pmrSn
     * @param pprSn
     * @param pcbSn
     */
    public void updateOrderInfoByProcedure(
            @Param("orderId") Long orderId, @Param("merchantId") Long merchantId, @Param("proxyId") Long proxyId, @Param("clientId") Long clientId,
            @Param("orderMoney") BigDecimal orderMoney, @Param("orderSn") String orderSn,
            @Param("merchantMoney") BigDecimal merchantMoney, @Param("merchantRatio") BigDecimal merchantRatio, @Param("merchantCommission") BigDecimal merchantCommission,
            @Param("proxyMoney") BigDecimal proxyMoney, @Param("proxyRatio") BigDecimal proxyRatio, @Param("proxyCommission") BigDecimal proxyCommission,
            @Param("clientMoney") BigDecimal clientMoney, @Param("clientRatio") BigDecimal clientRatio, @Param("clientCommission") BigDecimal clientCommission,
            @Param("profit") BigDecimal profit, @Param("xnprofit") BigDecimal xnprofit, @Param("otherId") Long otherId, @Param("otherMoney") BigDecimal otherMoney,
            @Param("pmrSn") String pmrSn, @Param("pprSn") String pprSn, @Param("pcbSn") String pcbSn
    );

    //指定时间内的 商户 渠道  金额  入单信息
    List<PayInComeOrder> selectInComeOrderStatistics(@Param("begin") Date begin, @Param("end") Date end, @Param("channelId") String channelName, @Param("merId") String merName, @Param("amount") BigDecimal amount);

    //指定时间内 商户 渠道 金额 订单状态 入单信息
    List<PayRateStatistics> inComeOrderRateStatistics(@Param("begin") Date begin, @Param("end") Date end, @Param("channelId") String channelName, @Param("merId") String merName, @Param("amount") BigDecimal amount, @Param("mcid") Integer mcid, @Param("tname") String tname, @Param("cname") String cname);
    

    //统计情况
    List<Map<String, Object>> queryStatistics(@Param("begin") Date begin, @Param("end") Date end);
}
