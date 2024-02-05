package com.city.city_collector.admin.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.annotation.AdminPermission;
import com.city.adminpermission.exception.UserNotFoundException;
import com.city.city_collector.admin.system.entity.SysMenu;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysMenuService;
import com.city.city_collector.admin.system.service.SysRoleService;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.common.util.GoogleGenerator;
import com.city.city_collector.common.util.MD5Util;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.util.ZxingUtil;

@Controller
@RequestMapping({"/system/sysUser"})
public class SysUserController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    SysRoleService sysRoleService;

    @Value("${static.folder}")
    private String staticFolder;

    @AdminPermission({"admin:sysuser:list"})
    @RequestMapping({"/view"})
    public String view() {
        return "admin/system/sysuser/sysUser_list";
    }

    @AdminPermission({"admin:sysuser:list"})
    @RequestMapping({"/list"})
    @ResponseBody
    public PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize,
                           @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            Page page = new Page(pageNo, pageSize);

            this.sysUserService.queryPage(map, page, orders);
            return new PageResult(Integer.valueOf(0), "操作成功", Long.valueOf(page.getTotal()), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(Integer.valueOf(1), "操作失败", Long.valueOf(0L), null);
    }

    @AdminPermission({"admin:sysuser:add"})
    @RequestMapping({"/add"})
    public String add() {
        return "admin/system/sysuser/sysUser_edit";
    }

    @AdminPermission({"admin:sysuser:add"})
    @RequestMapping({"/addSave"})
    @ResponseBody
    public Message addSave(SysUser sysUser) {
        if (sysUser == null) {
            return Message.error("参数错误，操作失败");
        }

        if (StringUtils.isBlank(sysUser.getUsername())) {
            return Message.error("请输入登录名");
        }
        if (StringUtils.isBlank(sysUser.getPassword())) {
            return Message.error("请输入密码");
        }
        if (StringUtils.isBlank(sysUser.getStatus())) {
            return Message.error("请选择用户状态");
        }
        try {
            if (this.sysUserService.querySysUserByUsername(sysUser.getUsername()) != null) {
                return Message.error("用户已存在，添加失败");
            }

            sysUser.setPassword(MD5Util.MD5Encode(sysUser.getPassword(), "UTF-8"));

            this.sysUserService.addSave(sysUser);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysuser:edit"})
    @RequestMapping({"/edit"})
    public String edit(Long id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/system/sysuser/sysUser_edit";
        }

        SysUser sysUser = this.sysUserService.querySysUserById(id);
        if (sysUser == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
            return "admin/system/sysuser/sysUser_edit";
        }
        sysUser.setPassword(null);
        model.addAttribute("result", sysUser);
        return "admin/system/sysuser/sysUser_edit";
    }

    @AdminPermission({"admin:sysuser:edit"})
    @RequestMapping({"/editSave"})
    @ResponseBody
    public Message editSave(SysUser sysUser) {
        if (sysUser == null) {
            return Message.error("参数错误，操作失败");
        }

        if (sysUser.getId() == null) {
            return Message.error("请选择需要编辑的记录");
        }
        try {
            if (StringUtils.isNotBlank(sysUser.getPassword())) {
                sysUser.setPassword(MD5Util.MD5Encode(sysUser.getPassword(), "UTF-8"));
            }
            this.sysUserService.editSave(sysUser);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysuser:delete"})
    @RequestMapping({"/delete"})
    @ResponseBody
    public Message delete(@RequestParam(value = "ids[]", required = false) Long[] ids, HttpServletRequest request) {
        if ((ids == null) || (ids.length == 0))
            return Message.error("请选择要删除的数据");
        try {
            if (ids != null) {
                SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
                for (int i = 0; i < ids.length; i++) {
                    if (ids[i].equals(sysUser.getId())) {
                        return Message.error("不能删除自己！");
                    }
                }
            }

            this.sysUserService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysuser:usermenu"})
    @RequestMapping({"/userMenuList"})
    public String userMenuList(Long userId, Model model) {
        List menuList = this.sysMenuService.queryAllMenu();
        String menuIds = this.sysUserService.queryUserMenuIds(userId);

        model.addAttribute("menuList", menuList);
        model.addAttribute("menuIds", menuIds);
        return "admin/system/sysmenu/sysMenuSelectMulti";
    }

    @AdminPermission({"admin:sysuser:usermenu"})
    @RequestMapping({"/addSaveUserMenu"})
    @ResponseBody
    public Message addSaveUserMenu(Long userId, @RequestParam(value = "menuIds[]", required = false) Long[] menuIds) {
        try {
            if (userId == null) {
                return Message.error("请选择用户");
            }

            if (this.sysUserService.querySysUserById(userId) == null) {
                return Message.error("用户不存在或已被删除");
            }
            this.sysUserService.addSaveUserMenu(userId, menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysuser:userrole"})
    @RequestMapping({"/userRoleList"})
    public String userRoleList(Long userId, Model model) {
        List roleList = this.sysRoleService.queryAllRole();
        String roleIds = this.sysUserService.queryUserRoleIds(userId);

        model.addAttribute("roleList", roleList);
        model.addAttribute("roleIds", roleIds);
        return "admin/system/sysrole/sysRoleListCheck";
    }

    @AdminPermission({"admin:sysuser:userrole"})
    @RequestMapping({"/addSaveUserRole"})
    @ResponseBody
    public Message addSaveUserRole(Long userId, @RequestParam(value = "roleIds[]", required = false) Long[] roleIds) {
        try {
            if (userId == null) {
                return Message.error("请选择用户");
            }

            if (this.sysUserService.querySysUserById(userId) == null) {
                return Message.error("用户不存在或已被删除");
            }

            this.sysUserService.addSaveUserRole(userId, roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @RequestMapping({"/getUserMenu"})
    @ResponseBody
    public Message getUserMenu(HttpServletRequest request) {

        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            List userMenus = this.sysUserService.queryUserMenus(sysUser.getId());
            return Message.success("操作成功", userMenus);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return Message.error("获取用户菜单失败");
    }

    @RequestMapping({"/openChromeIdValid"})
    @ResponseBody
    public Message openChromeIdValid(HttpServletRequest request, Integer val) {
        try {
            if (val == null) {
                return Message.error("参数错误");
            }

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);

            SysUser user1 = sysUserService.querySysUserById(sysUser.getId());
            if (user1 == null) {
                return Message.error("非法用户");
            }
            if (user1.getValidStatus().equals(val)) {
                String msg = "启用";
                if (val.intValue() == 0) {
                    msg = "关闭";
                }
                return Message.error("谷歌验证已" + msg + ",无需重复" + msg);
            }

            SysUser u1 = new SysUser();
            u1.setId(sysUser.getId());
            u1.setValidStatus(val);
            u1.setValidCode("");
            if (val.intValue() == 1) {
                u1.setValidCode(GoogleGenerator.generateSecretKey());
            }
            sysUserService.updateChromeId(u1);
            if (val.intValue() == 1) {
                Map<String, String> result = new HashMap<String, String>();
                result.put("name", user1.getUsername());
                result.put("key", u1.getValidCode());
                //cyber 修改：关闭谷歌验证码下发
                //result.put("img", ZxingUtil.createQRCodeStr(GoogleGenerator.getQRBarcode(user1.getUsername(), u1.getValidCode())));
                result.put("img","0");
                return Message.success("操作成功", result);
            }
            return Message.success("操作成功");
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return Message.error("操作失败");
    }
}
