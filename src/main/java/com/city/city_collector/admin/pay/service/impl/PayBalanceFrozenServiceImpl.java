package com.city.city_collector.admin.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.dao.PayBalanceFrozenDao;
import com.city.city_collector.admin.pay.service.PayBalanceFrozenService;
import com.city.city_collector.admin.system.dao.SysUserDao;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.pay.entity.PayBalanceFrozen;

/**
 * Description:余额冻结-service实现类
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Service
public class PayBalanceFrozenServiceImpl implements PayBalanceFrozenService {

    @Autowired
    PayBalanceFrozenDao payBalanceFrozenDao;

    @Autowired
    SysUserDao sysUserDao;

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
        int total = payBalanceFrozenDao.queryCount(params);
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
        page.setResults(payBalanceFrozenDao.queryPage(params));
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
    public void addSave(PayBalanceFrozen payBalanceFrozen) {
        payBalanceFrozenDao.addSave(payBalanceFrozen);
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
    public PayBalanceFrozen querySingle(Map<String, Object> params) {
        return payBalanceFrozenDao.querySingle(params);
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
    public void editSave(PayBalanceFrozen payBalanceFrozen) {
        payBalanceFrozenDao.editSave(payBalanceFrozen);
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
        payBalanceFrozenDao.delete(ids_str);
    }

    //解冻
    @Transactional
    public void updateStatus(PayBalanceFrozen balanceFrozen) {

        SysUser user1 = new SysUser();
        user1.setId(balanceFrozen.getUserId());
        user1.setFrozenMoney(balanceFrozen.getMoney().negate());
        sysUserDao.userFrozenMoney(user1);

        balanceFrozen.setStatus(1);
        payBalanceFrozenDao.editSave(balanceFrozen);
    }

    @Transactional
    public void createBalanceFrozen(PayBalanceFrozen balanceFrozen) {

        SysUser user1 = new SysUser();
        user1.setId(balanceFrozen.getUserId());
        user1.setFrozenMoney(balanceFrozen.getMoney());
        sysUserDao.userFrozenMoney(user1);

        balanceFrozen.setStatus(0);
        balanceFrozen.setFrozenType(6);
        payBalanceFrozenDao.addSave(balanceFrozen);
    }
}
