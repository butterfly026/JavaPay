package com.city.city_collector.queryorder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.service.PayOrderService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.GroovyScript;
import com.city.city_collector.channel.bean.QueryOrderInfo;
import com.city.city_collector.channel.bean.QueryOrderResult;
import com.city.city_collector.channel.util.GroovyUtil;
import com.city.city_collector.channel.util.HttpUtil;
import groovy.lang.Binding;

public class QueryOrderThread implements Runnable {

    /***
     * 当前使用的脚本
     */
    private GroovyScript gs = null;

    /**
     * 订单service
     */
    private PayOrderService payOrderService;
    /**
     * 上游通道
     */
    private PayClientChannel payClientChannel;
    /**
     * 上游对象
     */
    private SysUser user;

    /**
     * 通道参数
     */
    private Map<String, Object> channelParams;
    /**
     * 订单对象
     **/
    private PayOrder order;

    private boolean complete = false;

    public QueryOrderThread(PayOrderService payOrderService, GroovyScript gs, PayClientChannel payClientChannel, SysUser user,
                            Map<String, Object> channelParams, PayOrder order) {
        this.payOrderService = payOrderService;
        this.gs = gs;
        this.payClientChannel = payClientChannel;
        this.user = user;
        this.channelParams = channelParams;
        this.order = order;
    }

    @Override
    public void run() {
        try {
            Binding binding = new Binding();
            binding.setProperty("order", order);
            binding.setProperty("clientChannel", payClientChannel);
            binding.setProperty("client", user);
            binding.setProperty("channelParams", channelParams);

            Object data = GroovyUtil.runScript(gs.getQueryOrderData(), binding);
            if (data == null) {
                QueryOrderManager.getInstance().createErrorLog("查单程序有误,查单失败");
                return;
            }
            QueryOrderInfo qoi = (QueryOrderInfo) data;
            if (!qoi.isNeedRequest()) {//不需要发起请求

                if (qoi.isOrderSuccess()) {//订单支付成功
                    order.setPayTime(new Date());
                    order.setClientSn(qoi.getClientSn());
                    order.setOrderStatus(1);

                    payOrderService.updateOrderPayStatus(order);
                    QueryOrderManager.getInstance().addSuccess();
                } else {
                    QueryOrderManager.getInstance().addFail();
                }
            } else {

                QueryOrderResult qos = HttpUtil.requestData_QueryOrder(user.getUrlquery(), qoi.getRequestType(), qoi.getParams(), new HashMap<String, String>(), qoi.getCharset(),
                        qoi.getReqType(), gs.getDealQueryOrderData(), order, payClientChannel, user, channelParams);
                if (qos.isStatus()) {//订单支付成功
                    order.setPayTime(new Date());
                    order.setClientSn(qoi.getClientSn());
                    order.setOrderStatus(1);

                    payOrderService.updateOrderPayStatus(order);
                    QueryOrderManager.getInstance().addSuccess();
                } else {
                    if (qos.getMsg() != null && qos.getMsg().startsWith("E-")) {
                        QueryOrderManager.getInstance().createErrorLog(order.getSn() + ">" + qos.getMsg());
                    }
                    QueryOrderManager.getInstance().addFail();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            QueryOrderManager.getInstance().createErrorLog(order.getSn() + ">" + "查单失败:" + e.getMessage());
            QueryOrderManager.getInstance().addFail();
        } finally {
            complete = true;
            QueryOrderManager.getInstance().updateThreadPool();
        }
    }

    /**
     * 是否完成
     *
     * @return
     */
    public boolean isComplete() {
        return this.complete;
    }
}
