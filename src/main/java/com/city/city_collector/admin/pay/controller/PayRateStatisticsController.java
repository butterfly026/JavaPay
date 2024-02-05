package com.city.city_collector.admin.pay.controller;


import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.admin.pay.service.PayRateStatisticsService;
import com.city.city_collector.common.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
@RequestMapping("/system/payOrderStatistics")
public class PayRateStatisticsController {

    @Autowired
    private PayRateStatisticsService payRateStatisticsService;

    @RequestMapping("/view")
    @AdminPermission(value = {"admin:payOrderStatistics:view"})
    public String view(Model model) {
        return "admin/pay/payOrderStatistics/view";
    }

    @RequestMapping("/newview")
    @AdminPermission(value = {"admin:payOrderStatistics:view"})
    public String newview(Model model) {
        return "admin/pay/payOrderStatistics/newview";
    }

    @RequestMapping("/inComeOrder")
    @ResponseBody
    @AdminPermission(value = {"admin:payOrderStatistics:incomeOrder"})
    public Message inComeOrderStatistics(String channelName, String merName, BigDecimal amount, Integer timeUnit) {
        return Message.success("success", payRateStatisticsService.selectInComeOrderStatistics(channelName, merName, amount, timeUnit));
    }


    @RequestMapping("/rateStatistics")
    @ResponseBody
    @AdminPermission(value = {"admin:payOrderStatistics:rateStatistics"})
    public Message inComeOrderRateStatistics(String channelName, String merName, BigDecimal amount, Integer timeUnit) {
        return Message.success("success", payRateStatisticsService.inComeOrderRateStatistics(channelName, merName, amount, timeUnit));
    }

    @RequestMapping("/rateStatisticsNew")
    @ResponseBody
    @AdminPermission(value = {"admin:payOrderStatistics:rateStatistics"})
    public Message inComeOrderRateStatistics(String channelName, String merName, BigDecimal amount, Integer timeUnit,String startTime,String endTime,Integer mcid,String tname,String cname) {
        return Message.success("success", payRateStatisticsService.inComeOrderRateStatistics(channelName, merName, amount, timeUnit,startTime,endTime,mcid,tname,cname));
    }

    @RequestMapping("/todayOrderInfo")
    @ResponseBody
    @AdminPermission(value = {"admin:payOrderStatistics:todayOrderInfo"})
    public Message todayOrderInfo() {
        return Message.success("success", payRateStatisticsService.todayOrderInfo());
    }

}
