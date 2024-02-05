
package com.city.city_collector.common.util;

import com.city.city_collector.admin.pay.service.PayOrderService;
import com.city.city_collector.configuration.SpringUtil;

/**
 * 备用查单进程
 */
public class OrderDealThread implements Runnable {


    @Override
    public void run() {
        PayOrderService payService = (PayOrderService) SpringUtil.getBean("payOrderService");
        System.out.println("备用线程启动:" + payService);
        while (true) {
            try {
                Thread.sleep(6000L);
                payService.orderSuccess(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
