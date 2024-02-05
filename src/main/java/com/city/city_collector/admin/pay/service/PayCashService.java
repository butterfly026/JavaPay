package com.city.city_collector.admin.pay.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.system.entity.SysUser;

/**
 * Description:商户提现-service接口
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
public interface PayCashService {
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
    public Page queryPage(Map<String, Object> params, Page page, String[] orders);

    /**
     * Description:保存记录
     *
     * @param payCash
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void addSave(PayCash payCash);

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:demo
     * @since 2020-6-29
     */
    public PayCash querySingle(Map<String, Object> params);

    /**
     * Description:更新记录
     *
     * @param payCash
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void editSave(PayCash payCash);

    /**
     * Description:删除指定记录
     *
     * @param ids ID数组
     * @return void
     * @author:demo
     * @since 2020-6-29
     */
    public void delete(Long[] ids);

    /**
     * 创建提现订单
     *
     * @param payCash
     * @param user
     * @author:nb
     */
    public void createCash(PayCash payCash, SysUser user, Integer type,Boolean fromApi);

    /**
     * 更新提现状态
     *
     * @param payCash
     * @author:nb
     */
    public void updateCashStatus(PayCash payCash);

    /**
     * 拒绝提现
     *
     * @param payCash
     * @author:nb
     */
    public void refuseCash(PayCash payCash, Integer type);

    /**
     * 上游代付提交
     *
     * @param payCash
     * @param client
     * @param type
     * @param money
     * @param clientCommission
     * @param clientRealMoney
     * @author:nb
     */
    public void merchantClientCashSubmit(PayCash payCash, SysUser client, Integer type, BigDecimal money, BigDecimal clientCommission, BigDecimal clientRealMoney);


    public void cashSuc(PayCash payCash);

    public void cashFail(PayCash payCash);

    public void cashRefuse(PayCash payCash);
    
    /**
     * 解冻/冻结用户余额和金额
     *
     * @param user
     * @author:nb
     */
    public void userFrozenMoneyAndMoney(SysUser user);

    /**
     * 发送通知
     *
     * @param paycash
     * @author:nb
     */
    public void sendCashNotify(PayCash paycash);

    /**
     * 查询已成功且通知状态为未通知且由API发起的订单
     *
     * @return
     * @author:nb
     */
    public List<PayCash> querySuccessCashOrder();

    /**
     * 查询需要通知的订单
     *
     * @return
     * @author:nb
     */
    public List<PayCash> queryNeedNotifyCashOrder();

    public void orderSuccessSingle(PayCash cash);

    public List<Map<String, Object>> queryExportList(Map<String, Object> params, String[] orders);

    public List<Map<String, Object>> queryExportListMerchant(Map<String, Object> params, String[] orders);

    public Map<String, Object> queryCashDetail(Map<String, Object> params);

    public void updateCashRemark(PayCash payCash);

    /**
     * 更换上游代付
     *
     * @param payCash
     * @param client
     * @param type
     * @param money
     * @param clientCommission
     * @param clientRealMoney
     * @author:nb
     */
    public void merchantChangeClientCashSubmit(PayCash payCash, SysUser client, Integer type, BigDecimal money, BigDecimal clientCommission, BigDecimal clientRealMoney);

    public List<Map<String, Object>> queryClientInfoByModel();

    /**
     * 查询待处理的订单数
     **/
    public int queryWaitDealCount();

    public int updateAutoModel(Map<String, Object> params);

    public Map<String, Object>queryAutoPayModule();

    public int queryCashOrderCountByMerchant(Map<String, Object> params);
}
