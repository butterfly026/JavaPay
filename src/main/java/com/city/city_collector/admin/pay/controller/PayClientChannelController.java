package com.city.city_collector.admin.pay.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;

import com.city.adminpermission.AdminPermissionManager;
import com.city.city_collector.admin.pay.dao.PayClientChannelDao;
import com.city.city_collector.admin.pay.entity.PayMerchantChannel;
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

import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.common.util.AESUtil;
import com.city.city_collector.common.util.MD5Util;
import com.city.city_collector.common.util.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.system.entity.SysUser;

/**
 * Description:上游通道-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payClientChannel")
public class PayClientChannelController {
    @Autowired
    PayClientChannelService payClientChannelService;

    @Autowired
    PayChannelTypeService payChannelTypeService;

    @Autowired
    PayMerchantChannelService payMerchantChannelService;

    @Autowired
    PayClientService payClientService;

    @Autowired
    PayClientModelService payClientModelService;

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
    @AdminPermission(value = {"admin:payclientchannel:list"})
    @RequestMapping("/view")
    public String view(Model model) {
        model.addAttribute("clientList", payClientService.querySelectList());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ncode", "paytm");
        model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));
        return "admin/pay/payclientchannel/list";
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
    @AdminPermission(value = {"admin:payclientchannel:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            map.put("ctype", 0);
            // 分页查询
            payClientChannelService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }

    /**
     * Description:打开新增页面
     *
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payclientchannel:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("clientList", payClientService.querySelectList());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ncode", "paytm");
        model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));

//        Map < String, Object > params=new HashMap<String,Object>();
//        params.put("orderStatus", 1);
        model.addAttribute("modelList", payClientModelService.queryListNoJson(params));

        return "admin/pay/payclientchannel/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payClientChannel
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payclientchannel:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayClientChannel payClientChannel) {
        //数据验证
        if (payClientChannel == null) {
            return Message.error("参数错误");
        }
//	    if(StringUtils.isBlank(payClientChannel.getPaytmkey())) {
//	        return Message.error("商户秘钥不能为空");
//	    }

        try {
            if (payClientChannel.getAlarm() == null) {
                return Message.error("预警设置有误");
            }
//	        if(payClientChannel.getAlarm().intValue()==1) {//开启预警

            if (payClientChannel.getAlarmNumber() == null || payClientChannel.getAlarmNumberup() == null || payClientChannel.getAlarmNumber().doubleValue() < 0
                    || payClientChannel.getAlarmNumberup().doubleValue() < 0
                    || payClientChannel.getAlarmNumber().doubleValue() > payClientChannel.getAlarmNumberup().doubleValue()) {
                return Message.error("预警下限不能超过上限");
            }

//	        }

//	        //秘钥加密
//	        String md5=MD5Util.MD5Encode(payClientChannel.getPaytmkey(), "UTF-8");
//	        String uuid=UUID.randomUUID().toString().replace("-", "");
//	        String pwd=md5.substring(md5.length()-7)+uuid.substring(uuid.length()-4)+KEY;
//
//	        payClientChannel.setPaytmmd5(md5);
//	        payClientChannel.setPaytmuid(uuid);
//	        payClientChannel.setPaytmkey(AESUtil.encrypt(payClientChannel.getPaytmkey(), pwd));
            payClientChannel.setCtype(0);
            payClientChannelService.addSave(payClientChannel);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            PaySensitiveLog paySensitiveLog = new PaySensitiveLog(sysUser.getId(), sysUser.getUsername(), new Date(), "addChannel", "新增渠道 费率:" + payClientChannel.getRatio(), payClientChannel.getId() + "_" + payClientChannel.getName(), CommonUtil.getIp(request));
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
    @AdminPermission(value = {"admin:payclientchannel:edit"})
    @RequestMapping("/edit")
    public String edit(Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/pay/payclientchannel/edit";
        }
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        //获取需要修改的记录数据
        PayClientChannel result = payClientChannelService.querySingle(params);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {
//	        //秘钥解密
//	        String paytmKey=result.getPaytmkey();
//	        if(!StringUtils.isBlank(paytmKey)) {
//	            String md5=result.getPaytmmd5();
//	            String uuid=result.getPaytmuid();
//
//	            String pwd=md5.substring(md5.length()-7)+uuid.substring(uuid.length()-4)+KEY;
//	            try {
//                    result.setPaytmkey(AESUtil.decrypt(result.getPaytmkey(), pwd));
//                }catch (Exception e) {
//                    result.setPaytmkey("");
//                }
//	        }

            params.put("ncode", "paytm");

            model.addAttribute("result", result);
            model.addAttribute("clientList", payClientService.querySelectList());
            model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));

            params = new HashMap<String, Object>();
//	        params.put("orderStatus", 1);

            model.addAttribute("modelList", payClientModelService.queryListNoJson(params));
        }

        return "admin/pay/payclientchannel/edit";
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
    @AdminPermission(value = {"admin:payclientchannel:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(PayClientChannel payClientChannel) {
        //数据验证
        if (payClientChannel == null) {
            return Message.error("参数错误");
        }


        try {

            if (payClientChannel.getAlarm() == null) {
                return Message.error("预警设置有误");
            }
//            if(payClientChannel.getAlarm().intValue()==1) {//开启预警

            if (payClientChannel.getAlarmNumber() == null || payClientChannel.getAlarmNumberup() == null || payClientChannel.getAlarmNumber().doubleValue() < 0
                    || payClientChannel.getAlarmNumberup().doubleValue() < 0
                    || payClientChannel.getAlarmNumber().doubleValue() > payClientChannel.getAlarmNumberup().doubleValue()) {
                return Message.error("预警下限不能超过上限");
            }

            if (payClientChannel.getRetryNumber() == null || payClientChannel.getRetryNumber() < 1 || payClientChannel.getRetryNumber() > 5) {
                return Message.error("通道尝试次数不符合要求！");
            }

//            }

            //秘钥加密
//            String md5=MD5Util.MD5Encode(payClientChannel.getPaytmkey(), "UTF-8");
//            String uuid=UUID.randomUUID().toString().replace("-", "");
//            String pwd=md5.substring(md5.length()-7)+uuid.substring(uuid.length()-4)+KEY;
//
//            payClientChannel.setPaytmmd5(md5);
//            payClientChannel.setPaytmuid(uuid);
//            payClientChannel.setPaytmkey(AESUtil.encrypt(payClientChannel.getPaytmkey(), pwd));
            payClientChannel.setCtype(0);

            HashMap<String, Object> map = new HashMap<>();
            map.put("id", payClientChannel.getId());
            PayClientChannel payClientChannelOld = payClientChannelService.querySingle(map);
            payClientChannelService.editSave(payClientChannel);

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            PaySensitiveLog paySensitiveLog = new PaySensitiveLog(sysUser.getId(), sysUser.getUsername(), new Date(), "updateChannel", "编辑渠道 当前费率:" + payClientChannelOld.getRatio() + " 修改后费率:" + payClientChannel.getRatio(), payClientChannel.getId() + "_" + payClientChannel.getName(), CommonUtil.getIp(request));
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
    @AdminPermission(value = {"admin:payclientchannel:delete"})
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
                PayClientChannel payClientChannel = payClientChannelService.querySingle(map);
                PaySensitiveLog paySensitiveLog = new PaySensitiveLog(sysUser.getId(), sysUser.getUsername(), new Date(), "deleteChannel", "删除渠道 费率:" + payClientChannel.getRatio(), payClientChannel.getId() + "_" + payClientChannel.getName(), CommonUtil.getIp(request));
                paySensitiveLogService.insertLog(paySensitiveLog);
            }

            payClientChannelService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:payclientchannel:status"})
    @RequestMapping("/upStatus")
    public @ResponseBody
    Message upStatus(Long id, Integer status) {
        //数据验证
        if (id == null || status == null) {
            return Message.error("参数错误");
        }

        try {
            payClientChannelService.updateStatus(id, status);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:payclientchannel:edit"})
    @RequestMapping("/yjStatus")
    public @ResponseBody
    Message yjStatus(Long id, Integer status) {
        //数据验证
        if (id == null || status == null) {
            return Message.error("参数错误");
        }

        try {
            payClientChannelService.updateAlarmStatus(id, status);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    @AdminPermission(value = {"admin:payclientchannel:status"})
    @RequestMapping("/updateChannelData")
    public @ResponseBody
    Message updateChannelData(@RequestParam(value = "ids[]") Long[] ids, Integer status) {
        //数据验证
        if (ids == null || status == null) {
            return Message.error("参数错误");
        }

        try {
            payClientChannelService.updateChannelData(ids, status);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    @AdminPermission(value = {"admin:payclientchannel:edit"})
    @RequestMapping("/editYj")
    public String editYj(Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/pay/payclientchannel/edit_yj";
        }
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        //获取需要修改的记录数据
        PayClientChannel result = payClientChannelService.querySingle(params);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {

            model.addAttribute("result", result);
            model.addAttribute("clientList", payClientService.querySelectList());
        }

        return "admin/pay/payclientchannel/edit_yj";
    }

    @AdminPermission(value = {"admin:payclientchannel:edit"})
    @RequestMapping("/editYjSave")
    public @ResponseBody
    Message editYjSave(PayClientChannel payClientChannel) {
        //数据验证
        if (payClientChannel == null) {
            return Message.error("参数错误");
        }

        try {

            if (payClientChannel.getAlarm() == null) {
                return Message.error("预警设置有误");
            }
//            if(payClientChannel.getAlarm().intValue()==1) {//开启预警

            if (payClientChannel.getAlarmNumber() == null || payClientChannel.getAlarmNumberup() == null || payClientChannel.getAlarmNumber().doubleValue() < 0
                    || payClientChannel.getAlarmNumberup().doubleValue() < 0
                    || payClientChannel.getAlarmNumber().doubleValue() > payClientChannel.getAlarmNumberup().doubleValue()) {
                return Message.error("预警下限不能超过上限");
            }

//            }

            payClientChannelService.editYjSave(payClientChannel);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:payclientchannel:edit"})
    @RequestMapping({"/merchantChannelList"})
    public String merchantChannelList(Long id, Model model) {
        //查询可以使用的商户通道
        model.addAttribute("mcList", payClientChannelService.queryMerchantChannelList(id));

        //查询已分配的商户通道
        model.addAttribute("mcIds", payClientChannelService.queryClientMerchantIds(id));
        return "admin/pay/payclientchannel/merchantListCheckNew";
    }

    @AdminPermission(value = {"admin:payclientchannel:edit"})
    @RequestMapping("/addSaveMerchantChannel")
    public @ResponseBody
    Message addSaveMerchantChannel(Long id, String data
//            ,@RequestParam(value = "channelIds[]", required = false) Long[] channelIds
    ) {
        //数据验证
        if (id == null) {
            return Message.error("请选择上游通道");
        }

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayClientChannel payClientChannel = payClientChannelService.querySingle(params);
            if (payClientChannel == null) {
                return Message.error("上游通道不存在");
            }
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
                BigDecimal ratio = payClientChannel.getRatio();
                List<PayMerchantChannel> payMerchantChannels = payMerchantChannelService.queryPayMerchantByIds(ids_str);
                for (int i = 0; i < payMerchantChannels.size(); i++) {
                    PayMerchantChannel payMerchantChannel = payMerchantChannels.get(i);
                    if (ratio.compareTo(payMerchantChannel.getMerchantRatio()) >= 0) {
                        return Message.error("商户费率低于上游费率，请核对通道：" + payMerchantChannel.getName());
                    }
                }
            }

            payClientChannelService.addSaveMerchantChannel(id, list);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }
}
