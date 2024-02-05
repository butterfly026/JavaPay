package com.city.city_collector.admin.system.entity;


public class SysImage {
    /**
     * 图片ID
     */
    private Long id;

    /**
     * 目录ID
     */
    private Long folderId;

    /**
     * 目录名
     */
    private String folderName;

    /**
     * 图片名
     */
    private String name;

    /**
     * 创建人ID
     */
    private Long userId;

    /**
     * 创建人称呼
     */
    private String userName;

    /**
     * 创建人登录名
     */
    private String userUserName;

    /**
     * 图片路径
     */
    private String path;

    /**
     * 缩略图路径
     */
    private String thumbPath;

    /**
     * 状态：0,删除 1,正常
     */
    private String status;

    /**
     * 文件大小，格式为 xxB xxKB xxMB xxGB，取两位小数
     */
    private String size;

    /**
     * 文件类型，jpg,bmp,png,gif,jpeg
     */
    private String type;

    /**
     * 获取图片ID
     *
     * @return id 图片ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置图片ID
     *
     * @param id 图片ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取目录ID
     *
     * @return folderId 目录ID
     */
    public Long getFolderId() {
        return folderId;
    }

    /**
     * 设置目录ID
     *
     * @param folderId 目录ID
     */
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    /**
     * 获取图片名
     *
     * @return name 图片名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置图片名
     *
     * @param name 图片名
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

    /**
     * 获取创建人登录名
     *
     * @return userUserName 创建人登录名
     */
    public String getUserUserName() {
        return userUserName;
    }

    /**
     * 设置创建人登录名
     *
     * @param userUserName 创建人登录名
     */
    public void setUserUserName(String userUserName) {
        this.userUserName = userUserName;
    }

    /**
     * 获取图片路径
     *
     * @return path 图片路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置图片路径
     *
     * @param path 图片路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取缩略图路径
     *
     * @return thumbPath 缩略图路径
     */
    public String getThumbPath() {
        return thumbPath;
    }

    /**
     * 设置缩略图路径
     *
     * @param thumbPath 缩略图路径
     */
    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    /**
     * 获取状态：0删除1正常
     *
     * @return status 状态：0删除1正常
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态：0删除1正常
     *
     * @param status 状态：0删除1正常
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取文件大小，格式为xxBxxKBxxMBxxGB，取两位小数
     *
     * @return size 文件大小，格式为xxBxxKBxxMBxxGB，取两位小数
     */
    public String getSize() {
        return size;
    }

    /**
     * 设置文件大小，格式为xxBxxKBxxMBxxGB，取两位小数
     *
     * @param size 文件大小，格式为xxBxxKBxxMBxxGB，取两位小数
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * 获取文件类型，jpgbmppnggifjpeg
     *
     * @return type 文件类型，jpgbmppnggifjpeg
     */
    public String getType() {
        return type;
    }

    /**
     * 设置文件类型，jpgbmppnggifjpeg
     *
     * @param type 文件类型，jpgbmppnggifjpeg
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取目录名
     *
     * @return folderName 目录名
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * 设置目录名
     *
     * @param folderName 目录名
     */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
