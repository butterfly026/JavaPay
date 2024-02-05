package com.city.city_collector.common.util;

import java.math.BigDecimal;

import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.service.PayCashService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;

public class CashUtil {
    /**
     * 创建提现订单
     *
     * @param sysUserService
     * @param merchantNo
     * @param amount
     * @param merchantSn
     * @param proxyCommission
     * @param commission
     * @param realMoney
     * @param bankAccountNo
     * @param bankAccountName
     * @param bankName
     * @param bankNameSub
     * @param bankId
     * @param bankAccountIdNo
     * @param bankAccountMobile
     * @param bankCode
     * @param bankIfsc
     * @param bankNation
     * @param cashType
     * @param notifyUrl
     * @param remark
     * @param channelType
     * @param payCashService
     * @return
     */
    public synchronized static CashMessage createMerchantCash(SysUserService sysUserService, String merchantNo, BigDecimal amount
            , String merchantSn, BigDecimal proxyCommission, BigDecimal commission, BigDecimal realMoney
            , String bankAccountNo, String bankAccountName, String bankName, String bankNameSub, String bankId
            , String bankAccountIdNo, String bankAccountMobile, String bankCode, String bankIfsc, String bankNation
            , Integer cashType, String notifyUrl, String remark, Integer channelType, PayCashService payCashService) {
        SysUser user = sysUserService.querySysUserByUsername(merchantNo);
        if (user == null || user.getType() == null || user.getType().intValue() != 2) {
            return new CashMessage(false, "商户不存在!", "");
        }
        BigDecimal money1 = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
        BigDecimal frozenMoney = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
        if (money1.compareTo(amount) < 0 || money1.subtract(frozenMoney).compareTo(amount) < 0) {
            return new CashMessage(false, "可提现金额不足！", "");
        }
        //创建提现订单
        PayCash payCash = new PayCash();

        payCash.setMerchantId(user.getId());

        payCash.setAmount(amount);
        payCash.setMerchantSn(merchantSn);
        payCash.setSn(SnUtil.createSn(ApplicationData.getInstance().getConfig().getCashsnpre()));

        if (user.getProxyId() != null) {
            payCash.setProxyId(user.getProxyId());
            payCash.setProxyMoney(proxyCommission);
        }

        //API接口生成
        payCash.setCashType(1);

        payCash.setCommission(commission);
        payCash.setRealMoney(realMoney);

        payCash.setBankAccno(bankAccountNo);
        payCash.setBankAccname(bankAccountName);
        payCash.setBankName(bankName);
        payCash.setBankSubname(bankNameSub);
        payCash.setBankId(bankId);
        payCash.setBankAccid(bankAccountIdNo);
        payCash.setBankAccmobile(bankAccountMobile);

        payCash.setBankCode(bankCode);

        payCash.setBankIfsc(bankIfsc);
        payCash.setBankNation(bankNation);
        payCash.setBtype(cashType);

        payCash.setNotifyUrl(notifyUrl);
//        payCash.setNotifyStatus(0);
//        payCash.setRemark(remark);
        payCash.setApiremark(remark);
        payCash.setStatus(0);
        payCash.setPayStatus(0);
        payCash.setChannelType(channelType);

//        payCashService.addSave(payCash);
        payCashService.createCash(payCash, user, 1, true);

        return new CashMessage(true, "操作完成!", payCash.getSn());
    }

}
