package com.city.city_collector.admin.pay.dao;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

public interface YajinDao {
    int insertYajin(@Param("id") Long id, @Param("yajin") int yajin, @Param("money") int i);

    int deleteYajin(@Param("id") Long id);

    int updateYajin(@Param("id") Long id, @Param("yajin") int yajin);

    Integer selectYajin(@Param("id") Long id);

    int addMoney(@Param("id") Long clientId, @Param("money") BigDecimal money);

    Map<String, Integer> selectYajinMoney(@Param("id") Long clientId);

    int updateMoney(@Param("id") Long id);
}
