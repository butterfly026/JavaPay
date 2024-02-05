package com.city.city_collector.admin.system.entity;

import java.math.BigDecimal;
import java.util.Date;


public class SysUser {

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
     * 账号
     */
    private String username;

    /**
     * 昵称2
     */
    private String nickname;

    /**
     * 昵称
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机
     */
    private String phone;
    /**
     * 上次登录日期
     */
    private Date lastLoginDate;
    /**
     * 状态（0、禁用 1、可用）
     */
    private String status;
    /**
     * 用户最后发起登录请求的时间
     */
    private Date loginReqDate;
    /**
     * 用户版本号
     */
    private Integer version;

    /**
     * 描述
     **/
    private String descript;

    private BigDecimal money;

    private BigDecimal frozenMoney;

    private Long proxyId;

    private String proxyNo;

    private String proxyName;

    private BigDecimal quota;

    private BigDecimal totalQuota;

    private BigDecimal cashCommission;
    private BigDecimal cashRatio;
    private BigDecimal minCommission;

    private Integer type;

    private BigDecimal jrcg;
    private BigDecimal jrcgl;
    private BigDecimal lscg;
    private BigDecimal lscgl;
    private BigDecimal xdl;

    private Integer api;

    private String merchantNo;
    private String merchantMy;
    private String merchantCode;
    private String merchantIp;
    private String merchantGy;
    private String sy;

    private String url;

    private String payword;

    private Integer gotype;

    private BigDecimal amount;

    private String urlpay;

    private String urlcash;

    private String adminip;

    private String apiip;

    private String paykey;

    private String paykeyname;

    /**
     * 谷歌验证器秘钥
     */
    private String validCode;

    /**
     * 是否开启谷歌验证
     */
    private Integer validStatus;

    private Integer cashMode;

    private BigDecimal ptmcashCommission;
    private Integer ptmcashMode;

    private BigDecimal usdtcashCommission;
    private Integer usdtcashMode;

    private BigDecimal musdtcashCommission;
    private BigDecimal mptmcashCommission;
    private BigDecimal mcashCommission;

    /**
     * 查单地址
     */
    private String urlquery;

    /**
     * 下发配置
     */
    private String cashConfig;

    /**
     * 代付最低金额
     */
    private BigDecimal dfminCommission;

    /**
     * 下发配置描述字符串
     */
    private String cashConfigstr;

    // 累计金额
    private BigDecimal totalmoney;

    private BigDecimal yajin;

    public BigDecimal getYajin() {
        return yajin;
    }

    public void setYajin(BigDecimal yajin) {
        this.yajin = yajin;
    }

    /**
     * totalmoney
     *
     * @return totalmoney
     */
    public BigDecimal getTotalmoney() {
        return totalmoney;
    }

    /**
     * 设置 totalmoney
     *
     * @param totalmoney totalmoney
     */
    public void setTotalmoney(BigDecimal totalmoney) {
        this.totalmoney = totalmoney;
    }

    /**
     * cashConfigstr
     *
     * @return cashConfigstr
     */
    public String getCashConfigstr() {
        return cashConfigstr;
    }

    /**
     * 设置 cashConfigstr
     *
     * @param cashConfigstr cashConfigstr
     */
    public void setCashConfigstr(String cashConfigstr) {
        this.cashConfigstr = cashConfigstr;
    }

    /**
     * dfminCommission
     *
     * @return dfminCommission
     */
    public BigDecimal getDfminCommission() {
        return dfminCommission;
    }

    /**
     * 设置 dfminCommission
     *
     * @param dfminCommission dfminCommission
     */
    public void setDfminCommission(BigDecimal dfminCommission) {
        this.dfminCommission = dfminCommission;
    }

    /**
     * cashConfig
     *
     * @return cashConfig
     */
    public String getCashConfig() {
        return cashConfig;
    }

    /**
     * 设置 cashConfig
     *
     * @param cashConfig cashConfig
     */
    public void setCashConfig(String cashConfig) {
        this.cashConfig = cashConfig;
    }

    /**
     * urlquery
     *
     * @return urlquery
     */
    public String getUrlquery() {
        return urlquery;
    }

    /**
     * 设置 urlquery
     *
     * @param urlquery urlquery
     */
    public void setUrlquery(String urlquery) {
        this.urlquery = urlquery;
    }

    /**
     * musdtcashCommission
     *
     * @return musdtcashCommission
     */
    public BigDecimal getMusdtcashCommission() {
        return musdtcashCommission;
    }

    /**
     * 设置 musdtcashCommission
     *
     * @param musdtcashCommission musdtcashCommission
     */
    public void setMusdtcashCommission(BigDecimal musdtcashCommission) {
        this.musdtcashCommission = musdtcashCommission;
    }

    /**
     * mptmcashCommission
     *
     * @return mptmcashCommission
     */
    public BigDecimal getMptmcashCommission() {
        return mptmcashCommission;
    }

    /**
     * 设置 mptmcashCommission
     *
     * @param mptmcashCommission mptmcashCommission
     */
    public void setMptmcashCommission(BigDecimal mptmcashCommission) {
        this.mptmcashCommission = mptmcashCommission;
    }

    /**
     * mcashCommission
     *
     * @return mcashCommission
     */
    public BigDecimal getMcashCommission() {
        return mcashCommission;
    }

    /**
     * 设置 mcashCommission
     *
     * @param mcashCommission mcashCommission
     */
    public void setMcashCommission(BigDecimal mcashCommission) {
        this.mcashCommission = mcashCommission;
    }

    /**
     * cashMode
     *
     * @return cashMode
     */
    public Integer getCashMode() {
        return cashMode;
    }

    /**
     * 设置 cashMode
     *
     * @param cashMode cashMode
     */
    public void setCashMode(Integer cashMode) {
        this.cashMode = cashMode;
    }

    /**
     * ptmcashCommission
     *
     * @return ptmcashCommission
     */
    public BigDecimal getPtmcashCommission() {
        return ptmcashCommission;
    }

    /**
     * 设置 ptmcashCommission
     *
     * @param ptmcashCommission ptmcashCommission
     */
    public void setPtmcashCommission(BigDecimal ptmcashCommission) {
        this.ptmcashCommission = ptmcashCommission;
    }

    /**
     * ptmcashMode
     *
     * @return ptmcashMode
     */
    public Integer getPtmcashMode() {
        return ptmcashMode;
    }

    /**
     * 设置 ptmcashMode
     *
     * @param ptmcashMode ptmcashMode
     */
    public void setPtmcashMode(Integer ptmcashMode) {
        this.ptmcashMode = ptmcashMode;
    }

    /**
     * usdtcashCommission
     *
     * @return usdtcashCommission
     */
    public BigDecimal getUsdtcashCommission() {
        return usdtcashCommission;
    }

    /**
     * 设置 usdtcashCommission
     *
     * @param usdtcashCommission usdtcashCommission
     */
    public void setUsdtcashCommission(BigDecimal usdtcashCommission) {
        this.usdtcashCommission = usdtcashCommission;
    }

    /**
     * usdtcashMode
     *
     * @return usdtcashMode
     */
    public Integer getUsdtcashMode() {
        return usdtcashMode;
    }

    /**
     * 设置 usdtcashMode
     *
     * @param usdtcashMode usdtcashMode
     */
    public void setUsdtcashMode(Integer usdtcashMode) {
        this.usdtcashMode = usdtcashMode;
    }

    /**
     * paykey
     *
     * @return paykey
     */
    public String getPaykey() {
        return paykey;
    }

    /**
     * 设置 paykey
     *
     * @param paykey paykey
     */
    public void setPaykey(String paykey) {
        this.paykey = paykey;
    }

    /**
     * paykeyname
     *
     * @return paykeyname
     */
    public String getPaykeyname() {
        return paykeyname;
    }

    /**
     * 设置 paykeyname
     *
     * @param paykeyname paykeyname
     */
    public void setPaykeyname(String paykeyname) {
        this.paykeyname = paykeyname;
    }

    /**
     * adminip
     *
     * @return adminip
     */
    public String getAdminip() {
        return adminip;
    }

    /**
     * 设置 adminip
     *
     * @param adminip adminip
     */
    public void setAdminip(String adminip) {
        this.adminip = adminip;
    }

    /**
     * apiip
     *
     * @return apiip
     */
    public String getApiip() {
        return apiip;
    }

    /**
     * 设置 apiip
     *
     * @param apiip apiip
     */
    public void setApiip(String apiip) {
        this.apiip = apiip;
    }

    /**
     * urlpay
     *
     * @return urlpay
     */
    public String getUrlpay() {
        return urlpay;
    }

    /**
     * 设置 urlpay
     *
     * @param urlpay urlpay
     */
    public void setUrlpay(String urlpay) {
        this.urlpay = urlpay;
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
     * 获取账号
     *
     * @return username 账号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置账号
     *
     * @param username 账号
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取昵称2
     *
     * @return nickname 昵称2
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称2
     *
     * @param nickname 昵称2
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    /**
     * 获取昵称
     *
     * @return name 昵称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置昵称
     *
     * @param name 昵称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取密码
     *
     * @return password 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取手机
     *
     * @return phone 手机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机
     *
     * @param phone 手机
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取上次登录日期
     *
     * @return lastLoginDate 上次登录日期
     */
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * 设置上次登录日期
     *
     * @param lastLoginDate 上次登录日期
     */
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    /**
     * 获取状态（0、禁用1、可用）
     *
     * @return status 状态（0、禁用1、可用）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态（0、禁用1、可用）
     *
     * @param status 状态（0、禁用1、可用）
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取用户版本号
     *
     * @return version 用户版本号
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置用户版本号
     *
     * @param version 用户版本号
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 获取用户最后发起登录请求的时间
     *
     * @return loginReqDate 用户最后发起登录请求的时间
     */
    public Date getLoginReqDate() {
        return loginReqDate;
    }

    /**
     * 设置用户最后发起登录请求的时间
     *
     * @param loginReqDate 用户最后发起登录请求的时间
     */
    public void setLoginReqDate(Date loginReqDate) {
        this.loginReqDate = loginReqDate;
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
     * frozenMoney
     *
     * @return frozenMoney
     */
    public BigDecimal getFrozenMoney() {
        return frozenMoney;
    }

    /**
     * 设置 frozenMoney
     *
     * @param frozenMoney frozenMoney
     */
    public void setFrozenMoney(BigDecimal frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    /**
     * proxyId
     *
     * @return proxyId
     */
    public Long getProxyId() {
        return proxyId;
    }

    /**
     * 设置 proxyId
     *
     * @param proxyId proxyId
     */
    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    /**
     * proxyNo
     *
     * @return proxyNo
     */
    public String getProxyNo() {
        return proxyNo;
    }

    /**
     * 设置 proxyNo
     *
     * @param proxyNo proxyNo
     */
    public void setProxyNo(String proxyNo) {
        this.proxyNo = proxyNo;
    }

    /**
     * proxyName
     *
     * @return proxyName
     */
    public String getProxyName() {
        return proxyName;
    }

    /**
     * 设置 proxyName
     *
     * @param proxyName proxyName
     */
    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    /**
     * quota
     *
     * @return quota
     */
    public BigDecimal getQuota() {
        return quota;
    }

    /**
     * 设置 quota
     *
     * @param quota quota
     */
    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    /**
     * totalQuota
     *
     * @return totalQuota
     */
    public BigDecimal getTotalQuota() {
        return totalQuota;
    }

    /**
     * 设置 totalQuota
     *
     * @param totalQuota totalQuota
     */
    public void setTotalQuota(BigDecimal totalQuota) {
        this.totalQuota = totalQuota;
    }

    /**
     * cashCommission
     *
     * @return cashCommission
     */
    public BigDecimal getCashCommission() {
        return cashCommission;
    }

    /**
     * 设置 cashCommission
     *
     * @param cashCommission cashCommission
     */
    public void setCashCommission(BigDecimal cashCommission) {
        this.cashCommission = cashCommission;
    }

    /**
     * cashRatio
     *
     * @return cashRatio
     */
    public BigDecimal getCashRatio() {
        return cashRatio;
    }

    /**
     * 设置 cashRatio
     *
     * @param cashRatio cashRatio
     */
    public void setCashRatio(BigDecimal cashRatio) {
        this.cashRatio = cashRatio;
    }

    /**
     * minCommission
     *
     * @return minCommission
     */
    public BigDecimal getMinCommission() {
        return minCommission;
    }

    /**
     * 设置 minCommission
     *
     * @param minCommission minCommission
     */
    public void setMinCommission(BigDecimal minCommission) {
        this.minCommission = minCommission;
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
     * xdl
     *
     * @return xdl
     */
    public BigDecimal getXdl() {
        return xdl;
    }

    /**
     * 设置 xdl
     *
     * @param xdl xdl
     */
    public void setXdl(BigDecimal xdl) {
        this.xdl = xdl;
    }

    /**
     * api
     *
     * @return api
     */
    public Integer getApi() {
        return api;
    }

    /**
     * 设置 api
     *
     * @param api api
     */
    public void setApi(Integer api) {
        this.api = api;
    }

    /**
     * merchantNo
     *
     * @return merchantNo
     */
    public String getMerchantNo() {
        return merchantNo;
    }

    /**
     * 设置 merchantNo
     *
     * @param merchantNo merchantNo
     */
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    /**
     * merchantMy
     *
     * @return merchantMy
     */
    public String getMerchantMy() {
        return merchantMy;
    }

    /**
     * 设置 merchantMy
     *
     * @param merchantMy merchantMy
     */
    public void setMerchantMy(String merchantMy) {
        this.merchantMy = merchantMy;
    }

    /**
     * merchantCode
     *
     * @return merchantCode
     */
    public String getMerchantCode() {
        return merchantCode;
    }

    /**
     * 设置 merchantCode
     *
     * @param merchantCode merchantCode
     */
    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    /**
     * merchantIp
     *
     * @return merchantIp
     */
    public String getMerchantIp() {
        return merchantIp;
    }

    /**
     * 设置 merchantIp
     *
     * @param merchantIp merchantIp
     */
    public void setMerchantIp(String merchantIp) {
        this.merchantIp = merchantIp;
    }

    /**
     * merchantGy
     *
     * @return merchantGy
     */
    public String getMerchantGy() {
        return merchantGy;
    }

    /**
     * 设置 merchantGy
     *
     * @param merchantGy merchantGy
     */
    public void setMerchantGy(String merchantGy) {
        this.merchantGy = merchantGy;
    }

    /**
     * sy
     *
     * @return sy
     */
    public String getSy() {
        return sy;
    }

    /**
     * 设置 sy
     *
     * @param sy sy
     */
    public void setSy(String sy) {
        this.sy = sy;
    }

    /**
     * url
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置 url
     *
     * @param url url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * payword
     *
     * @return payword
     */
    public String getPayword() {
        return payword;
    }

    /**
     * 设置 payword
     *
     * @param payword payword
     */
    public void setPayword(String payword) {
        this.payword = payword;
    }

    /**
     * gotype
     *
     * @return gotype
     */
    public Integer getGotype() {
        return gotype;
    }

    /**
     * 设置 gotype
     *
     * @param gotype gotype
     */
    public void setGotype(Integer gotype) {
        this.gotype = gotype;
    }

    /**
     * amount
     *
     * @return amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置 amount
     *
     * @param amount amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * urlcash
     *
     * @return urlcash
     */
    public String getUrlcash() {
        return urlcash;
    }

    /**
     * 设置 urlcash
     *
     * @param urlcash urlcash
     */
    public void setUrlcash(String urlcash) {
        this.urlcash = urlcash;
    }

    /**
     * validCode
     *
     * @return validCode
     */
    public String getValidCode() {
        return validCode;
    }

    /**
     * 设置 validCode
     *
     * @param validCode validCode
     */
    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    /**
     * validStatus
     *
     * @return validStatus
     */
    public Integer getValidStatus() {
        return validStatus;
    }

    /**
     * 设置 validStatus
     *
     * @param validStatus validStatus
     */
    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }
}
