package com.city.city_collector.admin.system.entity;


public class SysRole {
    /**
     * ID
     */
    private Long id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 角色状态  0:禁用   1:可用
     */
    private String status;

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
     * 获取部门名
     *
     * @return name 部门名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置部门名
     *
     * @param name 部门名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取描述
     *
     * @return desc 描述
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置描述
     *
     * @param desc 描述
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 获取角色状态0:禁用1:可用
     *
     * @return status 角色状态0:禁用1:可用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置角色状态0:禁用1:可用
     *
     * @param status 角色状态0:禁用1:可用
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
