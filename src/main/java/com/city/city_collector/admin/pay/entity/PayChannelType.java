package com.city.city_collector.admin.pay.entity;

import java.util.Date;

/**
 * Description:通道类型
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayChannelType {
    /**
     * ID
     */
    private Long id;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新日期
     */
    private Date updateTime;

    private String name;

    private String descript;

    private String code;

    /**
     * name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * descript
     *
     * @return descript
     */
    public String getDescript() {
        return descript;
    }

    /**
     * 设置 descript
     *
     * @param descript descript
     */
    public void setDescript(String descript) {
        this.descript = descript;
    }

    /**
     * 获取ID
     *
     * @return id ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建日期
     *
     * @return createTime 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新日期
     *
     * @return updateTime 更新日期
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新日期
     *
     * @param updateTime 更新日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * code
     *
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置 code
     *
     * @param code code
     */
    public void setCode(String code) {
        this.code = code;
    }
}
