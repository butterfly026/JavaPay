package com.city.city_collector.admin.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.city.city_collector.admin.system.dao.SysFolderDao;
import com.city.city_collector.admin.system.dao.SysImageDao;
import com.city.city_collector.admin.system.entity.SysFolder;
import com.city.city_collector.admin.system.entity.SysImage;
import com.city.city_collector.admin.system.service.SysFolderService;

@Service
public class SysFolderServiceImpl implements SysFolderService {

    @Autowired
    SysFolderDao sysFolderMapper;

    @Autowired
    SysImageDao sysImageMapper;

    public void addSave(SysFolder sysFolder) {
        this.sysFolderMapper.addSave(sysFolder);
    }

    public void editSave(SysFolder sysFolder) {
        this.sysFolderMapper.editSave(sysFolder);
    }

    @Transactional
    public void delete(Long[] ids) {
        String ids_str = "";
        for (int i = 0; i < ids.length; i++) {
            ids_str = ids_str + ids[i] + ",";
        }
        if (ids_str.endsWith(",")) {
            ids_str = ids_str.substring(0, ids_str.length() - 1);
        }
        this.sysFolderMapper.delete(ids_str);

        SysImage sysImage = new SysImage();
        sysImage.setStatus("0");
        for (int i = 0; i < ids.length; i++) {
            sysImage.setFolderId(ids[i]);
            this.sysImageMapper.updateStatusByFolder(sysImage);
        }
    }

    public SysFolder querySysFolderById(Long id) {
        Map params = new HashMap();
        params.put("id", id);
        return this.sysFolderMapper.querySingle(params);
    }

    public List<Map<String, Object>> queryList(Map<String, Object> params) {
        return this.sysFolderMapper.queryList(params);
    }
}
