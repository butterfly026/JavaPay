package com.city.city_collector.admin.pay.entity;

import java.util.Date;

/**
 * Description:Excel导出
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayExcelexport {
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

    /**
     * 文件下载名
     */
    private String name;
    /**
     * 下载链接
     */
    private String downUrl;
    /**
     * 文件生成路径
     */
    private String filePath;
    /**
     * 处理状态（0:处理中,1:处理成功,-1：处理失败）
     */
    private int dealStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 数据来源
     */
    private Integer source;

    private Long userId;

    /**
     * userId
     *
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置 userId
     *
     * @param userId userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * source
     *
     * @return source
     */
    public Integer getSource() {
        return source;
    }

    /**
     * 设置 source
     *
     * @param source source
     */
    public void setSource(Integer source) {
        this.source = source;
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
     * downUrl
     *
     * @return downUrl
     */
    public String getDownUrl() {
        return downUrl;
    }

    /**
     * 设置 downUrl
     *
     * @param downUrl downUrl
     */
    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    /**
     * filePath
     *
     * @return filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 设置 filePath
     *
     * @param filePath filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * dealStatus
     *
     * @return dealStatus
     */
    public int getDealStatus() {
        return dealStatus;
    }

    /**
     * 设置 dealStatus
     *
     * @param dealStatus dealStatus
     */
    public void setDealStatus(int dealStatus) {
        this.dealStatus = dealStatus;
    }

    /**
     * remark
     *
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 remark
     *
     * @param remark remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
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

}
