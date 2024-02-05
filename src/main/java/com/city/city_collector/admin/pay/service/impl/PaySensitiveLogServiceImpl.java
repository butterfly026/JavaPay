package com.city.city_collector.admin.pay.service.impl;

import com.city.city_collector.admin.pay.dao.PayLogDao;
import com.city.city_collector.admin.pay.entity.PaySensitiveLog;
import com.city.city_collector.admin.pay.service.PaySensitiveLogService;
import com.city.city_collector.common.bean.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaySensitiveLogServiceImpl implements PaySensitiveLogService {

    @Autowired
    private PayLogDao payLogDao;

    @Override
    public Page queryPage(Map<String, Object> params, Page page, String[] orders) {

        //获取记录总数
        int total = payLogDao.querySensitiveLogCount(params);
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
        page.setResults(payLogDao.querySensitiveLogPage(params));
        return page;

    }

    @Override
    public void insertLog(PaySensitiveLog paySensitiveLog) {
        payLogDao.insertSensitiveLog(paySensitiveLog);
    }


}
