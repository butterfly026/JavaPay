package com.city.city_collector.admin.pay.service.impl;

import com.city.city_collector.admin.pay.dao.YajinDao;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.entity.PaySensitiveLog;
import com.city.city_collector.admin.pay.service.PaySensitiveLogService;
import com.city.city_collector.admin.pay.service.YajinService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.paylog.PayLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
public class YajinServiceImpl implements YajinService {

    @Autowired
    private YajinDao yajinDao;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PaySensitiveLogService paySensitiveLogService;

    @Override
    public void addUserYajin(SysUser sysUser) {

        if (null == sysUser.getYajin()) return;

        int yajin = sysUser.getYajin().intValue();
        if (yajin < 1) return;

        yajinDao.insertYajin(sysUser.getId(), yajin, 0);

    }

    @Override
    public void editUserYajin(SysUser sysUser) {

        if (null == sysUser.getYajin()) return;

        int yajin = sysUser.getYajin().intValue();
        if (yajin < 1) yajinDao.deleteYajin(sysUser.getId());
        else {
            Integer yajinSelect = yajinDao.selectYajin(sysUser.getId());
            if (null == yajinSelect) yajinDao.insertYajin(sysUser.getId(), yajin, 0);
            else yajinDao.updateYajin(sysUser.getId(), yajin);
        }

    }

    @Override
    public void selectYajin(SysUser result) {
        Integer yajin = yajinDao.selectYajin(result.getId());
        if (null != yajin) result.setYajin(new BigDecimal(yajin));
    }

    @Override
    public void addMoney(PayOrder po) {
        Integer yajin = yajinDao.selectYajin(po.getClientId());
        if (null == yajin) return;

        yajinDao.addMoney(po.getClientId(), po.getMoney());

        Map<String, Integer> map = yajinDao.selectYajinMoney(po.getClientId());
        if (map.get("money") >= map.get("yajin")) {

            try {
                SysUser sysUser = new SysUser();
                sysUser.setId(po.getClientId());
                sysUser.setStatus("0");
                sysUserService.editSave(sysUser);
                //更新通道对象信息
                sysUserService.updateChannelData();


                PaySensitiveLog paySensitiveLog = new PaySensitiveLog(po.getClientId(),"",new Date(),"autoCloseClient","自动关闭通道: 余额: "+map.get("money") +" 押金: "+map.get("yajin"),"", "");
                paySensitiveLogService.insertLog(paySensitiveLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean canOpen(Long id) {
        Map<String, Integer> map = yajinDao.selectYajinMoney(id);
        if (null == map) return true;

        if (map.get("yajin") > map.get("money")) return true;
        return false;
    }

    @Override
    public void resetYajin(Long id) {
        yajinDao.updateMoney(id);
    }


}
