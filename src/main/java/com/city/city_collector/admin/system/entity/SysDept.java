package com.city.city_collector.admin.system.entity;


public class SysDept {
    /**
     * ID
     */
    private Long id;
    /**
     * 上级部门ID
     */
    private Long fid;
    /**
     * 部门名
     */
    private String name;
    /**
     * 序号
     */
    private Integer order;
    /**
     * 描述
     */
    private String desc;
    /**
     * 父级部门名
     */
    private String fname;

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
     * 获取上级部门ID
     *
     * @return fid 上级部门ID
     */
    public Long getFid() {
        return fid;
    }

    /**
     * 设置上级部门ID
     *
     * @param fid 上级部门ID
     */
    public void setFid(Long fid) {
        this.fid = fid;
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
     * 获取序号
     *
     * @return order 序号
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * 设置序号
     *
     * @param order 序号
     */
    public void setOrder(Integer order) {
        this.order = order;
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
     * 获取父级部门名
     *
     * @return fname 父级部门名
     */
    public String getFname() {
        return fname;
    }

    /**
     * 设置父级部门名
     *
     * @param fname 父级部门名
     */
    public void setFname(String fname) {
        this.fname = fname;
    }
}
