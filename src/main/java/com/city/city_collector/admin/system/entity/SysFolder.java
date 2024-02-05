package com.city.city_collector.admin.system.entity;


public class SysFolder {
    /**
     * ID
     */
    private Long id;
    /**
     * 父目录ID
     */
    private Long fid;
    /**
     * 目录名
     */
    private String name;
    /**
     * 创建人ID
     */
    private Long userId;
    /**
     * 父目录名
     */
    private String fname;
    /**
     * 目录类型  0：图片目录   1：文件目录
     */
    private String type;
    /**
     * 创建人称呼
     */
    private String userName;

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
     * 获取父目录ID
     *
     * @return fid 父目录ID
     */
    public Long getFid() {
        return fid;
    }

    /**
     * 设置父目录ID
     *
     * @param fid 父目录ID
     */
    public void setFid(Long fid) {
        this.fid = fid;
    }

    /**
     * 获取目录名
     *
     * @return name 目录名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置目录名
     *
     * @param name 目录名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取创建人ID
     *
     * @return userId 创建人ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置创建人ID
     *
     * @param userId 创建人ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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

    /**
     * 获取目录类型0：图片目录1：文件目录
     *
     * @return type 目录类型0：图片目录1：文件目录
     */
    public String getType() {
        return type;
    }

    /**
     * 设置目录类型0：图片目录1：文件目录
     *
     * @param type 目录类型0：图片目录1：文件目录
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取创建人称呼
     *
     * @return userName 创建人称呼
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置创建人称呼
     *
     * @param userName 创建人称呼
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
