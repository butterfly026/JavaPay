package com.city.city_collector.admin.pay.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.dao.PayChannelTypeDao;
import com.city.city_collector.admin.pay.service.PayChannelTypeService;
import com.city.city_collector.admin.pay.entity.PayChannelType;

/**
 * Description:通道类型-service实现类
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Service
public class PayChannelTypeServiceImpl implements PayChannelTypeService {

    @Autowired
    PayChannelTypeDao payChannelTypeDao;

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
        int total = payChannelTypeDao.queryCount(params);
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
        page.setResults(payChannelTypeDao.queryPage(params));
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
    public void addSave(PayChannelType payChannelType) {
        payChannelTypeDao.addSave(payChannelType);
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
    public PayChannelType querySingle(Map<String, Object> params) {
        return payChannelTypeDao.querySingle(params);
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
    public void editSave(PayChannelType payChannelType) {
        payChannelTypeDao.editSave(payChannelType);
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
        payChannelTypeDao.delete(ids_str);
    }

    public PayChannelType queryChannelTypeByCode(String code) {
        return payChannelTypeDao.queryChannelTypeByCode(code);
    }

    public List<PayChannelType> queryAllList(Map<String, Object> params) {
        return payChannelTypeDao.queryAllList(params);
    }
}
