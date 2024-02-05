package com.city.city_collector.admin.pay.controller;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.admin.pay.service.PaySensitiveLogService;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Description:敏感操作日志-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/paySensitiveLog")
public class PaySensitiveLogController {

    @Autowired
    private PaySensitiveLogService paySensitiveLogService;


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
    //  @AdminPermission(value={"admin:paySensitiveLog:list"})
    @RequestMapping("/list")
    @ResponseBody
    public PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            paySensitiveLogService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }


}
