package com.city.city_collector.admin.system.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.admin.system.entity.SysRole;
import com.city.city_collector.admin.system.service.SysMenuService;
import com.city.city_collector.admin.system.service.SysRoleService;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.common.util.Message;

@Controller
@RequestMapping({"/system/sysRole"})
public class SysRoleController {

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysMenuService sysMenuService;

    @AdminPermission({"admin:sysuser:list"})
    @RequestMapping({"/view"})
    public String view() {
        return "admin/system/sysrole/sysRole_list";
    }

    @AdminPermission({"admin:sysrole:list"})
    @RequestMapping({"/list"})
    @ResponseBody
    public PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize,
                           @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            Page page = new Page(pageNo, pageSize);

            this.sysRoleService.queryPage(map, page, orders);
            return new PageResult(Integer.valueOf(0), "操作成功", Long.valueOf(page.getTotal()), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(Integer.valueOf(1), "操作失败", Long.valueOf(0L), null);
    }

    @AdminPermission({"admin:sysrole:add"})
    @RequestMapping({"/add"})
    public String add() {
        return "admin/system/sysrole/sysRole_edit";
    }

    @AdminPermission({"admin:sysrole:add"})
    @RequestMapping({"/addSave"})
    @ResponseBody
    public Message addSave(SysRole sysRole) {
        if (sysRole == null) {
            return Message.error("参数错误，操作失败");
        }

        if (StringUtils.isBlank(sysRole.getName())) {
            return Message.error("请输入角色名称");
        }
        if (StringUtils.isBlank(sysRole.getStatus())) {
            return Message.error("请输入角色状态");
        }
        try {
            this.sysRoleService.addSave(sysRole);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysrole:edit"})
    @RequestMapping({"/edit"})
    public String edit(Long id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/system/sysrole/sysRole_list";
        }

        SysRole sysRole = this.sysRoleService.querySysRoleById(id);
        if (sysRole == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
            return "admin/system/sysrole/sysRole_edit";
        }
        model.addAttribute("result", sysRole);
        return "admin/system/sysrole/sysRole_edit";
    }

    @AdminPermission({"admin:sysrole:edit"})
    @RequestMapping({"/editSave"})
    @ResponseBody
    public Message editSave(SysRole sysRole) {
        if (sysRole == null) {
            return Message.error("参数错误，操作失败");
        }

        if (sysRole.getId() == null) {
            return Message.error("请选择需要编辑的记录");
        }
        if (StringUtils.isBlank(sysRole.getName())) {
            return Message.error("请输入角色名称");
        }
        if (StringUtils.isBlank(sysRole.getStatus())) {
            return Message.error("请选择角色状态");
        }
        try {
            this.sysRoleService.editSave(sysRole);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysrole:delete"})
    @RequestMapping({"/delete"})
    @ResponseBody
    public Message delete(@RequestParam(value = "ids[]", required = false) Long[] ids) {
        if ((ids == null) || (ids.length == 0))
            return Message.error("请选择要删除的数据");
        try {
            this.sysRoleService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysrole:rolemenu"})
    @RequestMapping({"/roleMenuList"})
    public String roleMenuList(Long roleId, Model model) {
        List menuList = this.sysMenuService.queryAllMenu();
        String menuIds = this.sysRoleService.queryRoleMenuIds(roleId);

        model.addAttribute("menuList", menuList);
        model.addAttribute("menuIds", menuIds);
        return "admin/system/sysmenu/sysMenuSelectMulti";
    }

    @AdminPermission({"admin:sysrole:rolemenu"})
    @RequestMapping({"/addSaveRoleMenu"})
    @ResponseBody
    public Message addSaveRoleMenu(Long roleId, @RequestParam(value = "menuIds[]", required = false) Long[] menuIds) {
        try {
            if (roleId == null) {
                return Message.error("请选择角色");
            }

            if (this.sysRoleService.querySysRoleById(roleId) == null) {
                return Message.error("角色不存在或已被删除");
            }

            this.sysRoleService.addSaveRoleMenu(roleId, menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysrole:roleuser"})
    @RequestMapping({"/roleUserList"})
    public String roleUserList(Long roleId, Model model) {
        List userList = this.sysUserService.queryAllUser();
        String userIds = this.sysRoleService.queryRoleUserIds(roleId);

        model.addAttribute("userIds", userIds);
        model.addAttribute("userList", userList);
        return "admin/system/sysuser/sysUserListCheck";
    }

    @AdminPermission({"admin:sysrole:roleuser"})
    @RequestMapping({"/addSaveRoleUser"})
    @ResponseBody
    public Message addSaveRoleUser(Long roleId, @RequestParam(value = "userIds[]", required = false) Long[] userIds) {
        try {
            if (roleId == null) {
                return Message.error("请选择角色");
            }

            if (this.sysRoleService.querySysRoleById(roleId) == null) {
                return Message.error("角色不存在或已被删除");
            }

            this.sysRoleService.addSaveRoleUser(roleId, userIds);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }
}
