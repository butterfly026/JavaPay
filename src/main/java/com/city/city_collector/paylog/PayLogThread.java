package com.city.city_collector.paylog;

import com.city.city_collector.admin.pay.entity.PayLog;
import com.city.city_collector.admin.pay.service.PayLogService;
import com.city.city_collector.configuration.SpringUtil;

public class PayLogThread implements Runnable {

    private PayLog payLog;

    //type==0   日志    type==1 下单日志
    private int type;


    public PayLogThread(PayLog payLog, int type) {
        this.payLog = payLog;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            if (type == 0) {
                if (this.payLog != null) {
                    PayLogService payLogService = (PayLogService) SpringUtil.getBean("payLogService");
                    payLogService.addSave(this.payLog);
                    System.out.println("写入日志成功");
                }
            } else if (type == 1) {
                PayLogService payLogService = (PayLogService) SpringUtil.getBean("payLogService");
                payLogService.addPayOrderAccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("写入日志失败");
        }
    }
}
