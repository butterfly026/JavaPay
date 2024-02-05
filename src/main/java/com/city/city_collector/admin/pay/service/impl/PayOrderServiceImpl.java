package com.city.city_collector.admin.pay.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.city.util.Logger;
import com.city.city_collector.admin.pay.dao.PayOrderDao;
import com.city.city_collector.admin.pay.dao.PaytmOrderDao;
import com.city.city_collector.admin.pay.entity.PayClientBalance;
import com.city.city_collector.admin.pay.entity.PayMemchantRecord;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.entity.PayProxyRecord;
import com.city.city_collector.admin.pay.entity.PaytmOrder;
import com.city.city_collector.admin.pay.service.PayClientBalanceService;
import com.city.city_collector.admin.pay.service.PayMemchantRecordService;
import com.city.city_collector.admin.pay.service.PayOrderService;
import com.city.city_collector.admin.pay.service.PayProxyRecordService;
import com.city.city_collector.admin.system.dao.SysUserDao;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.util.Base64;
import com.city.city_collector.channel.util.SignUtil;
import com.city.city_collector.channel.util.TimeUtil;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.util.ApiMessageCode;
import com.city.city_collector.common.util.SnUtil;
import com.city.city_collector.paylog.PayLogManager;
import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.catalina.Lifecycle;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:支付订单-service实现类
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Service("payOrderService")
public class PayOrderServiceImpl implements PayOrderService {
    @Autowired
    PayOrderDao payOrderDao;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    PayProxyRecordService payProxyRecordService;
    @Autowired
    PayMemchantRecordService payMemchantRecordService;
    @Autowired
    PayClientBalanceService payClientBalanceService;
    @Autowired
    SysUserDao sysUserDao;
    @Autowired
    PaytmOrderDao paytmOrderDao;

    /**
     * Description:分页查询
     *
     * @param params 参数键值对象
     * @param page   分页对象
     * @param orders 排序
     * @return Page
     * @author:demo
     * @since 2020-6-29
     */
    public Page queryPage(Map<String, Object> params, Page page, String[] orders) {
        //获取记录总数
        long l1 = System.currentTimeMillis();
        int total = payOrderDao.queryCount(params);
        page.setTotal(total);
        params.put("start", page.getStartRow());
        params.put("end", page.getPageSize());
        //拼接排序字段
        String orderby = "";
        if (orders != null && orders.length > 0) {

            for (String str : orders) {
                String[] arr1 = str.split(" ");
                if (arr1.length == 2) {
                    orderby += "`" + arr1[0] + "` " + arr1[1] + ",";
                }
            }
            if (orderby.endsWith(",")) {
                orderby = orderby.substring(0, orderby.length() - 1);
            }
            params.put("orderby", orderby);
        }
        //获取分页数据
        List<Long> idList = payOrderDao.queryPageIds(params);
        if (idList != null && !idList.isEmpty()) {
            String ids = "";
            for (Long id : idList) {
                ids += id + ",";
            }
            ids = ids.substring(0, ids.length() - 1);
            params = new HashMap<String, Object>();
            params.put("ids", ids);
            if (StringUtils.isNotBlank(orderby)) {
                params.put("orderby", orderby);
            }
            page.setResults(payOrderDao.queryPageNew(params));
        }
        System.out.println(System.currentTimeMillis() - l1);
        return page;
    }

    /**
     * Description:保存记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    @Override
    public void addSave(PayOrder payOrder) {
        payOrderDao.addSave(payOrder);
    }

    @Override
    public void updateOrderStatus(PayOrder payOrder) {
        payOrderDao.updateOrderStatus(payOrder);
    }

    @Override
    public void updateOrderPlat(PayOrder payOrder) {
        payOrderDao.updateOrderPlat(payOrder);
    }

    @Override
    public void updateOrderSuccess(PayOrder payOrder) {
        payOrderDao.updateOrderSuccess(payOrder);
    }

    @Transactional
    public void addSaveByPaytm(PayOrder payOrder, PaytmOrder paytmOrder) {
        payOrderDao.addSave(payOrder);

        paytmOrderDao.addSave(paytmOrder);
    }

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    @Override
    public PayOrder querySingle(Map<String, Object> params) {
        return payOrderDao.querySingle(params);
    }

    /**
     * Description:更新记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    @Override
    public void editSave(PayOrder payOrder) {
        payOrderDao.editSave(payOrder);
    }

    /**
     * Description:删除指定记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    @Override
    public void delete(Long[] ids) {
        String ids_str = "";
        for (int i = 0; i < ids.length; i++) {
            ids_str += ids[i] + ",";
        }
        if (ids_str.endsWith(",")) {
            ids_str = ids_str.substring(0, ids_str.length() - 1);
        }
        payOrderDao.delete(ids_str);
    }

    /**
     * 更新订单状态
     *
     * @param payOrder
     * @author:nb
     */
    public void updateOrderPayStatus(PayOrder payOrder) {
        payOrderDao.updateOrderPayStatus(payOrder);
    }

    /**
     * 订单成功处理
     */
//    @Transactional(rollbackFor = Exception.class)
//    public void orderSuccess() {
//        System.out.println("成功的订单处理...");
//        //查询成功尚未处理过的订单
//        List < PayOrder > poList = payOrderDao.querySuccessOrder();
//        //处理订单
//        for(PayOrder po:poList) {
//
//          //订单金额
//            BigDecimal orderMoney=po.getMoney();
//            //---更新商户的余额
//            SysUser merchant=sysUserService.querySysUserById(po.getMerchantId());
//            BigDecimal money=merchant.getMoney();
//            if(money==null) money=BigDecimal.ZERO;
//
//            SysUser proxy=null;
//            if(merchant.getProxyId()!=null) {
//                proxy=sysUserService.querySysUserById(merchant.getProxyId());
//            }
//            //100
//            BigDecimal hundred=new BigDecimal(100);
//            //商户收入=订单金额*(100-商户费率)/100
//            //商户手续费=订单金额*商户费率/100
//            BigDecimal merchantCommission=orderMoney.multiply(po.getMerchantRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
//            BigDecimal merchantMoney=orderMoney.subtract(merchantCommission);
//            merchant.setMoney(money.add(merchantMoney));
//            sysUserService.updateUserMoney(merchant);
//
//            //保存余额变更记录
//            PayMemchantRecord pmr=new PayMemchantRecord();
//            pmr.setNo(SnUtil.createSn(""));
//            pmr.setMerchantId(merchant.getId());
//            pmr.setMerchantNo(merchant.getUsername());
//            pmr.setMerchantName(merchant.getName());
//            pmr.setOrderSn(po.getSn());
//            pmr.setMoney(merchantMoney);
//            pmr.setBalance(merchant.getMoney());
//            pmr.setType(2);
//            pmr.setRemark("订单金额:"+orderMoney+"元,费率"+po.getMerchantRatio()+"%,扣除手续费:"+merchantCommission+"元");
//            payMemchantRecordService.addSave(pmr);
//
//            BigDecimal proxyCommission=BigDecimal.ZERO;
//            if(proxy!=null) {//代理手续费
//                proxyCommission=orderMoney.multiply(po.getProxyRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
//                if(proxy.getMoney()==null) proxy.setMoney(BigDecimal.ZERO);
//                proxy.setMoney(proxy.getMoney().add(proxyCommission));
//                sysUserService.updateUserMoney(proxy);
//
//                PayProxyRecord ppr=new PayProxyRecord();
//                ppr.setNo(SnUtil.createSn(""));
//                ppr.setProxyId(proxy.getId());
//                ppr.setProxyNo(proxy.getUsername());
//                ppr.setProxyName(proxy.getName());
//                ppr.setOrderSn(po.getSn());
//                ppr.setMoney(proxyCommission);
//                ppr.setBalance(proxy.getMoney());
//                ppr.setType(2);
//                ppr.setRemark("订单手续费，订单金额:"+orderMoney+"元,返佣费率:"+po.getProxyRatio()+"%");
//                payProxyRecordService.addSave(ppr);
//            }
//
//            //上游余额
//            SysUser client=sysUserService.querySysUserById(po.getClientId());
//            if(client.getMoney()==null) client.setMoney(BigDecimal.ZERO);
//            BigDecimal clientComission=orderMoney.multiply(po.getClientRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
//            BigDecimal clientMoney=orderMoney.subtract(clientComission);
//            client.setMoney(client.getMoney().add(clientMoney));
//
//            sysUserService.updateUserMoney(client);
//
//            PayClientBalance pcb=new PayClientBalance();
//            pcb.setNo(SnUtil.createSn(""));
//            pcb.setClientId(client.getId());
//            pcb.setClientNo(client.getUsername());
//            pcb.setClientName(client.getName());
//            pcb.setOrderSn(po.getSn());
//            pcb.setMoney(clientMoney);
//            pcb.setBalance(client.getMoney());
//            pcb.setType(2);
//            pcb.setRemark("订单金额:"+orderMoney+"元,费率"+po.getClientRatio()+"%,扣除手续费:"+clientComission+"元");
//            payClientBalanceService.addSave(pcb);
//
//            //添加订单收益，更新订单状态
//            po.setNotifyStatus(1);
//
//            po.setProfit(merchantCommission.subtract(clientComission).subtract(proxyCommission));
//            payOrderDao.dealSuccessOrder(po);
//
//            if(po.getId().longValue()==6L) {
//                throw new RuntimeException("t1");
//            }
//        }
//    }

//    /**
//     * 订单成功后的单个处理
//     * @author:nb
//     * @param po
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public synchronized void orderSuccessSingle(PayOrder po) {
//        try {
//
//            //订单金额
//            BigDecimal orderMoney=po.getMoney();
//            //---更新商户的余额
//            SysUser merchant=sysUserService.querySysUserById(po.getMerchantId());
//            BigDecimal money=merchant.getMoney();
//            if(money==null) money=BigDecimal.ZERO;
//
//            SysUser proxy=null;
//            if(merchant.getProxyId()!=null) {
//                proxy=sysUserService.querySysUserById(merchant.getProxyId());
//            }
//            //100
//            BigDecimal hundred=new BigDecimal(100);
//            //商户收入=订单金额*(100-商户费率)/100
//            //商户手续费=订单金额*商户费率/100
//            BigDecimal merchantCommission=orderMoney.multiply(po.getMerchantRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
//            BigDecimal merchantMoney=orderMoney.subtract(merchantCommission);
//
//            SysUser u1=new SysUser();
//            u1.setId(merchant.getId());
//            u1.setMoney(merchantMoney);
//            u1.setFrozenMoney(BigDecimal.ZERO);
////            merchant.setMoney(money.add(merchantMoney));
//            sysUserDao.userFrozenMoneyAndMoney(u1);
//
//            merchant=sysUserService.querySysUserById(po.getMerchantId());
//            //保存余额变更记录
//            PayMemchantRecord pmr=new PayMemchantRecord();
//            pmr.setNo(SnUtil.createSn(""));
//            pmr.setMerchantId(merchant.getId());
//            pmr.setMerchantNo(merchant.getUsername());
//            pmr.setMerchantName(merchant.getName());
//            pmr.setOrderSn(po.getSn());
//            pmr.setMoney(merchantMoney);
//            pmr.setBalance(merchant.getMoney());
//            pmr.setType(2);
//
//            pmr.setCommission(merchantCommission);
//            pmr.setRatio(po.getMerchantRatio());
//
//            pmr.setRemark("订单金额:"+orderMoney+"元,费率"+po.getMerchantRatio()+"%,扣除手续费:"+merchantCommission+"元");
//            payMemchantRecordService.addSave(pmr);
//
//            BigDecimal proxyCommission=BigDecimal.ZERO;
//            if(proxy!=null) {//代理手续费
//                proxyCommission=orderMoney.multiply(po.getProxyRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
//                if(proxy.getMoney()==null) proxy.setMoney(BigDecimal.ZERO);
//                proxy.setMoney(proxy.getMoney().add(proxyCommission));
////                sysUserService.updateUserMoney(proxy);
//
//                u1=new SysUser();
//                u1.setId(proxy.getId());
//                u1.setMoney(proxyCommission);
//                u1.setFrozenMoney(BigDecimal.ZERO);
//                sysUserDao.userFrozenMoneyAndMoney(u1);
//                proxy=sysUserService.querySysUserById(merchant.getProxyId());
//
//                PayProxyRecord ppr=new PayProxyRecord();
//                ppr.setNo(SnUtil.createSn(""));
//                ppr.setProxyId(proxy.getId());
//                ppr.setProxyNo(proxy.getUsername());
//                ppr.setProxyName(proxy.getName());
//                ppr.setOrderSn(po.getSn());
//                ppr.setMoney(proxyCommission);
//                ppr.setBalance(proxy.getMoney());
//                ppr.setType(2);
//
//                ppr.setCommission(proxyCommission);
//                ppr.setRatio(po.getProxyRatio());
//
//                ppr.setRemark("订单手续费，订单金额:"+orderMoney+"元,返佣费率:"+po.getProxyRatio()+"%");
//                payProxyRecordService.addSave(ppr);
//
//                po.setProxyId(proxy.getId());
//                po.setProxyNo(proxy.getUsername());
//            }
//
//            //上游余额
//            SysUser client=sysUserService.querySysUserById(po.getClientId());
//            if(client.getMoney()==null) client.setMoney(BigDecimal.ZERO);
//            BigDecimal clientComission=orderMoney.multiply(po.getClientRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
//            BigDecimal clientMoney=orderMoney.subtract(clientComission);
////            client.setMoney(client.getMoney().add(clientMoney));
//
////            sysUserService.updateUserMoney(client);
//            u1=new SysUser();
//            u1.setId(client.getId());
//            u1.setMoney(clientMoney);
//            u1.setFrozenMoney(BigDecimal.ZERO);
//            sysUserDao.userFrozenMoneyAndMoney(u1);
//            client=sysUserService.querySysUserById(po.getClientId());
//
//            PayClientBalance pcb=new PayClientBalance();
//            pcb.setNo(SnUtil.createSn(""));
//            pcb.setClientId(client.getId());
//            pcb.setClientNo(client.getUsername());
//            pcb.setClientName(client.getName());
//            pcb.setOrderSn(po.getSn());
//            pcb.setMoney(clientMoney);
//            pcb.setBalance(client.getMoney());
//            pcb.setType(2);
//
//            pcb.setRatio(po.getClientRatio());
//            pcb.setCommission(clientComission);
//
//            pcb.setRemark("订单金额:"+orderMoney+"元,费率"+po.getClientRatio()+"%,扣除手续费:"+clientComission+"元");
//            payClientBalanceService.addSave(pcb);
//
//            //添加订单收益，更新订单状态
//            BigDecimal profit=merchantCommission.subtract(clientComission).subtract(proxyCommission);
//
//            po.setNotifyStatus(1);
//
//            po.setProfit(profit);
//            payOrderDao.dealSuccessOrder(po);
//
//            //发送订单通知
////            sendOrderNotify(po);
//        }catch(Exception e) {
//            e.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        }
//    }

//    @Transactional(rollbackFor = Exception.class)
    public synchronized void orderSuccessSingle(PayOrder po) {
        try {
            //订单金额
            BigDecimal orderMoney = po.getMoney();
            // 查询商户和代理ID   商户id,代理id
            SysUser merchant = sysUserService.querySysUserById(po.getMerchantId());
            Long merchantId = po.getMerchantId();
            Long proxyId = merchant.getProxyId();
            Long clientId = po.getClientId();
            //100
            BigDecimal hundred = new BigDecimal(100);
            BigDecimal merchantCommission = orderMoney.multiply(po.getMerchantRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal merchantMoney = orderMoney.subtract(merchantCommission);
            String pmrSn = SnUtil.createSn("pmr");
            String pprSn = SnUtil.createSn("ppr");
            String pcbSn = SnUtil.createSn("pcb");

            BigDecimal proxyCommission = BigDecimal.ZERO;
            if (proxyId != null) {
                proxyCommission = orderMoney.multiply(po.getProxyRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            BigDecimal clientCommission = orderMoney.multiply(po.getClientRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal clientMoney = orderMoney.subtract(clientCommission);

            BigDecimal profit = merchantCommission.subtract(clientCommission).subtract(proxyCommission);
            BigDecimal xnprofit = profit;

            Long otherId = null;//已弃用
            BigDecimal otherMoney = BigDecimal.ZERO;
            System.out.println("PROCEDURE ctl:>>>>>>>>>>>>>>>>>");
            payOrderDao.updateOrderInfoByProcedure(po.getId(), merchantId, proxyId, clientId, orderMoney,
                    po.getSn(), merchantMoney, po.getMerchantRatio(), merchantCommission,
                    proxyCommission, po.getProxyRatio(), proxyCommission,
                    clientMoney, po.getClientRatio(), clientCommission,
                    profit, xnprofit, otherId, otherMoney,
                    pmrSn, pprSn, pcbSn);
            //发送订单通知
            //sendOrderNotify(po);
        } catch (Exception e) {
            e.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * 查询已成功且通知状态为未通知的订单
     *
     * @return
     * @author:nb
     */
    public List<PayOrder> querySuccessOrder() {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            List<Long> idList = payOrderDao.querySuccessOrderIdsNew(params);

            if (idList != null && !idList.isEmpty()) {
                String ids = "";
                for (Long id : idList) {
                    ids += id + ",";
                }
                ids = ids.substring(0, ids.length() - 1);
                params = new HashMap<String, Object>();
                params.put("ids", ids);

                return payOrderDao.querySuccessOrderListNew(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<PayOrder>();
    }
    public String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        String result = "";
        for (Map.Entry<String, Object> et : map.entrySet()) {
            if (et.getValue() != null) {
                if (result == "") {
                    result = et.getKey() + "=" + et.getValue();
                } else {
                    result = result + "&" + et.getKey() + "=" + et.getValue();
                }
            }
        }
        return result;
    }
    /**
     * 查询需要通知的订单
     *
     * @return
     */
    public List<PayOrder> queryNeedNotifyOrder() {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            //只查一天之内的
//            params.put("startTime", TimeUtil.getDateMinuteBySS(86400L‬));
            params.put("startTime", TimeUtil.getDateMinuteBySS(86400L));

            return payOrderDao.queryNeedNotifyOrder(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<PayOrder>();
    }

    @Override
    public void sendOrderNotify(PayOrder po) {
        if (ApplicationData.getInstance().getConfig().getServermodel().intValue() == 1) {
                if (po.getDealStatus() == null) po.setDealStatus(0);
                if (po.getNotifyCount() == null) po.setNotifyCount(0);
    
                po.setNotifyCount(po.getNotifyCount() + 1);
                payOrderDao.updateNotifyOrder(po);
    
                SysUser merchant = sysUserService.querySysUserById(po.getMerchantId());
                //发送通知
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Map<String, Object> param = new TreeMap<String, Object>();
                param.put("merchantNo", merchant.getUsername());
                param.put("sn", po.getSn());
                param.put("merchantSn", po.getMerchantSn());
                param.put("orderStatus", po.getOrderStatus() + "");
                param.put("notifyStatus", po.getDealStatus() + "");
                param.put("payTime", sdf.format(po.getPayTime()));
                param.put("createTime", sdf.format(po.getCreateTime()));
                param.put("money", po.getMoney() + "");
                param.put("amount", po.getAmount() + "");
                param.put("remark", po.getRemark());
                param.put("sign", SignUtil.getMD5Sign(param, merchant.getMerchantMy()));
                FormBody.Builder formBuilder = new FormBody.Builder();
                if (param != null && !param.isEmpty()) {
                    Iterator<Entry<String, Object>> iterator = param.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Entry<String, Object> et = iterator.next();
                        if (et.getValue() != null) formBuilder.add(et.getKey(), et.getValue() + "");
                    }
                }
    
                Request request = new Request.Builder()
                                    .url(po.getNotifyUrl())
                                    .post(formBuilder.build())
                                    .build();
    
                OkHttpClient okhttp = new OkHttpClient.Builder()
                                        .connectTimeout(10, TimeUnit.SECONDS)
                                        .writeTimeout(10, TimeUnit.SECONDS)
                                        .readTimeout(10, TimeUnit.SECONDS).build();
    
    
                //创建log信息
                PayLogManager.getInstance().createPayLogByMerchantNotify(po.getSn(), "发起下游商户通知", new Gson().toJson(param), "", po.getClientId(), po.getClientChannelId(), po.getMerchantId(), po.getMerchantChannelId());
                /*
                Request request = new Request.Builder()
                        .url(po.getNotifyUrl())
                        .post(formBuilder.build())
                        .build();
    
                OkHttpClient okhttp = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
                        */
                Call call = okhttp.newCall(request);
    
                call.enqueue(new HttpCallBack(po));
        } else if(ApplicationData.getInstance().getConfig().getServermodel().intValue() == 2) {
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"数据送到队列 HTTPMQ begin");
            //cyber 修改 数据送到高速队列
            if (po.getDealStatus() == null) po.setDealStatus(0);
            if (po.getNotifyCount() == null) po.setNotifyCount(0);

            po.setNotifyCount(po.getNotifyCount() + 1);
            payOrderDao.updateNotifyOrder(po);

            SysUser merchant = sysUserService.querySysUserById(po.getMerchantId());
            //发送通知
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, Object> param = new TreeMap<String, Object>();
            param.put("merchantNo", merchant.getUsername());
            param.put("sn", po.getSn());
            param.put("merchantSn", po.getMerchantSn());
            param.put("orderStatus", po.getOrderStatus() + "");
            param.put("notifyStatus", po.getDealStatus() + "");
            param.put("payTime", sdf.format(po.getPayTime()));
            param.put("createTime", sdf.format(po.getCreateTime()));
            param.put("money", po.getMoney() + "");
            param.put("amount", po.getAmount() + "");
            param.put("remark", po.getRemark());
            
            param.put("sign", SignUtil.getMD5Sign(param, merchant.getMerchantMy()));

           
            FormBody.Builder formBuilder = new FormBody.Builder();

            if (param != null && !param.isEmpty()) {
                Iterator<Entry<String, Object>> iterator = param.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Object> et = iterator.next();
                    if (et.getValue() != null) formBuilder.add(et.getKey(), et.getValue() + "");
                }
            }
            
            //创建log信息
            PayLogManager.getInstance().createPayLogByMerchantNotify(po.getSn(), "发起下游商户通知 SRV2", new Gson().toJson(param), "", po.getClientId(), po.getClientChannelId(), po.getMerchantId(), po.getMerchantChannelId());
            
            /*
            {
                merchantNo	String	是		商户号
                sn	        String	是		平台订单号
                merchantSn	String	是		商户订单编号
                orderStatus	Double	是		订单状态：0,待支付。1，支付成功
                notifyStatus	Double	是		通知状态：0，未通知。1，已通知
                payTime	    String	是		交易时间
                                                    格式：yyyy-MM-dd HH:mm:ss
                createTime	String	是		创建时间
                                            格式：yyyy-MM-dd HH:mm:ss
                money	double	是		订单金额
                amount	double	是		交易金额
                remark	String	否		备注，由下单接口传入  为空则不参与签名
                sign	String			签名

                {"queryType":"orderSuccessNotify",
                "queryUrl":"http://18.167.146.113/api/pay/notify/271",
                "queryFormat":"POST",
                "queryData":"status=1&customerid=11028&sdpayno=LN2022052313434298291a60&sdorderno=LN2022052313434298291a60&order_fee=343.23&total_fee=343.23&paytype=alism&remark=&sign=cb6e13878defbf029633fcdac6e0f9a2",
                "returnUrl":"http://127.0.0.1/AsyncEvent/resGet"}

                {"queryData":{"encodedNames":["amount","createTime","merchantNo","merchantSn","money","notifyStatus","orderStatus","payTime","sign","sn"],"encodedValues":["232.23","2022-05-23%2019%3A42%3A48","test_merchant","KK5b1c37c2bd700e0b1401","232.23","0","1","2022-05-23%2016%3A02%3A43","4EE9685B66538D8FC74978B557C51F7F","LN202205231142457307b1fd"]},"queryFormat":"POST","queryType":"orderSuccessNotify","queryUrl":"http://18.167.146.113","returnUrl":"http://127.0.0.1/AsyncEvent/resGet"}
            }
            */
            
            String urlparams = this.getUrlParamsByMap(param);
            Map<String, Object> notifyParam = new TreeMap<String, Object>();
            notifyParam.put("queryType", "orderSuccessNotify");
            notifyParam.put("queryUrl", po.getNotifyUrl());
            notifyParam.put("queryFormat", "POST");
            notifyParam.put("queryData", urlparams);
            notifyParam.put("returnUrl", ApplicationData.getInstance().getConfig().getNotifyurl() + "/api/pay/mqResponseNotify");
            
            String json = new Gson().toJson(notifyParam);
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"数据送到队列 HTTPMQ - json:\r" + json);

            String queueUrl = ApplicationData.getInstance().getConfig().getQueueUrl().replaceAll("amp;", "");
            String queueRequest=queueUrl + "&data="+ Base64.encodeToString(json);
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"HTTPMQ put = " + queueRequest);

            Request request = new Request.Builder()
                                .url(queueRequest)
                                .build();

            OkHttpClient okhttp = new OkHttpClient.Builder()
                                    .connectTimeout(10, TimeUnit.SECONDS)
                                    .writeTimeout(10, TimeUnit.SECONDS)
                                    .readTimeout(10, TimeUnit.SECONDS).build();

            Call call = okhttp.newCall(request);
            
            call.enqueue(new HttpCallBack(po));

            //设置通知订单成功
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"默认设置通知订单成功\r\n");
            po.setDealStatus(1);
            payOrderDao.updateNotifyOrder(po);
            String logStr = "<span style='color:green;'>[通知成功]</span>";
            PayLogManager.getInstance().createPayLogByMerchantNotify(po.getSn(), logStr + "通知下游商户", "", "Response默认设置", po.getClientId(), po.getClientChannelId(), po.getMerchantId(), po.getMerchantChannelId());
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"数据送到队列 HTTPMQ end");

         } else{
            System.out.println("send notify...");
            // 向远程服务器发送请求
            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("sn", po.getSn());
            formBuilder.add("mmpkey", "Gkks@11@skd=HSQWoiL^2");

            Request request = new Request.Builder()
                    .url(ApplicationData.getInstance().getConfig().getPaynotifyurl() + "/orderNmbNotify")
                    .post(formBuilder.build())
                    .build();
            OkHttpClient okhttp = new OkHttpClient.Builder().connectTimeout(20L, TimeUnit.SECONDS).writeTimeout(20L, TimeUnit.SECONDS).readTimeout(20L, TimeUnit.SECONDS).connectionPool(ApplicationData.getConnectionPool()).build();
            Call call = okhttp.newCall(request);
            call.enqueue(new HttpCallBack(po));        
        }
    }

    class HttpCallBack implements Callback {
        PayOrder po = null;

        public HttpCallBack(PayOrder po) {
            this.po = po;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            System.out.println("发送订单回调请求失败===" + po.getSn() + "," + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response rsp) throws IOException {
            if (ApplicationData.getInstance().getConfig().getServermodel().intValue() == 1) {
                String logStr = "";
                String responseStr = "";
                if (rsp.isSuccessful()) {
                    String data = rsp.body().string();
                    System.out.println("订单回调请求发送成功===" + po.getSn() + "," + data);
                    if (po != null && StringUtils.isNotBlank(data) && "success".equals(data)) {
                        po.setDealStatus(1);
                        payOrderDao.updateNotifyOrder(po);
                    }
                    logStr = "<span style='color:green;'>[通知成功]</span>";
                    responseStr = data;
                } else {
                    System.out.println("交互失败===");
                    logStr = "<span style='color:red;'>[通知失败]</span>";
                    responseStr = rsp.code() + "," + rsp.message();
                }
                PayLogManager.getInstance().createPayLogByMerchantNotify(po.getSn(), logStr + "通知下游商户", "", responseStr, po.getClientId(), po.getClientChannelId(), po.getMerchantId(), po.getMerchantChannelId());
            }
            System.out.println("发送订单回调请求成功===" + po.getSn());
            rsp.close();

        }

    }

    /**
     * 查询订单详细信息 -
     *
     * @param id
     * @return
     * @author:nb
     */
    public Map<String, Object> queryOrderDetailNoPay(Long id, String sn) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("sn", sn);
        return payOrderDao.queryOrderDetailNoPay(params);
    }

    public Map<String, Object> queryOrderDetailSuccess(Long id, String sn) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("sn", sn);
        return payOrderDao.queryOrderDetailSuccess(params);
    }

    public void updateOrderMendStatus(PayOrder po) {
        payOrderDao.updateOrderMendStatus(po);
    }

    /**
     * 补单-弃用
     */
//    @Transactional(rollbackFor = Exception.class)
//    public void mendOrderStatus(PayOrder po) {
//        //订单金额
//        BigDecimal orderMoney = po.getMoney();
//        //---更新商户的余额
//        SysUser merchant = sysUserService.querySysUserById(po.getMerchantId());
//        BigDecimal money = merchant.getMoney();
//        if (money == null) money = BigDecimal.ZERO;
//
//        SysUser proxy = null;
//        if (merchant.getProxyId() != null) {
//            proxy = sysUserService.querySysUserById(merchant.getProxyId());
//        }
//        //100
//        BigDecimal hundred = new BigDecimal(100);
//        //商户收入=订单金额*(100-商户费率)/100
//        //商户手续费=订单金额*商户费率/100
//        BigDecimal merchantCommission = orderMoney.multiply(po.getMerchantRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal merchantMoney = orderMoney.subtract(merchantCommission);
////        merchant.setMoney(money.add(merchantMoney));
////        sysUserService.updateUserMoney(merchant);
//        SysUser u1 = new SysUser();
//        u1.setId(merchant.getId());
//        u1.setMoney(merchantMoney);
//        u1.setFrozenMoney(BigDecimal.ZERO);
//        sysUserDao.userFrozenMoneyAndMoney(u1);
//
//        //保存余额变更记录
//        PayMemchantRecord pmr = new PayMemchantRecord();
//        pmr.setNo(SnUtil.createSn(""));
//        pmr.setMerchantId(merchant.getId());
//        pmr.setMerchantNo(merchant.getUsername());
//        pmr.setMerchantName(merchant.getName());
//        pmr.setOrderSn(po.getSn());
//        pmr.setMoney(merchantMoney);
//        pmr.setBalance(money.add(merchantMoney));
//        pmr.setType(2);
//
//        pmr.setCommission(merchantCommission);
//        pmr.setRatio(po.getMerchantRatio());
//
//        pmr.setRemark("订单金额:" + orderMoney + "元,费率" + po.getMerchantRatio() + "%,扣除手续费:" + merchantCommission + "元");
//        payMemchantRecordService.addSave(pmr);
//
//        BigDecimal proxyCommission = BigDecimal.ZERO;
//        if (proxy != null) {//代理手续费
//            proxyCommission = orderMoney.multiply(po.getProxyRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
//            if (proxy.getMoney() == null) proxy.setMoney(BigDecimal.ZERO);
//
////            proxy.setMoney(proxy.getMoney().add(proxyCommission));
////            sysUserService.updateUserMoney(proxy);
//            u1 = new SysUser();
//            u1.setId(proxy.getId());
//            u1.setMoney(proxyCommission);
//            u1.setFrozenMoney(BigDecimal.ZERO);
//            sysUserDao.userFrozenMoneyAndMoney(u1);
//
//            PayProxyRecord ppr = new PayProxyRecord();
//            ppr.setNo(SnUtil.createSn(""));
//            ppr.setProxyId(proxy.getId());
//            ppr.setProxyNo(proxy.getUsername());
//            ppr.setProxyName(proxy.getName());
//            ppr.setOrderSn(po.getSn());
//            ppr.setMoney(proxyCommission);
//            ppr.setBalance(proxy.getMoney().add(proxyCommission));
//            ppr.setType(2);
//
//            ppr.setCommission(proxyCommission);
//            ppr.setRatio(po.getProxyRatio());
//
//            ppr.setRemark("订单手续费，订单金额:" + orderMoney + "元,返佣费率:" + po.getProxyRatio() + "%");
//            payProxyRecordService.addSave(ppr);
//        }
//
//        //上游余额
//        SysUser client = sysUserService.querySysUserById(po.getClientId());
//        if (client.getMoney() == null) client.setMoney(BigDecimal.ZERO);
//        BigDecimal clientComission = orderMoney.multiply(po.getClientRatio()).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);
//        BigDecimal clientMoney = orderMoney.subtract(clientComission);
////        client.setMoney(client.getMoney().add(clientMoney));
////
////        sysUserService.updateUserMoney(client);
//        u1 = new SysUser();
//        u1.setId(client.getId());
//        u1.setMoney(clientMoney);
//        u1.setFrozenMoney(BigDecimal.ZERO);
//        sysUserDao.userFrozenMoneyAndMoney(u1);
//
//        PayClientBalance pcb = new PayClientBalance();
//        pcb.setNo(SnUtil.createSn(""));
//        pcb.setClientId(client.getId());
//        pcb.setClientNo(client.getUsername());
//        pcb.setClientName(client.getName());
//        pcb.setOrderSn(po.getSn());
//        pcb.setMoney(clientMoney);
//        pcb.setBalance(client.getMoney().add(clientMoney));
//        pcb.setType(2);
//
//        pcb.setRatio(po.getClientRatio());
//        pcb.setCommission(clientComission);
//
//        pcb.setRemark("订单金额:" + orderMoney + "元,费率" + po.getClientRatio() + "%,扣除手续费:" + clientComission + "元");
//        payClientBalanceService.addSave(pcb);
//
//        //添加订单收益，更新订单状态
//        po.setNotifyStatus(1);
////        po.setPayTime(new Date());
//        po.setProfit(merchantCommission.subtract(clientComission).subtract(proxyCommission));
//        payOrderDao.dealSuccessOrder(po);
//
//        //发送订单通知
////        sendOrderNotify(po);
//        //更新补单状态
//        payOrderDao.mendOrderStatus(po);
//
////        System.out.println(3/0);
//    }

    public PayOrder queryMendOrder(Map<String, Object> params) {
        return payOrderDao.queryMendOrder(params);
    }

    public PayOrder queryOrderInfo(Map<String, Object> params) {
        return payOrderDao.queryOrderInfo(params);
    }


    public List<Map<String, Object>> queryExportList(Map<String, Object> params, String[] orders) {
        if ((orders != null) && (orders.length > 0)) {
            String orderby = "";
            for (String str : orders) {
                String[] arr1 = str.split(" ");
                if (arr1.length == 2) {
                    orderby = orderby + "`" + arr1[0] + "` " + arr1[1] + ",";
                }
            }
            if (orderby.endsWith(",")) {
                orderby = orderby.substring(0, orderby.length() - 1);
            }
            params.put("orderby", orderby);
        }
        return payOrderDao.queryExportList(params);
    }

    public List<Map<String, Object>> queryExportListMerchant(Map<String, Object> params, String[] orders) {
        if ((orders != null) && (orders.length > 0)) {
            String orderby = "";
            for (String str : orders) {
                String[] arr1 = str.split(" ");
                if (arr1.length == 2) {
                    orderby = orderby + "`" + arr1[0] + "` " + arr1[1] + ",";
                }
            }
            if (orderby.endsWith(",")) {
                orderby = orderby.substring(0, orderby.length() - 1);
            }
            params.put("orderby", orderby);
        }
        return payOrderDao.queryExportListMerchant(params);
    }

    public Map<String, Object> queryTotalAdmin(Map<String, Object> params) {
        return payOrderDao.queryTotalAdmin(params);
    }

    public Map<String, Object> queryTotalMerchant(Map<String, Object> params) {
        return payOrderDao.queryTotalMerchant(params);
    }

    @Override
    public PayOrder querySingle1(Map<String, Object> params) {
        return payOrderDao.querySingle1(params);
    }

    @Override
    public List<Map<String, Object>> queryClientModelList() {
        return payOrderDao.queryClientModelList();
    }

    @Override
    public List<PayOrder> queryNeedQueryOrdersList(Map<String, Object> params) {
        return payOrderDao.queryNeedQueryOrdersList(params);
    }

    @Override
    public void batchNotifyMerchant(Map<String, Object> map) {
        payOrderDao.batchNotifyMerchant(map);
    }

    public synchronized void orderSuccess(int i) {
        System.out.println("order deal..." + i);

        long l = System.currentTimeMillis();
        List<PayOrder> poList = querySuccessOrder();
        System.out.println("querytime:" + (System.currentTimeMillis() - l));

        //处理订单
        if (poList != null && !poList.isEmpty()) {
            for (PayOrder po : poList) {
                l = System.currentTimeMillis();
                orderSuccessSingle(po);
                System.out.println("ordertime:" + po.getId() + ">>" + (System.currentTimeMillis() - l));
            }
        }
    }
}


