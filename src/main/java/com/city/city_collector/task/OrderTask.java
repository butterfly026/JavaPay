package com.city.city_collector.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.service.PayCashService;
import com.city.city_collector.admin.pay.service.PayOrderService;

@Component
public class OrderTask {

    @Autowired
    PayOrderService payOrderService;
    @Autowired
    PayCashService payCashService;

    /**
     * 订单成功时处理
     *
     * @author:nb
     */
    @Scheduled(cron = "0/2 * * * * ?")
    public synchronized void orderSuccess() {
        payOrderService.orderSuccess(0);
//        System.out.println("order deal...");
////        payOrderService.orderSuccess();
//        
//        long l=System.currentTimeMillis();
//        List < PayOrder > poList = payOrderService.querySuccessOrder();
//        System.out.println("querytime:"+(System.currentTimeMillis()-l));
////        System.out.println("order1:"+poList);
//        //处理订单
//        if(poList!=null && !poList.isEmpty()) {
//            for(PayOrder po:poList) {
//                l=System.currentTimeMillis();
//                payOrderService.orderSuccessSingle(po);
//                System.out.println("ordertime:"+po.getId()+">>"+(System.currentTimeMillis()-l));
//            }
//        }
    }


//    @Scheduled(cron = "0/3 * * * * ?")
//    public synchronized void clearConnection() {
//        try {
//            ConnectionPool cp=ApplicationData.getConnectionPool();
//            System.out.println("连接数:"+cp.connectionCount()+"--空闲数:"+cp.idleConnectionCount());
//            //移除空闲连接
//            cp.evictAll();
//            System.out.println("清理后连接数:"+cp.connectionCount()+"--空闲数:"+cp.idleConnectionCount());
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 订单通知
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void orderNotify() {
        //单台服务器模式
        Integer serverMode = ApplicationData.getInstance().getConfig().getServermodel();
        //cyber修改 服务器模式1，2都走这里
        if ((serverMode.intValue() == 1) || (serverMode.intValue() == 2)) {
            long l1 = System.currentTimeMillis();
            //查询sql service类的方法与同前缀的xml对应
            List<PayOrder> poList = payOrderService.queryNeedNotifyOrder();
            System.out.println("notifysearch:" + (System.currentTimeMillis() - l1) + " serverMode:" + serverMode.toString());
            //处理订单
            for (PayOrder po : poList) {
                payOrderService.sendOrderNotify(po);
            }
            System.out.println("notifycomplete1:" + (System.currentTimeMillis() - l1) + " serverMode:" + serverMode.toString());

            List<PayCash> payCashList = payCashService.queryNeedNotifyCashOrder();
            for (PayCash pc : payCashList) {
                payCashService.sendCashNotify(pc);
            }
            System.out.println("notifycomplete2:" + (System.currentTimeMillis() - l1) + " serverMode:" + serverMode.toString());
        }
        //cyber修改，通过高速队列处理
    }
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void autoCash()  throws Exception{
//        Map<String, List<Long>> stringListMap = payCashService.queryFinishListANAN();
//        Map<String, List<Long>> stringListMap2 = payCashService.queryFinishListJISU();
//        Map<String, List<Long>> stringListMap3 = payCashService.queryFinishListXiaoQiang();
//
//        dealCashMap(stringListMap);
//        dealCashMap(stringListMap2);
//        dealCashMap(stringListMap3);
//    }
//
//    private void dealCashMap( Map<String, List<Long>> stringListMap){
//        if (null==stringListMap) return;
//
//        List<Long> longs = stringListMap.get("success");
//        List<Long> longsFail = stringListMap.get("fail");
//
//        for (Long aLong : longs) {
//            payCashController.cashSuc(aLong,"代付 查询 自动成功");
//        }
//
//        for (Long aLong : longsFail) {
//            payCashController.cashFail(aLong,"代付 查询 自动失败");
//        }
//
//    }
}
