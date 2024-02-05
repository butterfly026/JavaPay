package com.city.city_collector.admin.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:上游通道
 *
 * @author demo
 * @since 2020-6-29
 */
public class PayClientChannel {
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

    private Long clientId;

    private String clientNo;

    private String clientName;

    private Integer channelTypeId;

    private String params;

    private String descript;

    private String startTime;

    private String endTime;

    private BigDecimal minMoney;

    private BigDecimal maxMoney;

    private String money;

    private BigDecimal ratio;

    private Integer status;

    private BigDecimal dayMoney;

    private BigDecimal jrcg;

    private BigDecimal jrdds;

    private BigDecimal jrcgl;

    private BigDecimal lscg;

    private BigDecimal lscgl;

    /**
     * 金额判定模式(0：最大最小金额，1：固定金额)
     */
    private Integer mtype;

    private String keyname;

    private Integer del;

    /**
     * 访问类型(0：api转发，1：表单转发)
     */
    private Integer gtype;
    /**
     * 通道类型。0，普通上游通道，1，paytm通道
     **/
    private Integer ctype;
    /**
     * 商户ID
     **/
    private String paytmid;
    /**
     * 商户秘钥
     **/
    private String paytmkey;
    /**
     * MD5秘钥
     **/
    private String paytmmd5;
    /**
     * UUID
     **/
    private String paytmuid;

    /**
     * 是否开启预警
     **/
    private Integer alarm;
    /**
     * 成功率预警值
     */
    private BigDecimal alarmNumber;

    /**
     * 成功率预警值上限
     */
    private BigDecimal alarmNumberup;

    /**
     * 此通道拉单时，可以尝试多少次。 最多5次。
     */
    private Integer retryNumber;

    public Integer getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(Integer retryNumber) {
        this.retryNumber = retryNumber;
    }

    /**
     * 优先处理平台.0.通用 1. ios 2. android 3. other
     * 默认值 0
     */
    private Integer primaryPlatform;


    public Integer getPrimaryPlatform() {
        return primaryPlatform;
    }

    public void setPrimaryPlatform(Integer primaryPlatform) {
        this.primaryPlatform = primaryPlatform;
    }

    /**
     * alarmNumberup
     *
     * @return alarmNumberup
     */
    public BigDecimal getAlarmNumberup() {
        return alarmNumberup;
    }

    /**
     * 设置 alarmNumberup
     *
     * @param alarmNumberup alarmNumberup
     */
    public void setAlarmNumberup(BigDecimal alarmNumberup) {
        this.alarmNumberup = alarmNumberup;
    }

    /**
     * alarm
     *
     * @return alarm
     */
    public Integer getAlarm() {
        return alarm;
    }

    /**
     * 设置 alarm
     *
     * @param alarm alarm
     */
    public void setAlarm(Integer alarm) {
        this.alarm = alarm;
    }

    /**
     * alarmNumber
     *
     * @return alarmNumber
     */
    public BigDecimal getAlarmNumber() {
        return alarmNumber;
    }

    /**
     * 设置 alarmNumber
     *
     * @param alarmNumber alarmNumber
     */
    public void setAlarmNumber(BigDecimal alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    /**
     * ctype
     *
     * @return ctype
     */
    public Integer getCtype() {
        return ctype;
    }

    /**
     * 设置 ctype
     *
     * @param ctype ctype
     */
    public void setCtype(Integer ctype) {
        this.ctype = ctype;
    }

    /**
     * paytmid
     *
     * @return paytmid
     */
    public String getPaytmid() {
        return paytmid;
    }

    /**
     * 设置 paytmid
     *
     * @param paytmid paytmid
     */
    public void setPaytmid(String paytmid) {
        this.paytmid = paytmid;
    }

    /**
     * paytmkey
     *
     * @return paytmkey
     */
    public String getPaytmkey() {
        return paytmkey;
    }

    /**
     * 设置 paytmkey
     *
     * @param paytmkey paytmkey
     */
    public void setPaytmkey(String paytmkey) {
        this.paytmkey = paytmkey;
    }

    /**
     * paytmmd5
     *
     * @return paytmmd5
     */
    public String getPaytmmd5() {
        return paytmmd5;
    }

    /**
     * 设置 paytmmd5
     *
     * @param paytmmd5 paytmmd5
     */
    public void setPaytmmd5(String paytmmd5) {
        this.paytmmd5 = paytmmd5;
    }

    /**
     * paytmuid
     *
     * @return paytmuid
     */
    public String getPaytmuid() {
        return paytmuid;
    }

    /**
     * 设置 paytmuid
     *
     * @param paytmuid paytmuid
     */
    public void setPaytmuid(String paytmuid) {
        this.paytmuid = paytmuid;
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
     * clientId
     *
     * @return clientId
     */
    public Long getClientId() {
        return clientId;
    }

    /**
     * 设置 clientId
     *
     * @param clientId clientId
     */
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    /**
     * clientNo
     *
     * @return clientNo
     */
    public String getClientNo() {
        return clientNo;
    }

    /**
     * 设置 clientNo
     *
     * @param clientNo clientNo
     */
    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    /**
     * clientName
     *
     * @return clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 设置 clientName
     *
     * @param clientName clientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * channelTypeId
     *
     * @return channelTypeId
     */
    public Integer getChannelTypeId() {
        return channelTypeId;
    }

    /**
     * 设置 channelTypeId
     *
     * @param channelTypeId channelTypeId
     */
    public void setChannelTypeId(Integer channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    /**
     * params
     *
     * @return params
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置 params
     *
     * @param params params
     */
    public void setParams(String params) {
        this.params = params;
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
     * startTime
     *
     * @return startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 设置 startTime
     *
     * @param startTime startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * endTime
     *
     * @return endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 设置 endTime
     *
     * @param endTime endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * minMoney
     *
     * @return minMoney
     */
    public BigDecimal getMinMoney() {
        return minMoney;
    }

    /**
     * 设置 minMoney
     *
     * @param minMoney minMoney
     */
    public void setMinMoney(BigDecimal minMoney) {
        this.minMoney = minMoney;
    }

    /**
     * maxMoney
     *
     * @return maxMoney
     */
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }

    /**
     * 设置 maxMoney
     *
     * @param maxMoney maxMoney
     */
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }

    /**
     * money
     *
     * @return money
     */
    public String getMoney() {
        return money;
    }

    /**
     * 设置 money
     *
     * @param money money
     */
    public void setMoney(String money) {
        this.money = money;
    }

    /**
     * ratio
     *
     * @return ratio
     */
    public BigDecimal getRatio() {
        return ratio;
    }

    /**
     * 设置 ratio
     *
     * @param ratio ratio
     */
    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
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
     * dayMoney
     *
     * @return dayMoney
     */
    public BigDecimal getDayMoney() {
        return dayMoney;
    }

    /**
     * 设置 dayMoney
     *
     * @param dayMoney dayMoney
     */
    public void setDayMoney(BigDecimal dayMoney) {
        this.dayMoney = dayMoney;
    }

    /**
     * jrcg
     *
     * @return jrcg
     */
    public BigDecimal getJrcg() {
        return jrcg;
    }

    /**
     * 设置 jrcg
     *
     * @param jrcg jrcg
     */
    public void setJrcg(BigDecimal jrcg) {
        this.jrcg = jrcg;
    }

    /**
     * jrdds
     *
     * @return jrdds
     */
    public BigDecimal getJrdds() {
        return jrdds;
    }

    /**
     * 设置 jrdds
     *
     * @param jrdds jrdds
     */
    public void setJrdds(BigDecimal jrdds) {
        this.jrdds = jrdds;
    }

    /**
     * jrcgl
     *
     * @return jrcgl
     */
    public BigDecimal getJrcgl() {
        return jrcgl;
    }

    /**
     * 设置 jrcgl
     *
     * @param jrcgl jrcgl
     */
    public void setJrcgl(BigDecimal jrcgl) {
        this.jrcgl = jrcgl;
    }

    /**
     * lscg
     *
     * @return lscg
     */
    public BigDecimal getLscg() {
        return lscg;
    }

    /**
     * 设置 lscg
     *
     * @param lscg lscg
     */
    public void setLscg(BigDecimal lscg) {
        this.lscg = lscg;
    }

    /**
     * lscgl
     *
     * @return lscgl
     */
    public BigDecimal getLscgl() {
        return lscgl;
    }

    /**
     * 设置 lscgl
     *
     * @param lscgl lscgl
     */
    public void setLscgl(BigDecimal lscgl) {
        this.lscgl = lscgl;
    }

    /**
     * mtype
     *
     * @return mtype
     */
    public Integer getMtype() {
        return mtype;
    }

    /**
     * 设置 mtype
     *
     * @param mtype mtype
     */
    public void setMtype(Integer mtype) {
        this.mtype = mtype;
    }

    /**
     * keyname
     *
     * @return keyname
     */
    public String getKeyname() {
        return keyname;
    }

    /**
     * 设置 keyname
     *
     * @param keyname keyname
     */
    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    /**
     * gtype
     *
     * @return gtype
     */
    public Integer getGtype() {
        return gtype;
    }

    /**
     * 设置 gtype
     *
     * @param gtype gtype
     */
    public void setGtype(Integer gtype) {
        this.gtype = gtype;
    }


}
