package com.city.city_collector.admin.pay.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.dao.PayMemchantRecordDao;
import com.city.city_collector.admin.pay.service.PayMemchantRecordService;
import com.city.city_collector.admin.system.dao.SysUserDao;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.pay.entity.PayMemchantRecord;

/**
 * Description:商户流水-service实现类
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Service
public class PayMemchantRecordServiceImpl implements PayMemchantRecordService {

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
        int total = payMemchantRecordDao.queryCount(params);
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
        page.setResults(payMemchantRecordDao.queryPage(params));
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
    public void addSave(PayMemchantRecord payMemchantRecord) {
        payMemchantRecordDao.addSave(payMemchantRecord);
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
    public PayMemchantRecord querySingle(Map<String, Object> params) {
        return payMemchantRecordDao.querySingle(params);
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
    public void editSave(PayMemchantRecord payMemchantRecord) {
        payMemchantRecordDao.editSave(payMemchantRecord);
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
        payMemchantRecordDao.delete(ids_str);
    }

    /**
     * 调整商户余额
     *
     * @param pmr
     * @param merchant
     * @author:nb
     */
    @Transactional
    public void updateMerchantMoney(PayMemchantRecord pmr, SysUser merchant) {
        //扣除用户金额
        SysUser user1 = new SysUser();
        user1.setId(merchant.getId());
        user1.setFrozenMoney(BigDecimal.ZERO);
        user1.setMoney(pmr.getMoney());
        sysUserMapper.userFrozenMoneyAndMoney(user1);
        //保存调账记录
        pmr.setBalance(merchant.getMoney().add(pmr.getMoney()));
        pmr.setRemark("人工调账，扣减余额" + pmr.getMoney().abs() + "元");
        payMemchantRecordDao.addSave(pmr);
    }

    @Transactional
    public void updateMerchantMoney_add(PayMemchantRecord pmr, SysUser merchant) {
        //扣除用户金额
        SysUser user1 = new SysUser();
        user1.setId(merchant.getId());
        user1.setFrozenMoney(BigDecimal.ZERO);
        user1.setMoney(pmr.getMoney());
        sysUserMapper.userFrozenMoneyAndMoney(user1);
        //保存调账记录
        pmr.setBalance(merchant.getMoney().add(pmr.getMoney()));
        pmr.setRemark("人工调账，增加余额" + pmr.getMoney() + "元");
        payMemchantRecordDao.addSave(pmr);
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
        return payMemchantRecordDao.queryExportList(params);
    }

}