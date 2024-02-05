package com.city.city_collector.admin.system.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.system.entity.SysMenu;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.channel.bean.ChannelInfo;

public abstract interface SysUserDao {
    public abstract int queryCount(Map<String, Object> paramMap);

    public abstract List<Map<String, Object>> queryPage(Map<String, Object> paramMap);

    public abstract void addSave(SysUser paramSysUser);

    public abstract SysUser querySingle(Map<String, Object> paramMap);

    public abstract void editSave(SysUser paramSysUser);

    public abstract void delete(String paramString);

    public abstract List<Map<String, Object>> queryAllUser();

    public abstract String queryUserMenuIds(Long paramLong);

    public abstract void deleteUserMenu(String paramString);

    public abstract void addSaveUserMenu(List<Map<String, Long>> paramList);

    public abstract String queryUserRoleIds(Long paramLong);

    public abstract void deleteUserRole(String paramString);

    public abstract void addSaveUserRole(List<Map<String, Long>> paramList);

    public abstract List<SysMenu> queryUserMenus(Long paramLong);

    public abstract List<String> queryUserPermission(Long paramLong);

    /**
     * 导出excel
     *
     * @param paramMap
     * @return
     * @author:nb
     */
    public List<Map<String, Object>> queryExportList(Map<String, Object> paramMap);

    public List<SysUser> queryMerchantOrClient();

    public List<ChannelInfo> queryMerchantClientChannelList();

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


    public void userFrozenMoney(SysUser user);

    public void userDisFrozenMoney(SysUser user);

    /**
     * 获取所有有效上游信息
     *
     * @return
     * @author:nb
     */
    public List<Map<String, Object>> queryClientInfoList();


    public List<SysUser> queryAllClientInfo();

    public void userFrozenMoneyAndMoney(SysUser user);

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

    /**
     * 查询paytm相关的有效通道列表
     *
     * @return
     * @author:nb
     */
    public List<ChannelInfo> queryMerchantClientChannelListByPaytm();

    public Map<String, Object> queryProxyInfo(Map<String, Object> params);

    public int querySysDealOrder();

    /**
     * 查询指定时间段内的订单数
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> queryOrderCountCharts(Map<String, Object> params);

    /**
     * 查询预警订单
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> queryOrderCountAlarm(Map<String, Object> params);

}
