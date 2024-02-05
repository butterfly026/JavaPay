package com.city.city_collector.admin.city.service;

import java.util.Map;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.admin.city.entity.Config;

/**
 * Description:栏目-service接口
 *
 * @author BG
 * @version 1.0
 * @since 2020-11-12
 */
public interface ConfigService {
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
    public Page queryPage(Map<String, Object> params, Page page, String[] orders);

    /**
     * Description:保存记录
     *
     * @param config
     * @return void
     * @author:BG
     * @since 2020-11-12
     */
    public void addSave(Config config);

    /**
     * Description:查询单条记录
     *
     * @param params 参数键值对象
     * @return Map
     * @author:BG
     * @since 2020-11-12
     */
    public Config querySingle(Map<String, Object> params);

    /**
     * Description:更新记录
     *
     * @param config
     * @return void
     * @author:BG
     * @since 2020-11-12
     */
    public void editSave(Config config);

    /**
     * Description:删除指定记录
     *
     * @param ids ID数组
     * @return void
     * @author:BG
     * @since 2020-11-12
     */
    public void delete(Long[] ids);

    /**
     * 更新代付开关
     * @param integer
     */
    public void updateAutoDaiFu(Integer integer);
}
