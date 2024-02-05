package com.city.city_collector.admin.pay.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayClientCash;
import com.city.city_collector.admin.pay.service.PayClientCashService;

/**
 * Description:上游代付-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payClientCash")
public class PayClientCashController {
    @Autowired
    PayClientCashService payClientCashService;


    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:payclientcash:list"})
    @RequestMapping("/view")
    public String view() {
        return "admin/pay/payclientcash/list";
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
    @AdminPermission(value = {"admin:payclientcash:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payClientCashService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:payclientcash:add"})
    @RequestMapping("/add")
    public String add() {
        return "admin/pay/payclientcash/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payClientCash
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payclientcash:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayClientCash payClientCash) {
        //数据验证
        if (payClientCash == null) {
            return Message.error("参数错误");
        }

        try {

            payClientCashService.addSave(payClientCash);
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
    @AdminPermission(value = {"admin:payclientcash:edit"})
    @RequestMapping("/edit")
    public String edit(Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/pay/payclientcash/edit";
        }
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        //获取需要修改的记录数据
        PayClientCash result = payClientCashService.querySingle(params);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {
            model.addAttribute("result", result);
        }
        return "admin/pay/payclientcash/edit";
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
    @AdminPermission(value = {"admin:payclientcash:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(PayClientCash payClientCash) {
        //数据验证
        if (payClientCash == null) {
            return Message.error("参数错误");
        }


        try {

            payClientCashService.editSave(payClientCash);
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
    @AdminPermission(value = {"admin:payclientcash:delete"})
    @RequestMapping("/delete")
    public @ResponseBody
    Message delete(@RequestParam(value = "ids[]") Long[] ids) {
        if (ids != null && ids.length == 0) {
            return Message.error("请选择要删除的数据");
        }
        try {

            payClientCashService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }
}
