package com.city.city_collector.admin.pay.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.city.city_collector.admin.pay.service.YajinService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.common.util.MD5Util;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayClient;
import com.city.city_collector.admin.pay.service.PayClientModelService;
import com.city.city_collector.admin.pay.service.PayClientService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;

/**
 * Description:上游管理-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payClient")
public class PayClientController {
    @Autowired
    PayClientService payClientService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    PayClientModelService payClientModelService;

    @Autowired
    private YajinService yajinService;

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:payclient:list"})
    @RequestMapping("/view")
    public String view() {
        return "admin/pay/payclient/list";
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
    @AdminPermission(value = {"admin:payclient:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payClientService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:payclient:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("payStatus", 1);
        model.addAttribute("modelList", payClientModelService.queryListNoJson(params));

        return "admin/pay/payclient/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payClient
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payclient:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(SysUser sysUser) {
        //数据验证
        if (sysUser == null) {
            return Message.error("参数错误");
        }

        if (StringUtils.isBlank(sysUser.getUsername())) {
            return Message.error("账号不能为空");
        }
        if (StringUtils.isBlank(sysUser.getName())) {
            return Message.error("上游名不能为空");
        }

        try {
            //判断账号是否已存在
            if (sysUserService.querySysUserByUsername(sysUser.getUsername()) != null) {
                return Message.error("账号已存在！");
            }

            //设置初始化密码
            String pwd = UUID.randomUUID().toString().replace("-", "").substring(4, 12);

            //初始化数据
            sysUser.setPassword(MD5Util.MD5Encode(pwd, "UTF-8"));
//            sysUser.setStatus("0");
            sysUser.setMoney(BigDecimal.ZERO);
            sysUser.setFrozenMoney(BigDecimal.ZERO);
            sysUser.setQuota(BigDecimal.ZERO);
            sysUser.setTotalQuota(BigDecimal.ZERO);
//            sysUser.setCashCommission(BigDecimal.ZERO);
//            sysUser.setCashRatio(BigDecimal.ZERO);
            sysUser.setMinCommission(BigDecimal.ZERO);
            sysUser.setType(3);

//            if(sysUser.getPayword()!=null) {
//                sysUser.setPassword(MD5Util.MD5Encode(sysUser.getPayword(), "UTF-8").toLowerCase());
//            }

            sysUserService.addSave(sysUser);

            Long[] roleId = new Long[]{5L};
            sysUserService.addSaveUserRole(sysUser.getId(), roleId);

            //更新通道对象信息
            sysUserService.updateChannelData();

            Map<String, String> p = new HashMap<String, String>();
            p.put("msg", "");
            p.put("pwd", pwd);
            p.put("username", sysUser.getUsername());

            yajinService.addUserYajin(sysUser);

            return Message.success("操作成功", p);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("操作失败:" + e.getMessage());
        }
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
    @AdminPermission(value = {"admin:payclient:edit"})
    @RequestMapping("/edit")
    public String edit(Long id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/pay/payclient/edit";
        }
        //获取需要修改的记录数据
        SysUser result = sysUserService.querySysUserById(id);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {
            yajinService.selectYajin(result);
            model.addAttribute("result", result);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("payStatus", 1);
            model.addAttribute("modelList", payClientModelService.queryListNoJson(params));
        }
        return "admin/pay/payclient/edit";
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
    @AdminPermission(value = {"admin:payclient:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(SysUser sysUser) {
        //数据验证
        if (sysUser == null) {
            return Message.error("参数错误");
        }

        try {
//            if("[payword]".equals(sysUser.getPayword())) {
//                sysUser.setPayword(null);
//            }

//            if(sysUser.getPayword()!=null) {
//                sysUser.setPassword(MD5Util.MD5Encode(sysUser.getPayword(), "UTF-8").toLowerCase());
//            }

            sysUser.setUsername(null);
//            sysUserService.editSave(sysUser);
            payClientService.editSave(sysUser);
            //更新通道对象信息
            sysUserService.updateChannelData();

            yajinService.editUserYajin(sysUser);

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    /**
     * Description:更新用户状态
     *
     * @param
     * @param
     * @param id 编辑的记录ID
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payclient:status"})
    @RequestMapping("/upStatus")
    public @ResponseBody
    Message upStatus(Long id, String status) {
        //数据验证
        if (id == null || status == null) {
            return Message.error("参数错误");
        }


        try {
            SysUser sysUser = new SysUser();
            sysUser.setId(id);
            sysUser.setStatus(status);

            sysUserService.editSave(sysUser);

            //更新通道对象信息
            sysUserService.updateChannelData();
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @RequestMapping("/yajinStatus")
    public @ResponseBody
    Message yajinStatus(Long id) {

        boolean canOpen = yajinService.canOpen(id);

        return Message.success("", canOpen ? 1 : 0);
    }

    @RequestMapping("/resetYajin")
    public @ResponseBody
    Message resetYajin(Long id) {

        yajinService.resetYajin(id);

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
    @AdminPermission(value = {"admin:payclient:delete"})
    @RequestMapping("/delete")
    public @ResponseBody
    Message delete(@RequestParam(value = "ids[]") Long[] ids) {
        if (ids != null && ids.length == 0) {
            return Message.error("请选择要删除的数据");
        }
        try {

            payClientService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    @AdminPermission(value = {"admin:payclient:edit"})
    @RequestMapping("/uppayword")
    public @ResponseBody
    Message uppayword(Long id, String payword) {
        if (id == null || StringUtils.isBlank(payword)) {
            return Message.error("参数错误");
        }
        try {
            payword = MD5Util.MD5Encode(payword, "utf-8").toUpperCase();
            SysUser client = sysUserService.querySysUserById(id);
            if (client == null) {
                return Message.error("上游不存在");
            }
            client.setPayword(payword);
            payClientService.updatePayword(client);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }
}
