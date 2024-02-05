package com.city.city_collector.admin.system.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.admin.system.entity.SysDept;
import com.city.city_collector.admin.system.service.SysDeptService;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.common.util.Message;

@Controller
@RequestMapping({"/system/sysDept"})
public class SysDeptController {

    @Autowired
    SysDeptService sysDeptService;

    @Autowired
    SysUserService sysUserService;

    @AdminPermission({"admin:sysdept:list"})
    @RequestMapping({"/view"})
    public String view() {
        return "admin/system/sysdept/sysDept_list";
    }

    @AdminPermission({"admin:sysdept:list"})
    @RequestMapping({"/list"})
    @ResponseBody
    public Message list() {
        try {
            return Message.success("查询成功", this.sysDeptService.queryAllDept());
        } catch (Exception e) {
        }
        return Message.error();
    }

    @RequestMapping({"/deptSelectSingle"})
    public String menuSelectSingle(Model model) {
        model.addAttribute("deptList", this.sysDeptService.queryAllDept());
        return "admin/system/sysdept/sysDeptSelectSingle";
    }

    @AdminPermission({"admin:sysdept:add"})
    @RequestMapping({"/add"})
    public String add() {
        return "admin/system/sysdept/sysDept_edit";
    }

    @AdminPermission({"admin:sysdept:add"})
    @RequestMapping({"/addSave"})
    @ResponseBody
    public Message addSave(SysDept sysDept) {
        if (sysDept == null) {
            return Message.error("参数错误，操作失败");
        }

        if (StringUtils.isBlank(sysDept.getName())) {
            return Message.error("请输入部门名");
        }
        try {
            this.sysDeptService.addSave(sysDept);
            return Message.success("操作成功", sysDept);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error();
    }

    @AdminPermission({"admin:sysdept:edit"})
    @RequestMapping({"/edit"})
    public String edit(Long id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/system/sysdept/sysDept_edit";
        }

        SysDept sysDept = this.sysDeptService.querySysDeptById(id);
        if (sysDept == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
            return "admin/system/sysdept/sysDept_edit";
        }
        model.addAttribute("result", sysDept);
        return "admin/system/sysdept/sysDept_edit";
    }

    @AdminPermission({"admin:sysdept:edit"})
    @RequestMapping({"/editSave"})
    @ResponseBody
    public Message editSave(SysDept sysDept) {
        if (sysDept == null) {
            return Message.error("参数错误，操作失败");
        }

        if (sysDept.getId() == null) {
            return Message.error("请选择需要编辑的记录");
        }
        if (StringUtils.isBlank(sysDept.getName())) {
            return Message.error("请输入部门名");
        }
        if ((sysDept.getFid() != null) && (sysDept.getId().equals(sysDept.getFid()))) {
            return Message.error("上级部门不能是自身");
        }
        try {
            this.sysDeptService.editSave(sysDept);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysdept:delete"})
    @RequestMapping({"/delete"})
    @ResponseBody
    public Message delete(@RequestParam(value = "ids[]", required = false) Long[] ids) {
        if ((ids == null) || (ids.length == 0))
            return Message.error("请选择要删除的数据");
        try {
            this.sysDeptService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysdept:deptuser"})
    @RequestMapping({"/addSaveDeptUser"})
    @ResponseBody
    public Message addSaveDeptUser(Long deptId, @RequestParam(value = "userIds[]", required = false) Long[] userIds) {
        try {
            if (deptId == null) {
                return Message.error("请选择部门");
            }

            if (this.sysDeptService.queryDeptUserIds(deptId) == null) {
                return Message.error("部门不存在或已被删除");
            }

            this.sysDeptService.addSaveUserDept(deptId, userIds);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysdept:deptuser"})
    @RequestMapping({"/deptUserList"})
    public String deptUserList(Long deptId, Model model) {
        String userIds = this.sysDeptService.queryDeptUserIds(deptId);

        List userList = this.sysUserService.queryAllUser();
        model.addAttribute("userIds", userIds);
        model.addAttribute("userList", userList);
        model.addAttribute("deptId", deptId);
        return "admin/system/sysuser/sysUserListCheck";
    }
}
