package com.city.city_collector.admin.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.admin.system.entity.SysFolder;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysFolderService;
import com.city.city_collector.admin.system.service.SysImageService;
import com.city.city_collector.common.util.Message;

@Controller
@RequestMapping({"/system/sysFolder"})
public class SysFolderController {

    @Autowired
    SysFolderService sysFolderService;

    @Autowired
    SysImageService sysImageService;

    @AdminPermission({"admin:sysfolder:add"})
    @RequestMapping({"/add"})
    public String add() {
        return "admin/system/sysfolder/sysFolder_edit";
    }

    @AdminPermission({"admin:sysfolder:add"})
    @RequestMapping({"/addSave"})
    @ResponseBody
    public Message addSave(SysFolder sysFolder, HttpServletRequest request) {
        if (sysFolder == null) {
            return Message.error("参数错误，操作失败");
        }

        if (StringUtils.isBlank(sysFolder.getName())) {
            return Message.error("请输入目录名");
        }
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);

            sysFolder.setType("0");
            sysFolder.setUserId(sysUser.getId());
            this.sysFolderService.addSave(sysFolder);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysfolder:edit"})
    @RequestMapping({"/edit"})
    public String edit(Long id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/system/sysfolder/sysFolder_edit";
        }

        SysFolder sysFolder = this.sysFolderService.querySysFolderById(id);
        if (sysFolder == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
            return "admin/system/sysfolder/sysFolder_edit";
        }
        model.addAttribute("result", sysFolder);
        return "admin/system/sysfolder/sysFolder_edit";
    }

    @AdminPermission({"admin:sysfolder:edit"})
    @RequestMapping({"/editSave"})
    @ResponseBody
    public Message editSave(SysFolder sysFolder) {
        if (sysFolder == null) {
            return Message.error("参数错误，操作失败");
        }

        if (sysFolder.getId() == null) {
            return Message.error("请选择需要编辑的记录");
        }
        if (StringUtils.isBlank(sysFolder.getName())) {
            return Message.error("请输入目录名");
        }
        try {
            this.sysFolderService.editSave(sysFolder);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysfolder:delete"})
    @RequestMapping({"/delete"})
    @ResponseBody
    public Message delete(@RequestParam("ids[]") Long[] ids) {
        if ((ids == null) || (ids.length == 0))
            return Message.error("请选择要删除的数据");
        try {
            this.sysFolderService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }
}
