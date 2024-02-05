package com.city.city_collector.admin.system.service.impl;

import com.city.city_collector.admin.system.dao.SysNotebookDao;
import com.city.city_collector.admin.system.entity.SysNotebook;
import com.city.city_collector.admin.system.service.SysNotebookService;
import com.city.city_collector.common.bean.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysNotebookServiceImpl implements SysNotebookService {

    @Autowired
    private SysNotebookDao sysNotebookDao;


    @Override
    public void queryPage(Map<String, Object> params, Page page, String[] orders) {
        int total = this.sysNotebookDao.count(params);
        page.setTotal(total);
        params.put("start", Integer.valueOf(page.getStartRow()));
        params.put("end", page.getPageSize());

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

        page.setResults(this.sysNotebookDao.page(params));

    }

    @Override
    public void addSave(SysNotebook sysNotebook) {
        sysNotebookDao.insert(sysNotebook);
    }

    @Override
    public SysNotebook query(Integer id) {
        return sysNotebookDao.query(id);
    }

    @Override
    public void editSave(SysNotebook sysNotebook) {
        sysNotebookDao.update(sysNotebook);
    }


}
