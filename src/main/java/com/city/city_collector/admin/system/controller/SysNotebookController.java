package com.city.city_collector.admin.system.controller;


import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.annotation.AdminPermission;
import com.city.adminpermission.exception.UserNotFoundException;
import com.city.city_collector.admin.system.entity.SysNotebook;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysNotebookService;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.common.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping({"/system/notebook"})
public class SysNotebookController {

    @Autowired
    private SysNotebookService sysNotebookService;

    @Autowired
    private HttpServletRequest request;

    //列表 由时间排序     新增   修改       //删除  暂时不做


    @AdminPermission({"admin:notebook:list"})
    @RequestMapping({"/view"})
    public String view() {
        return "admin/system/notebook/list";
    }


    @AdminPermission({"admin:notebook:list"})
    @RequestMapping({"/list"})
    @ResponseBody
    public PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize,
                           @RequestParam(value = "orders[]", required = false) String[] orders) {

        Page page = new Page(pageNo, pageSize);

        sysNotebookService.queryPage(map, page, orders);

        return new PageResult(Integer.valueOf(0), "操作成功", Long.valueOf(page.getTotal()), page.getResults());
    }

    @AdminPermission({"admin:notebook:add"})
    @RequestMapping({"/add"})
    public String add() {
        return "admin/system/notebook/add";
    }


    @AdminPermission({"admin:notebook:add"})
    @RequestMapping({"/addSave"})
    @ResponseBody
    public Message addSave(SysNotebook sysNotebook) throws UserNotFoundException {
        if (sysNotebook == null) {
            return Message.error("参数错误，操作失败");
        }
        SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
        sysNotebook.setUserId(Integer.parseInt(sysUser.getId() + ""));
        sysNotebook.setUsername(sysUser.getUsername());
        sysNotebook.setCreateTime(new Date());
        this.sysNotebookService.addSave(sysNotebook);
        return Message.success("操作成功");
    }

    @AdminPermission({"admin:notebook:edit"})
    @RequestMapping({"/edit"})
    public String edit(Integer id, Model model) {
        if (id == null) {
        }

        SysNotebook sysNotebook = this.sysNotebookService.query(id);
        if (sysNotebook == null) {
        }

        model.addAttribute("result", sysNotebook);
        return "admin/system/notebook/edit";
    }


    @AdminPermission({"admin:notebook:edit"})
    @RequestMapping({"/editSave"})
    @ResponseBody
    public Message editSave(SysNotebook sysNotebook) throws UserNotFoundException {
        if (sysNotebook == null) {
            return Message.error("参数错误，操作失败");
        }

        if (sysNotebook.getId() == null) {
            return Message.error("请选择需要编辑的记录");
        }

        SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
        sysNotebook.setUpdateBy(Integer.parseInt(sysUser.getId() + ""));
        sysNotebook.setUpUsername(sysUser.getUsername());
        sysNotebook.setUpdateTime(new Date());
        this.sysNotebookService.editSave(sysNotebook);
        return Message.success();
    }


}
