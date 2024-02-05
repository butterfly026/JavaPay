
package com.city.city_collector.channel;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.city.util.Logger;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayMerchantChannel;
import com.city.city_collector.admin.pay.service.PayClientChannelService;
import com.city.city_collector.admin.pay.service.PayMerchantChannelService;
import com.city.city_collector.admin.pay.service.PayMerchantService;
import com.city.city_collector.channel.bean.ClientChannel;
import com.city.city_collector.channel.bean.GroovyScript;
import com.city.city_collector.channel.bean.MerchantChannel;
import com.city.city_collector.channel.bean.OrderInfo;
import com.city.city_collector.channel.bean.OrderPreInfo;
import com.city.city_collector.channel.util.GroovyUtil;
import com.city.city_collector.channel.util.HttpUtil;
import com.city.city_collector.paylog.PayLogManager;
import groovy.lang.Binding;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import org.apache.http.HttpHost;
import org.apache.http.cookie.ClientCookie;
import org.springframework.beans.factory.annotation.Autowired;

import com.city.city_collector.admin.city.util.Logger;
import com.city.city_collector.channel.bean.*;
import com.city.city_collector.channel.util.GroovyUtil;
import com.city.city_collector.channel.util.HttpUtil;
import com.city.city_collector.paylog.PayLogManager;

import groovy.lang.Binding;

/**
 * @author nb
 * @Description: 通道管理类
 */
public class ChannelManager1 {
    /*
     * @Autowired
     * PayMerchantChannelService payMerchantChannelService;
     * 
     * @Autowired
     * PayClientChannelService payClientChannelService;
     */
    private static int g_channel_idx;
    private final static ChannelManager1 cm = new ChannelManager1();

    private ChannelManager1() {
        g_channel_idx=-1;
    }

    /**
     * 获取管理对象实例
     *
     * @return
     * @author:nb
     */
    public static ChannelManager1 getInstance() {
        return cm;
    }

    // public static final Map<String,ClientChannel> channels=new
    // HashMap<String,ClientChannel>();

    // static {
    // channels.put("Akpay", new AkPayClientChannel());
    // channels.put("Alibank99", new Alibank99ClientChannel());
    // channels.put("MiDian", new MiDianClientChannel());
    // channels.put("Alibank55", new Alibank55ClientChannel());
    // channels.put("HongXing", new HongXingClientChannel());
    // channels.put("MingXing", new MingXingClientChannel());
    // channels.put("TianMa", new TianMaClientChannel());
    // channels.put("SuYun", new SuYunClientChannel());
    // channels.put("YouFu", new YouFuClientChannel());
    // }

    // /** 上游通道列表 */
    // private List < ClientChannel > clientChannelList;

    // /** 商户通道列表 */
    // private List<MerchantChannel> merchantChannelList;
    /**
     * 商户通道
     **/
    private Map<String, MerchantChannel> merchantMap = new HashMap<String, MerchantChannel>();

    private Map<String, GroovyScript> scriptList = new HashMap<String, GroovyScript>();

    private Map<String, Integer> channelIndex = new HashMap<String, Integer>();

    public void initChannelScript(Map<String, GroovyScript> list) {
        this.scriptList = list;
    }

    public Map<String, GroovyScript> getScriptList() {
        return this.scriptList;
    }

    /**
     * 根据键名获取脚本对象
     *
     * @param keyname
     * @return
     * @author:nb
     */
    public GroovyScript getGroovyScriptByKeyname(String keyname) {
        return scriptList == null ? null : scriptList.get(keyname);
    }

    /**
     * 更新通道数据
     *
     * @param merchantChannelList
     * @author:nb
     */
    public void updateChannelData(List<MerchantChannel> merchantChannelList) {
        // this.clientChannelList=clientChannelList;
        // this.merchantChannelList=merchantChannelList;

        merchantMap = new HashMap<String, MerchantChannel>();

        for (MerchantChannel mc : merchantChannelList) {
            merchantMap.put(mc.getNo() + "_" + mc.getPayType(), mc);
        }

    }

    public boolean merchantOpen(String merchantNo, Long payType, BigDecimal amount) {
        MerchantChannel mc = merchantMap.get(merchantNo + "_" + payType);
        if (mc == null) {// 商户通道已关闭
            return false;
        }

        // 上游列表
        List<ClientChannel> clientChannels = mc.getClientChannels();
        if (!clientChannels.isEmpty()) {
            for (int i = 0; i < clientChannels.size(); i++) {
                ClientChannel cc = clientChannels.get(i);
                // 有一个通道可用,就返回可下发订单
                if (checkSingleChannel(cc, amount)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkSingleChannel(ClientChannel cc, BigDecimal amount) {
        // 判断通道是否开启
        if (cc.getMinTime() != null && cc.getMaxTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            Long time = Long.parseLong(sdf.format(new Date()));
            if (time < cc.getMinTime().longValue() || time > cc.getMaxTime().longValue()) {// 不在开启时间段内
                return false;
            }
        }

        Integer type = cc.getType();
        // 通道模式
        if (type == null || type.intValue() == 0) {
            if (cc.getMinMoney() != null && cc.getMaxMoney() != null) {
                if (amount.compareTo(cc.getMinMoney()) < 0 || amount.compareTo(cc.getMaxMoney()) > 0) {
                    return false;
                }
            }
        } else {
            boolean flag = false;
            for (BigDecimal m : cc.getMoneyList()) {
                if (m.compareTo(amount) == 0) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    public boolean isFeeError(Long channelid, Long merchantChannelId,
            PayMerchantChannelService payMerchantChannelService, PayClientChannelService payClientChannelService) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", channelid);
        PayClientChannel clientChannel = payClientChannelService.querySingle(params);

        params.put("id", merchantChannelId);
        PayMerchantChannel merchantChannel = payMerchantChannelService.querySingle(params);

        if ((merchantChannel.getMerchantRatio().floatValue() - clientChannel.getRatio().floatValue()) < 0) {
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,
                    "isFeeError 检测到费率倒挂" + " 商户通道：[" + merchantChannelId + " - " + merchantChannel.getName() + " - 费率："
                            + merchantChannel.getMerchantRatio() + "] 通道：[" + channelid + " - "
                            + clientChannel.getName() + " - 费率：" + clientChannel.getRatio() + "]");
            return true;
        }

        // merchantChannel.getProxyRatio();
        // merchantChannel.getMerchantRatio();
        return false;
    }
    /*
     * 0
     * 通用
     * 1
     * 苹果
     * 2
     * 安卓
     * 3
     * 其它
     * 
     */

    private Integer[] GeRndIdxs(int min, int max) {
        // 生成随机数索引数组
        boolean notIs = false;
        List<Integer> exclude = new ArrayList<>();
        if (max - min <= 0) {
            return new Integer[] { 0 };
        }
        Integer nIdx[] = new Integer[max-min];
        for(Integer i=0;i<nIdx.length;i++){
            do{
                Integer rand = (Integer)(min + (int)(Math.random() * (max-min)));
                notIs = new HashSet<>(exclude).contains((Object)rand);
                if(!notIs){
                    exclude.add(rand);
                    nIdx[i]=rand;
                }
            }while(notIs);
        }
        return nIdx;
    }

    private static boolean is_short=false;
    public static List<ClientChannel> getNewSortChannel(List<ClientChannel> channels) {
        if(channels==null)
        {
            return null;
        }
        Collections.sort(channels);
       
        Integer diff=-1; 
        is_short=false;
        for (ClientChannel clientChannel : channels) {
            if(diff==-1){
                diff = clientChannel.getPriority();
                continue;
            }
            if(diff!=clientChannel.getPriority()){
                diff = clientChannel.getPriority();
                is_short=true;
                break;
            }
        } 
        //如果没有排序就随机排序
        if(!is_short){
            //取消洗牌改用索引累加
            //Collections.shuffle(channels);

        }
        return channels;
    }
    public static ClientChannel getRandomChannel(List<ClientChannel> channels,Map<ClientChannel,Integer> diff) {


        if(channels.size()<=1)//如果只有一个通道，就直接用这个通道 //下面的算法，没有考虑到只有一个通道,并且权重小于2的情况
        {
            return channels.get(0);
        }


        Integer maxArray=0;
        for (Integer cnt=0;cnt<channels.size();cnt++) { 
            maxArray +=channels.get(cnt).getPriority()+1;
        }
        ClientChannel channel_pool[]=new ClientChannel[maxArray];


        //开始计算权重分配产生权重池
        Integer count=0;
        for (Integer cnt=0;cnt<channels.size();cnt++) { 
            Integer Priority = channels.get(cnt).getPriority();
            
            //if(Priority==0 || Priority>200)continue;
            for (Integer pcnt=0;pcnt<Priority+1;pcnt++) {
                channel_pool[count] = channels.get(cnt);
                count++;
            }
        }

        Integer idx = (Integer)(0 + (int)(Math.random() * maxArray));
        ClientChannel retc = channel_pool[idx];
        if(diff!=null){
            if(diff.size()>=channels.size()){
                diff.clear();
            }
            if(diff.containsKey(retc)){
                try{
                    retc = getRandomChannel(channels,diff);
                }catch(Exception e){
                    System.out.println(e);
                }
            }

            diff.put(retc,1);
        }
        return retc;
    }
    private static void incChannelIdx(int maxChannel){
        if(g_channel_idx==-1){
            g_channel_idx = 0;
        }else{
            g_channel_idx++;
            g_channel_idx %= maxChannel;
        }
    }
    private static int getChannelIdx(){
        return g_channel_idx;
    }
    private String[] platform = { "通用", "苹果", "安卓", "其他" };
    public OrderInfo createPayOrder(String merchantNo, Long payType, String sn, BigDecimal amount, String domain,Long merchantId, Integer platform, String remark, String clientIp,PayMerchantChannelService payMerchantChannelService, PayClientChannelService payClientChannelService) 
    {
        //Map<ClientChannel,Integer> mpc = new HashMap<>();
        Integer idxs[]=null;
        MerchantChannel mc = merchantMap.get(merchantNo + "_" + payType);
        if (mc == null) {// 商户通道已关闭
            return null;
        }
        // 上游列表
        List<ClientChannel> oldclientChannels = mc.getClientChannels();
        List<ClientChannel> newClientChannels = getNewSortChannel(oldclientChannels);
        if (!oldclientChannels.isEmpty()) {
            // 获取操作总耗时，防止过度超时,单位毫秒
            long timeout = 30000;// 总操作超时30秒
            long timeout_start = System.currentTimeMillis();


            // 1. 优先 判断平台是否支持(客户端操作系统) kahn
            if (ApplicationData.getInstance().getConfig().getPlatformvalid().intValue() == 1) {
                for (int ci = 0; ci < newClientChannels.size(); ci++) {
                    Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"通道数：" + newClientChannels.size()+" 随机排序：" + new Gson().toJson(idxs));
                   // ClientChannel gcc = newClientChannels.get(idxs[ci]);
                   ClientChannel gcc=null;
                   if(!is_short){
                        incChannelIdx(newClientChannels.size());
                        gcc =  newClientChannels.get(getChannelIdx());
                        
                    }else{
                        gcc =  newClientChannels.get(ci);
                    }
                    if (isFeeError(gcc.getId(), mc.getId(), payMerchantChannelService, payClientChannelService)) {
                        // 判断是否费率倒挂
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走平台优先模式A <费率倒挂>" + "订单：" + sn+ " 商户：" + mc.getId() + " 通道：" + gcc.getId() + " -> 尝试下一个通道");
                        continue;
                    }
                    // 0 pc 1 苹果 2 安卓 3 pc
                    Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"createPayOrder-走平台优先模式A8 : 订单：" + sn + " 通道平台：" + this.platform[gcc.getPrimaryPlatform()]+ "  访问端平台：" + this.platform[platform] + " 通道：" + gcc.getId() + " - "+ gcc.getName());
                    if (null != gcc.getPrimaryPlatform() && gcc.getPrimaryPlatform().equals(platform)) {
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"createPayOrder-走平台优先模式A 使用通道：" + gcc.getId() + " - " + gcc.getName());
                        OrderInfo oi = createPayOrder1(gcc, sn, amount, domain, merchantId, mc, platform, remark,clientIp);
                        if (oi != null) {
                            return oi;
                        }
                    }

                    if ((System.currentTimeMillis() - timeout_start) > timeout) {
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走平台优先模式A <总操作时间超时> 订单：" + sn);
                        return null;
                    }
                }
                timeout_start = System.currentTimeMillis();
                idxs=null;
                //Collections.sort(newClientChannels);
                //Integer priority = newClientChannels.get(0).getPriority();
                
               // if (priority.intValue() > 0) {// 走优先级模式 通道优先级
                    for (int i = 0; i < newClientChannels.size(); i++) {

                        ClientChannel cc=null;
                        if(!is_short){
                            incChannelIdx(newClientChannels.size());
                            cc =  newClientChannels.get(getChannelIdx());
                             
                         }else{
                            cc =  newClientChannels.get(i);
                         }

                        if (isFeeError(cc.getId(), mc.getId(), payMerchantChannelService, payClientChannelService)) {
                            // 判断是否费率倒挂
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走优先级模式B <费率倒挂>" + "订单：" + sn + " 商户：" + mc.getId() + " 通道：" + cc.getId() + " -> 尝试下一个通道");
                            continue;
                        }
                        
                        if (null != cc.getPrimaryPlatform() && (this.platform[cc.getPrimaryPlatform()].equals("通用") || this.platform[cc.getPrimaryPlatform()].equals("其他"))) {

                        //if (null != cc.getPrimaryPlatform() && !cc.getPrimaryPlatform().equals(platform)) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走优先级模式B : " + sn + ",ClientChannelName:" + cc.getName() + ",ClientChannelID:" + cc.getId() + ",PrimaryPlatform:" + this.platform[cc.getPrimaryPlatform()] + " " + platform);
                            OrderInfo oi = createPayOrder1(cc, sn, amount, domain, merchantId, mc, platform, remark,clientIp);
                            if (oi != null) {
                                return oi;
                            }
                        }

                        if ((System.currentTimeMillis() - timeout_start) > timeout) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"createPayOrder-走优先级模式B <总操作时间超时> 订单：" + sn);
                            return null;
                        }
                    }
                    /*
                } else {// 轮询模式
                    int startIndex = getChannelStartIndex(merchantNo + "_" + payType) % newClientChannels.size();
                    // 获取符合条件的上游列表
                    for (int i = startIndex; i < newClientChannels.size(); i++) {
                        ClientChannel cc = newClientChannels.get(i);
                        if (isFeeError(cc.getId(), mc.getId(), payMerchantChannelService, payClientChannelService)) {
                            // 判断是否费率倒挂
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrderr-轮询模式C <费率倒挂>" + "订单：" + sn + " 商户：" + mc.getId() + " 通道：" + cc.getId() + " -> 尝试下一个通道");
                            continue;
                        }

                        if (null != cc.getPrimaryPlatform() && (this.platform[cc.getPrimaryPlatform()].equals(this.platform[platform]) || this.platform[cc.getPrimaryPlatform()].equals("通用") || this.platform[cc.getPrimaryPlatform()].equals("其他"))) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-轮询模式C : " + sn + ",ClientChannelName:" + cc.getName()+ ",ClientChannelID:" + cc.getId() + ",选中-访问:"+ this.platform[cc.getPrimaryPlatform()] + " - " + this.platform[platform]);
                            OrderInfo oi = createPayOrder1(cc, sn, amount, domain, merchantId, mc, platform, remark,clientIp);
                            if (oi != null) {
                                return oi;
                            }
                        }
                        if ((System.currentTimeMillis() - timeout_start) > timeout) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-轮询模式C <总操作时间超时> 订单：" + sn);
                            return null;
                        }
                    }
                } */

            } else {
                Integer priority = newClientChannels.get(0).getPriority();
                if (priority.intValue() > 0) {// 走优先级模式 通道优先级
                    for (int i = 0; i < newClientChannels.size(); i++) {
                        //ClientChannel cc = clientChannels.get(i);
                        ClientChannel cc =  newClientChannels.get(i);//getRandomChannel(clientChannels,mpc);
                        if (isFeeError(cc.getId(), mc.getId(), payMerchantChannelService, payClientChannelService)) {
                            // 判断是否费率倒挂
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走优先级模式 <费率倒挂>" + "订单：" + sn+ " 商户：" + mc.getId() + " 通道：" + cc.getId() + " -> 尝试下一个通道");
                            continue;
                        }
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"createPayOrder-走优先级模式 : " + sn + ",ClientChannelName:" + cc.getName()+ ",ClientChannelID:" + cc.getId() + ",PrimaryPlatform:"+ cc.getPrimaryPlatform() + " " + platform);
                        OrderInfo oi = createPayOrder1(cc, sn, amount, domain, merchantId, mc, platform, remark,
                                clientIp);
                        if (oi != null) {
                            return oi;
                        }

                        if ((System.currentTimeMillis() - timeout_start) > timeout) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走优先级模式 <总操作时间超时> 订单：" + sn);
                            return null;
                        }
                    }
                } else {// 轮询模式
                    int startIndex = getChannelStartIndex(merchantNo + "_" + payType) % newClientChannels.size();
                    // 获取符合条件的上游列表
                    for (int i = startIndex; i < newClientChannels.size(); i++) {
                        ClientChannel cc = newClientChannels.get(i);
                        if (isFeeError(cc.getId(), mc.getId(), payMerchantChannelService, payClientChannelService)) {
                            // 判断是否费率倒挂
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrderr-轮询模式 <费率倒挂>" + "订单：" + sn+ " 商户：" + mc.getId() + " 通道：" + cc.getId() + " -> 尝试下一个通道");
                            continue;
                        }
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"createPayOrder-轮询模式 : " + sn + ",ClientChannelName:" + cc.getName()+ ",ClientChannelID:" + cc.getId() + ",PrimaryPlatform:"+ cc.getPrimaryPlatform() + " " + platform);
                        OrderInfo oi = createPayOrder1(cc, sn, amount, domain, merchantId, mc, platform, remark,
                                clientIp);
                        if (oi != null) {
                            return oi;
                        }
                        if ((System.currentTimeMillis() - timeout_start) > timeout) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-轮询模式 <总操作时间超时> 订单：" + sn);
                            return null;
                        }
                    }
                }
            }

        }
        return null;
    }

/*
    private String[] platform = { "通用", "苹果", "安卓", "其他" };
    public OrderInfo createPayOrder(String merchantNo, Long payType, String sn, BigDecimal amount, String domain,Long merchantId, Integer platform, String remark, String clientIp,PayMerchantChannelService payMerchantChannelService, PayClientChannelService payClientChannelService) 
    {
        Integer idxs[]=null;
        MerchantChannel mc = merchantMap.get(merchantNo + "_" + payType);
        if (mc == null) {// 商户通道已关闭
            return null;
        }
        // 上游列表
        List<ClientChannel> clientChannels = mc.getClientChannels();

        if (!clientChannels.isEmpty()) {
            // 获取操作总耗时，防止过度超时,单位毫秒
            long timeout = 30000;// 总操作超时30秒
            long timeout_start = System.currentTimeMillis();

            // 1. 优先 判断平台是否支持(客户端操作系统) kahn
            if (ApplicationData.getInstance().getConfig().getPlatformvalid().intValue() == 1) {
                for (int ci = 0; ci < clientChannels.size(); ci++) {
               
                    ClientChannel gcc = clientChannels.get(ci);
                    if (isFeeError(gcc.getId(), mc.getId(), payMerchantChannelService, payClientChannelService)) {
                        // 判断是否费率倒挂
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走平台优先模式A <费率倒挂>" + "订单：" + sn+ " 商户：" + mc.getId() + " 通道：" + gcc.getId() + " -> 尝试下一个通道");
                        continue;
                    }
                    // 0 pc 1 苹果 2 安卓 3 pc
                    Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走平台优先模式A8 : 订单：" + sn + " 通道平台：" + this.platform[gcc.getPrimaryPlatform()] + "  访问端平台：" + this.platform[platform] + " 通道：" + gcc.getId() + " - " + gcc.getName());
                    if (null != gcc.getPrimaryPlatform() && gcc.getPrimaryPlatform().equals(platform)) {
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"createPayOrder-走平台优先模式A 使用通道：" + gcc.getId() + " - " + gcc.getName());
                        OrderInfo oi = createPayOrder1(gcc, sn, amount, domain, merchantId, mc, platform, remark,clientIp);
                        if (oi != null) {
                            return oi;
                        }
                    }

                    if ((System.currentTimeMillis() - timeout_start) > timeout) {
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走平台优先模式A <总操作时间超时> 订单：" + sn);
                        return null;
                    }
                }
                timeout_start = System.currentTimeMillis();
                idxs=null;
                Integer priority = clientChannels.get(0).getPriority();
                //clientChannels.get(0).getPriority();

                if (priority.intValue() > 0) {// 走优先级模式 通道优先级
                    for (int i = 0; i < clientChannels.size(); i++) {
    
                        ClientChannel cc = clientChannels.get(i);
                        if (isFeeError(cc.getId(), mc.getId(), payMerchantChannelService, payClientChannelService)) {
                            // 判断是否费率倒挂
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走优先级模式B <费率倒挂>" + "订单：" + sn + " 商户：" + mc.getId() + " 通道：" + cc.getId() + " -> 尝试下一个通道");
                            continue;
                        }

                        if (null != cc.getPrimaryPlatform() && !cc.getPrimaryPlatform().equals(platform)) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"createPayOrder-走优先级模式B : " + sn + ",ClientChannelName:" + cc.getName() + ",ClientChannelID:" + cc.getId() + ",PrimaryPlatform:" + this.platform[cc.getPrimaryPlatform()] + " " + platform);
                            OrderInfo oi = createPayOrder1(cc, sn, amount, domain, merchantId, mc, platform, remark,clientIp);
                            if (oi != null) {
                                return oi;
                            }
                        }

                        if ((System.currentTimeMillis() - timeout_start) > timeout) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"createPayOrder-走优先级模式B <总操作时间超时> 订单：" + sn);
                            return null;
                        }
                    }
                } else {// 轮询模式
                    int startIndex = getChannelStartIndex(merchantNo + "_" + payType) % clientChannels.size();

                    Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-轮询模式：startIndex=" + startIndex + " [ "+getChannelStartIndex(merchantNo + "_" + payType) + " % "+clientChannels.size()+" ]");
                    // 获取符合条件的上游列表
                    for (int i = startIndex; i < clientChannels.size(); i++) {
                        ClientChannel cc = clientChannels.get(i);
                        if (isFeeError(cc.getId(), mc.getId(), payMerchantChannelService, payClientChannelService)) {
                            // 判断是否费率倒挂
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrderr-轮询模式B <费率倒挂>" + "订单：" + sn + " 商户：" + mc.getId() + " 通道：" + cc.getId() + " -> 尝试下一个通道");
                            continue;
                        }
                        if (null != cc.getPrimaryPlatform() && !cc.getPrimaryPlatform().equals(platform)) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"createPayOrder-轮询模式B : " + sn + ",ClientChannelName:" + cc.getName()+ ",ClientChannelID:" + cc.getId() + ",选中-访问:"+ this.platform[cc.getPrimaryPlatform()] + " - " + this.platform[platform]);
                            OrderInfo oi = createPayOrder1(cc, sn, amount, domain, merchantId, mc, platform, remark,clientIp);
                            if (oi != null) {
                                return oi;
                            }
                        }
                        if ((System.currentTimeMillis() - timeout_start) > timeout) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-轮询模式B <总操作时间超时> 订单：" + sn);
                            return null;
                        }
                    }
                }

            } else {
                Integer priority = clientChannels.get(0).getPriority();
                if (priority.intValue() > 0) {// 走优先级模式 通道优先级
                    for (int i = 0; i < clientChannels.size(); i++) {
                        ClientChannel cc = clientChannels.get(i);
                        if (isFeeError(cc.getId(), mc.getId(), payMerchantChannelService, payClientChannelService)) {
                            // 判断是否费率倒挂
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走优先级模式 <费率倒挂>" + "订单：" + sn
                                    + " 商户：" + mc.getId() + " 通道：" + cc.getId() + " -> 尝试下一个通道");
                            continue;
                        }
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,
                                "createPayOrder-走优先级模式 : " + sn + ",ClientChannelName:" + cc.getName()
                                        + ",ClientChannelID:" + cc.getId() + ",PrimaryPlatform:"
                                        + cc.getPrimaryPlatform() + " " + platform);
                        OrderInfo oi = createPayOrder1(cc, sn, amount, domain, merchantId, mc, platform, remark,
                                clientIp);
                        if (oi != null) {
                            return oi;
                        }

                        if ((System.currentTimeMillis() - timeout_start) > timeout) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-走优先级模式 <总操作时间超时> 订单：" + sn);
                            return null;
                        }
                    }
                } else {// 轮询模式
                    int startIndex = getChannelStartIndex(merchantNo + "_" + payType) % clientChannels.size();
                    // 获取符合条件的上游列表
                    for (int i = startIndex; i < clientChannels.size(); i++) {
                        ClientChannel cc = clientChannels.get(i);
                        if (isFeeError(cc.getId(), mc.getId(), payMerchantChannelService, payClientChannelService)) {
                            // 判断是否费率倒挂
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrderr-轮询模式 <费率倒挂>" + "订单：" + sn
                                    + " 商户：" + mc.getId() + " 通道：" + cc.getId() + " -> 尝试下一个通道");
                            continue;
                        }
                        Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,
                                "createPayOrder-轮询模式 : " + sn + ",ClientChannelName:" + cc.getName()
                                        + ",ClientChannelID:" + cc.getId() + ",PrimaryPlatform:"
                                        + cc.getPrimaryPlatform() + " " + platform);
                        OrderInfo oi = createPayOrder1(cc, sn, amount, domain, merchantId, mc, platform, remark,
                                clientIp);
                        if (oi != null) {
                            return oi;
                        }
                        if ((System.currentTimeMillis() - timeout_start) > timeout) {
                            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "createPayOrder-轮询模式 <总操作时间超时> 订单：" + sn);
                            return null;
                        }
                    }
                }
            }

        }
        return null;
    }
    */
    public OrderInfo createPayOrder1(ClientChannel cc, String sn, BigDecimal amount, String domain, Long merchantId,MerchantChannel mc, Integer platform, String remark, String clientIp) {
        if (checkSingleChannel(cc, amount)) {
            return createOrder(cc, sn, domain, amount, merchantId, mc, platform, remark, clientIp);
        }
        return null;
    }

    // public ClientChannel getClientChannelByMerchant(String merchantNo, Long
    // payType, Long clientChannelId) {
    // MerchantChannel mc = merchantMap.get(merchantNo + "_" + payType);
    // if (mc == null) {//商户通道已关闭
    // return null;
    // }
    // // 上游列表
    // List<ClientChannel> clientChannels = mc.getClientChannels();
    // for (ClientChannel cc : clientChannels) {
    // if (clientChannelId.equals(cc.getId())) {
    // return cc;
    // }
    // }
    // return null;
    // }

    public OrderInfo createOrder(ClientChannel cc, String sn, String domain, BigDecimal amount, Long merchantId,MerchantChannel mc, Integer platform, String remark, String clientIp) {
        // 尝试日志,生成页面访问后执行这里
        
        
        for (int i = 0; i < cc.getRetryNumber(); i++) {
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"系统订单号 3 : " + sn + " 通道: " + cc.getName() + " 尝试次数："+i);
            PayLogManager.getInstance().createPayLogByOrderRequest(sn,"系统订单号 3 : " + sn + " 通道: " + cc.getName() + " 尝试次数：" + i, "", "", null, null, cc.getId(), null);

            try {
                // 如果脚本存在，调用脚本发起下单请求，否则continue
                String keyname = cc.getKeyname();
                GroovyScript gs = getGroovyScriptByKeyname(keyname);
                if (gs == null || gs.getDealOrderData() == null || gs.getOrderParams() == null || gs.getNotifyData() == null) {
                    PayLogManager.getInstance().createPayLogByOrderRequest(sn, cc.getName() + " 下单通道不可用", "", "", null,cc.getId(), merchantId, null);
                    return null;
                }
                Binding binding = new Binding();
                binding.setProperty("id", cc.getId());
                binding.setProperty("merchantNo", cc.getMerchantNo());
                binding.setProperty("domain", domain);
                binding.setProperty("merchantMy", cc.getMerchantMy());
                binding.setProperty("channelParams", cc.getParams());
                binding.setProperty("sn", sn);
                binding.setProperty("amount", amount);
                binding.setProperty("platform", platform);
                binding.setProperty("remark", remark);
                binding.setProperty("clientIp", clientIp);
                // kahn createReqParams
                Object data = GroovyUtil.runScript(gs.getOrderParams(), binding);
                if (data == null) {
                    return null;
                }
                OrderPreInfo opi = (OrderPreInfo) data;

                PayLogManager.getInstance().createPayLogByOrderRequest(sn, "向" + cc.getName() + " 通道发起下单请求", "", "",null, cc.getId(), merchantId, null);
                //kahn 订单号沿用发起端订单号
                //opi.getParams().put("sn", sn);
                OrderInfo oi = null;
                if (opi.getIsOrderInfo() == 1) {
                    oi = new OrderInfo();
                    oi.setFlag(true);

                    Map<String, Object> p = opi.getParams();
                    oi.setData((String) p.get("data"));
                   
                    String payUrl = (String) p.get("payUrl");
                    if (!payUrl.startsWith("http")) {
                        payUrl = cc.getUrlpay() + payUrl;
                    }
                    
                    oi.setSn((String) p.get("sn"));
                    oi.setPayUrl(payUrl);
                    oi.setName("");
                    oi.setCard("");
                    oi.setBankName("");

                    oi.setClientId(cc.getClient() == null ? null : cc.getClient().getId());
                    oi.setClientNo(cc.getClient() == null ? null : cc.getClient().getNo());
                    oi.setClientName(cc.getClient() == null ? null : cc.getClient().getName());

                    oi.setClientChannelId(cc.getId());
                } else {
                    oi = HttpUtil.requestData(cc.getUrlpay(), opi.getRequestType(), opi.getParams(), opi.getHeaders(),
                            new HashMap<String, String>(), opi.getCharset(),
                            merchantId, sn, opi.getReqType(), cc.getId(),
                            cc.getClient() == null ? null : cc.getClient().getId(), gs.getDealOrderData(),
                            cc.getClient() == null ? null : cc.getClient().getNo(),
                            cc.getClient() == null ? null : cc.getClient().getName(), cc.getParams(), opi.getuseProxy());
                }
                // OrderInfo oi=cc.createOrder(sn,amount,domain,payType,merchantId);

                if (oi == null || !oi.getFlag()) {
                   
                } else {// 下单成功
                    oi.setMerchantChannelId(mc.getId());

                    // clientChannels.add(cc);
                    // clientChannels.remove(index);

                    // System.out.println("下单成功，通道ID:"+cc.getId()+" 通道名:"+cc.getName()+" 通道对象："+new
                    // Gson().toJson(clientChannels));

                    oi.setMerchantNo(cc.getMerchantNo());
                    return oi;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /***
     * 获取本次运行的起始索引
     * 
     * @param key
     * @return
     */
    public synchronized int getChannelStartIndex(String key) {
        if (channelIndex.get(key) == null) {
            channelIndex.put(key, 0);
            return 0;
        }
        int l1 = channelIndex.get(key).intValue() + 1;
        if (l1 > 99999999) {
            l1 = 0;
        }
        channelIndex.put(key, l1);
        return l1;
    }

    // public OrderPreInfo createSbOrder(Long id, String merchantNo, String
    // merchantMy,
    // Map < String, Object > channelParams, String sn, BigDecimal amount, String
    // domain,String url) {
    // try {
    //// String url="http://eb.gateway.725989.com:30001/api/v1/lang_order_post.do";
    //
    // Map < String,Object> params=new TreeMap<String,Object>();
    //
    // params.put("order", sn);
    // params.put("price", amount);
    //
    // params.put("uid", merchantNo);
    //
    // params.put("notify_url", domain+Constants.ASYNC_URL+"/"+id);
    //
    // String pt=(String)channelParams.get("pt");
    //
    // params.put("upuid",channelParams.get("upuid"));
    //
    //
    // String
    // signStr="key="+merchantMy+"&"+"notify_url="+URLEncoder.encode(domain+Constants.ASYNC_URL+"/"+id,"UTF-8")+"&order="+params.get("order")+"&price="+params.get("price")+"&uid="+params.get("uid")+"&upuid="+params.get("upuid");
    //
    // String sign=MD5Util.MD5Encode(signStr, "UTF-8").toLowerCase();
    //
    // params.put("sign", sign);
    //
    // domain="http://203.86.232.123";
    //
    // String data=HttpUtil.commonRequestDataXl(url, params, sn, id,5);
    // if(data!=null) {
    // if(StringUtils.isBlank(data)) {
    // return null;
    // }
    // JsonParser jp=new JsonParser();
    //
    // JsonObject jobj=jp.parse(data).getAsJsonObject();
    // if(!"109".equals(jobj.get("retcode").getAsString()) ||
    // !"wait".equals(jobj.get("pay_status").getAsString()) ) {
    // return null;
    // }
    //
    // String requestSign="";
    // String requestParam="";
    // if("wx".equals(pt)) {//wx
    // String arr=URLDecoder.decode(jobj.get("pay_url").getAsString(),"UTF-8");
    // String[] arrdata=arr.split("~");
    // requestSign=arrdata[0].replace("\"", "&quot;");
    // requestParam=arrdata[1].replace("\"", "&quot;");
    // }else {//zfb
    // String arr=URLDecoder.decode(jobj.get("pay_url_ali").getAsString(),"UTF-8");
    // String[] arrdata=arr.split("~");
    // requestSign=arrdata[0].replace("\"", "&quot;");
    // requestParam=arrdata[1].replace("\"", "&quot;");
    // }
    // String sbuf="<form name='postSubmit' method='post'
    // action='https://pay.it.10086.cn/payprod-format/h5/dup_submit' >";
    // sbuf+="<input type='hidden' name='sign' value='"+requestSign+"' />";
    // sbuf+="<input type='hidden' name='request_params' value='"+requestParam+"'
    // />";
    // sbuf+="</form> <script>document.postSubmit.submit();</script>";
    //
    // Map < String,Object > datas=new HashMap<String,Object>();
    // datas.put("data", sbuf.toString());
    // datas.put("payUrl", domain+"/api/pay/formRequest?sn="+sn);
    // datas.put("sn", jobj.get("bank_order").getAsString());
    //
    // OrderPreInfo opi=new OrderPreInfo();
    // opi.setCharset("UTF-8");
    // opi.setParams(datas);
    // // opi.setReqType(0);
    // opi.setIsOrderInfo(1);
    // opi.setRequestType(com.city.city_collector.channel.util.HttpUtil.RequestType.POST);
    //
    // sbuf=null;
    // return opi;
    // }
    // }catch(Exception e) {
    // e.printStackTrace();
    // }
    // return null;
    // }

}
