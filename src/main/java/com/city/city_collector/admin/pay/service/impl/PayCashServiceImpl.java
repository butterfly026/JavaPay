package com.city.city_collector.admin.pay.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.city.city_collector.admin.pay.controller.PayCashController;
import com.city.city_collector.channel.util.ThreadUtils;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.paylog.PayLogManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.dao.PayCashDao;
import com.city.city_collector.admin.pay.entity.PayBalanceFrozen;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientBalance;
import com.city.city_collector.admin.pay.entity.PayMemchantRecord;
import com.city.city_collector.admin.pay.entity.PayProxyRecord;
import com.city.city_collector.admin.pay.service.PayBalanceFrozenService;
import com.city.city_collector.admin.pay.service.PayCashService;
import com.city.city_collector.admin.pay.service.PayClientBalanceService;
import com.city.city_collector.admin.pay.service.PayMemchantRecordService;
import com.city.city_collector.admin.pay.service.PayProxyRecordService;
//import com.city.city_collector.admin.pay.service.impl.PayOrderServiceImpl.HttpCallBack;
import com.city.city_collector.admin.system.dao.SysUserDao;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.util.SignUtil;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.util.SnUtil;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:商户提现-service实现类
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Service
public class PayCashServiceImpl implements PayCashService {

    @Autowired
    PayCashDao payCashDao;
    @Autowired
    SysUserDao sysUserMapper;
    @Autowired
    SysUserService sysUserService;

    @Autowired
    PayProxyRecordService payProxyRecordService;
    @Autowired
    PayMemchantRecordService payMemchantRecordService;
    @Autowired
    PayClientBalanceService payClientBalanceService;

    @Autowired
    PayBalanceFrozenService payBalanceFrozenService;

    @Autowired
    private PayCashController payCashController;

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
    @Override
    public Page queryPage(Map<String, Object> params, Page page, String[] orders) {
        //获取记录总数
        int total = payCashDao.queryCount(params);
        page.setTotal(total);
        params.put("start", page.getStartRow());
        params.put("end", page.getPageSize());
        //拼接排序字段
        if (orders != null && orders.length > 0) {
            String orderby = "";
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
        page.setResults(payCashDao.queryPage(params));
        return page;
    }

    /**
     * Description:保存记录
     */
    @Override
    public void addSave(PayCash payCash) {
        payCashDao.addSave(payCash);
    }

    /**
     * Description:查询单条记录
     */
    @Override
    public PayCash querySingle(Map<String, Object> params) {
        return payCashDao.querySingle(params);
    }

    /**
     * Description:更新记录
     */
    @Override
    public void editSave(PayCash payCash) {
        payCashDao.editSave(payCash);
    }

    /**
     * Description:删除指定记录
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
        payCashDao.delete(ids_str);
    }

    /**
     * 创建提现订单
     *
     * @param payCash
     * @param user
     * @param fromApi 是否从api调用. 是,使用自动代付 否不使用
     * @author:nb
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCash(PayCash payCash, SysUser user, Integer type, Boolean fromApi) {
        try {
            //保存提现订单
            payCashDao.addSave(payCash);

            //后台手工创建的不需要自动下发
            //1. 自动代付打开   2. 2000以内自动提交   3. 只有代付使用,下发不使用
            if (fromApi
                    && payCash.getChannelType() == 1
                    && ApplicationData.getInstance().getConfig().getAutoDaiFu() == 1
                    && payCash.getAmount().compareTo(new BigDecimal(2000)) == -1) {
                Map<String, Object> autoPayModule = queryAutoPayModule();
                //自动代付提交
                if (autoPayModule != null) {

                    ThreadUtils.getInstance().runDelay(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, Object> queryParams = new HashMap<>();
                            queryParams.put("sn", payCash.getSn());
                            PayCash payCash1 = payCashDao.querySingle(queryParams);
                            payCash1.setPayStatus(3);
                            payCashDao.updateCashStatus(payCash1);
                            Message message = payCashController.merchantClientCashSubmit(payCash.getId(), 1, user.getId(), payCash.getRealMoney(), null, null, Long.valueOf(autoPayModule.get("id").toString()), "代付2000以内提交", null);
//
                            PayCash payCash2 = payCashDao.querySingle(queryParams);
                            String remark = payCash2.getRemark() == null ? "" : payCash2.getRemark();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateStr = sdf.format(new Date());
                            if (message.getCode() != 200) {
                                //下单失败
                                System.out.println("issued 2-1: 自动代付失败 " + message.getMsg());
                                remark += " <span style='color:red;'>" + dateStr + " 自动代付失败:" + autoPayModule.get("id") + "  " + message.getMsg() + "</span><br/>";
                            } else {
                                System.out.println("issued 2-2: 自动代付成功 sn: " + payCash.getSn());
                                remark += " <span style='color:green;'>" + dateStr + " 自动代付成功: " + autoPayModule.get("id") + "</span><br/>";
                            }
                            payCash2.setRemark(remark);
                            payCashDao.updateCashRemark(payCash2);
                        }
                    });
                }
            }


            SysUser user1 = new SysUser();
            user1.setId(user.getId());
            user1.setFrozenMoney(payCash.getAmount());
            sysUserMapper.userFrozenMoney(user1);

            //添加冻结记录
            PayBalanceFrozen pbf = new PayBalanceFrozen();
            pbf.setType(type);
            pbf.setUserId(user.getId());
            pbf.setMoney(payCash.getAmount());

            pbf.setStatus(0);
            pbf.setFrozenType(0);
            pbf.setRemark("用户发起提现，金额" + payCash.getAmount() + "元");
            pbf.setOrderSn(payCash.getSn());
            payBalanceFrozenService.addSave(pbf);
            System.out.println("issued 2: 保存订单成功 sn: " + payCash.getSn());
            PayLogManager.getInstance().createPayLogByCashRequest(payCash.getSn(), "代付订单保存成功", "", "", payCash.getClientId(), 0L, payCash.getMerchantId(), 0L);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * 更新提现状态
     *
     * @param payCash
     * @author:nb
     */
    @Override
    @Transactional
    public void updateCashStatus(PayCash payCash) {
        payCashDao.updateCashStatus(payCash);
    }

    /**
     * 拒绝提现
     *
     * @param payCash
     * @author:nb
     */
    @Override
    @Transactional
    public void refuseCash(PayCash payCash, Integer type) {
        //更新状态为拒绝
        payCash.setPayStatus(2);
        payCash.setNotifyStatus(1);
        payCashDao.updateCashStatus(payCash);

        SysUser user = sysUserService.querySysUserById(payCash.getMerchantId());
        //解锁金额
        SysUser user1 = new SysUser();
        user1.setId(user.getId());
        user1.setFrozenMoney(payCash.getAmount().negate());
        sysUserMapper.userFrozenMoney(user1);

        //添加解冻记录
        PayBalanceFrozen pbf = new PayBalanceFrozen();
        pbf.setType(type);
        pbf.setUserId(user.getId());
        pbf.setMoney(payCash.getAmount());

        pbf.setStatus(1);
        pbf.setFrozenType(2);
        pbf.setRemark("拒绝提现，解冻金额" + payCash.getAmount() + "元");
        pbf.setOrderSn(payCash.getSn());
        payBalanceFrozenService.addSave(pbf);

        //发送下发成功通知
        if (StringUtils.isNotBlank(payCash.getNotifyUrl())) {
            sendCashNotify(payCash);
        }
    }

    /**
     * 上游代付提交
     *
     * @param payCash
     * @param client
     * @param type
     * @param money
     * @param clientCommission
     * @param clientRealMoney
     * @author:nb
     */
    @Override
    @Transactional
    public void merchantClientCashSubmit(PayCash payCash, SysUser client, Integer type, BigDecimal money, BigDecimal clientCommission, BigDecimal clientRealMoney) {
        //更新提现表记录
        payCash.setPayStatus(4);
        payCash.setClientId(client.getId());
        payCash.setClientMoney(money);
        payCash.setClientCommission(clientCommission);
        payCash.setClientRealMoney(clientRealMoney);
        payCash.setPayType(type);
        payCashDao.updateMerchantClientCashSubmit(payCash);
        //冻结上游余额
        SysUser user1 = new SysUser();
        user1.setId(client.getId());
        user1.setFrozenMoney(money);
        sysUserMapper.userFrozenMoney(user1);

        //添加冻结记录
        PayBalanceFrozen pbf = new PayBalanceFrozen();
        pbf.setType(2);
        pbf.setUserId(client.getId());
        pbf.setMoney(money);

        pbf.setStatus(0);
        pbf.setFrozenType(0);
        pbf.setRemark("上游代付，冻结金额" + money + "元");
        pbf.setOrderSn(payCash.getSn());
        payBalanceFrozenService.addSave(pbf);
    }

    @Override
    @Transactional
    public void merchantChangeClientCashSubmit(PayCash payCash, SysUser client, Integer type, BigDecimal money, BigDecimal clientCommission, BigDecimal clientRealMoney) {
        //解冻原上游金额
        SysUser user1 = new SysUser();
        user1.setId(payCash.getClientId());
        user1.setFrozenMoney(payCash.getClientMoney().negate());
        sysUserMapper.userFrozenMoney(user1);

        //添加解冻记录
        PayBalanceFrozen pbf = new PayBalanceFrozen();
        pbf.setType(2);
        pbf.setUserId(client.getId());
        pbf.setMoney(payCash.getClientMoney());

        pbf.setStatus(1);
        pbf.setFrozenType(5);
        pbf.setRemark("更换上游代付，解冻金额" + payCash.getClientMoney() + "元");
        pbf.setOrderSn(payCash.getSn());
        payBalanceFrozenService.addSave(pbf);

        //更新提现表记录
        payCash.setPayStatus(4);
        payCash.setClientId(client.getId());
        payCash.setClientMoney(money);
        payCash.setClientCommission(clientCommission);
        payCash.setClientRealMoney(clientRealMoney);
        payCash.setPayType(type);
        payCashDao.updateMerchantClientCashSubmit(payCash);
        //冻结上游余额
        user1 = new SysUser();
        user1.setId(client.getId());
        user1.setFrozenMoney(money);
        sysUserMapper.userFrozenMoney(user1);

        //添加冻结记录
        pbf = new PayBalanceFrozen();
        pbf.setType(2);
        pbf.setUserId(client.getId());
        pbf.setMoney(money);

        pbf.setStatus(1);
        pbf.setFrozenType(5);
        pbf.setRemark("上游代付，冻结金额" + money + "元");
        pbf.setOrderSn(payCash.getSn());
        payBalanceFrozenService.addSave(pbf);
    }

    @Override
    public void userFrozenMoneyAndMoney(SysUser user) {
        sysUserMapper.userFrozenMoneyAndMoney(user);
    }

    @Override
    @Transactional
    public void cashSuc(PayCash payCash) {
        //更新提现记录状态
        payCash.setPayStatus(5);
        payCash.setNotifyStatus(1);
        payCash.setPayTime(new Date());
        payCashDao.updateCashStatus(payCash);
        //解冻上游冻结余额，扣除上游余额
        SysUser client = sysUserService.querySysUserById(payCash.getClientId());
        if (client != null) {
            if (client.getMoney().compareTo(payCash.getClientMoney()) < 0) {
                throw new RuntimeException("上游" + client.getUsername() + "可扣减余额不足!扣减额:" + payCash.getClientMoney() + "元,上游余额:" + client.getMoney() + "元");
            }

            SysUser user1 = new SysUser();
            user1.setId(client.getId());
            user1.setFrozenMoney(payCash.getClientMoney().negate());
            user1.setMoney(payCash.getClientMoney().negate());
            sysUserMapper.userFrozenMoneyAndMoney(user1);

            //添加解冻记录
            PayBalanceFrozen pbf = new PayBalanceFrozen();
            pbf.setType(2);
            pbf.setUserId(client.getId());
            pbf.setMoney(payCash.getClientMoney());

            pbf.setStatus(1);
            pbf.setFrozenType(4);
            pbf.setRemark("提现失败，解冻金额" + payCash.getClientMoney() + "元");
            pbf.setOrderSn(payCash.getSn());
            payBalanceFrozenService.addSave(pbf);

            //添加余额变动记录
            PayClientBalance pcb = new PayClientBalance();
            pcb.setNo(SnUtil.createSn(""));
            pcb.setClientId(client.getId());
            pcb.setClientNo(client.getUsername());
            pcb.setClientName(client.getName());
            pcb.setOrderSn(payCash.getSn());
            pcb.setMoney(payCash.getClientMoney().negate());
            pcb.setBalance(client.getMoney().subtract(payCash.getClientMoney()));
            pcb.setType(1);

            pcb.setRatio(BigDecimal.ZERO);
            pcb.setCommission(payCash.getClientCommission());

            pcb.setRemark("提现金额:" + payCash.getClientMoney() + "元");
            payClientBalanceService.addSave(pcb);
        }

        //解冻商户冻结余额
        SysUser merchant = sysUserService.querySysUserById(payCash.getMerchantId());
        if (merchant != null) {
            if (merchant.getMoney().compareTo(payCash.getAmount()) < 0) {
                throw new RuntimeException("商户" + merchant.getUsername() + "可扣减余额不足!扣减额:" + payCash.getAmount() + "元,商户余额:" + merchant.getMoney() + "元");
            }
            //解锁金额
            SysUser user1 = new SysUser();
            user1.setId(merchant.getId());
            user1.setFrozenMoney(payCash.getAmount().negate());
            user1.setMoney(payCash.getAmount().negate());
            sysUserMapper.userFrozenMoneyAndMoney(user1);

            //添加解冻记录
            PayBalanceFrozen pbf = new PayBalanceFrozen();

            if (payCash.getUserType() != null && payCash.getUserType() == 1) {//代理
                pbf.setType(0);
            } else {
                pbf.setType(1);
            }

            pbf.setUserId(merchant.getId());
            pbf.setMoney(payCash.getAmount());

            pbf.setStatus(1);
            pbf.setFrozenType(3);
            pbf.setRemark("提现成功，解冻金额" + payCash.getAmount() + "元");
            pbf.setOrderSn(payCash.getSn());
            payBalanceFrozenService.addSave(pbf);

            if (payCash.getUserType() != null && payCash.getUserType() == 1) {//代理
                PayProxyRecord ppr = new PayProxyRecord();
                ppr.setNo(SnUtil.createSn(""));
                ppr.setProxyId(merchant.getId());
                ppr.setProxyNo(merchant.getUsername());
                ppr.setProxyName(merchant.getName());
                ppr.setOrderSn(payCash.getSn());
                ppr.setMoney(payCash.getAmount().negate());
                ppr.setBalance(merchant.getMoney().subtract(payCash.getAmount()));
                ppr.setType(1);

                ppr.setCommission(payCash.getProxyMoney());
                ppr.setRatio(BigDecimal.ZERO);

                ppr.setRemark("提现金额:" + payCash.getAmount() + "元");
                payProxyRecordService.addSave(ppr);
            } else {
                //添加余额变动记录
                PayMemchantRecord pmr = new PayMemchantRecord();
                pmr.setNo(SnUtil.createSn(""));
                pmr.setMerchantId(merchant.getId());
                pmr.setMerchantNo(merchant.getUsername());
                pmr.setMerchantName(merchant.getName());
                pmr.setOrderSn(payCash.getSn());
                pmr.setMoney(payCash.getAmount().negate());
                pmr.setBalance(merchant.getMoney().subtract(payCash.getAmount()));
                pmr.setType(1);

                pmr.setCommission(payCash.getCommission());
                pmr.setRatio(BigDecimal.ZERO);

                pmr.setRemark("提现金额:" + payCash.getAmount() + "元");
                payMemchantRecordService.addSave(pmr);
            }

        }
        //添加代理返佣费
        if (payCash.getProxyId() != null && payCash.getProxyMoney() != null) {
            SysUser proxy = sysUserService.querySysUserById(payCash.getProxyId());
            SysUser u1 = new SysUser();
            u1.setId(proxy.getId());
            u1.setMoney(payCash.getProxyMoney());
            u1.setFrozenMoney(BigDecimal.ZERO);
            sysUserMapper.userFrozenMoneyAndMoney(u1);
            proxy = sysUserService.querySysUserById(merchant.getProxyId());

            PayProxyRecord ppr = new PayProxyRecord();
            ppr.setNo(SnUtil.createSn(""));
            ppr.setProxyId(proxy.getId());
            ppr.setProxyNo(proxy.getUsername());
            ppr.setProxyName(proxy.getName());
            ppr.setOrderSn(payCash.getSn());
            ppr.setMoney(payCash.getProxyMoney());
            ppr.setBalance(proxy.getMoney());
            ppr.setType(2);

            ppr.setCommission(payCash.getProxyMoney());
            ppr.setRatio(BigDecimal.ZERO);

            ppr.setRemark("提现手续费，订单金额:" + payCash.getRealMoney() + "元");
            payProxyRecordService.addSave(ppr);
        }
        //发送下发成功通知
        if (StringUtils.isNotBlank(payCash.getNotifyUrl())) {
            sendCashNotify(payCash);
        }
    }

    @Override
    @Transactional
    public void cashFail(PayCash payCash) {
        //更新提现记录状态
        payCash.setPayStatus(1);
        payCash.setNotifyStatus(1);
        payCashDao.updateCashStatus(payCash);
        //解冻上游冻结余额
        SysUser client = sysUserService.querySysUserById(payCash.getClientId());
        if (client != null) {
            //解锁金额
            SysUser user1 = new SysUser();
            user1.setId(client.getId());
            user1.setFrozenMoney(payCash.getClientMoney().negate());
            sysUserMapper.userFrozenMoney(user1);

            //添加解冻记录
            PayBalanceFrozen pbf = new PayBalanceFrozen();
            pbf.setType(2);
            pbf.setUserId(client.getId());
            pbf.setMoney(payCash.getClientMoney());

            pbf.setStatus(1);
            pbf.setFrozenType(4);
            pbf.setRemark("提现失败，解冻金额" + payCash.getClientMoney() + "元");
            pbf.setOrderSn(payCash.getSn());
            payBalanceFrozenService.addSave(pbf);
        }
        //解冻商户冻结余额
        SysUser merchant = sysUserService.querySysUserById(payCash.getMerchantId());
        if (merchant != null) {
            //解锁金额
            SysUser user1 = new SysUser();
            user1.setId(merchant.getId());
            user1.setFrozenMoney(payCash.getAmount().negate());
            sysUserMapper.userFrozenMoney(user1);

            //添加解冻记录
            PayBalanceFrozen pbf = new PayBalanceFrozen();
            pbf.setType(1);
            pbf.setUserId(merchant.getId());
            pbf.setMoney(payCash.getAmount());

            pbf.setStatus(1);
            pbf.setFrozenType(4);
            pbf.setRemark("提现失败，解冻金额" + payCash.getAmount() + "元");
            pbf.setOrderSn(payCash.getSn());
            payBalanceFrozenService.addSave(pbf);
        }

        //发送下发失败通知
        if (StringUtils.isNotBlank(payCash.getNotifyUrl())) {
            sendCashNotify(payCash);
        }
    }
    
    @Override
    @Transactional
    public void cashRefuse(PayCash payCash) {
        //更新提现记录状态
        payCash.setPayStatus(2);
        payCash.setNotifyStatus(1);
        payCashDao.updateCashStatus(payCash);
        //解冻上游冻结余额
        SysUser client = sysUserService.querySysUserById(payCash.getClientId());
        if (client != null) {
            //解锁金额
            SysUser user1 = new SysUser();
            user1.setId(client.getId());
            user1.setFrozenMoney(payCash.getClientMoney().negate());
            sysUserMapper.userFrozenMoney(user1);

            //添加解冻记录
            PayBalanceFrozen pbf = new PayBalanceFrozen();
            pbf.setType(2);
            pbf.setUserId(client.getId());
            pbf.setMoney(payCash.getClientMoney());

            pbf.setStatus(1);
            pbf.setFrozenType(4);
            pbf.setRemark("提现失败，解冻金额" + payCash.getClientMoney() + "元");
            pbf.setOrderSn(payCash.getSn());
            payBalanceFrozenService.addSave(pbf);
        }
        //解冻商户冻结余额
        SysUser merchant = sysUserService.querySysUserById(payCash.getMerchantId());
        if (merchant != null) {
            //解锁金额
            SysUser user1 = new SysUser();
            user1.setId(merchant.getId());
            user1.setFrozenMoney(payCash.getAmount().negate());
            sysUserMapper.userFrozenMoney(user1);

            //添加解冻记录
            PayBalanceFrozen pbf = new PayBalanceFrozen();
            pbf.setType(1);
            pbf.setUserId(merchant.getId());
            pbf.setMoney(payCash.getAmount());

            pbf.setStatus(1);
            pbf.setFrozenType(4);
            pbf.setRemark("提现失败，解冻金额" + payCash.getAmount() + "元");
            pbf.setOrderSn(payCash.getSn());
            payBalanceFrozenService.addSave(pbf);
        }

        //发送下发失败通知
        if (StringUtils.isNotBlank(payCash.getNotifyUrl())) {
            sendCashNotify(payCash);
        }
    }

    @Override
    public void sendCashNotify(PayCash paycash) {
        if (ApplicationData.getInstance().getConfig().getServermodel().intValue() == 1) {
            if (paycash.getDealStatus() == null) paycash.setDealStatus(0);
            if (paycash.getNotifyCount() == null) paycash.setNotifyCount(0);

            paycash.setNotifyCount(paycash.getNotifyCount() + 1);
            payCashDao.updateNotifyOrder(paycash);

            if (StringUtils.isBlank(paycash.getNotifyUrl()) || (!paycash.getNotifyUrl().toLowerCase().startsWith("http") && !paycash.getNotifyUrl().toLowerCase().startsWith("https"))) {
                System.out.println("cash notify error:" + paycash.getNotifyUrl());
                return;
            }

            SysUser merchant = sysUserService.querySysUserById(paycash.getMerchantId());
            //发送通知
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, Object> param = new TreeMap<String, Object>();
            //        param.put("merchantNo",merchant.getUsername());
            param.put("sn", paycash.getSn());
            param.put("merchantSn", paycash.getMerchantSn());
            param.put("payStatus", paycash.getPayStatus() == null ? 0 : paycash.getPayStatus());
            param.put("notifyStatus", paycash.getNotifyStatus() == null ? 0 : paycash.getNotifyStatus());
            param.put("payTime", paycash.getPayTime() == null ? "" : sdf.format(paycash.getPayTime()));
            param.put("createTime", sdf.format(paycash.getCreateTime()));
            param.put("amount", paycash.getAmount());
            param.put("fee", paycash.getCommission());//手续费
            param.put("realMoney", paycash.getRealMoney());//实际支付
            param.put("remark", paycash.getApiremark());

            System.out.println("remark:" + paycash.getRemark() + " | " + paycash.getApiremark());

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
                    .url(paycash.getNotifyUrl())
                    .post(formBuilder.build())
                    .build();

            OkHttpClient okhttp = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
            Call call = okhttp.newCall(request);

            call.enqueue(new HttpCallBack(paycash));
        } else {
            System.out.println("send cashnotify...");
            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("sn", paycash.getSn());
            formBuilder.add("mmpkey", "Khdks-=,.LI68KdJ+=}^KkJ");

            Request request = new Request.Builder()
                    .url(ApplicationData.getInstance().getConfig().getPaynotifyurl() + "/cashNmbNotify")
                    .post(formBuilder.build())
                    .build();
            OkHttpClient okhttp = new OkHttpClient.Builder().connectTimeout(10L, TimeUnit.SECONDS).writeTimeout(10L, TimeUnit.SECONDS)
                    .readTimeout(10L, TimeUnit.SECONDS).connectionPool(ApplicationData.getConnectionPool()).build();
            Call call = okhttp.newCall(request);
            call.enqueue(new HttpCallBack(paycash));
        }
    }

    class HttpCallBack implements Callback {
        PayCash pc = null;

        public HttpCallBack(PayCash pc) {
            this.pc = pc;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            System.out.println("发送提现通知请求失败===" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response rsp) throws IOException {
            if (ApplicationData.getInstance().getConfig().getServermodel().intValue() == 1) {
                if (rsp.isSuccessful()) {
                    String data = rsp.body().string();
                    System.out.println("提现请求发送成功===" + data);
                    if (pc != null && StringUtils.isNotBlank(data)) {
                        pc.setDealStatus(1);
                        //                    pc.setNotifyStatus(1);
                        payCashDao.updateNotifyOrder(pc);
                    }
                } else {
                    System.out.println("提现交互失败===");
                }
            }
            System.out.println("发送提现通知请求成功===");
            rsp.close();
        }

    }

    /**
     * 查询已成功且通知状态为未通知且由API发起的订单
     *
     * @return
     * @author:nb
     */
    @Override
    public List<PayCash> querySuccessCashOrder() {
        return payCashDao.querySuccessCashOrder();
    }

    /**
     * 查询需要通知的订单
     *
     * @return
     * @author:nb
     */
    @Override
    public List<PayCash> queryNeedNotifyCashOrder() {
        try {
            return payCashDao.queryNeedNotifyCashOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<PayCash>();
    }

    @Override
    @Transactional
    public synchronized void orderSuccessSingle(PayCash payCash) {
        try {
            if (payCash.getStatus().intValue() == 5) {//成功
                payCash.setPayStatus(5);
                payCash.setNotifyStatus(1);
                payCash.setPayTime(new Date());
                payCashDao.updateCashStatus(payCash);
                //解冻上游冻结余额，扣除上游余额
                SysUser client = sysUserService.querySysUserById(payCash.getClientId());
                if (client != null) {
                    if (client.getMoney().compareTo(payCash.getClientMoney()) < 0) {
                        throw new RuntimeException("上游" + client.getUsername() + "可扣减余额不足!扣减额:" + payCash.getClientMoney() + "元,上游余额:" + client.getMoney() + "元");
                    }

                    SysUser user1 = new SysUser();
                    user1.setId(client.getId());
                    user1.setFrozenMoney(payCash.getClientMoney().negate());
                    user1.setMoney(payCash.getClientMoney().negate());
                    sysUserMapper.userFrozenMoneyAndMoney(user1);

                    //添加解冻记录
                    PayBalanceFrozen pbf = new PayBalanceFrozen();
                    pbf.setType(2);
                    pbf.setUserId(client.getId());
                    pbf.setMoney(payCash.getClientMoney());

                    pbf.setStatus(1);
                    pbf.setFrozenType(4);
                    pbf.setRemark("提现成功，解冻金额" + payCash.getClientMoney() + "元");
                    pbf.setOrderSn(payCash.getSn());
                    payBalanceFrozenService.addSave(pbf);

                    //添加余额变动记录
                    PayClientBalance pcb = new PayClientBalance();
                    pcb.setNo(SnUtil.createSn(""));
                    pcb.setClientId(client.getId());
                    pcb.setClientNo(client.getUsername());
                    pcb.setClientName(client.getName());
                    pcb.setOrderSn(payCash.getSn());
                    pcb.setMoney(payCash.getClientMoney().negate());
                    pcb.setBalance(client.getMoney().subtract(payCash.getClientMoney()));
                    pcb.setType(1);

                    pcb.setRatio(BigDecimal.ZERO);
                    pcb.setCommission(payCash.getClientCommission());

                    pcb.setRemark("提现金额:" + payCash.getClientMoney() + "元");
                    payClientBalanceService.addSave(pcb);
                }

                //解冻商户冻结余额
                SysUser merchant = sysUserService.querySysUserById(payCash.getMerchantId());
                if (merchant != null) {
                    if (merchant.getMoney().compareTo(payCash.getAmount()) < 0) {
                        throw new RuntimeException("商户" + merchant.getUsername() + "可扣减余额不足!扣减额:" + payCash.getAmount() + "元,商户余额:" + merchant.getMoney() + "元");
                    }
                    //解锁金额
                    SysUser user1 = new SysUser();
                    user1.setId(merchant.getId());
                    user1.setFrozenMoney(payCash.getAmount().negate());
                    user1.setMoney(payCash.getAmount().negate());
                    sysUserMapper.userFrozenMoneyAndMoney(user1);

                    //添加解冻记录
                    PayBalanceFrozen pbf = new PayBalanceFrozen();
                    pbf.setType(1);
                    pbf.setUserId(merchant.getId());
                    pbf.setMoney(payCash.getAmount());

                    pbf.setStatus(1);
                    pbf.setFrozenType(3);
                    pbf.setRemark("提现成功，解冻金额" + payCash.getAmount() + "元");
                    pbf.setOrderSn(payCash.getSn());
                    payBalanceFrozenService.addSave(pbf);

                    //添加余额变动记录
                    PayMemchantRecord pmr = new PayMemchantRecord();
                    pmr.setNo(SnUtil.createSn(""));
                    pmr.setMerchantId(merchant.getId());
                    pmr.setMerchantNo(merchant.getUsername());
                    pmr.setMerchantName(merchant.getName());
                    pmr.setOrderSn(payCash.getSn());
                    pmr.setMoney(payCash.getAmount().negate());
                    pmr.setBalance(merchant.getMoney().subtract(payCash.getAmount()));
                    pmr.setType(1);

                    pmr.setCommission(payCash.getCommission());
                    pmr.setRatio(BigDecimal.ZERO);

                    pmr.setRemark("提现金额:" + payCash.getAmount() + "元");
                    payMemchantRecordService.addSave(pmr);
                }
            } else if (payCash.getStatus().intValue() == 1) {//失败
                //更新提现记录状态
                payCash.setPayStatus(1);
                payCash.setNotifyStatus(1);
                payCashDao.updateCashStatus(payCash);
                //解冻上游冻结余额
                SysUser client = sysUserService.querySysUserById(payCash.getClientId());
                if (client != null) {
                    //解锁金额
                    SysUser user1 = new SysUser();
                    user1.setId(client.getId());
                    user1.setFrozenMoney(payCash.getClientMoney().negate());
                    sysUserMapper.userFrozenMoney(user1);

                    //添加解冻记录
                    PayBalanceFrozen pbf = new PayBalanceFrozen();
                    pbf.setType(2);
                    pbf.setUserId(client.getId());
                    pbf.setMoney(payCash.getClientMoney());

                    pbf.setStatus(1);
                    pbf.setFrozenType(4);
                    pbf.setRemark("提现失败，解冻金额" + payCash.getClientMoney() + "元");
                    pbf.setOrderSn(payCash.getSn());
                    payBalanceFrozenService.addSave(pbf);
                }
                //解冻商户冻结余额
                SysUser merchant = sysUserService.querySysUserById(payCash.getMerchantId());
                if (merchant != null) {
                    //解锁金额
                    SysUser user1 = new SysUser();
                    user1.setId(merchant.getId());
                    user1.setFrozenMoney(payCash.getAmount().negate());
                    sysUserMapper.userFrozenMoney(user1);

                    //添加解冻记录
                    PayBalanceFrozen pbf = new PayBalanceFrozen();
                    pbf.setType(1);
                    pbf.setUserId(merchant.getId());
                    pbf.setMoney(payCash.getAmount());

                    pbf.setStatus(1);
                    pbf.setFrozenType(4);
                    pbf.setRemark("提现失败，解冻金额" + payCash.getAmount() + "元");
                    pbf.setOrderSn(payCash.getSn());
                    payBalanceFrozenService.addSave(pbf);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
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
        return payCashDao.queryExportList(params);
    }

    @Override
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
        return payCashDao.queryExportListMerchant(params);
    }

    @Override
    public Map<String, Object> queryCashDetail(Map<String, Object> params) {
        return payCashDao.queryCashDetail(params);
    }

    @Override
    public void updateCashRemark(PayCash payCash) {
        payCashDao.updateCashRemark(payCash);
    }

    @Override
    public List<Map<String, Object>> queryClientInfoByModel() {
        return payCashDao.queryClientInfoByModel();
    }

    @Override
    public int queryCashOrderCountByMerchant(Map<String, Object> params) {
        return payCashDao.queryCashOrderCountByMerchant(params);
    }

    /**
     * 查询待处理的订单数
     **/
    @Override
    public int queryWaitDealCount() {
        return payCashDao.queryWaitDealCount();
    }

    @Override
    public int updateAutoModel(Map<String, Object> params) {
        return payCashDao.updateAutoModel(params);
    }

    @Override
    public Map<String, Object> queryAutoPayModule() {
        return payCashDao.queryAutoPayModule();
    }
}
