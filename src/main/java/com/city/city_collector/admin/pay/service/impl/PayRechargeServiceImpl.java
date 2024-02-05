package com.city.city_collector.admin.pay.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.util.SnUtil;
import com.city.city_collector.admin.pay.dao.PayMemchantRecordDao;
import com.city.city_collector.admin.pay.dao.PayRechargeDao;
import com.city.city_collector.admin.pay.service.PayRechargeService;
import com.city.city_collector.admin.system.dao.SysUserDao;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.pay.entity.PayMemchantRecord;
import com.city.city_collector.admin.pay.entity.PayRecharge;

/**
 * Description:商户充值-service实现类
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Service
public class PayRechargeServiceImpl implements PayRechargeService {

    @Autowired
    PayRechargeDao payRechargeDao;

    @Autowired
    PayMemchantRecordDao payMemchantRecordDao;

    @Autowired
    SysUserDao sysUserMapper;

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
        int total = payRechargeDao.queryCount(params);
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
        page.setResults(payRechargeDao.queryPage(params));
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
    public void addSave(PayRecharge payRecharge) {
        payRechargeDao.addSave(payRecharge);
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
    public PayRecharge querySingle(Map<String, Object> params) {
        return payRechargeDao.querySingle(params);
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
    public void editSave(PayRecharge payRecharge) {
        payRechargeDao.editSave(payRecharge);
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
        payRechargeDao.delete(ids_str);
    }

    @Transactional
    public void paySuc(PayRecharge payRecharge, SysUser merchant) {
        //更新充值记录
        payRechargeDao.editSave(payRecharge);
        //添加商户余额
        SysUser user1 = new SysUser();
        user1.setId(merchant.getId());
        user1.setFrozenMoney(BigDecimal.ZERO);
        user1.setMoney(payRecharge.getMoney());
        sysUserMapper.userFrozenMoneyAndMoney(user1);
        //保存调账记录

        PayMemchantRecord pmr = new PayMemchantRecord();
        pmr.setNo(SnUtil.createSn(""));
        pmr.setMerchantId(payRecharge.getMerchantId());
        pmr.setMerchantNo(merchant.getUsername());
        pmr.setMerchantName(merchant.getName());
        pmr.setOrderSn(payRecharge.getSn());
        pmr.setMoney(payRecharge.getMoney());
        pmr.setBalance(merchant.getMoney().add(payRecharge.getMoney()));
        pmr.setType(4);

        pmr.setRemark("商户充值，增加余额" + payRecharge.getMoney() + "元");
        payMemchantRecordDao.addSave(pmr);
    }
}
