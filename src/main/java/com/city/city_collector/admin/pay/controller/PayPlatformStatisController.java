
package com.city.city_collector.admin.pay.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.util.PayExcelexportDataService;
import com.city.city_collector.common.util.PayExcelportThread;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayPlatformStatis;
import com.city.city_collector.admin.pay.service.PayExcelexportService;
import com.city.city_collector.admin.pay.service.PayPlatformStatisService;
import com.city.city_collector.admin.system.entity.SysUser;

/**
 * Description:平台统计-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payPlatformStatis")
public class PayPlatformStatisController {
    @Autowired
    PayPlatformStatisService payPlatformStatisService;

    @Value("${static.folder}")
    private String staticFolder;

    @Autowired
    PayExcelexportService payExcelexportService;

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:payplatformstatis:list"})
    @RequestMapping("/view")
    public String view(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        model.addAttribute("startTime", sdf.format(new Date()));
        return "admin/pay/payplatformstatis/list";
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
    @AdminPermission(value = {"admin:payplatformstatis:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, String type, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            if ("1".equals(type)) {
                map.put("timeFormat", "'%Y-%m'");
            } else {
                map.put("timeFormat", "'%Y-%m-%d'");
            }
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payPlatformStatisService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }


    /**
     * 导出excel
     *
     * @param param
     * @param orders
     * @param excelCols 表头
     * @author:nb
     */
    @AdminPermission(value = {"admin:payplatformstatis:export"})
    @RequestMapping("/exportExcel")
    public @ResponseBody
    Message exportExcel(@RequestParam Map<String, Object> param, String type, @RequestParam(value = "orders[]", required = false) String[] orders, String excelCols, HttpServletRequest request) {
        try {
            if ("1".equals(type)) {
                param.put("timeFormat", "'%Y-%m'");
            } else {
                param.put("timeFormat", "'%Y-%m-%d'");
            }
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            //request.getAttribute("serverName") + "/"
            new PayExcelportThread("平台统计" + sdf.format(new Date()), "../../", excelCols, staticFolder, sysUser.getId(), new PayExcelexportDataService() {

                @Override
                public List<Map<String, Object>> getExprotData(Map<String, Object> param, String[] orders) {
                    return payPlatformStatisService.queryExportList(param, orders);
                }
            }, payExcelexportService, param, orders, 3).start();

            return Message.success("启动成功");
        } catch (Exception e) {

            e.printStackTrace();

        }
        return Message.error("导出失败");
    }

    /**
     * Description:打开新增页面
     *
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payplatformstatis:add"})
    @RequestMapping("/add")
    public String add() {
        return "admin/pay/payplatformstatis/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payPlatformStatis
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payplatformstatis:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayPlatformStatis payPlatformStatis) {
        //数据验证
        if (payPlatformStatis == null) {
            return Message.error("参数错误");
        }

        try {

            payPlatformStatisService.addSave(payPlatformStatis);
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
    @AdminPermission(value = {"admin:payplatformstatis:edit"})
    @RequestMapping("/edit")
    public String edit(Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/pay/payplatformstatis/edit";
        }
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        //获取需要修改的记录数据
        PayPlatformStatis result = payPlatformStatisService.querySingle(params);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {
            model.addAttribute("result", result);
        }
        return "admin/pay/payplatformstatis/edit";
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
    @AdminPermission(value = {"admin:payplatformstatis:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(PayPlatformStatis payPlatformStatis) {
        //数据验证
        if (payPlatformStatis == null) {
            return Message.error("参数错误");
        }


        try {

            payPlatformStatisService.editSave(payPlatformStatis);
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
    @AdminPermission(value = {"admin:payplatformstatis:delete"})
    @RequestMapping("/delete")
    public @ResponseBody
    Message delete(@RequestParam(value = "ids[]") Long[] ids) {
        if (ids != null && ids.length == 0) {
            return Message.error("请选择要删除的数据");
        }
        try {

            payPlatformStatisService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }
}
