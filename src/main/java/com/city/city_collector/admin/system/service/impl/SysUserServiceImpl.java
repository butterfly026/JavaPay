package com.city.city_collector.admin.system.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.city.city_collector.channel.bean.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.city.adminpermission.AdminPermissionManager;
import com.city.city_collector.admin.pay.controller.PayClientChannelController;
import com.city.city_collector.admin.pay.controller.PaytmClientChannelController;
import com.city.city_collector.admin.pay.entity.PayBalanceFrozen;
import com.city.city_collector.admin.pay.service.PayBalanceFrozenService;
import com.city.city_collector.admin.system.dao.SysUserDao;
import com.city.city_collector.admin.system.entity.SysMenu;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.ChannelManager;
import com.city.city_collector.channel.ChannelManager1;
import com.city.city_collector.channel.PaytmChannelManager;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.util.AESUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserDao sysUserMapper;
    @Autowired
    PayBalanceFrozenService payBalanceFrozenService;

    @Override
    public Page queryPage(Map<String, Object> params, Page page, String[] orders) {
        int total = this.sysUserMapper.queryCount(params);
        page.setTotal(total);
        params.put("start", Integer.valueOf(page.getStartRow()));
        params.put("end", page.getPageSize());

        if ((orders != null) && (orders.length > 0)) {
            String orderby = "" ;
            for (String str : orders) {
                String[] arr1 = str.split(" ");
                if (arr1.length == 2) {
                    orderby = orderby + "`" + arr1[0] + "` " + arr1[1] + "," ;
                }
            }
            if (orderby.endsWith(",")) {
                orderby = orderby.substring(0, orderby.length() - 1);
            }
            params.put("orderby", orderby);
        }

        page.setResults(this.sysUserMapper.queryPage(params));

        return page;
    }

    public void addSave(SysUser sysUser) {
        this.sysUserMapper.addSave(sysUser);
    }

    public void editSave(SysUser sysUser) {
        this.sysUserMapper.editSave(sysUser);
    }

    public void delete(Long[] ids) {
        String ids_str = "" ;
        for (int i = 0; i < ids.length; i++) {
            ids_str = ids_str + ids[i] + "," ;
        }
        if (ids_str.endsWith(",")) {
            ids_str = ids_str.substring(0, ids_str.length() - 1);
        }
        this.sysUserMapper.delete(ids_str);
        AdminPermissionManager.updateSystemRoleVersion();
    }

    public SysUser querySysUserById(Long id) {
        Map params = new HashMap();
        params.put("id", id);
        return this.sysUserMapper.querySingle(params);
    }

    public SysUser querySysUserByUsername(String username) {
        Map params = new HashMap();
        params.put("username", username);
        return this.sysUserMapper.querySingle(params);
    }

    public List<Map<String, Object>> queryAllUser() {
        return this.sysUserMapper.queryAllUser();
    }

    public String queryUserMenuIds(Long userId) {
        return this.sysUserMapper.queryUserMenuIds(userId);
    }

    @Transactional
    public void addSaveUserMenu(Long userId, Long[] menuIds) {
        this.sysUserMapper.deleteUserMenu(userId + "");

        if (menuIds != null) {
            List list = new ArrayList();

            for (int i = 0; i < menuIds.length; i++) {
                Map map = new HashMap();
                map.put("user_id", userId);
                map.put("menu_id", menuIds[i]);
                list.add(map);

                if ((i > 0) && (i % 5000 == 0)) {
                    this.sysUserMapper.addSaveUserMenu(list);
                    list = new ArrayList();
                }
            }

            if (!list.isEmpty())
                this.sysUserMapper.addSaveUserMenu(list);
        }

        AdminPermissionManager.updateSystemRoleVersion();
    }

    public String queryUserRoleIds(Long userId) {
        return this.sysUserMapper.queryUserRoleIds(userId);
    }

    @Transactional
    public void addSaveUserRole(Long userId, Long[] roleIds) {
        this.sysUserMapper.deleteUserRole(userId + "");

        if (roleIds != null) {
            List list = new ArrayList();

            for (int i = 0; i < roleIds.length; i++) {
                Map map = new HashMap();
                map.put("user_id", userId);
                map.put("role_id", roleIds[i]);
                list.add(map);

                if ((i > 0) && (i % 5000 == 0)) {
                    this.sysUserMapper.addSaveUserRole(list);
                    list = new ArrayList();
                }
            }

            if (!list.isEmpty())
                this.sysUserMapper.addSaveUserRole(list);
        }
        AdminPermissionManager.updateSystemRoleVersion();
    }

    public List<SysMenu> queryUserMenus(Long id) {
        return this.sysUserMapper.queryUserMenus(id);
    }

    public List<String> queryUserPermission(Long id) {
        return this.sysUserMapper.queryUserPermission(id);
    }

    /**
     * 查询导出的数据
     *
     * @param param  参数
     * @param orders 排序字符串
     * @return
     * @author:nb
     */
    public List<Map<String, Object>> queryExportList(Map<String, Object> params, String[] orders) {
        if ((orders != null) && (orders.length > 0)) {
            String orderby = "" ;
            for (String str : orders) {
                String[] arr1 = str.split(" ");
                if (arr1.length == 2) {
                    orderby = orderby + "`" + arr1[0] + "` " + arr1[1] + "," ;
                }
            }
            if (orderby.endsWith(",")) {
                orderby = orderby.substring(0, orderby.length() - 1);
            }
            params.put("orderby", orderby);
        }
        return sysUserMapper.queryExportList(params);
    }


    /**
     * 更新通道数据
     *
     * @author:nb
     */
    public void updateChannelData() {
        //获取有效的商户或者上游
//      List<SysUser> userList=sysUserMapper.queryMerchantOrClient();
        /**获取所有有效通道--普通*/
        List<ChannelInfo> channelList = sysUserMapper.queryMerchantClientChannelList();

        //获取商户对象和上游对象
        List<Merchant> merchantList = new Vector<Merchant>();
        List<Client> clientList = new Vector<Client>();

        List<MerchantChannel> mcList = new Vector<MerchantChannel>();
        List<ClientChannel> ccList = new Vector<ClientChannel>();

        for (ChannelInfo ci : channelList) {

            Merchant merchant = null;
            for (Merchant mc : merchantList) {
                if (ci.getMerchantId().equals(mc.getId())) {
                    merchant = mc;
                    break;
                }
            }
            if (merchant == null) {
                merchant = new Merchant();
                merchant.setId(ci.getMerchantId());
                merchant.setNo(ci.getMerchantNo());
                merchant.setName(ci.getMerchantName());
                merchantList.add(merchant);
            }


            Client client = null;
            for (Client c : clientList) {
                if (ci.getClientId().equals(c.getId())) {
                    client = c;
                    break;
                }
            }
            if (client == null) {
                client = new Client();
                client.setId(ci.getClientId());
                client.setNo(ci.getClientNo());
                client.setName(ci.getClientName());//kahn ccname
                clientList.add(client);
            }

            MerchantChannel merchantChannel = null;
            for (MerchantChannel mcc : mcList) {
                if (mcc.getId().equals(ci.getMcId())) {
                    merchantChannel = mcc;
                    break;
                }
            }

            if (merchantChannel == null) {
                merchantChannel = new MerchantChannel();
                merchantChannel.setId(ci.getMcId());
                merchantChannel.setMerchant(merchant);
                merchantChannel.setNo(merchant.getNo());
                merchantChannel.setName(ci.getMcName());
                merchantChannel.setPayType(ci.getChanelTypeId());
                mcList.add(merchantChannel);
            }

            ClientChannel clientChannel = null;
//          for(ClientChannel cc:ccList) {
//              if(cc.getId().equals(ci.getCcId())) {
//                  clientChannel=cc;
//                  break;
//              }
//          }

            if (clientChannel == null) {
//              try {
//                  if(ChannelManager.channels.get(ci.getKeyname())!=null) {
//                      clientChannel=(ClientChannel)ChannelManager.channels.get(ci.getKeyname()).clone();
//                  }
//              }
//              catch (CloneNotSupportedException e) {
//
//                  e.printStackTrace();
//              }
//              if(clientChannel==null) continue;
                clientChannel = new ClientChannel();

                clientChannel.setId(ci.getCcId());
                clientChannel.setClient(client);
                //设置跳转方式
                clientChannel.setGtype(ci.getGtype());

                clientChannel.setMerchantMy(ci.getClientMerchantMy());
                clientChannel.setMerchantNo(ci.getClientMerchantNo());
                clientChannel.setUrlpay(ci.getUrlpay());
                //优先级
                clientChannel.setPriority(ci.getPriority());
                //优先平台
                clientChannel.setPrimaryPlatform(ci.getPrimaryPlatform());
                //允许尝试次数
                clientChannel.setRetryNumber(ci.getRetryNumber());

                //设置参数
                if (StringUtils.isNotBlank(ci.getParams())) {
                    try {
                        Map<String, Object> params = new Gson().fromJson(ci.getParams(), new TypeToken<HashMap<String, Object>>() {
                        }.getType());
                        clientChannel.setParams(params);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("参数不合法，略过...");
                    }
                }
                //设置IP
                if (StringUtils.isNotBlank(ci.getMerchantIp())) {
                    clientChannel.setMerchatnIp("," + ci.getMerchantIp() + ",");
                }

                clientChannel.setEndTime(ci.getEndTime());
                clientChannel.setStartTime(ci.getStartTime());

                //设置最小和最大时间
                if (StringUtils.isNotBlank(clientChannel.getStartTime())) {
                    clientChannel.setMinTime(Long.parseLong(clientChannel.getStartTime().replace(":", "")));
                }
                if (StringUtils.isNotBlank(clientChannel.getEndTime())) {
                    clientChannel.setMaxTime(Long.parseLong(clientChannel.getEndTime().replace(":", "")));
                }

                clientChannel.setMaxMoney(ci.getMaxMoney());
                clientChannel.setMinMoney(ci.getMinMoney());

                
                clientChannel.setName(ci.getClientName());//kahn ccname ci.getCcName()
                clientChannel.setNo(ci.getClientNo());
                clientChannel.setPayType(ci.getChanelTypeId());

                clientChannel.setType(ci.getMtype());

                clientChannel.setKeyname(ci.getKeyname());

                String moneyStrs = ci.getMoneyStr();
                if (StringUtils.isNotBlank(moneyStrs)) {
                    List<BigDecimal> moneys = new ArrayList<BigDecimal>();

                    String[] ms = moneyStrs.split(",");
                    for (int i = 0; i < ms.length; i++) {
                        if (StringUtils.isNotBlank(ms[i])) {
                            moneys.add(new BigDecimal(ms[i]));
                        }
                    }

                    clientChannel.setMoneyList(moneys);
                }

                ccList.add(clientChannel);
            }

            Vector<ClientChannel> clientChannels = merchantChannel.getClientChannels();
            if (clientChannels == null) {
                clientChannels = new Vector<ClientChannel>();
                merchantChannel.setClientChannels(clientChannels);
            }
            clientChannels.add(clientChannel);
        }

//      System.out.println("GGGGOOOO====");
        System.out.println("更新通道列表：" + new Gson().toJson(mcList));

        ChannelManager1.getInstance().updateChannelData(mcList);

        /**获取所有有效通道--paytm*/
//        List<ChannelInfo> channelList1 = sysUserMapper.queryMerchantClientChannelListByPaytm();
//
//        Map<Long, Vector<PaytmClient>> paytmMaps = new HashMap<Long, Vector<PaytmClient>>();
//
//        Vector<PaytmClient> clientList1;
//        for (ChannelInfo cinfo : channelList1) {
//            PaytmClient paytmClient = new PaytmClient();
//
//            //获取已保存的paytm商户通道对象
//            clientList1 = paytmMaps.get(cinfo.getMcId());
//            if (clientList1 == null) {
//                clientList1 = new Vector<PaytmClient>();
//                paytmMaps.put(cinfo.getMcId(), clientList1);
//            }
//
//            paytmClient.setStartTime(cinfo.getStartTime());
//            paytmClient.setEndTime(cinfo.getEndTime());
//
//            //设置最小和最大时间
//            if (StringUtils.isNotBlank(paytmClient.getStartTime())) {
//                paytmClient.setMinTime(Long.parseLong(paytmClient.getStartTime().replace(":", "")));
//            }
//            if (StringUtils.isNotBlank(paytmClient.getEndTime())) {
//                paytmClient.setMaxTime(Long.parseLong(paytmClient.getEndTime().replace(":", "")));
//            }
//
//            paytmClient.setMaxMoney(cinfo.getMaxMoney());
//            paytmClient.setMinMoney(cinfo.getMinMoney());
//
//            paytmClient.setMerchantChannelId(cinfo.getMcId());
//            paytmClient.setId(cinfo.getCcId());
//            paytmClient.setClientId(cinfo.getClientId());
//            paytmClient.setClientNo(cinfo.getClientNo());
//
//            paytmClient.setMid(cinfo.getPaytmid());
//            try {
//                //获取秘钥
//                String md5 = cinfo.getPaytmmd5();
//                String uuid = cinfo.getPaytmuid();
//
//                String pwd = md5.substring(md5.length() - 7) + uuid.substring(uuid.length() - 4) + PaytmClientChannelController.KEY;
//
//                String key = AESUtil.decrypt(cinfo.getPaytmkey(), pwd);
//
//                paytmClient.setMkey(key);
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            }
//
//            paytmClient.setType(cinfo.getMtype());
//
//            String moneyStrs = cinfo.getMoneyStr();
//            if (StringUtils.isNotBlank(moneyStrs)) {
//                List<BigDecimal> moneys = new ArrayList<BigDecimal>();
//
//                String[] ms = moneyStrs.split(",");
//                for (int i = 0; i < ms.length; i++) {
//                    if (StringUtils.isNotBlank(ms[i])) {
//                        moneys.add(new BigDecimal(ms[i]));
//                    }
//                }
//
//                paytmClient.setMoneyList(moneys);
//            }
//
//            clientList1.add(paytmClient);
//        }
//        PaytmChannelManager.getInstance().updateChannelData(paytmMaps);
    }

    /**
     * 更新用户余额或冻结金额
     *
     * @param sysUser
     * @author:nb
     */
    public void updateUserMoney(SysUser sysUser) {
        sysUserMapper.updateUserMoney(sysUser);
    }

    /**
     * 查询商户首页信息
     *
     * @param params
     * @return
     * @author:nb
     */
    public Map<String, Object> queryMerchantInfo(Map<String, Object> params) {
        return sysUserMapper.queryMerchantInfo(params);
    }

    /**
     * 冻结余额
     *
     * @param id          用户id
     * @param frozenMoney 冻结金额
     * @param type        用户类型
     * @author:nb
     */
    @Transactional
    public void userFrozenMoney(Long id, BigDecimal frozenMoney, Integer type, String sn) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setFrozenMoney(frozenMoney);
        sysUserMapper.userFrozenMoney(user);

        //添加冻结记录
        PayBalanceFrozen pbf = new PayBalanceFrozen();
        pbf.setType(type);
        pbf.setUserId(id);
        pbf.setMoney(frozenMoney);

        pbf.setStatus(0);
        pbf.setFrozenType(0);
        pbf.setRemark("用户提现" + frozenMoney + "元");
        pbf.setOrderSn(sn);
        payBalanceFrozenService.addSave(pbf);
    }

    /**
     * 解冻余额
     *
     * @param id
     * @param frozenMoney
     * @author:nb
     */
    @Transactional
    public void userDisFrozenMoney(Long id, BigDecimal frozenMoney, Integer type, String sn) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setFrozenMoney(frozenMoney);
        sysUserMapper.userDisFrozenMoney(user);

        //添加解冻记录
        PayBalanceFrozen pbf = new PayBalanceFrozen();
        pbf.setType(type);
        pbf.setUserId(id);
        pbf.setMoney(frozenMoney);

        pbf.setStatus(0);
        pbf.setFrozenType(1);
        pbf.setRemark("用户提现成功，解冻" + frozenMoney + "元");
        pbf.setJdTime(new Date());
        pbf.setOrderSn(sn);
        payBalanceFrozenService.addSave(pbf);
    }

    /**
     * 获取所有有效上游信息
     *
     * @return
     * @author:nb
     */
    public List<Map<String, Object>> queryClientInfoList() {
        return sysUserMapper.queryClientInfoList();
    }

    /**
     * 查询管理员首页信息
     *
     * @param params
     * @return
     * @author:nb
     */
    public Map<String, Object> queryAdminInfo(Map<String, Object> params) {
        return sysUserMapper.queryAdminInfo(params);
    }

    public List<Map<String, Object>> queryClientInfoListAll() {
        return sysUserMapper.queryClientInfoListAll();
    }

    public void updateChromeId(SysUser user) {
        sysUserMapper.updateChromeId(user);
    }

    public Map<String, Object> queryProxyInfo(Map<String, Object> params) {
        return sysUserMapper.queryProxyInfo(params);
    }

    public int querySysDealOrder() {
        return sysUserMapper.querySysDealOrder();
    }

    /**
     * 查询指定时间段内的订单数
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> queryOrderCountCharts(Map<String, Object> params) {
        return sysUserMapper.queryOrderCountCharts(params);
    }

    public List<Map<String, Object>> queryOrderCountAlarm(Map<String, Object> params) {
        return sysUserMapper.queryOrderCountAlarm(params);
    }
}
