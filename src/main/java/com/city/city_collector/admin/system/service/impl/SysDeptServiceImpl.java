package com.city.city_collector.admin.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.city.city_collector.admin.system.dao.SysDeptDao;
import com.city.city_collector.admin.system.entity.SysDept;
import com.city.city_collector.admin.system.service.SysDeptService;
import com.city.city_collector.common.bean.Page;

@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    SysDeptDao sysDeptMapper;

    public Page queryPage(Map<String, Object> params, Page page) {
        int total = this.sysDeptMapper.queryCount(params);
        page.setTotal(total);
        params.put("start", Integer.valueOf(page.getStartRow()));
        params.put("end", page.getPageSize());

        page.setResults(this.sysDeptMapper.queryPage(params));

        return page;
    }

    public List<Map<String, Object>> queryAllDept() {
        return this.sysDeptMapper.queryAllDept();
    }

    public void addSave(SysDept sysDept) {
        this.sysDeptMapper.addSave(sysDept);
    }

    public void editSave(SysDept sysDept) {
        this.sysDeptMapper.editSave(sysDept);
    }

    public void delete(Long[] ids) {
        String ids_str = "";
        for (int i = 0; i < ids.length; i++) {
            ids_str = ids_str + ids[i] + ",";
        }
        if (ids_str.endsWith(",")) {
            ids_str = ids_str.substring(0, ids_str.length() - 1);
        }
        this.sysDeptMapper.delete(ids_str);
    }

    public SysDept querySysDeptById(Long id) {
        Map params = new HashMap();
        params.put("id", id);
        return this.sysDeptMapper.querySingle(params);
    }

    public void addSaveUserDept(Long deptId, Long[] userIds) {
        this.sysDeptMapper.deleteUserDept(deptId + "");
        if (userIds != null) {
            List list = new ArrayList();

            for (int i = 0; i < userIds.length; i++) {
                Map map = new HashMap();
                map.put("dept_id", deptId);
                map.put("user_id", userIds[i]);
                list.add(map);

                if ((i > 0) && (i % 5000 == 0)) {
                    this.sysDeptMapper.addSaveUserDept(list);
                    list = new ArrayList();
                }
            }

            if (!list.isEmpty())
                this.sysDeptMapper.addSaveUserDept(list);
        }
    }

    public String queryDeptUserIds(Long deptId) {
        return this.sysDeptMapper.queryDeptUserIds(deptId);
    }
}
