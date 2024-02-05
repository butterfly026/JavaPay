package com.city.city_collector.admin.pay.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.annotation.AdminPermission;
import com.city.adminpermission.exception.UserNotFoundException;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.entity.PayBankCard;
import com.city.city_collector.admin.pay.entity.PayBankCode;
import com.city.city_collector.admin.pay.service.PayBankCardService;
import com.city.city_collector.admin.pay.service.PayBankCodeService;
import com.city.city_collector.admin.system.entity.SysUser;

/**
 * Description:银行编码-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payBankCard")
public class PayBankCardController {
    @Autowired
    PayBankCardService payBankCardService;
    @Autowired
    PayBankCodeService payBankCodeService;

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:paybankcard:list"})
    @RequestMapping("/view")
    public String view() {
        return "admin/pay/paybankcard/list";
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
    @AdminPermission(value = {"admin:paybankcard:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("userId", sysUser.getId());
            // 分页查询
            payBankCardService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:paybankcard:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("bankCodeList", payBankCodeService.queryAllList());
        Config config = ApplicationData.getInstance().getConfig();
        if (config.getCashMode().intValue() == 0) {//india
            return "admin/pay/paybankcard/edit";
        } else {//china
            return "admin/pay/paybankcard/edit_ch";
        }
    }

    /**
     * Description:新增保存操作
     *
     * @param payBankCard
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paybankcard:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayBankCard payBankCard, HttpServletRequest request) {
        //数据验证
        if (payBankCard == null) {
            return Message.error("参数错误");
        }

        try {
            Config config = ApplicationData.getInstance().getConfig();
            if (config.getCashMode().intValue() == 1) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("code", payBankCard.getBankCode());
                PayBankCode pbc = payBankCodeService.querySingle(params);
                if (pbc != null) {
                    payBankCard.setBankCode(pbc.getCode());
                    payBankCard.setBankName(pbc.getName());
                }
            } else {
                if (payBankCard.getBtype() != null && payBankCard.getBtype() == 1) {
                    payBankCard.setBankName("");
                    payBankCard.setBankIfsc("");
                    payBankCard.setBankNation("");
                    payBankCard.setCardName("");
                }
            }

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            payBankCard.setUserId(sysUser.getId());
            payBankCard.setUserName(sysUser.getName());
            payBankCard.setUserNo(sysUser.getUsername());

            payBankCardService.addSave(payBankCard);
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
    @AdminPermission(value = {"admin:paybankcard:edit"})
    @RequestMapping("/edit")
    public String edit(Integer id, Model model, HttpServletRequest request) {
        Config config = ApplicationData.getInstance().getConfig();
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
//	        return "admin/pay/paybankcard/edit";

            if (config.getCashMode().intValue() == 0) {//india
                return "admin/pay/paybankcard/edit";
            } else {//china
                return "admin/pay/paybankcard/edit_ch";
            }
        }
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        //获取需要修改的记录数据
        PayBankCard result = payBankCardService.querySingle(params);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {

            try {
                SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
                if (result.getUserId() == null || !result.getUserId().equals(sysUser.getId())) {
                    model.addAttribute("error", "只能操作自己的银行卡！");
                } else {
                    model.addAttribute("bankCodeList", payBankCodeService.queryAllList());
                    model.addAttribute("result", result);
                }
            } catch (UserNotFoundException e) {

                e.printStackTrace();
                model.addAttribute("error", "数据异常！");
            }

        }

        if (config.getCashMode().intValue() == 0) {//india
            return "admin/pay/paybankcard/edit";
        } else {//china
            return "admin/pay/paybankcard/edit_ch";
        }
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
    @AdminPermission(value = {"admin:paybankcard:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(PayBankCard payBankCard, HttpServletRequest request) {
        //数据验证
        if (payBankCard == null) {
            return Message.error("参数错误");
        }


        try {
            Config config = ApplicationData.getInstance().getConfig();
            if (config.getCashMode().intValue() == 1) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("code", payBankCard.getBankCode());
                PayBankCode pbc = payBankCodeService.querySingle(params);
                if (pbc != null) {
                    payBankCard.setBankCode(pbc.getCode());
                    payBankCard.setBankName(pbc.getName());
                }
            } else {
                if (payBankCard.getBtype() != null && payBankCard.getBtype() == 1) {
                    payBankCard.setBankName("");
                    payBankCard.setBankIfsc("");
                    payBankCard.setBankNation("");
                    payBankCard.setCardName("");
                }
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", payBankCard.getId());
            //获取需要修改的记录数据
            PayBankCard result = payBankCardService.querySingle(params);

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            if (result.getUserId() == null || !result.getUserId().equals(sysUser.getId())) {
                return Message.error("error", "只能操作自己的银行卡！");
            }

            payBankCardService.editSave(payBankCard);
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
    @AdminPermission(value = {"admin:paybankcard:merchantList"})
    @RequestMapping("/merchantView")
    public String merchantView() {
        return "admin/pay/paybankcard/merchant_list";
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
    @AdminPermission(value = {"admin:paybankcard:merchantList"})
    @RequestMapping("/merchantList")
    public @ResponseBody
    PageResult merchantList(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("userId", sysUser.getId());
            // 分页查询
            payBankCardService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }

    /**
     * Description:删除记录
     *
     * @param ids 需要删除的记录的ID数组
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paybankcard:delete", "admin:paybankcard:merchantDelete", "admin:paybankcard:proxyDelete"})
    @RequestMapping("/delete")
    public @ResponseBody
    Message delete(@RequestParam(value = "ids[]") Long[] ids, HttpServletRequest request) {
        if (ids != null && ids.length == 0) {
            return Message.error("请选择要删除的数据");
        }
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            payBankCardService.delete(ids, sysUser.getId());
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
    @AdminPermission(value = {"admin:paybankcard:proxyList"})
    @RequestMapping("/proxyView")
    public String proxyView() {
        return "admin/pay/paybankcard/proxy_list";
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
    @AdminPermission(value = {"admin:paybankcard:proxyList"})
    @RequestMapping("/proxyList")
    public @ResponseBody
    PageResult proxyList(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("userId", sysUser.getId());
            // 分页查询
            payBankCardService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }
}
