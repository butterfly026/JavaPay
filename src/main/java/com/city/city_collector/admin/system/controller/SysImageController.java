package com.city.city_collector.admin.system.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.admin.system.entity.SysImage;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysFolderService;
import com.city.city_collector.admin.system.service.SysImageService;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.util.Message;

@Controller
@RequestMapping({"/system/sysImage"})
public class SysImageController {

    @Autowired
    SysImageService sysImageService;

    @Autowired
    SysFolderService sysFolderService;

    @Value("${static.folder}")
    private String staticFolder;

    @Value("${server.servlet.context-path}")
    private String context;

//    @Autowired
//    FileFeignService fileService;

    @AdminPermission({"admin:sysuser:list"})
    @RequestMapping({"/view"})
    public String view(Model model) {
        Map params = new HashMap();
        params.put("type", "0");
        List folderList = this.sysFolderService.queryList(params);
        model.addAttribute("folderList", folderList);
        return "admin/system/sysimage/sysImage_list";
    }

    @AdminPermission({"admin:sysimage:list"})
    @RequestMapping({"/list"})
    @ResponseBody
    public Message list(@RequestParam Map<String, Object> map, Long folderId, Integer pageNo, Integer pageSize,
                        @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            Page page = new Page(pageNo, pageSize);

            map.put("folderId", folderId);
            this.sysImageService.queryPage(map, page, orders);
            return Message.success("操作成功", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error("程序异常，查询失败");
    }

    @AdminPermission({"admin:sysimage:add"})
    @RequestMapping({"/add"})
    public String add(Integer folderId, Model model) {
        Map params = new HashMap();
        params.put("type", "0");
        List folderList = this.sysFolderService.queryList(params);
        model.addAttribute("folderList", folderList);
        model.addAttribute("folderId", folderId);
        return "admin/system/sysimage/sysImage_upload";
    }

    @AdminPermission({"admin:sysimage:add"})
    @RequestMapping({"/addSave"})
    @ResponseBody
    public Message addSave(Long folderId, @RequestParam(value = "file", required = true) MultipartFile file,
                           HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            int index = file.getOriginalFilename().lastIndexOf(".");
            if (index == -1) {
                return Message.error("文件格式错误！");
            }
            String type = file.getOriginalFilename().substring(index + 1).toLowerCase();
            if (",jpg,jpeg,png,bmp,gif,".indexOf("," + type + ",") == -1) {
                return Message.error("文件格式错误！");
            }

            SysImage sysImage = sysImageService.uploaderFile(folderId, file, type, sysUser);
            //保存文件
//            String ssid = AdminPermissionManager.getJsessionid(request);
//            AppMessage msg = this.fileService.uploader(ssid, folderId, file, type);
//            if ((msg.getCode() == 200) && (msg.getResult() != null)) {
//                Map result = msg.getResult();
//                System.out.println("返回的结果:" + result);
//
//                SysImage sysImage = new SysImage();
//                sysImage.setUserId(sysUser.getId());
//                sysImage.setName(file.getOriginalFilename());
//                sysImage.setFolderId(folderId);
//                sysImage.setStatus("1");
//                sysImage.setType(type);
//                sysImage.setSize(result.get("size") == null ? "" : result.get("size").toString());
//                sysImage.setPath(result.get("path") == null ? "" : result.get("path").toString());
//                sysImage.setThumbPath(result.get("thumbPath") == null ? "" : result.get("thumbPath").toString());
//                this.sysImageService.addSave(sysImage);
//                return Message.success("文件上传成功", result);
//            }
//            return Message.error(msg.getMsg());
            System.out.println("图片ID：" + sysImage.getId());
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("thumbPath", sysImage.getThumbPath());
            result.put("path", sysImage.getPath());
            return Message.success("文件上传成功", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error("程序异常，文件上传失败");
    }

    @AdminPermission({"admin:sysimage:edit"})
    @RequestMapping({"/edit"})
    public String edit(Long id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/system/sysimage/sysImage_edit";
        }
        SysImage sysImage = this.sysImageService.querySysImageById(id);
        if (sysImage == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
            return "admin/system/sysimage/sysImage_edit";
        }
        model.addAttribute("result", sysImage);
        return "admin/system/sysimage/sysImage_edit";
    }

    @AdminPermission({"admin:sysimage:edit"})
    @RequestMapping({"/editSave"})
    @ResponseBody
    public Message editSave(SysImage sysImage) {
        if (sysImage == null) {
            return Message.error("参数错误，操作失败");
        }

        if (sysImage.getId() == null) {
            return Message.error("请选择需要编辑的记录");
        }
        if (StringUtils.isBlank(sysImage.getName())) {
            return Message.error("请输入新的文件名");
        }
        try {
            this.sysImageService.editSave(sysImage);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission({"admin:sysimage:delete"})
    @RequestMapping({"/delete"})
    @ResponseBody
    public Message delete(@RequestParam("ids[]") Long[] ids) {
        if ((ids == null) || (ids.length == 0)) {
            return Message.error("请选择要删除的数据");
        }
        try {
            this.sysImageService.updateStatusByIds(ids, "0");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @RequestMapping({"/download"})
    public void downLoadImage(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        if (id == null) {
            response.getOutputStream().write("文件不存在".getBytes());
            return;
        }
        try {
            SysImage sysImage = this.sysImageService.querySysImageById(id);
            if (sysImage == null || StringUtils.isBlank(sysImage.getPath())) {
                response.getOutputStream().write("您下载的文件可能已经被删除".getBytes());
                return;
            }
            String imgPath = sysImage.getPath();
            if (StringUtils.isNotBlank(context) && imgPath.startsWith(context)) {
                imgPath = imgPath.substring(context.length());
            }

//            String fpath = request.getSession().getServletContext().getRealPath("") + "/" + sysImage.getPath();
            String fpath = staticFolder + imgPath;
            System.out.println("下载的文件路径：" + fpath);
            File file = new File(fpath);
            if (!file.exists()) {
                response.getOutputStream().write("您下载的文件可能已经被删除".getBytes());
                return;
            }

            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + sysImage.getName());
            response.setContentType("application/octet-stream;charset=UTF-8");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(FileUtils.readFileToByteArray(file));
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.getOutputStream().write("程序错误，下载失败".getBytes());
        }
    }

    @AdminPermission({"admin:sysimage:list"})
    @RequestMapping({"/select"})
    public String selectImage(String type, String method, String source, Model model) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", "0");
        List folderList = this.sysFolderService.queryList(params);
        model.addAttribute("folderList", folderList);
        model.addAttribute("type", type);
        model.addAttribute("method", method);
        model.addAttribute("source", source);
        return "admin/system/sysimage/sysImage_select";
    }
}
