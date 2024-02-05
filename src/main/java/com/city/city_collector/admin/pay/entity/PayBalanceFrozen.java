package com.city.city_collector.admin.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:余额冻结
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayBalanceFrozen {
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

    private Long userId;

    private String userNo;

    private String userName;

    private BigDecimal money;

    private Integer frozenType;

    private String remark;

    private Integer status;

    private Date jdTime;

    private Integer type;

    private String orderSn;


    /**
     * orderSn
     *
     * @return orderSn
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置 orderSn
     *
     * @param orderSn orderSn
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
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
     * userNo
     *
     * @return userNo
     */
    public String getUserNo() {
        return userNo;
    }

    /**
     * 设置 userNo
     *
     * @param userNo userNo
     */
    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    /**
     * userName
     *
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置 userName
     *
     * @param userName userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * money
     *
     * @return money
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置 money
     *
     * @param money money
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * frozenType
     *
     * @return frozenType
     */
    public Integer getFrozenType() {
        return frozenType;
    }

    /**
     * 设置 frozenType
     *
     * @param frozenType frozenType
     */
    public void setFrozenType(Integer frozenType) {
        this.frozenType = frozenType;
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
     * status
     *
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置 status
     *
     * @param status status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * jdTime
     *
     * @return jdTime
     */
    public Date getJdTime() {
        return jdTime;
    }

    /**
     * 设置 jdTime
     *
     * @param jdTime jdTime
     */
    public void setJdTime(Date jdTime) {
        this.jdTime = jdTime;
    }

    /**
     * type
     *
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置 type
     *
     * @param type type
     */
    public void setType(Integer type) {
        this.type = type;
    }
}
