package com.city.city_collector.admin.pay.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.dao.PayMerchantChannelDao;
import com.city.city_collector.admin.pay.service.PayMerchantChannelService;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.admin.pay.entity.PayMerchantChannel;

/**
 * Description:商户通道-service实现类
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Service
public class PayMerchantChannelServiceImpl implements PayMerchantChannelService {

    @Autowired
    PayMerchantChannelDao payMerchantChannelDao;
    @Autowired
    SysUserService sysUserService;

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
        int total = payMerchantChannelDao.queryCount(params);
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
        page.setResults(payMerchantChannelDao.queryPage(params));
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
    public void addSave(PayMerchantChannel payMerchantChannel) {
        payMerchantChannelDao.addSave(payMerchantChannel);
        //更新通道信息
        sysUserService.updateChannelData();
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
    public PayMerchantChannel querySingle(Map<String, Object> params) {
        return payMerchantChannelDao.querySingle(params);
    }


    public PayMerchantChannel querySingleByMerchantId(Map<String, Object> params) {
        return payMerchantChannelDao.querySingleByMerchantId(params);
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
    public void editSave(PayMerchantChannel payMerchantChannel) {
        payMerchantChannelDao.editSave(payMerchantChannel);
        //更新通道信息
        sysUserService.updateChannelData();
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
        payMerchantChannelDao.delete(ids_str);
    }

    public void updateStatus(Long id, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("status", status);
        payMerchantChannelDao.updateStatus(params);
        //更新通道信息
        sysUserService.updateChannelData();
    }

    public String queryMerchantClientIds(Long id) {
        return payMerchantChannelDao.queryMerchantClientIds(id);
    }

    public List<Map<String, Object>> queryClientChannelList(Long id) {
        return payMerchantChannelDao.queryClientChannelList(id);
    }

    @Transactional
    public void addSaveClientChannel(Long id, List<Map<String, Long>> dataList) {
        //删除已关联的通道信息
        payMerchantChannelDao.deleteMerchantChannel(id);

        if (dataList != null && !dataList.isEmpty()) {
            List list = new ArrayList();

            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Long> dataMap = dataList.get(i);
                Map map = new HashMap();
                map.put("merchantChannelId", id);

                map.put("clientChannelId", dataMap.get("channelId"));
                map.put("priority", dataMap.get("priority"));
//                map.put("clientChannelId", channelIds[i]);
                list.add(map);

                if ((i > 0) && (i % 5000 == 0)) {
                    payMerchantChannelDao.addSaveMerchantChannel(list);
                    list = new ArrayList();
                }
            }

            if (!list.isEmpty())
                payMerchantChannelDao.addSaveMerchantChannel(list);
        }
        sysUserService.updateChannelData();
    }

    public List<Map<String, Object>> querySelectList() {
        return payMerchantChannelDao.querySelectList();
    }

    public void updateChannelData(Long[] ids, Integer status) {
        String ids_str = "";
        for (int i = 0; i < ids.length; i++) {
            ids_str += ids[i] + ",";
        }
        if (ids_str.endsWith(",")) {
            ids_str = ids_str.substring(0, ids_str.length() - 1);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids_str);
        params.put("status", status);

        payMerchantChannelDao.updateChannelDataStatus(params);
        //更新通道信息
        sysUserService.updateChannelData();
    }

    @Override
    public List<PayMerchantChannel> queryPayMerchantByIds(String ids) {
        return payMerchantChannelDao.queryPayMerchantChannelByIds(ids);
    }
}
