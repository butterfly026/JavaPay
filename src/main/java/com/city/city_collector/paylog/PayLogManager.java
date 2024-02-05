
package com.city.city_collector.paylog;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.city.city_collector.admin.pay.entity.PayLog;

/**
 * @author nb
 * @Description:
 */
public class PayLogManager {

    /**
     * 线程池对象
     */
    private ThreadPoolExecutor executor;

    private final static PayLogManager mg = new PayLogManager();

    private PayLogManager() {
        executor = new ThreadPoolExecutor(12, 24, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }

    public static PayLogManager getInstance() {
        return mg;
    }

    /**
     * 创建支付日志
     *
     * @param type              日志类型：1，订单请求。2，订单上游回调。3，订单通知下游
     * @param userId            操作人id
     * @param userName          操作人账号
     * @param sn                关联的订单
     * @param snType            订单类型：0：订单
     * @param descript          日志描述
     * @param requestData       请求数据
     * @param responseData      返回数据
     * @param clientId          上游id
     * @param clientChannelId   上游通道id
     * @param merchantId        商户id
     * @param merchantChannelId 商户通道id
     * @author:nb
     */
    public void createPayLog(Integer type, Long userId, String userName, String sn, Integer snType, String descript
            , String requestData, String responseData, Long clientId, Long clientChannelId, Long merchantId, Long merchantChannelId) {
        PayLog payLog = new PayLog();
        payLog.setType(type);
        payLog.setUserId(userId);
        payLog.setUserName(userName);
        payLog.setSn(sn);
        payLog.setSnType(snType);
        payLog.setDescript(descript);
        payLog.setRequestData(requestData);
        payLog.setResponseData(responseData);
        payLog.setClientId(clientId);
        payLog.setClientChannelId(clientChannelId);
        payLog.setMerchantId(merchantId);
        payLog.setMerchantChannelId(merchantChannelId);
        executor.execute(new PayLogThread(payLog, 0));
    }

    public void createOrderAccessLog() {
        executor.execute(new PayLogThread(null, 1));
    }

    /**
     * 创建订单请求日志
     *
     * @param sn                关联的订单
     * @param snType            订单类型：0：订单
     * @param descript          日志描述
     * @param requestData       请求数据
     * @param responseData      返回数据
     * @param clientId          上游id
     * @param clientChannelId   上游通道id
     * @param merchantId        商户id
     * @param merchantChannelId 商户通道id
     * @author:nb
     */
    public void createPayLogByOrderRequest(String sn, String descript
            , String requestData, String responseData, Long clientId, Long clientChannelId, Long merchantId, Long merchantChannelId) {

        createPayLog(1, 1L, "admin", sn, 0, descript, requestData, responseData, clientId, clientChannelId, merchantId, merchantChannelId);
    }

    public void createPayLogByClientNotify(String sn, String descript
            , String requestData, String responseData, Long clientId, Long clientChannelId, Long merchantId, Long merchantChannelId) {
        createPayLog(2, 1L, "admin", sn, 0, descript, requestData, responseData, clientId, clientChannelId, merchantId, merchantChannelId);
    }

    public void createPayLogByMerchantNotify(String sn, String descript
            , String requestData, String responseData, Long clientId, Long clientChannelId, Long merchantId, Long merchantChannelId) {
        createPayLog(3, 1L, "admin", sn, 0, descript, requestData, responseData, clientId, clientChannelId, merchantId, merchantChannelId);
    }


    public void createPayLogByCashRequest(String sn, String descript
            , String requestData, String responseData, Long clientId, Long clientChannelId, Long merchantId, Long merchantChannelId) {

        createPayLog(4, 1L, "admin", sn, 0, descript, requestData, responseData, clientId, clientChannelId, merchantId, merchantChannelId);
    }

    public void createPayCashLogByClientNotify(String sn, String descript
            , String requestData, String responseData, Long clientId, Long clientChannelId, Long merchantId, Long merchantChannelId) {
        createPayLog(5, 1L, "admin", sn, 0, descript, requestData, responseData, clientId, clientChannelId, merchantId, merchantChannelId);
    }

    public void createPayCashLogByMerchantNotify(String sn, String descript
            , String requestData, String responseData, Long clientId, Long clientChannelId, Long merchantId, Long merchantChannelId) {
        createPayLog(6, 1L, "admin", sn, 0, descript, requestData, responseData, clientId, clientChannelId, merchantId, merchantChannelId);
    }

    /**
     * 创建登录日志
     *
     * @author:nb
     */
    public void createLoginLog(String descript, String requestData) {
        createPayLog(8, 1L, "admin", "", 2, descript, requestData, "", null, null, null, null);
    }

    /**
     * 创建操作日志
     *
     * @author:nb
     */
    public void createOperationLog(Long userId, String username, String descript, String requestData) {
        createPayLog(9, userId, username, "", 2, descript, requestData, "", null, null, null, null);
    }

    public void createLoginFailLog(String descript, String requestData) {
        createPayLog(10, 1L, "admin", "", 2, descript, requestData, "", null, null, null, null);
    }
}
