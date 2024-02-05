package com.city.city_collector.admin.city.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.city.dao.ConfigDao;
import com.city.city_collector.admin.city.service.ConfigService;
import com.city.city_collector.admin.city.entity.Config;

/**
 * Description:栏目-service实现类
 *
 * @author BG
 * @version 1.0
 * @since 2020-11-12
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    ConfigDao configDao;

    /**
     * Description:分页查询
     *
     * @param params 参数键值对象
     * @param page   分页对象
     * @param orders 排序
     * @return Page
     * @author:BG
     * @since 2020-11-12
     */
    public Page queryPage(Map<String, Object> params, Page page, String[] orders) {
        //获取记录总数
        int total = configDao.queryCount(params);
        page.setTotal(total);
        params.put("start", page.getStartRow());
        params.put("end", page.getPageSize());
        //拼接排序字段
        if (orders != null && orders.length > 0) {
            String orderby = "";
            for (String str : orders) {
                String[] arr1 = str.split(" ");
                if (arr1.length == 2) {
                    orderby += "`" + arr1[0] + "` " + arr1[1] + ",";
                }
            }
            if (orderby.endsWith(",")) {
                orderby = orderby.substring(0, orderby.length() - 1);
            }
            params.put("orderby", orderby);
        }
        //获取分页数据
        page.setResults(configDao.queryPage(params));
        return page;
    }

    /**
     * Description:保存记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:BG
     * @since 2020-11-12
     */
    @Override
    public void addSave(Config config) {
        configDao.addSave(config);
    }

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:BG
     * @since 2020-11-12
     */
    @Override
    public Config querySingle(Map<String, Object> params) {
        return configDao.querySingle(params);
    }

    /**
     * Description:更新记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:BG
     * @since 2020-11-12
     */
    @Override
    public void editSave(Config config) {
        configDao.editSave(config);
    }

    /**
     * Description:删除指定记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:BG
     * @since 2020-11-12
     */
    @Override
    public void delete(Long[] ids) {
        String ids_str = "";
        for (int i = 0; i < ids.length; i++) {
            ids_str += ids[i] + ",";
        }
        if (ids_str.endsWith(",")) {
            ids_str = ids_str.substring(0, ids_str.length() - 1);
        }
        configDao.delete(ids_str);
    }

    @Override
    public void updateAutoDaiFu(Integer integer) {
        configDao.updateAutoDaiFu(integer);
    }
}
