package com.city.city_collector.admin.city.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:栏目
 *
 * @author BG
 * @since 2020-11-12
 */
public class Config {
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
     * 网站名
     */
    private String cname;
    /**
     * 访问设置
     */
    private String creq;
    /**
     * 后台访问设置
     */
    private String cadmin;
    /**
     * 页脚设置
     */
    private String cfoot;

    /**
     * logo
     */
    private String logo;

    /**
     * 货币单位
     */
    private String currency;

    /**
     * 提现模式
     */
    private Integer cashMode;

    /**
     * 回调IP
     **/
    private String nip;

    /**
     * 域名
     **/
    private String domain;

    /**
     * API域名
     **/
    private String apidomain;

    /**
     * 订单数
     */
    private Integer ordercount;

    /**
     * 通道关键词
     */
    private String channelkeyword;

    /**
     * 订单金额
     */
    private String ordermoney;

    /**
     * 首页模块
     */
    private Integer indexmodel;

    /**
     * 开启订单验证
     */
    private Integer ordervalid;
    /**
     * 开启平台优先 kahn
     */
    private Integer platformvalid;
    /**
     * 订单前缀
     */
    private String snpre;

    /**
     * 下发订单前缀
     */
    private String cashsnpre;

    /**
     * 传给上游的回调域名
     **/
    private String notifyurl;

    /**
     * 传给系统回调服务器的地址
     **/
    private String paynotifyurl;

    private String payClientUrl;   
    public String getPayClientUrl() {
        return payClientUrl;
    }

    public void setPayClientUrl(String payClientUrl) {
        this.payClientUrl = payClientUrl;
    }

    //cyber修改 高速订单通知队列
    private String queueUrl;

    public String getQueueUrl() {
        return queueUrl;
    }

    public void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    //cyber修改 高速订单通知队列
    private String proxyInfoBlock;

    public String getProxyInfoBlock() {
        return proxyInfoBlock;
    }

    public void setProxyInfoBlock(String proxyInfoBlock) {
        this.proxyInfoBlock = proxyInfoBlock;
    }
    //cyber修改 高速订单通知队列
    private String notifyUrl2;

    public String getnotifyUrl2() {
        return notifyUrl2;
    }

    public void setnotifyUrl2(String notifyUrl2) {
        this.notifyUrl2 = notifyUrl2;
    }  
    /**
     * 是否开启系统代理
     */
    private Integer proxyopen;
    /**
     * 代理费率
     */
    private BigDecimal proxyratio;

    /**
     * 多少秒之前
     */
    private Integer beforetime;
    /**
     * 统计多长时间
     */
    private Integer staticstime;

    /**
     * 服务器模式，0：多台，1：单台
     */
    private Integer servermodel;


    /**
     * 虚拟代理id
     **/
    private Long xnproxy;
    /**
     * 虚拟商户id
     **/
    private Long xnmc;
    /**
     * 虚拟商户号
     **/
    private String xnmcno;
    /**
     * 虚拟商户号列表
     **/
    private String xnmclist;
    /**
     * 商户支付宝通道id
     */
    private Long zfbcc;
    /**
     * 商户微信通道id
     */
    private Long wxcc;
    /**
     * 订单系数
     */
    private Long odxs;
    /**
     * 订单最小配额
     */
    private Long odmin;

    /**
     * 自动代付开关 0 关闭 1开启
     */
    private Integer autoDaiFu;

    private Integer bd;

    /**
     * 指定商户ID: 管理员方便操作使用
     */
    private Long selfMerchant;

    public Long getSelfMerchant() {
        return selfMerchant;
    }

    public void setSelfMerchant(Long selfMerchant) {
        this.selfMerchant = selfMerchant;
    }

    /**
     * xnproxy
     *
     * @return xnproxy
     */
    public Long getXnproxy() {
        return xnproxy;
    }

    /**
     * 设置 xnproxy
     *
     * @param xnproxy xnproxy
     */
    public void setXnproxy(Long xnproxy) {
        this.xnproxy = xnproxy;
    }

    /**
     * xnmc
     *
     * @return xnmc
     */
    public Long getXnmc() {
        return xnmc;
    }

    /**
     * 设置 xnmc
     *
     * @param xnmc xnmc
     */
    public void setXnmc(Long xnmc) {
        this.xnmc = xnmc;
    }

    /**
     * xnmcno
     *
     * @return xnmcno
     */
    public String getXnmcno() {
        return xnmcno;
    }

    /**
     * 设置 xnmcno
     *
     * @param xnmcno xnmcno
     */
    public void setXnmcno(String xnmcno) {
        this.xnmcno = xnmcno;
    }

    /**
     * xnmclist
     *
     * @return xnmclist
     */
    public String getXnmclist() {
        return xnmclist;
    }

    /**
     * 设置 xnmclist
     *
     * @param xnmclist xnmclist
     */
    public void setXnmclist(String xnmclist) {
        this.xnmclist = xnmclist;
    }

    /**
     * zfbcc
     *
     * @return zfbcc
     */
    public Long getZfbcc() {
        return zfbcc;
    }

    /**
     * 设置 zfbcc
     *
     * @param zfbcc zfbcc
     */
    public void setZfbcc(Long zfbcc) {
        this.zfbcc = zfbcc;
    }

    /**
     * wxcc
     *
     * @return wxcc
     */
    public Long getWxcc() {
        return wxcc;
    }

    /**
     * 设置 wxcc
     *
     * @param wxcc wxcc
     */
    public void setWxcc(Long wxcc) {
        this.wxcc = wxcc;
    }

    /**
     * odxs
     *
     * @return odxs
     */
    public Long getOdxs() {
        return odxs;
    }

    /**
     * 设置 odxs
     *
     * @param odxs odxs
     */
    public void setOdxs(Long odxs) {
        this.odxs = odxs;
    }

    /**
     * odmin
     *
     * @return odmin
     */
    public Long getOdmin() {
        return odmin;
    }

    /**
     * 设置 odmin
     *
     * @param odmin odmin
     */
    public void setOdmin(Long odmin) {
        this.odmin = odmin;
    }

    /**
     * servermodel
     *
     * @return servermodel
     */
    public Integer getServermodel() {
        return servermodel == null ? 0 : servermodel;
    }

    /**
     * 设置 servermodel
     *
     * @param servermodel servermodel
     */
    public void setServermodel(Integer servermodel) {
        this.servermodel = servermodel;
    }

    /**
     * beforetime
     *
     * @return beforetime
     */
    public Integer getBeforetime() {
        return beforetime == null ? 0 : beforetime;
    }

    /**
     * 设置 beforetime
     *
     * @param beforetime beforetime
     */
    public void setBeforetime(Integer beforetime) {
        this.beforetime = beforetime;
    }

    /**
     * staticstime
     *
     * @return staticstime
     */
    public Integer getStaticstime() {
        return staticstime == null ? 0 : staticstime;
    }

    /**
     * 设置 staticstime
     *
     * @param staticstime staticstime
     */
    public void setStaticstime(Integer staticstime) {
        this.staticstime = staticstime;
    }

    /**
     * proxyopen
     *
     * @return proxyopen
     */
    public Integer getProxyopen() {
        return proxyopen == null ? 0 : proxyopen;
    }

    /**
     * 设置 proxyopen
     *
     * @param proxyopen proxyopen
     */
    public void setProxyopen(Integer proxyopen) {
        this.proxyopen = proxyopen;
    }

    /**
     * proxyratio
     *
     * @return proxyratio
     */
    public BigDecimal getProxyratio() {
        return proxyratio == null ? BigDecimal.ZERO : proxyratio;
    }

    /**
     * 设置 proxyratio
     *
     * @param proxyratio proxyratio
     */
    public void setProxyratio(BigDecimal proxyratio) {
        this.proxyratio = proxyratio;
    }

    /**
     * notifyurl
     *
     * @return notifyurl
     */
    public String getNotifyurl() {
        return notifyurl;
    }

    /**
     * 设置 notifyurl
     *
     * @param notifyurl notifyurl
     */
    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl;
    }

    /**
     * paynotifyurl
     *
     * @return paynotifyurl
     */
    public String getPaynotifyurl() {
        return paynotifyurl;
    }

    /**
     * 设置 paynotifyurl
     *
     * @param paynotifyurl paynotifyurl
     */
    public void setPaynotifyurl(String paynotifyurl) {
        this.paynotifyurl = paynotifyurl;
    }

    /**
     * snpre
     *
     * @return snpre
     */
    public String getSnpre() {
        return snpre == null ? "" : snpre;
    }

    /**
     * 设置 snpre
     *
     * @param snpre snpre
     */
    public void setSnpre(String snpre) {
        this.snpre = snpre;
    }

    /**
     * cashsnpre
     *
     * @return cashsnpre
     */
    public String getCashsnpre() {
        return cashsnpre == null ? "CA" : cashsnpre;
    }

    /**
     * 设置 cashsnpre
     *
     * @param cashsnpre cashsnpre
     */
    public void setCashsnpre(String cashsnpre) {
        this.cashsnpre = cashsnpre;
    }
    /**
     * platformvalid
     *
     * @return platformvalid
     */
    public Integer getPlatformvalid() {
        return platformvalid == null ? 0 : platformvalid;
    }

    /**
     * 设置 platformvalid
     *
     * @param platformvalid platformvalid
     */
    public void setPlatformvalid(Integer platformvalid) {
        this.platformvalid = platformvalid;
    }

    /**
     * ordervalid
     *
     * @return ordervalid
     */
    public Integer getOrdervalid() {
        return ordervalid == null ? 0 : ordervalid;
    }

    /**
     * 设置 ordervalid
     *
     * @param ordervalid ordervalid
     */
    public void setOrdervalid(Integer ordervalid) {
        this.ordervalid = ordervalid;
    }

    /**
     * indexmodel
     *
     * @return indexmodel
     */
    public Integer getIndexmodel() {
        return indexmodel == null ? 0 : indexmodel;
    }

    /**
     * 设置 indexmodel
     *
     * @param indexmodel indexmodel
     */
    public void setIndexmodel(Integer indexmodel) {
        this.indexmodel = indexmodel;
    }

    /**
     * ordercount
     *
     * @return ordercount
     */
    public Integer getOrdercount() {
        return ordercount;
    }

    /**
     * 设置 ordercount
     *
     * @param ordercount ordercount
     */
    public void setOrdercount(Integer ordercount) {
        this.ordercount = ordercount;
    }

    /**
     * channelkeyword
     *
     * @return channelkeyword
     */
    public String getChannelkeyword() {
        return channelkeyword;
    }

    /**
     * 设置 channelkeyword
     *
     * @param channelkeyword channelkeyword
     */
    public void setChannelkeyword(String channelkeyword) {
        this.channelkeyword = channelkeyword;
    }

    /**
     * ordermoney
     *
     * @return ordermoney
     */
    public String getOrdermoney() {
        return ordermoney;
    }

    /**
     * 设置 ordermoney
     *
     * @param ordermoney ordermoney
     */
    public void setOrdermoney(String ordermoney) {
        this.ordermoney = ordermoney;
    }

    /**
     * apidomain
     *
     * @return apidomain
     */
    public String getApidomain() {
        return apidomain;
    }

    /**
     * 设置 apidomain
     *
     * @param apidomain apidomain
     */
    public void setApidomain(String apidomain) {
        this.apidomain = apidomain;
    }

    /**
     * nip
     *
     * @return nip
     */
    public String getNip() {
        return nip;
    }

    /**
     * 设置 nip
     *
     * @param nip nip
     */
    public void setNip(String nip) {
        this.nip = nip;
    }

    /**
     * domain
     *
     * @return domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * 设置 domain
     *
     * @param domain domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * cadmin
     *
     * @return cadmin
     */
    public String getCadmin() {
        return cadmin;
    }

    /**
     * 设置 cadmin
     *
     * @param cadmin cadmin
     */
    public void setCadmin(String cadmin) {
        this.cadmin = cadmin;
    }

    /**
     * cname
     *
     * @return cname
     */
    public String getCname() {
        return cname;
    }

    /**
     * 设置 cname
     *
     * @param cname cname
     */
    public void setCname(String cname) {
        this.cname = cname;
    }

    /**
     * creq
     *
     * @return creq
     */
    public String getCreq() {
        return creq;
    }

    /**
     * 设置 creq
     *
     * @param creq creq
     */
    public void setCreq(String creq) {
        this.creq = creq;
    }

    /**
     * cfoot
     *
     * @return cfoot
     */
    public String getCfoot() {
        return cfoot;
    }

    /**
     * 设置 cfoot
     *
     * @param cfoot cfoot
     */
    public void setCfoot(String cfoot) {
        this.cfoot = cfoot;
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
     * logo
     *
     * @return logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 设置 logo
     *
     * @param logo logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * currency
     *
     * @return currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 设置 currency
     *
     * @param currency currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * cashMode
     *
     * @return cashMode
     */
    public Integer getCashMode() {
        return cashMode == null ? 0 : cashMode;
    }

    /**
     * 设置 cashMode
     *
     * @param cashMode cashMode
     */
    public void setCashMode(Integer cashMode) {
        this.cashMode = cashMode;
    }

    public Integer getAutoDaiFu() {
        return autoDaiFu;
    }

    public void setBd(Integer bd) {
        this.bd = bd;
    }

    public Integer getBd() {
        return bd;
    }

    public void setAutoDaiFu(Integer autoDaiFu) {
        this.autoDaiFu = autoDaiFu;
    }
}
