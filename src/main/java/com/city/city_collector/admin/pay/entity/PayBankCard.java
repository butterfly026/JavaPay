package com.city.city_collector.admin.pay.entity;

import java.util.Date;

/**
 * Description:银行编码
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayBankCard {
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

    private String cardNo;

    private String cardName;

    private String bankName;

    private String bankSubname;

    private String bankId;

    private String cardId;

    private String cardMobile;

    private String bankCode;

    private Long userId;

    private String userNo;

    private String userName;

    private Integer userType;

    private String province;

    private String city;

    private Integer del;

    private String bankIfsc;

    private String bankNation;

    private Integer btype;

    /**
     * bankIfsc
     *
     * @return bankIfsc
     */
    public String getBankIfsc() {
        return bankIfsc;
    }

    /**
     * 设置 bankIfsc
     *
     * @param bankIfsc bankIfsc
     */
    public void setBankIfsc(String bankIfsc) {
        this.bankIfsc = bankIfsc;
    }

    /**
     * bankNation
     *
     * @return bankNation
     */
    public String getBankNation() {
        return bankNation;
    }

    /**
     * 设置 bankNation
     *
     * @param bankNation bankNation
     */
    public void setBankNation(String bankNation) {
        this.bankNation = bankNation;
    }

    /**
     * btype
     *
     * @return btype
     */
    public Integer getBtype() {
        return btype;
    }

    /**
     * 设置 btype
     *
     * @param btype btype
     */
    public void setBtype(Integer btype) {
        this.btype = btype;
    }

    /**
     * del
     *
     * @return del
     */
    public Integer getDel() {
        return del;
    }

    /**
     * 设置 del
     *
     * @param del del
     */
    public void setDel(Integer del) {
        this.del = del;
    }

    /**
     * province
     *
     * @return province
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置 province
     *
     * @param province province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * city
     *
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置 city
     *
     * @param city city
     */
    public void setCity(String city) {
        this.city = city;
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
     * cardNo
     *
     * @return cardNo
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * 设置 cardNo
     *
     * @param cardNo cardNo
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * cardName
     *
     * @return cardName
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * 设置 cardName
     *
     * @param cardName cardName
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * bankName
     *
     * @return bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置 bankName
     *
     * @param bankName bankName
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * bankSubname
     *
     * @return bankSubname
     */
    public String getBankSubname() {
        return bankSubname;
    }

    /**
     * 设置 bankSubname
     *
     * @param bankSubname bankSubname
     */
    public void setBankSubname(String bankSubname) {
        this.bankSubname = bankSubname;
    }

    /**
     * bankId
     *
     * @return bankId
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * 设置 bankId
     *
     * @param bankId bankId
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * cardId
     *
     * @return cardId
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * 设置 cardId
     *
     * @param cardId cardId
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * cardMobile
     *
     * @return cardMobile
     */
    public String getCardMobile() {
        return cardMobile;
    }

    /**
     * 设置 cardMobile
     *
     * @param cardMobile cardMobile
     */
    public void setCardMobile(String cardMobile) {
        this.cardMobile = cardMobile;
    }

    /**
     * bankCode
     *
     * @return bankCode
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * 设置 bankCode
     *
     * @param bankCode bankCode
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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
     * userType
     *
     * @return userType
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * 设置 userType
     *
     * @param userType userType
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
