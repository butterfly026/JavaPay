
package com.city.city_collector.channel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.city.city_collector.channel.bean.ClientChannel;
import com.city.city_collector.channel.bean.MerchantChannel;
import com.city.city_collector.channel.bean.OrderInfo;
import com.city.city_collector.paylog.PayLogManager;
import com.google.gson.Gson;

/**
 * @author nb
 * @Description: 通道管理类
 */
public class ChannelManager {

    private final static ChannelManager cm = new ChannelManager();

    private ChannelManager() {
    }

    /**
     * 获取管理对象实例
     *
     * @return
     * @author:nb
     */
    public static ChannelManager getInstance() {
        return cm;
    }

    public static final Map<String, ClientChannel> channels = new HashMap<String, ClientChannel>();

    static {
//        channels.put("Akpay", new AkPayClientChannel());
//        channels.put("Alibank99", new Alibank99ClientChannel());
//        channels.put("MiDian", new MiDianClientChannel());
//        channels.put("Alibank55", new Alibank55ClientChannel());
//        channels.put("HongXing", new HongXingClientChannel());
//        channels.put("MingXing", new MingXingClientChannel());
//        channels.put("TianMa", new TianMaClientChannel());
//        channels.put("SuYun", new SuYunClientChannel());
//        channels.put("YouFu", new YouFuClientChannel());
//        channels.put("LeapGerry", new YouFuClientChannel());
    }

//    /** 上游通道列表 */
//    private List < ClientChannel > clientChannelList;

//    /** 商户通道列表 */
//    private List<MerchantChannel> merchantChannelList;
    /**
     * 商户通道
     **/
    private Map<String, MerchantChannel> merchantMap = new HashMap<String, MerchantChannel>();

    /**
     * 更新通道数据
     *
     * @param merchantChannelList
     * @author:nb
     */
    public void updateChannelData(List<MerchantChannel> merchantChannelList) {
//        this.clientChannelList=clientChannelList;
//        this.merchantChannelList=merchantChannelList;

        merchantMap = new HashMap<String, MerchantChannel>();

        for (MerchantChannel mc : merchantChannelList) {
            merchantMap.put(mc.getNo() + "_" + mc.getPayType(), mc);
        }

    }
/*
    public OrderInfo createPayOrder(String merchantNo, Long payType, String sn, BigDecimal amount, String domain, Long merchantId) {
        MerchantChannel mc = merchantMap.get(merchantNo + "_" + payType);
        if (mc == null) {//商户通道已关闭
            return null;
        }
        // 上游列表
        List<ClientChannel> clientChannels = mc.getClientChannels();

        //获取符合条件的上游列表
        for (int i = 0; i < clientChannels.size(); i++) {
            ClientChannel cc = clientChannels.get(i);
            //判断通道是否开启
            if (cc.getMinTime() != null && cc.getMaxTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                Long time = Long.parseLong(sdf.format(new Date()));
                if (time < cc.getMinTime().longValue() || time > cc.getMaxTime().longValue()) {//不在开启时间段内
                    continue;
                }
            }

            Integer type = cc.getType();
            //通道模式
            if (type == null || type.intValue() == 0) {
                if (cc.getMinMoney() != null && cc.getMaxMoney() != null) {
                    if (amount.compareTo(cc.getMinMoney()) < 0 || amount.compareTo(cc.getMaxMoney()) > 0) {
                        continue;
                    }
                }
                //下单
//                OrderInfo oi=null;
//                if(cc.getGtype()!=null && cc.getGtype().intValue()==1) {//表单模式
//
//                }else {
//
//                }

                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "向" + cc.getName() + " 通道发起下单请求", "", "", null, cc.getId(), merchantId, null);

                OrderInfo oi = cc.createOrder(sn, amount, domain, payType, merchantId);

                if (oi == null || !oi.getFlag()) {
                    continue;
                } else {//下单成功
                    oi.setMerchantChannelId(mc.getId());

                    clientChannels.add(cc);
                    clientChannels.remove(i);

                    System.out.println("下单成功，通道ID:" + cc.getId() + " 通道名:" + cc.getName() + " 通道对象：" + new Gson().toJson(clientChannels));
                    return oi;
                }
            } else {
                boolean flag = false;
                for (BigDecimal m : cc.getMoneyList()) {
                    if (m.compareTo(amount) == 0) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    //下单
                    OrderInfo oi = cc.createOrder(sn, amount, domain, payType, merchantId);

                    if (oi == null || !oi.getFlag()) {
                        continue;
                    } else {//下单成功
                        oi.setMerchantChannelId(mc.getId());
                        clientChannels.add(cc);
                        clientChannels.remove(i);

                        System.out.println("下单成功，通道ID:" + cc.getId() + " 通道名:" + cc.getName() + " 通道对象：" + new Gson().toJson(clientChannels));
                        return oi;
                    }
                }
            }
        }
        return null;
    }

    public ClientChannel getClientChannelByMerchant(String merchantNo, Long payType, Long clientChannelId) {
        MerchantChannel mc = merchantMap.get(merchantNo + "_" + payType);
        if (mc == null) {//商户通道已关闭
            return null;
        }
        // 上游列表
        List<ClientChannel> clientChannels = mc.getClientChannels();
        for (ClientChannel cc : clientChannels) {
            if (clientChannelId.equals(cc.getId())) {
                return cc;
            }
        }
        return null;
    }*/
}
