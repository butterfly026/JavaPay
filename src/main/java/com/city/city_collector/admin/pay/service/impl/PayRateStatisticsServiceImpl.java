package com.city.city_collector.admin.pay.service.impl;

import com.city.city_collector.admin.pay.dao.PayOrderDao;
import com.city.city_collector.admin.pay.entity.PayInComeOrder;
import com.city.city_collector.admin.pay.entity.PayRateStatistics;
import com.city.city_collector.admin.pay.service.PayRateStatisticsService;
import com.city.city_collector.common.util.CommonUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PayRateStatisticsServiceImpl implements PayRateStatisticsService {

    @Autowired
    private PayOrderDao payOrderDao;

    // 商户名  渠道名

    public List<Map<String, Object>> todayOrderInfo() {
        Date now = new Date();
        Date endTime = CommonUtil.getEndTime(now);
        Date startTime = CommonUtil.getStartTime(now);

       /* Date endTime = null;
        Date startTime = null;
        try{
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-05-14 22:02:00");
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-05-14 22:00:00");
        }catch (Exception e){}
*/

        List<Map<String, Object>> maps = payOrderDao.queryStatistics(startTime, endTime);
        return maps;
    }

    @Override
    public Map<String, Object> selectInComeOrderStatistics(String channelName, String merName, BigDecimal amount, Integer timeUnit) {

        Date now = new Date();
        //  10s之前
        Date early = new Date(now.getTime() - timeUnit * 1000);
        /*Date now = null;
        Date early = null;
        try{
             now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-05-14 22:02:00");
             early = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-05-14 22:00:00");
        }catch (Exception e){}*/


        List<PayInComeOrder> payInComeOrders = payOrderDao.selectInComeOrderStatistics(early, now, channelName, merName, amount);
        // 算渠道的单  算总量

        //  Map<String,PayInComeOrder> merAmountStatisticsMap = new LinkedHashMap<>();
        Map<String, PayInComeOrder> merStatisticsMap = new LinkedHashMap<>();
        //  Map<String,PayInComeOrder> channelAmountStatisticsMap = new LinkedHashMap<>();
        Map<String, PayInComeOrder> channelStatisticsMap = new LinkedHashMap<>();
        for (PayInComeOrder payInComeOrder : payInComeOrders) {

            //  String merAmountkey =payInComeOrder.getMerchantId()+"_"+payInComeOrder.getAmount();
            //  buildStatisticsMap(merAmountStatisticsMap,merAmountkey,payInComeOrder,"merAmount");

            String merKey = payInComeOrder.getMerchantId() + "";
            buildStatisticsMap(merStatisticsMap, merKey, payInComeOrder, "mer");

            //     String channelAmountKey = payInComeOrder.getClientChannelId()+"_"+payInComeOrder.getAmount();
            //   buildStatisticsMap(channelAmountStatisticsMap,channelAmountKey,payInComeOrder,"channelAmount");

            String channelKey = payInComeOrder.getClientChannelId() + "";
            buildStatisticsMap(channelStatisticsMap, channelKey, payInComeOrder, "channel");

        }

        Map<String, Object> inComeOrderMap = new HashMap<>();
        //inComeOrderMap.put("merChannelAmount",payInComeOrders);
        // inComeOrderMap.put("merAmount",merAmountStatisticsMap.values());
        inComeOrderMap.put("mer", merStatisticsMap.values());
        //   inComeOrderMap.put("channelAmount", channelAmountStatisticsMap.values());
        inComeOrderMap.put("channel", channelStatisticsMap.values());

        inComeOrderMap.put("begin", DateFormatUtils.format(early, "HH:mm:ss"));
        inComeOrderMap.put("end", DateFormatUtils.format(now, "HH:mm:ss"));

        return inComeOrderMap;
    }

    private void buildStatisticsMap(Map<String, PayInComeOrder> statisticsMap, String key, PayInComeOrder payInComeOrder, String modelType) {
        PayInComeOrder value;
        if ((value = statisticsMap.get(key)) == null) {
            value = new PayInComeOrder();
            value.setCount(payInComeOrder.getCount());
            switch (modelType) {
                case "merAmount":
                    value.setAmount(payInComeOrder.getAmount());
                    value.setMerchantId(payInComeOrder.getMerchantId());
                    value.setMerchantName(payInComeOrder.getMerchantName());
                    break;
                case "mer":
                    value.setMerchantId(payInComeOrder.getMerchantId());
                    value.setMerchantName(payInComeOrder.getMerchantName());
                    break;
                case "channelAmount":
                    value.setClientChannelId(payInComeOrder.getClientChannelId());
                    value.setChannelName(payInComeOrder.getChannelName());
                    value.setAmount(payInComeOrder.getAmount());
                    break;
                case "channel":
                    value.setClientChannelId(payInComeOrder.getClientChannelId());
                    value.setChannelName(payInComeOrder.getChannelName());
                    break;
                default:
                    break;
            }
            value.setBegin(payInComeOrder.getBegin());
            value.setEnd(payInComeOrder.getEnd());
            statisticsMap.put(key, value);
        } else {
            value.setCount(value.getCount() + payInComeOrder.getCount());
        }
    }
    @Override
    public Map<String, Object> inComeOrderRateStatistics(String channelName, String merName, BigDecimal amount, Integer timeUnit) {

        Date now = new Date();
        // 2分钟之前
        Date early = new Date(now.getTime() - timeUnit * 1000);

        /*Date now = null;
        Date early = null;
        try{
            now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-05-14 22:02:00");
            early = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-05-14 22:00:00");
        }catch (Exception e){}*/

        List<PayRateStatistics> payRateStatistics = payOrderDao.inComeOrderRateStatistics(early, now, channelName, merName, amount,null,"","");

        Map<String, PayRateStatistics> merChannelAmountStatisticsMap = new LinkedHashMap<>();
        Map<String, PayRateStatistics> merAmountStatisticsMap = new LinkedHashMap<>();
          Map<String,PayRateStatistics> merStatisticsMap = new LinkedHashMap<>();
        Map<String, PayRateStatistics> channelAmountStatisticsMap = new LinkedHashMap<>();
        Map<String, PayRateStatistics> channelStatisticsMap = new LinkedHashMap<>();

        for (PayRateStatistics payRateStatistic : payRateStatistics) {
            String merChannelAmountKey = payRateStatistic.getMerchantId() + "_" + payRateStatistic.getClientChannelId() + "_" + payRateStatistic.getAmount();
            buildRateStatisticsMap(merChannelAmountStatisticsMap, merChannelAmountKey, payRateStatistic, "merChannelAmount");

            String merAmountKey = payRateStatistic.getMerchantId() + "_" + payRateStatistic.getAmount();
            buildRateStatisticsMap(merAmountStatisticsMap, merAmountKey, payRateStatistic, "merAmount");

                String merKey = payRateStatistic.getMerchantId()+"";
                buildRateStatisticsMap(merStatisticsMap,merKey,payRateStatistic,"mer");

            String channelAmountKey = payRateStatistic.getClientChannelId() + "_" + payRateStatistic.getAmount();
            buildRateStatisticsMap(channelAmountStatisticsMap, channelAmountKey, payRateStatistic, "channelAmount");

            String channelKey = payRateStatistic.getClientChannelId() + "";
            buildRateStatisticsMap(channelStatisticsMap, channelKey, payRateStatistic, "channel");
        }

        Map<String, Object> rateMap = new HashMap<>();

        rateMap.put("merChannelAmount", merChannelAmountStatisticsMap.values());
        rateMap.put("merAmount", merAmountStatisticsMap.values());
           rateMap.put("mer",merStatisticsMap.values());
        rateMap.put("channelAmount", channelAmountStatisticsMap.values());
        rateMap.put("channel", channelStatisticsMap.values());

        rateMap.put("begin", DateFormatUtils.format(early, "HH:mm:ss"));
        rateMap.put("end", DateFormatUtils.format(now, "HH:mm:ss"));

        return rateMap;
    }


    @Override
    public Map<String, Object> inComeOrderRateStatistics(String channelName,String merName, BigDecimal amount, Integer timeUnit,String startTime,String endTime,Integer mcid,String tname,String cname) {

        Date now=null,early=null;
        List<PayRateStatistics> payRateStatistics=null;
        if(timeUnit==-1){
            Date _now = new Date();
            now = CommonUtil.getEndTime(_now);
            early = CommonUtil.getStartTime(_now);
        }else if(timeUnit!=0){
            now = new Date();
            early = new Date(now.getTime() - timeUnit * 1000);
        }

        if((startTime!= null || startTime.isEmpty()==false) && (endTime!= null || endTime.isEmpty()==false)){

            try {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                early = ft.parse(startTime);
                now = ft.parse(endTime);
            } catch (ParseException e) {
    
                e.printStackTrace();
            }            
        }
        

 
        payRateStatistics = payOrderDao.inComeOrderRateStatistics(early, now, channelName, merName, amount, mcid, tname, cname);
        //orderStatus:-2是未获取连接,-1是获取链接失败,0待支付,1支付成功
        Map<String, Object> rateMap = new HashMap<>();
        rateMap.put("merStatistics", payRateStatistics);
        rateMap.put("begin", DateFormatUtils.format(early, "HH:mm:ss"));
        rateMap.put("end", DateFormatUtils.format(now, "HH:mm:ss"));
        rateMap.put("early", early);
        rateMap.put("now", now);
        rateMap.put("channelName", channelName);
        rateMap.put("merName", amount);
        rateMap.put("mcid", mcid);
        rateMap.put("tname", tname);
        rateMap.put("cname", cname);
        return rateMap;
    }

    private void buildRateStatisticsMap(Map<String, PayRateStatistics> statisticsMap, String key, PayRateStatistics payRateStatistics, String modelType) {

        PayRateStatistics value;
        if ((value = statisticsMap.get(key)) == null) {
            value = new PayRateStatistics();
            // 成功和失败的订单合并成一条
            if (1 == payRateStatistics.getOrderStatus()) value.setCount(payRateStatistics.getCount());
            else value.setFailCount(payRateStatistics.getCount());
            switch (modelType) {
                case "merChannelAmount":
                    value.setMerchantId(payRateStatistics.getMerchantId());
                    value.setMerchantName(payRateStatistics.getMerchantName());
                    value.setChannelName(payRateStatistics.getChannelName());
                    value.setClientChannelId(payRateStatistics.getClientChannelId());
                    value.setAmount(payRateStatistics.getAmount());
                    break;
                case "merAmount":
                    value.setMerchantId(payRateStatistics.getMerchantId());
                    value.setMerchantName(payRateStatistics.getMerchantName());
                    value.setAmount(payRateStatistics.getAmount());
                    break;
                case "mer":
                    value.setMerchantId(payRateStatistics.getMerchantId());
                    value.setMerchantName(payRateStatistics.getMerchantName());
                    break;
                case "channelAmount":
                    value.setChannelName(payRateStatistics.getChannelName());
                    value.setClientChannelId(payRateStatistics.getClientChannelId());
                    value.setAmount(payRateStatistics.getAmount());
                    break;
                case "channel":
                    value.setChannelName(payRateStatistics.getChannelName());
                    value.setClientChannelId(payRateStatistics.getClientChannelId());
                    break;
                default:
                    break;
            }
            value.setBegin(payRateStatistics.getBegin());
            value.setEnd(payRateStatistics.getEnd());
            statisticsMap.put(key, value);
        } else {
            if (1 == payRateStatistics.getOrderStatus())
                value.setCount(value.getCount() + payRateStatistics.getCount());
            else value.setFailCount(value.getFailCount() + payRateStatistics.getCount());
        }

    }


}
