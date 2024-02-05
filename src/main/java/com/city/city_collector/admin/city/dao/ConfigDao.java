package com.city.city_collector.admin.city.dao;

import java.util.List;
import java.util.Map;

import com.city.city_collector.admin.city.entity.Config;

/**
 * Description:栏目-Mapper接口
 *
 * @author BG
 * @version 1.0
 * @since 2020-11-12
 */
public interface ConfigDao {
    /**
     * Description:查询记录总条数
     *
     * @param params 参数键值对象
     * @return int
     * @author:BG
     * @since 2020-11-12
     */
    public int queryCount(Map<String, Object> params);

    /**
     * Description:查询分页数据
     *
     * @param params 参数键值对象
     * @return List<Map>
     * @author:BG
     * @since 2020-11-12
     */
    public List<Map<String, Object>> queryPage(Map<String, Object> params);

    /***
     * Description:保存记录
     * @author:BG
     * @since 2020-11-12
     * @param params 参数键值对象
     * @return void
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
     * @param params 参数键值对象
     * @return void
     * @author:BG
     * @since 2020-11-12
     */
    public void editSave(Config config);

    /**
     * Description:删除指定记录
     *
     * @param params 参数键值对象
     * @return void
     * @author:BG
     * @since 2020-11-12
     */
    public void delete(String ids);

    void updateAutoDaiFu(Integer integer);
}
