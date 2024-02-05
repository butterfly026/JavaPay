package com.city.city_collector.admin.pay.service;

import com.city.city_collector.admin.pay.entity.PayInComeOrder;
import com.city.city_collector.admin.pay.entity.PayRateStatistics;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PayRateStatisticsService {

    Map<String, Object> selectInComeOrderStatistics(String channelName, String merName, BigDecimal amount, Integer timeUnit);

    

    Map<String, Object> inComeOrderRateStatistics(String channelName, String merName, BigDecimal amount, Integer timeUnit);


    Map<String, Object> inComeOrderRateStatistics(String channelName,  String merName, BigDecimal amount, Integer timeUnit,String startTime,String endTime,Integer mcid,String tname,String cname);

    List<Map<String, Object>> todayOrderInfo();

}
