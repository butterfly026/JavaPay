package com.city.city_collector.admin.pay.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.common.util.GoogleGenerator;
import com.city.city_collector.common.util.MD5Util;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.util.PayExcelexportDataService;
import com.city.city_collector.common.util.PayExcelportThread;
import com.city.city_collector.common.util.ZxingUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qqqq.excleport.Poi4ExcelExportManager;
import com.qqqq.excleport.common.Constants;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayMerchant;
import com.city.city_collector.admin.pay.entity.PayProxy;
import com.city.city_collector.admin.pay.service.PayExcelexportService;
import com.city.city_collector.admin.pay.service.PayMerchantService;
import com.city.city_collector.admin.pay.service.PayProxyService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.bean.CashConfig;
import com.city.city_collector.channel.bean.PayRatio;

/**
 * Description:商户管理-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payMerchant")
public class PayMerchantController {
    @Autowired
    PayMerchantService payMerchantService;
    @Autowired
    PayProxyService payProxyService;

    @Autowired
    SysUserService sysUserService;

    @Value("${static.folder}")
    private String staticFolder;


    @Autowired
    PayExcelexportService payExcelexportService;

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:paymerchant:list"})
    @RequestMapping("/view")
    public String view(Model model) {
        model.addAttribute("proxyList", payProxyService.querySelectList());
        model.addAttribute("merchantList", payMerchantService.querySelectList());
        return "admin/pay/paymerchant/list";
    }

    /**
     * Description:分页查询
     *
     * @param map      参数组
     * @param pageNo   当前页码
     * @param pageSize 每页记录数，为null使用默认值20
     * @param orders   排序列表
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchant:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payMerchantService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }

    /**
     * Description:打开新增页面
     *
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchant:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("proxyList", payProxyService.querySelectList());
        return "admin/pay/paymerchant/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payMerchant
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchant:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(SysUser sysUser
            , BigDecimal commission, BigDecimal ratioCommission, BigDecimal proxyCommission, BigDecimal proxyRatioCommission
            , BigDecimal upicommission, BigDecimal upiratioCommission, BigDecimal upiproxyCommission, BigDecimal upiproxyRatioCommission
            , BigDecimal usdtcommission, BigDecimal usdtratioCommission, BigDecimal usdtproxyCommission, BigDecimal usdtproxyRatioCommission
            , BigDecimal dfcommission, BigDecimal dfratioCommission, BigDecimal dfproxyCommission, BigDecimal dfproxyRatioCommission
            , BigDecimal dfupicommission, BigDecimal dfupiratioCommission, BigDecimal dfupiproxyCommission, BigDecimal dfupiproxyRatioCommission
            , BigDecimal dfusdtcommission, BigDecimal dfusdtratioCommission, BigDecimal dfusdtproxyCommission, BigDecimal dfusdtproxyRatioCommission
    ) {
        //数据验证
        if (sysUser == null) {
            return Message.error("参数错误");
        }

        if (StringUtils.isBlank(sysUser.getUsername())) {
            return Message.error("账号不能为空");
        }
        if (StringUtils.isBlank(sysUser.getName())) {
            return Message.error("商户名不能为空");
        }

        try {
            CashConfig cashConfig = new CashConfig();
            StringBuffer sbuf = new StringBuffer("下发规则：");
            PayRatio pr = new PayRatio(commission, ratioCommission, proxyCommission, proxyRatioCommission);
            cashConfig.setBankRatio(pr);
            sbuf.append("银行卡[商户手续费:下发金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:下发金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            pr = new PayRatio(upicommission, upiratioCommission, upiproxyCommission, upiproxyRatioCommission);
            cashConfig.setUpiRatio(pr);
            sbuf.append(" ,UPI[商户手续费:下发金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:下发金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            pr = new PayRatio(usdtcommission, usdtratioCommission, usdtproxyCommission, usdtproxyRatioCommission);
            cashConfig.setUsdtRatio(pr);
            sbuf.append(" ,USDT[商户手续费:下发金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:下发金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            sbuf.append(";代付规则：");
            pr = new PayRatio(dfcommission, dfratioCommission, dfproxyCommission, dfproxyRatioCommission);
            cashConfig.setDfbankRatio(pr);
            sbuf.append("银行卡[商户手续费:代付金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:代付金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            pr = new PayRatio(dfupicommission, dfupiratioCommission, dfupiproxyCommission, dfupiproxyRatioCommission);
            cashConfig.setDfupiRatio(pr);
            sbuf.append(" ,UPI[商户手续费:代付金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:代付金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            pr = new PayRatio(dfusdtcommission, dfusdtratioCommission, dfusdtproxyCommission, dfusdtproxyRatioCommission);
            cashConfig.setDfusdtRatio(pr);
            sbuf.append(" ,USDT[商户手续费:代付金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:代付金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
            sysUser.setCashConfig(gson.toJson(cashConfig));
            sysUser.setCashConfigstr(sbuf.toString());

            //判断账号是否已存在
            if (sysUserService.querySysUserByUsername(sysUser.getUsername()) != null) {
                return Message.error("账号已存在！");
            }

            //获取代理数据
            if (sysUser.getProxyId() != null) {
                SysUser u1 = sysUserService.querySysUserById(sysUser.getProxyId());
                sysUser.setProxyNo(u1.getUsername());
                sysUser.setProxyName(u1.getName());
            }

            //设置初始化密码
            String pwd = UUID.randomUUID().toString().replace("-", "").substring(4, 12);

            //初始化数据
            sysUser.setPassword(MD5Util.MD5Encode(pwd, "UTF-8"));
//            sysUser.setStatus("0");
            sysUser.setMoney(BigDecimal.ZERO);
            sysUser.setFrozenMoney(BigDecimal.ZERO);
            sysUser.setQuota(BigDecimal.ZERO);
            sysUser.setTotalQuota(BigDecimal.ZERO);
//            sysUser.setCashCommission(BigDecimal.ZERO);
            sysUser.setCashRatio(BigDecimal.ZERO);
            sysUser.setMerchantMy(UUID.randomUUID().toString().replace("-", ""));
//            sysUser.setMinCommission(BigDecimal.ZERO);
            sysUser.setType(2);

            sysUserService.addSave(sysUser);

            Long[] roleId = new Long[]{3L};
            sysUserService.addSaveUserRole(sysUser.getId(), roleId);

            //更新通道对象信息
            sysUserService.updateChannelData();

            Map<String, String> p = new HashMap<String, String>();
            p.put("msg", "");
            p.put("pwd", pwd);
            p.put("username", sysUser.getUsername());

            return Message.success("操作成功", p);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("操作失败:" + e.getMessage());
        }
    }

    /**
     * Description:打开编辑页面
     *
     * @param id    记录ID
     * @param model model对象
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchant:edit"})
    @RequestMapping("/edit")
    public String edit(Long id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/pay/paymerchant/edit";
        }

        //获取需要修改的记录数据
        SysUser result = sysUserService.querySysUserById(id);

        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {
            String config = result.getCashConfig();
            if (StringUtils.isNotBlank(config)) {
                CashConfig cg = new Gson().fromJson(config, CashConfig.class);
                model.addAttribute("bankRatio", cg.getBankRatio());
                model.addAttribute("upiRatio", cg.getUpiRatio());
                model.addAttribute("usdtRatio", cg.getUsdtRatio());
                model.addAttribute("dfbankRatio", cg.getDfbankRatio());
                model.addAttribute("dfupiRatio", cg.getDfupiRatio());
                model.addAttribute("dfusdtRatio", cg.getDfusdtRatio());
            }
            model.addAttribute("result", result);
            model.addAttribute("proxyList", payProxyService.querySelectList());
        }

        return "admin/pay/paymerchant/edit";
    }

    /**
     * Description:编辑保存
     *
     * @param
     * @param
     * @param id 编辑的记录ID
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchant:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(SysUser sysUser
            , BigDecimal commission, BigDecimal ratioCommission, BigDecimal proxyCommission, BigDecimal proxyRatioCommission
            , BigDecimal upicommission, BigDecimal upiratioCommission, BigDecimal upiproxyCommission, BigDecimal upiproxyRatioCommission
            , BigDecimal usdtcommission, BigDecimal usdtratioCommission, BigDecimal usdtproxyCommission, BigDecimal usdtproxyRatioCommission
            , BigDecimal dfcommission, BigDecimal dfratioCommission, BigDecimal dfproxyCommission, BigDecimal dfproxyRatioCommission
            , BigDecimal dfupicommission, BigDecimal dfupiratioCommission, BigDecimal dfupiproxyCommission, BigDecimal dfupiproxyRatioCommission
            , BigDecimal dfusdtcommission, BigDecimal dfusdtratioCommission, BigDecimal dfusdtproxyCommission, BigDecimal dfusdtproxyRatioCommission
    ) {
        //数据验证
        if (sysUser == null) {
            return Message.error("参数错误");
        }


        try {
            CashConfig cashConfig = new CashConfig();
            StringBuffer sbuf = new StringBuffer("下发规则：");
            PayRatio pr = new PayRatio(commission, ratioCommission, proxyCommission, proxyRatioCommission);
            cashConfig.setBankRatio(pr);
            sbuf.append("银行卡[商户手续费:下发金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:下发金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            pr = new PayRatio(upicommission, upiratioCommission, upiproxyCommission, upiproxyRatioCommission);
            cashConfig.setUpiRatio(pr);
            sbuf.append(" ,UPI[商户手续费:下发金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:下发金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            pr = new PayRatio(usdtcommission, usdtratioCommission, usdtproxyCommission, usdtproxyRatioCommission);
            cashConfig.setUsdtRatio(pr);
            sbuf.append(" ,USDT[商户手续费:下发金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:下发金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            sbuf.append(";代付规则：");
            pr = new PayRatio(dfcommission, dfratioCommission, dfproxyCommission, dfproxyRatioCommission);
            cashConfig.setDfbankRatio(pr);
            sbuf.append("银行卡[商户手续费:代付金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:代付金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            pr = new PayRatio(dfupicommission, dfupiratioCommission, dfupiproxyCommission, dfupiproxyRatioCommission);
            cashConfig.setDfupiRatio(pr);
            sbuf.append(" ,UPI[商户手续费:代付金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:代付金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            pr = new PayRatio(dfusdtcommission, dfusdtratioCommission, dfusdtproxyCommission, dfusdtproxyRatioCommission);
            cashConfig.setDfusdtRatio(pr);
            sbuf.append(" ,USDT[商户手续费:代付金额x" + pr.getRatioCommission() + "%+" + pr.getCommission() + ",代理佣金:代付金额x" + pr.getProxyRatioCommission() + "%+" + pr.getProxyCommission() + "]");

            Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
            sysUser.setCashConfig(gson.toJson(cashConfig));
            sysUser.setCashConfigstr(sbuf.toString());

            //获取代理数据
            if (sysUser.getProxyId() != null) {
                SysUser u1 = sysUserService.querySysUserById(sysUser.getProxyId());
                sysUser.setProxyNo(u1.getUsername());
                sysUser.setProxyName(u1.getName());
            }

            sysUser.setUsername(null);
            payMerchantService.editSave(sysUser);

            //更新通道对象信息
            sysUserService.updateChannelData();
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    /**
     * Description:更新用户状态
     *
     * @param
     * @param
     * @param id 编辑的记录ID
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchant:status"})
    @RequestMapping("/upStatus")
    public @ResponseBody
    Message upStatus(Long id, String status) {
        //数据验证
        if (id == null || status == null) {
            return Message.error("参数错误");
        }


        try {
            SysUser sysUser = new SysUser();
            sysUser.setId(id);
            sysUser.setStatus(status);

            sysUserService.editSave(sysUser);

            //更新通道对象信息
            sysUserService.updateChannelData();

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    /**
     * Description:重置登录密码
     *
     * @param
     * @param
     * @param id 编辑的记录ID
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchant:dlmm"})
    @RequestMapping("/dlmm")
    public @ResponseBody
    Message dlmm(Long id) {
        //数据验证
        if (id == null) {
            return Message.error("参数错误");
        }


        try {
            SysUser sysUser = new SysUser();
            sysUser.setId(id);
            //设置初始化密码
            String pwd = UUID.randomUUID().toString().replace("-", "").substring(4, 12);

            //初始化数据
            sysUser.setPassword(MD5Util.MD5Encode(pwd, "UTF-8"));

            sysUserService.editSave(sysUser);

            Map<String, String> p = new HashMap<String, String>();
            p.put("msg", "");
            p.put("pwd", pwd);

            return Message.success("操作成功", p);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
    }

    /**
     * Description:重置登录密码
     *
     * @param
     * @param
     * @param id 编辑的记录ID
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchant:zfmm"})
    @RequestMapping("/zfmm")
    public @ResponseBody
    Message zfmm(Long id) {
        //数据验证
        if (id == null) {
            return Message.error("参数错误");
        }


        try {
            SysUser sysUser = new SysUser();
            sysUser.setId(id);
            //设置初始化密码
            String pwd = UUID.randomUUID().toString().replace("-", "").substring(4, 12);

            //初始化数据
            sysUser.setPayword(MD5Util.MD5Encode(pwd, "UTF-8"));

            sysUserService.editSave(sysUser);

            Map<String, String> p = new HashMap<String, String>();
            p.put("msg", "");
            p.put("pwd", pwd);

            return Message.success("操作成功", p);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
    }

    /**
     * Description:删除记录
     *
     * @param ids 需要删除的记录的ID数组
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymerchant:delete"})
    @RequestMapping("/delete")
    public @ResponseBody
    Message delete(@RequestParam(value = "ids[]") Long[] ids) {
        if (ids != null && ids.length == 0) {
            return Message.error("请选择要删除的数据");
        }
        try {

            payMerchantService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    @RequestMapping("/merchantAdminIp")
    public @ResponseBody
    Message merchantAdminIp(String adminip, String paywordAdmin, HttpServletRequest request) {
        //数据验证
        if (StringUtils.isBlank(adminip) || StringUtils.isBlank(paywordAdmin)) {
            return Message.error("参数错误");
        }

        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);

            SysUser user = sysUserService.querySysUserById(sysUser.getId());
            if (!MD5Util.MD5Encode(paywordAdmin, "UTF-8").equals(user.getPayword())) {
                return Message.error("参数错误");
            }
            if (user.getType().intValue() != 2) {
                return Message.error("参数错误");
            }
            payMerchantService.updateMerchantAdminip(user.getId(), adminip);

            return Message.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
    }

    @RequestMapping("/merchantApiIp")
    public @ResponseBody
    Message merchantApiIp(String apiip, String paywordApi, HttpServletRequest request) {
        //数据验证
        if (StringUtils.isBlank(apiip) || StringUtils.isBlank(paywordApi)) {
            return Message.error("参数错误");
        }

        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);

            SysUser user = sysUserService.querySysUserById(sysUser.getId());
            if (!MD5Util.MD5Encode(paywordApi, "UTF-8").equals(user.getPayword())) {
                return Message.error("参数错误");
            }
            if (user.getType().intValue() != 2) {
                return Message.error("参数错误");
            }
            payMerchantService.updateMerchantApiip(user.getId(), apiip);

            return Message.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
    }

    @RequestMapping("/viewMerchantData")
    public @ResponseBody
    Message viewMerchantData(String paywordView, HttpServletRequest request) {
        //数据验证
        if (StringUtils.isBlank(paywordView)) {
            return Message.error("参数错误");
        }

        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);

            SysUser user = sysUserService.querySysUserById(sysUser.getId());
            if (!MD5Util.MD5Encode(paywordView, "UTF-8").equals(user.getPayword())) {
                return Message.error("参数错误");
            }
            if (user.getType().intValue() != 2) {
                return Message.error("参数错误");
            }

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("my", user.getMerchantMy());
            param.put("apiip", user.getApiip());
            param.put("adminip", user.getAdminip());

            if (user.getValidStatus() != null && user.getValidStatus().intValue() == 1) {
                param.put("un", user.getUsername());
                param.put("validCode", user.getValidCode());
                //cyber 修改：关闭谷歌验证码下发
                //param.put("img", ZxingUtil.createQRCodeStr(GoogleGenerator.getQRBarcode(user.getUsername(), user.getValidCode())));
                param.put("img","0");
            }

            return Message.success("操作成功", param);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
    }


    @AdminPermission(value = {"admin:paymerchant:zfmm"})
    @RequestMapping("/restoreAdminip")
    public @ResponseBody
    Message restoreAdminip(Long id) {
        //数据验证
        if (id == null) {
            return Message.error("参数错误");
        }


        try {
            SysUser merchant = sysUserService.querySysUserById(id);
            if (merchant == null || merchant.getType() == null || merchant.getType().intValue() != 2) {
                return Message.error("商户不存在");
            }

            payMerchantService.updateMerchantAdminip(id, "");

            return Message.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
    }

    @AdminPermission(value = {"admin:paymerchant:zfmm"})
    @RequestMapping("/restoreApiip")
    public @ResponseBody
    Message restoreApiip(Long id) {
        //数据验证
        if (id == null) {
            return Message.error("参数错误");
        }

        try {
            SysUser merchant = sysUserService.querySysUserById(id);
            if (merchant == null || merchant.getType() == null || merchant.getType().intValue() != 2) {
                return Message.error("商户不存在");
            }

            payMerchantService.updateMerchantApiip(id, "");
            return Message.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
    }

    @AdminPermission(value = {"admin:paymerchant:export"})
    @RequestMapping("/exportExcel")
    public @ResponseBody
    Message exportExcelMerchant(@RequestParam Map<String, Object> param, @RequestParam(value = "orders[]", required = false) String[] orders, String excelCols, HttpServletRequest request) {

        try {

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            //request.getAttribute("serverName") + "/"
            new PayExcelportThread("商户数据" + sdf.format(new Date()), "../../", excelCols, staticFolder, sysUser.getId(), new PayExcelexportDataService() {

                @Override
                public List<Map<String, Object>> getExprotData(Map<String, Object> param, String[] orders) {
                    return payMerchantService.queryExportListMerchant(param, orders);
                }
            }, payExcelexportService, param, orders, 2).start();
            return Message.success("启动成功");

//            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);

//            List < Map < String, Object > > queryExportList = payMerchantService.queryExportListMerchant(param, orders);
//
//            Poi4ExcelExportManager pem=Poi4ExcelExportManager.Builder()
//                    .fileName("商户列表")
//                    .outModel(Constants.MODEL_OUTPUT_EXCEL_LARGE)
//                    .excelSaveFolder(staticFolder+"excelExport")
//                    .cellPropertys(excelCols)
//                    .fileModel(Constants.MODEL_FILE_CREATE_RANDOM)
//                    ;
//            String filePath=pem.createExcel(queryExportList);
////            String filePath=ExcelExportManager.Builder().fileName("商户列表").excelType(Constants.EXCEL_TYPE_XSSF).createExcel(staticFolder+"excelExport", excelCols, queryExportList);
//            System.out.println("导出文件路径："+filePath);
//            //将filePath转换为url
////            String urlPre=(String)request.getAttribute("serverName");
//            String url=(String)request.getAttribute("serverName")+"/"+filePath.substring(staticFolder.length()).replace("\\", "/");
//            System.out.println(url);
//            return Message.success("",url);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return Message.error("导出失败");
    }
}
