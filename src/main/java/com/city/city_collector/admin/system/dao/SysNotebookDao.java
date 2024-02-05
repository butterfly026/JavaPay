package com.city.city_collector.admin.system.dao;

import com.city.city_collector.admin.system.entity.SysNotebook;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysNotebookDao {

    int count(Map<String, Object> map);

    List<Map<String, Object>> page(Map<String, Object> map);

    int insert(SysNotebook sysNotebook);

    SysNotebook query(@Param("id") Integer id);

    void update(SysNotebook sysNotebook);

}
