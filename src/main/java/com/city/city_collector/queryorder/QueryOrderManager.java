
package com.city.city_collector.queryorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.service.PayOrderService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.GroovyScript;

/**
 * @author nb
 * @Description:
 */
public class QueryOrderManager {

    /**
     * 线程池对象
     */
    private ThreadPoolExecutor executor;

    private final static QueryOrderManager qom = new QueryOrderManager();

    private QueryOrderManager() {
        executor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    private boolean run = false;

    /**
     * 处理的总数
     */
    private int allCount = 0;

    /**
     * 同时查询的订单最大数量
     */
    private int maxDownLoadCount = 20;

    /**
     * 成功订单数
     */
    private int success = 0;

    /**
     * 失败订单数
     */
    private int fail = 0;

//    /***
//     * 当前使用的脚本
//     */
//    private GroovyScript gs=null;
//    
//    /**
//     * 订单service
//     */
//    private PayOrderService payOrderService;
//    /** 上游通道 */
//    private PayClientChannel payClientChannel;
//    /** 上游对象 */
//    private SysUser user;
//    
//    /** 通道参数 */
//    private Map < String, Object> channelParams;
//    
//    /** 需要查询的订单列表 */
//    private List < PayOrder > orders;

    /** 订单索引 */
//    private int orderIndex=0;

    /**
     * 错误日志
     */
    private List<String> errorLogs = new ArrayList<String>();

    /***
     * 等待处理的队列
     */
    private List<QueryOrderThread> queueList = new Vector<QueryOrderThread>();
    /***
     * 正在处理的队列
     */
    private List<QueryOrderThread> runningList = new Vector<QueryOrderThread>();

    /**
     * 获取实例
     *
     * @return
     */
    public static QueryOrderManager getInstance() {
        return qom;
    }

    /**
     * run
     *
     * @return run
     */
    public boolean isRun() {
        return run;
    }

    /**
     * 开始查单
     *
     * @param payOrderService
     * @param gs
     * @param payClientChannel
     * @param user
     * @param channelParams
     * @param orders
     * @return
     */
    public QueryOrderMsg startQueryOrder(PayOrderService payOrderService, GroovyScript gs, PayClientChannel payClientChannel, SysUser user, Map<String, Object> channelParams, List<PayOrder> orders) {

        if (orders == null) {
            return new QueryOrderMsg(false, "查单列表为空");
        }

        if (run) {
            return new QueryOrderMsg(false, "查单程序正在运行,请停止正在运行的查单程序后再重新尝试");
        }

        run = true;

//        this.payOrderService=payOrderService;
//        this.payClientChannel=payClientChannel;
//        this.user=user;
//        this.gs=gs;
//        this.channelParams=channelParams;
//        this.orders=orders;

        this.allCount = orders.size();
        this.success = 0;
        this.fail = 0;
        this.errorLogs.clear();
//        this.orderIndex=0;

        this.queueList = new Vector<QueryOrderThread>();
        for (PayOrder order : orders) {
            QueryOrderThread queryOrderThread = new QueryOrderThread(payOrderService, gs, payClientChannel, user, channelParams, order);
            this.queueList.add(queryOrderThread);
        }
        this.runningList = new Vector<QueryOrderThread>();

        updateThreadPool();

        return new QueryOrderMsg(true, "查单程序启动成功!");
    }

    /**
     * 更新线程池
     */
    public synchronized void updateThreadPool() {
        QueryOrderThread queryOrderThread = null;
        //清理运行的线程中的终止线程
        for (int i = 0; i < runningList.size(); i++) {
            queryOrderThread = runningList.get(i);
            if (queryOrderThread.isComplete()) {
                runningList.remove(queryOrderThread);
                i--;
            }
        }

        if (runningList.size() < maxDownLoadCount) {//小于线程池的时候，添加任务
            System.out.println("运行行程较少，增加任务:" + runningList.size() + "   " + maxDownLoadCount);
            for (int i = 0; i < queueList.size(); i++) {
                queryOrderThread = queueList.get(i);
                executor.execute(queryOrderThread);
                runningList.add(queryOrderThread);
                queueList.remove(i);
                i--;
                if (runningList.size() >= maxDownLoadCount) {
                    break;
                }
            }
        }

        if (runningList.isEmpty() && queueList.isEmpty()) {
            //查单完成
            this.run = false;
        }
    }

    /**
     * 停止查单
     */
    public void shutDown() {
        queueList.clear();
    }

    /**
     * 查单失败的日志
     *
     * @param log
     */
    public void createErrorLog(String log) {
        errorLogs.add(log);
    }

    /**
     * 获取错误日志
     *
     * @return
     */
    public List<String> getErrorLogs() {
        return errorLogs;
    }

    /**
     * 获取查单的信息
     * total 总订单数    surplus  剩余订单数   query 正在处理的订单   run 是否正在运行
     *
     * @return
     */
    public Map<String, Object> getQueryInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", allCount);
        map.put("surplus", queueList.size());
        map.put("complete", allCount - queueList.size());
        map.put("query", runningList.size());

        map.put("success", success);
        map.put("fail", fail);
        map.put("run", run);
        return map;
    }

    public void addSuccess() {
        this.success++;
    }

    public void addFail() {
        this.fail++;
    }
}
