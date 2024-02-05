package com.city.city_collector.admin.pay.controller;

import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.common.util.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/pay/USDTCash")
public class PayCashUSDTController {


    @RequestMapping("/view")
    public String view(Model model) {
        return "admin/pay/paycashusdt/paycash_usdt";
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize) {
        return null;
    }

    //TODO 创建上的问题要自己摸索了
    @RequestMapping("/addSave")
    @ResponseBody
    public Message addSave(String linkType, String walletAddr, String payCashType, String payCashChannel, BigDecimal payCashAmount, String payPsw) {

        return null;
    }

    //TODO 自动下发
    @RequestMapping("/config")
    @ResponseBody
    public Message config(String linkType, String walletAddr, String payCashType, String payCashChannel, BigDecimal payCashAmount, String payPsw, String state) {

        return null;
    }

    //TODO 手续费和实时汇率问题

    @RequestMapping("/receive/{mid}")
    @ResponseBody
    public Message receive() {

        return null;
    }


}
