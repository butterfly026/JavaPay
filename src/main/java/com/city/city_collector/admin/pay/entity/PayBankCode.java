package com.city.city_collector.admin.pay.entity;

import java.util.Date;

/**
 * Description:银行编码
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayBankCode {
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

    private String code;

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

    public PayBankCode() {
    }

    public PayBankCode(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public PayBankCode(String idStr, String name, String code) {
        this.name = name;
        this.code = code;
        this.idStr = idStr;
    }

    private String idStr;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }
}
