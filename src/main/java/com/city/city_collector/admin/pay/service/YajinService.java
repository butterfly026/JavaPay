package com.city.city_collector.admin.pay.service;

import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.system.entity.SysUser;

public interface YajinService {
    void addUserYajin(SysUser sysUser);

    void editUserYajin(SysUser sysUser);

    void selectYajin(SysUser result);

    void addMoney(PayOrder po);

    boolean canOpen(Long id);

    void resetYajin(Long id);
}
