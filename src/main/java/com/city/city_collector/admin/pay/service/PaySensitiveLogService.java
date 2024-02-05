package com.city.city_collector.admin.pay.service;

import com.city.city_collector.admin.pay.entity.PaySensitiveLog;
import com.city.city_collector.common.bean.Page;

import java.util.Map;

public interface PaySensitiveLogService {
    Page queryPage(Map<String, Object> map, Page page, String[] orders);

    void insertLog(PaySensitiveLog paySensitiveLog);

}
