package com.city.city_collector.admin.pay.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.dao.PayClientChannelDao;
import com.city.city_collector.admin.pay.service.PayClientChannelService;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.admin.pay.entity.PayClientChannel;

/**
 * Description:上游通道-service实现类
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Service
public class PayClientChannelServiceImpl implements PayClientChannelService {

    @Autowired
    PayClientChannelDao payClientChannelDao;

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
        int total = payClientChannelDao.queryCount(params);
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
        page.setResults(payClientChannelDao.queryPage(params));
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
    public void addSave(PayClientChannel payClientChannel) {
        payClientChannelDao.addSave(payClientChannel);
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
    public PayClientChannel querySingle(Map<String, Object> params) {
        return payClientChannelDao.querySingle(params);
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
    public void editSave(PayClientChannel payClientChannel) {
        payClientChannelDao.editSave(payClientChannel);
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
        payClientChannelDao.delete(ids_str);
    }

    public void updateStatus(Long id, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("status", status);
        payClientChannelDao.updateStatus(params);
        //更新通道信息
        sysUserService.updateChannelData();
    }

    public List<Map<String, Object>> querySelectList() {
        return payClientChannelDao.querySelectList();
    }

    @Override
    public List<PayClientChannel> querySelectListByIds(String ids) {
        return payClientChannelDao.querySelectListByIds(ids);
    }

    public List<Map<String, Object>> querySelectListTest() {
        return payClientChannelDao.querySelectListTest();
    }

    public Map<String, Object> queryDetail(Map<String, Object> params) {
        return payClientChannelDao.queryDetail(params);
    }

    public List<Map<String, Object>> queryIndexEcharts() {
        return payClientChannelDao.queryIndexEcharts();
    }

    public List<Map<String, Object>> querySelectListTestPaytm() {
        return payClientChannelDao.querySelectListTestPaytm();
    }

    public void updateAlarmStatus(Long id, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("alarm", status);
        payClientChannelDao.updateAlarmStatus(params);
    }

    //批量更新状态
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

        payClientChannelDao.updateChannelDataStatus(params);
        //更新通道信息
        sysUserService.updateChannelData();
    }

    @Override
    public void editYjSave(PayClientChannel payClientChannel) {
        payClientChannelDao.editYjSave(payClientChannel);
    }

    @Override
    public String queryClientMerchantIds(Long id) {
        return payClientChannelDao.queryClientMerchantIds(id);
    }

    @Override
    public List<Map<String, Object>> queryMerchantChannelList(Long id) {
        return payClientChannelDao.queryMerchantChannelList(id);
    }

    @Transactional
    public void addSaveMerchantChannel(Long id, List<Map<String, Long>> dataList) {
        //删除已关联的通道信息
        payClientChannelDao.deleteClientChannel(id);

        if (dataList != null && !dataList.isEmpty()) {
            List list = new ArrayList();

            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Long> dataMap = dataList.get(i);
                Map map = new HashMap();
                map.put("merchantChannelId", dataMap.get("channelId"));

                map.put("clientChannelId", id);
                map.put("priority", dataMap.get("priority"));
//                map.put("clientChannelId", channelIds[i]);
                list.add(map);

                if ((i > 0) && (i % 5000 == 0)) {
                    payClientChannelDao.addSaveClientChannel(list);
                    list = new ArrayList();
                }
            }

            if (!list.isEmpty())
                payClientChannelDao.addSaveClientChannel(list);
        }
        sysUserService.updateChannelData();
    }
    public List<PayClientChannel> queryAllList(Map<String, Object> params) {
        return payClientChannelDao.queryAllList(params);
    }
}
