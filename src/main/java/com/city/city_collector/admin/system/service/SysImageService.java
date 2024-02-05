package com.city.city_collector.admin.system.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.simpleimage.SimpleImageException;
import com.city.city_collector.admin.system.entity.SysImage;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.common.bean.Page;

public abstract interface SysImageService {
    public abstract Page queryPage(Map<String, Object> paramMap, Page paramPage, String[] paramArrayOfString);

    public abstract void addSave(SysImage paramSysImage);

    public abstract void editSave(SysImage paramSysImage);

    public abstract void delete(Long[] paramArrayOfLong);

    public abstract SysImage querySysImageById(Long paramLong);

    public abstract void saveImage(SysImage paramSysImage);

    public abstract void updateStatusByIds(Long[] paramArrayOfLong, String paramString);

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
            throws IllegalStateException, IOException, SimpleImageException;

    public List<String> queryIndexImgList();
}
