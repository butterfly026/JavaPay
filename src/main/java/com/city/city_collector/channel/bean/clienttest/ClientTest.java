
package com.city.city_collector.channel.bean.clienttest;

import java.math.BigDecimal;
import java.util.Map;

import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.ApiInfo;
import com.city.city_collector.channel.bean.CashInfo;
import com.city.city_collector.channel.bean.NotifyInfo;
import com.city.city_collector.channel.bean.OrderInfo;
import com.city.city_collector.channel.bean.OrderPreInfo;
import com.city.city_collector.channel.bean.QueryOrderInfo;
import com.city.city_collector.channel.bean.QueryOrderResult;

public abstract class ClientTest {

    public String merchantNo;
    public String merchantMy;
    public String domain = "http://localhost/";

    public Map<String, Object> channelParams;

    public String payUrl = "";
    public String cashUrl = "";
    public String queryUrl = "";

    /**
     * 下单前置处理-生成下单参数
     *
     * @param id
     * @param merchantNo
     * @param merchantMy
     * @param channelParams
     * @param sn
     * @param amount
     * @param domain
     * @param platform
     * @return
     * @author:nb
     */
    public abstract OrderPreInfo createReqParams(Long id, String merchantNo, String merchantMy, Map<String, Object> channelParams, String sn, BigDecimal amount, String domain,Integer platform) throws Exception;

    /**
     * 处理下单数据
     *
     * @param data
     * @param clientId
     * @param clientNo
     * @param clientName
     * @param clientChannelId
     * @return
     * @author:nb
     */
    public abstract OrderInfo dealOrderData(String data, Long clientId, String clientNo, String clientName, Long clientChannelId, String url, String sn, Map<String, Object> channelParams);

    /**
     * 通知数据
     *
     * @param params
     * @param merchantNo
     * @param merchantMy
     * @param channelParams
     * @return
     * @author:nb
     */
    public abstract NotifyInfo notifyData(Map<String, Object> params, String merchantNo, String merchantMy, Map<String, Object> channelParams, String body);

    /**
     * 下单前置处理-测试-生成下单参数
     *
     * @param id
     * @param merchantNo
     * @param merchantMy
     * @param channelParams
     * @param sn
     * @param amount
     * @param domain
     * @return
     * @author:nb
     */
    public abstract ApiInfo createReqParams_Test(Long id, String merchantNo, String merchantMy, Map<String, Object> channelParams, String sn, BigDecimal amount, String domain) throws Exception;

    /**
     * 代付申请-生成下单参数
     *
     * @param cash
     * @param client
     * @param domain
     * @param urlcash
     * @param keyname
     * @return
     * @author:nb
     */
    public abstract Map<String, Object> createReqParams_Cash(PayCash cash, SysUser client, String domain, String urlcash, String keyname);

    /**
     * 下单返回数据处理
     * @param data
     * @return
     * @author:nb
     */
//    public abstract CashInfo dealOrderData(String data);

    /**
     * 下单返回数据处理
     *
     * @param data
     * @return
     * @author:nb
     */
    public abstract CashInfo dealOrderData(String data, String sn, Map<String,Object> reqParams);

    public abstract QueryOrderInfo createQueryOrder(PayOrder order, PayClientChannel clientChannel, SysUser client, Map<String, Object> channelParams);

    public abstract QueryOrderResult dealQueryResult(String data, PayOrder order, PayClientChannel clientChannel, SysUser client, Map<String, Object> channelParams);
}
