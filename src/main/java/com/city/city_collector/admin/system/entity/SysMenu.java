package com.city.city_collector.admin.system.entity;


public class SysMenu {
    /**
     * ID
     */
    private Long id;
    /**
     * 上级菜单ID
     */
    private Long fid;
    /**
     * 菜单名
     */
    private String name;
    /**
     * 菜单地址
     */
    private String url;
    /**
     * 序号
     */
    private Integer order;
    /**
     * 状态
     */
    private String status;
    /**
     * 菜单权限
     */
    private String permission;
    /**
     * 类型  0:菜单级别 1:按钮级别
     */
    private String type;
    /**
     * 菜单图标路径
     */
    private String image;
    /**
     * 父目录名
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
     * 获取上级菜单ID
     *
     * @return fid 上级菜单ID
     */
    public Long getFid() {
        return fid;
    }

    /**
     * 设置上级菜单ID
     *
     * @param fid 上级菜单ID
     */
    public void setFid(Long fid) {
        this.fid = fid;
    }

    /**
     * 获取菜单名
     *
     * @return name 菜单名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置菜单名
     *
     * @param name 菜单名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取菜单地址
     *
     * @return url 菜单地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置菜单地址
     *
     * @param url 菜单地址
     */
    public void setUrl(String url) {
        this.url = url;
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
     * 获取状态
     *
     * @return status 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取菜单权限
     *
     * @return permission 菜单权限
     */
    public String getPermission() {
        return permission;
    }

    /**
     * 设置菜单权限
     *
     * @param permission 菜单权限
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * 获取类型0:菜单级别1:按钮级别
     *
     * @return type 类型0:菜单级别1:按钮级别
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型0:菜单级别1:按钮级别
     *
     * @param type 类型0:菜单级别1:按钮级别
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取菜单图标路径
     *
     * @return image 菜单图标路径
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置菜单图标路径
     *
     * @param image 菜单图标路径
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 获取父目录名
     *
     * @return fname 父目录名
     */
    public String getFname() {
        return fname;
    }

    /**
     * 设置父目录名
     *
     * @param fname 父目录名
     */
    public void setFname(String fname) {
        this.fname = fname;
    }
}
