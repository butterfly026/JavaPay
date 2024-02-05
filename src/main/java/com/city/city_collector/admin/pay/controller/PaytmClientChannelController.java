
package com.city.city_collector.admin.pay.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.admin.pay.entity.PayClientChannel;
import com.city.city_collector.admin.pay.service.PayChannelTypeService;
import com.city.city_collector.admin.pay.service.PayClientChannelService;
import com.city.city_collector.admin.pay.service.PayClientModelService;
import com.city.city_collector.admin.pay.service.PayClientService;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.common.util.AESUtil;
import com.city.city_collector.common.util.MD5Util;
import com.city.city_collector.common.util.Message;

/**
 * @author nb
 * @Description:
 */
@Controller
@RequestMapping("/system/paytmClientChannel")
public class PaytmClientChannelController {

    @Autowired
    PayClientChannelService payClientChannelService;

    @Autowired
    PayChannelTypeService payChannelTypeService;

    @Autowired
    PayClientService payClientService;

    @Autowired
    PayClientModelService payClientModelService;
    /**
     * 秘钥KEY
     **/
    public final static String KEY = "g@*nZ";

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:paytmclientchannel:list"})
    @RequestMapping("/view")
    public String view(Model model) {
//        Map < String,Object > params=new HashMap<String,Object>();
//        params.put("code", "paytm");

        model.addAttribute("clientList", payClientService.querySelectList());
//        model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));
        return "admin/pay/paytmclientchannel/list";
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
    @AdminPermission(value = {"admin:paytmclientchannel:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            map.put("ctype", 1);
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
    @AdminPermission(value = {"admin:paytmclientchannel:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("clientList", payClientService.querySelectList());
//        model.addAttribute("channelTypeList", payChannelTypeService.queryAllList());

//        Map < String, Object > params=new HashMap<String,Object>();
//        params.put("orderStatus", 1);
//        model.addAttribute("modelList", payClientModelService.queryListNoJson(params));

        return "admin/pay/paytmclientchannel/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payClientChannel
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paytmclientchannel:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayClientChannel payClientChannel) {
        //数据验证
        if (payClientChannel == null) {
            return Message.error("参数错误");
        }
        if (StringUtils.isBlank(payClientChannel.getPaytmkey())) {
            return Message.error("商户秘钥不能为空");
        }

        try {
            //秘钥加密
            String md5 = MD5Util.MD5Encode(payClientChannel.getPaytmkey(), "UTF-8");
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String pwd = md5.substring(md5.length() - 7) + uuid.substring(uuid.length() - 4) + KEY;

            payClientChannel.setPaytmmd5(md5);
            payClientChannel.setPaytmuid(uuid);
            payClientChannel.setPaytmkey(AESUtil.encrypt(payClientChannel.getPaytmkey(), pwd));

            payClientChannelService.addSave(payClientChannel);
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
    @AdminPermission(value = {"admin:paytmclientchannel:edit"})
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
            //秘钥解密
            String paytmKey = result.getPaytmkey();
            if (!StringUtils.isBlank(paytmKey)) {
                String md5 = result.getPaytmmd5();
                String uuid = result.getPaytmuid();

                String pwd = md5.substring(md5.length() - 7) + uuid.substring(uuid.length() - 4) + KEY;
                try {
                    result.setPaytmkey(AESUtil.decrypt(result.getPaytmkey(), pwd));
                } catch (Exception e) {
                    result.setPaytmkey("");
                }
            }

            params.put("code", "paytm");

            model.addAttribute("result", result);
            model.addAttribute("clientList", payClientService.querySelectList());
            model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));

//          params=new HashMap<String,Object>();
//          params.put("orderStatus", 1);

//          model.addAttribute("modelList", payClientModelService.queryListNoJson(params));
        }

        return "admin/pay/paytmclientchannel/edit";
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
    @AdminPermission(value = {"admin:paytmclientchannel:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(PayClientChannel payClientChannel) {
        //数据验证
        if (payClientChannel == null) {
            return Message.error("参数错误");
        }


        try {
            //秘钥加密

            String md5 = MD5Util.MD5Encode(payClientChannel.getPaytmkey(), "UTF-8");
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String pwd = md5.substring(md5.length() - 7) + uuid.substring(uuid.length() - 4) + KEY;

            payClientChannel.setPaytmmd5(md5);
            payClientChannel.setPaytmuid(uuid);
            payClientChannel.setPaytmkey(AESUtil.encrypt(payClientChannel.getPaytmkey(), pwd));

            payClientChannelService.editSave(payClientChannel);
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
    @AdminPermission(value = {"admin:paytmclientchannel:delete"})
    @RequestMapping("/delete")
    public @ResponseBody
    Message delete(@RequestParam(value = "ids[]") Long[] ids) {
        if (ids != null && ids.length == 0) {
            return Message.error("请选择要删除的数据");
        }
        try {

            payClientChannelService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:paytmclientchannel:status"})
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
}
