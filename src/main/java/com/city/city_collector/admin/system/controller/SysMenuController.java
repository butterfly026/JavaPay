package com.city.city_collector.admin.system.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.admin.system.entity.SysMenu;
import com.city.city_collector.admin.system.service.SysMenuService;
import com.city.city_collector.common.util.Message;

@Controller
@RequestMapping({"/system/sysMenu"})
public class SysMenuController {


    @Autowired
    SysMenuService sysMenuService;

    @AdminPermission({"admin:sysmenu:list"})
    @RequestMapping({"/view"})
    public String view() {
        return "admin/system/sysmenu/sysMenu_list";
    }

    @AdminPermission({"admin:sysmenu:list"})
    @RequestMapping({"/list"})
    @ResponseBody
    public Message list() {
        try {
            return Message.success("查询成功", this.sysMenuService.queryAllMenu());
        } catch (Exception e) {
        }
        return Message.error();
    }

    @RequestMapping({"/menuSelectSingle"})
    public String menuSelectSingle(Model model) {
        model.addAttribute("menuList", this.sysMenuService.queryAllMenu());
        return "admin/system/sysmenu/sysMenuSelectSingle";
    }

    @AdminPermission({"admin:sysmenu:add"})
    @RequestMapping({"/add"})
    public String add() {
        return "admin/system/sysmenu/sysMenu_edit";
    }

    @AdminPermission({"admin:sysmenu:add"})
    @RequestMapping({"/addSave"})
    @ResponseBody
    public Message addSave(SysMenu sysMenu) {
        if (sysMenu == null) {
            return Message.error("参数错误，操作失败");
        }

        if (StringUtils.isBlank(sysMenu.getName())) {
            return Message.error("请输入菜单名");
        }
        try {
            this.sysMenuService.addSave(sysMenu);
            return Message.success("操作成功", sysMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error();
    }

    @AdminPermission({"admin:sysmenu:edit"})
    @RequestMapping({"/edit"})
    public String edit(Long id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/system/sysmenu/sysMenu_edit";
        }

        SysMenu sysMenu = this.sysMenuService.querySysMenuById(id);
        if (sysMenu == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
            return "admin/system/sysmenu/sysMenu_edit";
        }
        model.addAttribute("result", sysMenu);
        return "admin/system/sysmenu/sysMenu_edit";
    }

    @AdminPermission({"admin:sysmenu:edit"})
    @RequestMapping({"/editSave"})
    @ResponseBody
    public Message editSave(SysMenu sysMenu) {
        if (sysMenu == null) {
            return Message.error("参数错误，操作失败");
        }

        if (sysMenu.getId() == null) {
            return Message.error("请选择需要编辑的记录");
        }
        if (StringUtils.isBlank(sysMenu.getName())) {
            return Message.error("请输入菜单名");
        }
        if ((sysMenu.getFid() != null) && (sysMenu.getId().equals(sysMenu.getFid()))) {
            return Message.error("上级菜单不能是自身");
        }
        try {
            this.sysMenuService.editSave(sysMenu);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysmenu:delete"})
    @RequestMapping({"/delete"})
    @ResponseBody
    public Message delete(@RequestParam(value = "ids[]", required = false) Long[] ids) {
        if ((ids == null) || (ids.length == 0))
            return Message.error("请选择要删除的数据");
        try {
            this.sysMenuService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }
}
