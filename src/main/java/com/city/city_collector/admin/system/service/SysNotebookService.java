package com.city.city_collector.admin.system.service;

import com.city.city_collector.admin.system.entity.SysNotebook;
import com.city.city_collector.common.bean.Page;

import java.util.List;
import java.util.Map;

public interface SysNotebookService {


    void addSave(SysNotebook sysNotebook);

    SysNotebook query(Integer id);

    void editSave(SysNotebook sysNotebook);

    void queryPage(Map<String, Object> map, Page page, String[] orders);
}
