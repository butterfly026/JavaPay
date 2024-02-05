package com.city.city_collector.admin.system.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.simpleimage.SimpleImageException;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.system.dao.SysImageDao;
import com.city.city_collector.admin.system.entity.SysImage;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysImageService;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.util.SimpleImageUtil;

@Service
public class SysImageServiceImpl implements SysImageService {

    @Value("${static.folder}")
    private String staticFolder;

    @Value("${server.servlet.context-path}")
    private String context;

    @Autowired
    SysImageDao sysImageMapper;

    public Page queryPage(Map<String, Object> params, Page page, String[] orders) {
        params.put("status", "1");

        int total = this.sysImageMapper.queryCount(params);
        page.setTotal(total);
        params.put("start", Integer.valueOf(page.getStartRow()));
        params.put("end", page.getPageSize());

        page.setResults(this.sysImageMapper.queryPage(params));

        if ((orders != null) && (orders.length > 0)) {
            String orderby = "";
            for (String str : orders) {
                String[] arr1 = str.split(" ");
                if (arr1.length == 2) {
                    orderby = orderby + "`" + arr1[0] + "` " + arr1[1] + ",";
                }
            }
            if (orderby.endsWith(",")) {
                orderby = orderby.substring(0, orderby.length() - 1);
            }
            params.put("orderby", orderby);
        }
        return page;
    }

    public void addSave(SysImage sysImage) {
        this.sysImageMapper.addSave(sysImage);
    }

    public void editSave(SysImage sysImage) {
        this.sysImageMapper.editSave(sysImage);
    }

    public void delete(Long[] ids) {
        String ids_str = "";
        for (int i = 0; i < ids.length; i++) {
            ids_str = ids_str + ids[i];
        }
        if (ids_str.endsWith(",")) {
            ids_str = ids_str.substring(0, ids_str.length() - 1);
        }
        this.sysImageMapper.delete(ids_str);

        // 更新图片缓存数据
        ApplicationData.getInstance().updateImgList(sysImageMapper.queryIndexImgList());
    }

    public SysImage querySysImageById(Long id) {
        Map params = new HashMap();
        params.put("id", id);
        return this.sysImageMapper.querySingle(params);
    }

    public void updateStatusByIds(Long[] ids, String status) {
        String ids_str = "";
        for (int i = 0; i < ids.length; i++) {
            ids_str = ids_str + ids[i] + ",";
        }
        if (ids_str.endsWith(",")) {
            ids_str = ids_str.substring(0, ids_str.length() - 1);
        }

        Map params = new HashMap();
        params.put("status", status);
        params.put("ids", ids_str);
        this.sysImageMapper.updateStatusByIds(params);

        // 更新图片缓存数据
        ApplicationData.getInstance().updateImgList(sysImageMapper.queryIndexImgList());
    }

    public void saveImage(SysImage sysImage) {
        this.sysImageMapper.addSave(sysImage);
        // 更新图片缓存数据
        ApplicationData.getInstance().updateImgList(sysImageMapper.queryIndexImgList());
    }

    /**
     * 上传文件
     *
     * @param folderId 目录id
     * @param file     文件
     * @param type     类型
     * @param sysUser  操作用户
     * @throws IllegalStateException
     * @throws IOException
     * @throws SimpleImageException
     * @author:nb
     */
    public SysImage uploaderFile(Long folderId, MultipartFile file, String type, SysUser sysUser)
            throws IllegalStateException, IOException, SimpleImageException {
//        Map result = new HashMap();

        String path = "file/";
        if (folderId != null)
            path = path + folderId.toString();
        else {
            path = path + "admin";
        }

        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");

        Date date = new Date();
        path = path + "/" + sim.format(date) + "/";
        sim = new SimpleDateFormat("yyyyMMddHHmmssS");
        String fileName = sim.format(date) + "." + type;

        File f = new File(this.staticFolder + path);
        if (!f.exists()) {
            f.mkdirs();
        }

        f = new File(f.getAbsolutePath(), fileName);
        file.transferTo(f);

        if (",jpg,jpeg,png,bmp,gif,".indexOf(type) != -1) {//如果是图片，生成缩略图
            File thumbFile = new File(this.staticFolder + path + "thumb" + "/");
            if (!thumbFile.exists()) {
                thumbFile.mkdirs();
            }
            thumbFile = new File(this.staticFolder + path + "thumb" + "/" + fileName);
            createThumbImage(f, thumbFile, type);

//            result.put("thumbPath", this.context + "/" + path + "thumb" + "/" + fileName);
        }

        DecimalFormat df = new DecimalFormat("######0.00");
        String fileSize = "";
        long fileLen = f.length();
        if (fileLen < 1024L)
            fileSize = fileLen + "B";
        else if (fileLen < 1048576L)
            fileSize = df.format(fileLen / 1024.0D) + "KB";
        else if (fileLen < 1073741824L)
            fileSize = df.format(fileLen / 1024.0D / 1024.0D) + "MB";
        else {
            fileSize = df.format(fileLen / 1024.0D / 1024.0D / 1024.0D) + "GB";
        }
//        result.put("size", fileSize);
//        result.put("name", file.getOriginalFilename());
//        result.put("path", this.context + "/" + path + fileName);
//        result.put("type", type);
        SysImage sysImage = new SysImage();
        sysImage.setUserId(sysUser.getId());
        sysImage.setName(file.getOriginalFilename());
        sysImage.setFolderId(folderId);
        sysImage.setStatus("1");
        sysImage.setType(type);
        sysImage.setSize(fileSize);
        sysImage.setPath(this.context + "/" + path + fileName);
        sysImage.setThumbPath(this.context + "/" + path + "thumb" + "/" + fileName);
        sysImageMapper.addSave(sysImage);

        // 更新图片缓存数据
        ApplicationData.getInstance().updateImgList(sysImageMapper.queryIndexImgList());

        return sysImage;
    }

    /**
     * 创建缩略图
     *
     * @param srcFile
     * @param destFile
     * @param fileType
     * @throws IOException
     * @throws SimpleImageException
     * @author:nb
     */
    private void createThumbImage(File srcFile, File destFile, String fileType)
            throws IOException, SimpleImageException {
        BufferedImage image = ImageIO.read(srcFile);
        int width = image.getWidth();
        int height = image.getHeight();
        int imgw = width;
        int imgh = height;
        if (width > 300) {
            if (height > 300) {
                imgh = height * 300 / width;
                imgw = 300;
                if (imgh > 300) {
                    imgh = 300;
                    imgw = width * 300 / height;
                }
            } else {
                imgw = 300;
                imgh = height * 300 / width;
            }
        } else if (height > 300) {
            imgh = 300;
            imgw = width * 300 / height;
        }

        SimpleImageUtil.scaleImage(srcFile, destFile, imgw, imgh, fileType);
    }

    public List<String> queryIndexImgList() {
        return sysImageMapper.queryIndexImgList();
    }
}
