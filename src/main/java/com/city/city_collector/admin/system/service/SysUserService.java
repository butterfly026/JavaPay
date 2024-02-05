package com.city.city_collector.admin.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysMenu;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.common.bean.Page;

public abstract interface SysUserService {
    public abstract Page queryPage(Map<String, Object> paramMap, Page paramPage, String[] paramArrayOfString);

    public abstract void addSave(SysUser paramSysUser);

    public abstract void editSave(SysUser paramSysUser);

    public abstract void delete(Long[] paramArrayOfLong);

    public abstract SysUser querySysUserById(Long paramLong);

    public abstract SysUser querySysUserByUsername(String paramString);

    public abstract List<Map<String, Object>> queryAllUser();

    public abstract String queryUserMenuIds(Long paramLong);

    public abstract void addSaveUserMenu(Long paramLong, Long[] paramArrayOfLong);

    public abstract String queryUserRoleIds(Long paramLong);

    public abstract void addSaveUserRole(Long paramLong, Long[] paramArrayOfLong);

    public abstract List<SysMenu> queryUserMenus(Long paramLong);

    public abstract List<String> queryUserPermission(Long paramLong);

    /**
     * 查询导出的数据
     *
     * @param param  参数
     * @param orders 排序字符串
     * @return
     * @author:nb
     */
    public List<Map<String, Object>> queryExportList(Map<String, Object> params, String[] orders);

    /**
     * 更新通道数据
     *
     * @author:nb
     */
    public void updateChannelData();

    /**
     * 更新用户余额或冻结金额
     *
     * @param sysUser
     * @author:nb
     */
    public void updateUserMoney(SysUser sysUser);


    /**
     * 查询商户首页信息
     *
     * @param params
     * @return
     * @author:nb
     */
    public Map<String, Object> queryMerchantInfo(Map<String, Object> params);

    /**
     * 冻结余额
     *
     * @param id
     * @param frozenMoney
     * @author:nb
     */
    public void userFrozenMoney(Long id, BigDecimal frozenMoney, Integer type, String sn);

    /**
     * 解冻余额
     *
     * @param id
     * @param frozenMoney
     * @author:nb
     */
    public void userDisFrozenMoney(Long id, BigDecimal frozenMoney, Integer type, String sn);

    /**
     * 获取所有有效上游信息
     *
     * @return
     * @author:nb
     */
    public List<Map<String, Object>> queryClientInfoList();

    /**
     * 查询管理员首页信息
     *
     * @param params
     * @return
     * @author:nb
     */
    public Map<String, Object> queryAdminInfo(Map<String, Object> params);

    public List<Map<String, Object>> queryClientInfoListAll();

    public void updateChromeId(SysUser user);

    public Map<String, Object> queryProxyInfo(Map<String, Object> params);

    public int querySysDealOrder();

    /**
     * 查询指定时间段内的订单数
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> queryOrderCountCharts(Map<String, Object> params);

    public List<Map<String, Object>> queryOrderCountAlarm(Map<String, Object> params);
}
