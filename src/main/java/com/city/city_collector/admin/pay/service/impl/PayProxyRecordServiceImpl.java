package com.city.city_collector.admin.pay.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.dao.PayProxyRecordDao;
import com.city.city_collector.admin.pay.service.PayProxyRecordService;
import com.city.city_collector.admin.system.dao.SysUserDao;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.pay.entity.PayProxyRecord;

/**
 * Description:代理流水-service实现类
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Service
public class PayProxyRecordServiceImpl implements PayProxyRecordService {

    @Autowired
    PayProxyRecordDao payProxyRecordDao;
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
        int total = payProxyRecordDao.queryCount(params);
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
        page.setResults(payProxyRecordDao.queryPage(params));
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
    public void addSave(PayProxyRecord payProxyRecord) {
        payProxyRecordDao.addSave(payProxyRecord);
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
    public PayProxyRecord querySingle(Map<String, Object> params) {
        return payProxyRecordDao.querySingle(params);
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
    public void editSave(PayProxyRecord payProxyRecord) {
        payProxyRecordDao.editSave(payProxyRecord);
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
        payProxyRecordDao.delete(ids_str);
    }

    public void updateProxyMoney(PayProxyRecord ppr, SysUser proxy) {
        //扣除用户金额
        SysUser user1 = new SysUser();
        user1.setId(proxy.getId());
        user1.setFrozenMoney(BigDecimal.ZERO);
        user1.setMoney(ppr.getMoney());
        sysUserMapper.userFrozenMoneyAndMoney(user1);
        //保存调账记录
        ppr.setBalance(proxy.getMoney().add(ppr.getMoney()));
        ppr.setRemark("人工调账，扣减余额" + ppr.getMoney().abs() + "元");
        payProxyRecordDao.addSave(ppr);
    }

    public void updateProxyMoney_add(PayProxyRecord ppr, SysUser proxy) {
        //扣除用户金额
        SysUser user1 = new SysUser();
        user1.setId(proxy.getId());
        user1.setFrozenMoney(BigDecimal.ZERO);
        user1.setMoney(ppr.getMoney());
        sysUserMapper.userFrozenMoneyAndMoney(user1);
        //保存调账记录
        ppr.setBalance(proxy.getMoney().add(ppr.getMoney()));
        ppr.setRemark("人工调账，增加余额" + ppr.getMoney() + "元");
        payProxyRecordDao.addSave(ppr);
    }

}
