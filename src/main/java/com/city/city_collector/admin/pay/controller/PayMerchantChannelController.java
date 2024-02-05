package com.city.city_collector.admin.pay.controller;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.entity.PaySensitiveLog;
import com.city.city_collector.admin.pay.service.*;
import com.city.city_collector.common.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.common.util.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayMerchantChannel;
import com.city.city_collector.admin.system.entity.SysUser;

/**
 * Description:商户通道-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payMerchantChannel")
public class PayMerchantChannelController {
    @Autowired
    PayMerchantChannelService payMerchantChannelService;

    @Autowired
    PayChannelTypeService payChannelTypeService;

    @Autowired
    PayMerchantService payMerchantService;

    @Autowired
    PayClientService payClientService;

    @Autowired
    PayClientChannelService payClientChannelService;

    @Autowired
    private PaySensitiveLogService paySensitiveLogService;

    @Autowired
    private HttpServletRequest request;

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:paymerchantchannel:list"})
    @RequestMapping("/view")
    public String view(Model model) {
        Map<String, Object> params = new HashMap<String, Object>();

        model.addAttribute("merchantList", payMerchantService.querySelectList());
        model.addAttribute("merchantChannelList", payMerchantChannelService.querySelectList());
        model.addAttribute("clientList", payClientChannelService.querySelectList());
        model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));
        return "admin/pay/paymerchantchannel/list";
    }

    /**
     * Description:分页查询
     *
     * @param map      参数组
     * @param pageNo   当前页码
     * @param pageSize 每页记录数，为null使用默认值20
     * @param orders   排序列表
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchantchannel:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payMerchantChannelService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }

    /**
     * Description:打开新增页面
     *erPayNormal(
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchantchannel:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        Map<String, Object> params = new HashMap<String, Object>();

        model.addAttribute("merchantList", payMerchantService.querySelectList());
        model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));
        return "admin/pay/paymerchantchannel/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payMerchantChannel
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchantchannel:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayMerchantChannel payMerchantChannel) {
        //数据验证
        if (payMerchantChannel == null) {
            return Message.error("参数错误");
        }

        try {

            payMerchantChannelService.addSave(payMerchantChannel);

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            PaySensitiveLog paySensitiveLog = new PaySensitiveLog(sysUser.getId(), sysUser.getUsername(), new Date(), "addMerChannel", "新增商户渠道 商户费率:" + payMerchantChannel.getMerchantRatio() + " 代理费率:" + payMerchantChannel.getProxyRatio(), payMerchantChannel.getId() + "_" + payMerchantChannel.getName(), CommonUtil.getIp(request));
            paySensitiveLogService.insertLog(paySensitiveLog);

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    /**
     * Description:打开编辑页面
     *
     * @param id    记录ID
     * @param model model对象
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchantchannel:edit"})
    @RequestMapping("/edit")
    public String edit(Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/pay/paymerchantchannel/edit";
        }
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        //获取需要修改的记录数据
        PayMerchantChannel result = payMerchantChannelService.querySingle(params);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {
            model.addAttribute("result", result);
            model.addAttribute("merchantList", payMerchantService.querySelectList());
            model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));
        }
        return "admin/pay/paymerchantchannel/edit";
    }

    /**
     * Description:编辑保存
     *
     * @param
     * @param
     * @param id 编辑的记录ID
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchantchannel:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(PayMerchantChannel payMerchantChannel) {
        //数据验证
        if (payMerchantChannel == null) {
            return Message.error("参数错误");
        }


        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", payMerchantChannel.getId());
            PayMerchantChannel payMerchantChannelOld = payMerchantChannelService.querySingle(map);
            payMerchantChannelService.editSave(payMerchantChannel);

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            PaySensitiveLog paySensitiveLog = new PaySensitiveLog(sysUser.getId(), sysUser.getUsername(), new Date(), "updateMerChannel", "编辑商户渠道 当前-商户费率:" + payMerchantChannelOld.getMerchantRatio() + " 代理费率:" + payMerchantChannelOld.getProxyRatio() + " 修改后-商户费率:" + payMerchantChannel.getMerchantRatio() + " 代理费率:" + payMerchantChannel.getProxyRatio(), payMerchantChannel.getId() + "_" + payMerchantChannel.getName(), CommonUtil.getIp(request));
            paySensitiveLogService.insertLog(paySensitiveLog);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    /**
     * Description:删除记录
     *
     * @param ids 需要删除的记录的ID数组
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchantchannel:delete"})
    @RequestMapping("/delete")
    public @ResponseBody
    Message delete(@RequestParam(value = "ids[]") Long[] ids) {
        if (ids != null && ids.length == 0) {
            return Message.error("请选择要删除的数据");
        }
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            HashMap<String, Object> map = new HashMap<>();
            for (Long id : ids) {
                map.put("id", id);
                PayMerchantChannel payMerchantChannel = payMerchantChannelService.querySingle(map);
                PaySensitiveLog paySensitiveLog = new PaySensitiveLog(sysUser.getId(), sysUser.getUsername(), new Date(), "deleteMerChannel", "删除商户渠道 商户费率:" + payMerchantChannel.getMerchantRatio() + " 代理费率:" + payMerchantChannel.getProxyRatio(), payMerchantChannel.getId() + "_" + payMerchantChannel.getName(), CommonUtil.getIp(request));
                paySensitiveLogService.insertLog(paySensitiveLog);
            }

            payMerchantChannelService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:paymerchantchannel:status"})
    @RequestMapping("/upStatus")
    public @ResponseBody
    Message upStatus(Long id, Integer status) {
        //数据验证
        if (id == null || status == null) {
            return Message.error("参数错误");
        }

        try {
            payMerchantChannelService.updateStatus(id, status);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:paymerchantchannel:status"})
    @RequestMapping("/updateChannelData")
    public @ResponseBody
    Message updateChannelData(@RequestParam(value = "ids[]") Long[] ids, Integer status) {
        //数据验证
        if (ids == null || status == null) {
            return Message.error("参数错误");
        }

        try {
            payMerchantChannelService.updateChannelData(ids, status);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    @AdminPermission({"admin:paymerchantchannel:fpsy"})
    @RequestMapping({"/clientChannelList"})
    public String clientChannelList(Long id, Model model) {
        //查询可以使用的上游通道
        model.addAttribute("ccList", payMerchantChannelService.queryClientChannelList(id));
        //查询已分配的上游通道
        model.addAttribute("ccIds", payMerchantChannelService.queryMerchantClientIds(id));
        return "admin/pay/paymerchantchannel/clientListCheckNew";
    }

    @AdminPermission(value = {"admin:paymerchantchannel:fpsy"})
    @RequestMapping("/addSaveClientChannel")
    public @ResponseBody
    Message addSaveClientChannel(Long id, String data
//            ,@RequestParam(value = "channelIds[]", required = false) Long[] channelIds
    ) {
        //数据验证
        if (id == null) {
            return Message.error("请选择商户通道");
        }

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayMerchantChannel payMerchantChannel = payMerchantChannelService.querySingle(params);
            if (payMerchantChannel == null) {
                return Message.error("商户通道不存在");
            }
            BigDecimal merchantRatio = payMerchantChannel.getMerchantRatio();

            data = data.replace("&quot;", "\"");
            List<Map<String, Long>> list = new Gson().fromJson(data, new TypeToken<ArrayList<Map<String, Long>>>() {
            }.getType());

            //查询所有上游通道费率
            String ids_str = "";
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Long> itemP = list.get(i);
                    Long channelId = itemP.get("channelId");
                    ids_str += channelId + ",";
                }
            }
            if (ids_str.endsWith(",")) {
                ids_str = ids_str.substring(0, ids_str.length() - 1);
            }
            if (StringUtils.isNotBlank(ids_str)) {
                List<PayClientChannel> payClientChannels = payClientChannelService.querySelectListByIds(ids_str);
                for (int i = 0; i < payClientChannels.size()    ; i++) {
                    PayClientChannel payClientChannel = payClientChannels.get(i);
                    if(merchantRatio.compareTo(payClientChannel.getRatio())<0){
                        return Message.error("商户费率低于上游费率，请核对通道："+payClientChannel.getName());
                    }
                }
            }



//            payMerchantChannelService.addSaveClientChannel(id,channelIds);
            payMerchantChannelService.addSaveClientChannel(id, list);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:paymerchantchannel:merchantList"})
    @RequestMapping("/merchantView")
    public String merchantView(Model model) {
        Map<String, Object> params = new HashMap<String, Object>();

        model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));
        return "admin/pay/paymerchantchannel/merchant_list";
    }

    /**
     * Description:分页查询
     *
     * @param map      参数组
     * @param pageNo   当前页码
     * @param pageSize 每页记录数，为null使用默认值20
     * @param orders   排序列表
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchantchannel:merchantList"})
    @RequestMapping("/merchantList")
    public @ResponseBody
    PageResult merchantList(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("merchantId", sysUser.getId());
            // 分页查询
            payMerchantChannelService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }
}
