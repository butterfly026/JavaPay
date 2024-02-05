package com.city.city_collector.admin.pay.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.city.city_collector.admin.city.service.ConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.apache.log4j.Level;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.annotation.AdminPermission;
import com.city.adminpermission.exception.UserNotFoundException;
import com.city.city_collector.common.util.ApiMessage;
import com.city.city_collector.common.util.MD5Util;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.util.PayExcelexportDataService;
import com.city.city_collector.common.util.PayExcelportThread;
import com.city.city_collector.common.util.SnUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.qqqq.excleport.Poi4ExcelExportManager;
import com.qqqq.excleport.common.Constants;

import groovy.lang.Binding;

import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.city.util.Logger;
import com.city.city_collector.admin.pay.entity.PayBankCard;
import com.city.city_collector.admin.pay.entity.PayBankCode;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayClientModel;
import com.city.city_collector.admin.pay.entity.PayOrder;
import com.city.city_collector.admin.pay.service.PayBankCardService;
import com.city.city_collector.admin.pay.service.PayBankCodeService;
import com.city.city_collector.admin.pay.service.PayCashService;
import com.city.city_collector.admin.pay.service.PayClientModelService;
import com.city.city_collector.admin.pay.service.PayClientService;
import com.city.city_collector.admin.pay.service.PayExcelexportService;
import com.city.city_collector.admin.pay.service.PayMerchantService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.ChannelManager;
import com.city.city_collector.channel.ChannelManager1;
import com.city.city_collector.channel.bean.CashConfig;
import com.city.city_collector.channel.bean.CashInfo;
import com.city.city_collector.channel.bean.ClientChannel;
import com.city.city_collector.channel.bean.GroovyScript;
import com.city.city_collector.channel.bean.PayRatio;
import com.city.city_collector.channel.util.GroovyUtil;
import com.city.city_collector.channel.util.HttpUtil;
import com.city.city_collector.channel.util.HttpUtil.RequestType;

/**
 * Description:商户提现-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payCash")
public class PayCashController {
    @Autowired
    PayCashService payCashService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    PayBankCardService payBankCardService;

    @Autowired
    PayBankCodeService payBankCodeService;

    @Autowired
    PayMerchantService payMerchantService;

    @Autowired
    PayClientService payClientService;

    @Autowired
    PayClientModelService payClientModelService;

    @Autowired
    PayExcelexportService payExcelexportService;

    @Autowired
    ConfigService configService;


    @Value("${static.folder}")
    private String staticFolder;

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:paycash:list"})
    @RequestMapping("/view")
    public String view(Model model, HttpServletRequest request) {
        model.addAttribute("merchantList", payMerchantService.querySelectList());
        model.addAttribute("clientList", payClientService.querySelectList());
        return "admin/pay/paycash/list" ;
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
    @AdminPermission(value = {"admin:paycash:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payCashService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:paycash:add"})
    @RequestMapping("/add")
    public String add(Model model, HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            
            SysUser user = sysUserService.querySysUserById(sysUser.getId());
            BigDecimal money = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
            BigDecimal frozenMoney = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
            model.addAttribute("money", money);
            model.addAttribute("frozenMoney", frozenMoney);
            model.addAttribute("amount", money.subtract(frozenMoney));
            model.addAttribute("bankCodeList", payBankCodeService.queryAllList());

            String cashConfig = user.getCashConfig();
            model.addAttribute("cashConfig", "{}");
            if (StringUtils.isNotBlank(cashConfig)) {
                CashConfig cc = new Gson().fromJson(cashConfig, CashConfig.class);
                PayRatio pr = cc.getBankRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                pr = cc.getUpiRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                pr = cc.getUsdtRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                pr = cc.getDfbankRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                pr = cc.getDfupiRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                pr = cc.getDfusdtRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                model.addAttribute("cashConfig", new GsonBuilder().serializeNulls().create().toJson(cc));
            }
//            model.addAttribute("cmode", user.getCashMode()==null?0:user.getCashMode().intValue());
//            model.addAttribute("pcmode", user.getCashMode()==null?0:user.getPtmcashMode().intValue());
//
//            model.addAttribute("ccommission", user.getCashCommission()==null?0:user.getCashCommission());
//            model.addAttribute("pccommission", user.getPtmcashCommission()==null?0:user.getPtmcashCommission());
//
//            if(user.getCashMode()!=null && user.getCashMode().intValue()==1) {
//                model.addAttribute("cashCommission_bank", "每笔收取<span style=\"color:green;\">"+user.getCashCommission()+"%</span>手续费");
//            }else {
//                model.addAttribute("cashCommission_bank", "每笔收取固定手续费<span style=\"color:green;\">Rs. "+user.getCashCommission()+"</span>");
//            }
//            if(user.getPtmcashMode()!=null && user.getPtmcashMode().intValue()==1) {
//                model.addAttribute("cashCommission_ptm", "每笔收取<span style=\"color:green;\">"+user.getPtmcashCommission()+"%</span>手续费");
//            }else {
//                model.addAttribute("cashCommission_ptm", "每笔收取固定手续费<span style=\"color:green;\">Rs. "+user.getPtmcashCommission()+"</span>");
//            }

            model.addAttribute("minCommission", sysUser.getMinCommission() == null ? 0 : sysUser.getMinCommission());
            model.addAttribute("dfminCommission", sysUser.getDfminCommission() == null ? 0 : sysUser.getDfminCommission());

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", user.getId());
            params.put("btype", 0);
            model.addAttribute("cardList", payBankCardService.queryList(params));

            params.put("btype", 1);
            model.addAttribute("ptmList", payBankCardService.queryList(params));
        } catch (UserNotFoundException e) {

            e.printStackTrace();
            model.addAttribute("error", "操作异常:" + e.getMessage());
        }


        return "admin/pay/paycash/edit" ;
    }

    /**
     * Description: 总后台使用 代理商户进行提现
     */
    //TODO:
    @AdminPermission(value = {"admin:paycash:add"})
    @RequestMapping("/addSelf")
    public String addSelf(Model model, HttpServletRequest request) {
        try {
//            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            Long selfMerchant = ApplicationData.getInstance().getConfig().getSelfMerchant();
            if (selfMerchant == null || selfMerchant == 0) {
                model.addAttribute("error", "请联系管理员配置参数");
                return "admin/pay/paycash/edit_self" ;
            }

            SysUser user = sysUserService.querySysUserById(selfMerchant);
            if(user==null){
                Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,  "找不到商户["+selfMerchant+"]！");
                model.addAttribute("error", "找不到商户["+selfMerchant+"]！");
                return "admin/pay/paycash/edit_self" ;
            }
            BigDecimal money = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
            BigDecimal frozenMoney = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
            model.addAttribute("userName", user.getUsername() + " " + user.getName());
            model.addAttribute("money", money);
            model.addAttribute("frozenMoney", frozenMoney);
            model.addAttribute("amount", money.subtract(frozenMoney));

            String cashConfig = user.getCashConfig();
            model.addAttribute("cashConfig", "{}");
            if (StringUtils.isNotBlank(cashConfig)) {
                CashConfig cc = new Gson().fromJson(cashConfig, CashConfig.class);
                PayRatio pr = cc.getBankRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                pr = cc.getUpiRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                pr = cc.getUsdtRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                pr = cc.getDfbankRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                pr = cc.getDfupiRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                pr = cc.getDfusdtRatio();
                if (pr != null) {
                    pr.setProxyCommission(null);
                    pr.setProxyRatioCommission(null);
                }
                model.addAttribute("cashConfig", new GsonBuilder().serializeNulls().create().toJson(cc));
            }
//            model.addAttribute("cmode", user.getCashMode()==null?0:user.getCashMode().intValue());
//            model.addAttribute("pcmode", user.getCashMode()==null?0:user.getPtmcashMode().intValue());
//
//            model.addAttribute("ccommission", user.getCashCommission()==null?0:user.getCashCommission());
//            model.addAttribute("pccommission", user.getPtmcashCommission()==null?0:user.getPtmcashCommission());
//
//            if(user.getCashMode()!=null && user.getCashMode().intValue()==1) {
//                model.addAttribute("cashCommission_bank", "每笔收取<span style=\"color:green;\">"+user.getCashCommission()+"%</span>手续费");
//            }else {
//                model.addAttribute("cashCommission_bank", "每笔收取固定手续费<span style=\"color:green;\">Rs. "+user.getCashCommission()+"</span>");
//            }
//            if(user.getPtmcashMode()!=null && user.getPtmcashMode().intValue()==1) {
//                model.addAttribute("cashCommission_ptm", "每笔收取<span style=\"color:green;\">"+user.getPtmcashCommission()+"%</span>手续费");
//            }else {
//                model.addAttribute("cashCommission_ptm", "每笔收取固定手续费<span style=\"color:green;\">Rs. "+user.getPtmcashCommission()+"</span>");
//            }
           
            model.addAttribute("minCommission", user.getMinCommission() == null ? 0 : user.getMinCommission());
            model.addAttribute("dfminCommission", user.getDfminCommission() == null ? 0 : user.getDfminCommission());

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", user.getId());
            params.put("btype", 0);
            model.addAttribute("cardList", payBankCardService.queryList(params));

         
            model.addAttribute("bankList", payBankCodeService.queryAllList());

            List<SysUser> sysuserLst = payMerchantService.querySelectList();

            model.addAttribute("merchantList", sysuserLst);
//            params.put("btype", 1);
//            model.addAttribute("ptmList", payBankCardService.queryList(params));
        } catch (Exception e) {

            e.printStackTrace();
            model.addAttribute("error", "操作异常:" + e.getMessage());
        }


        return "admin/pay/paycash/edit_self" ;
    }

    /**
     * Description:新增保存操作
     */
    @AdminPermission(value = {"admin:paycash:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(String ownername,String bankCode,String cardNo,Integer btype, Integer channelType, BigDecimal money, Long selfMerchant, String payPwd, HttpServletRequest request) {
        //数据验证
        if (btype == null || money == null || channelType == null || StringUtils.isBlank(payPwd) || StringUtils.isBlank(cardNo) || cardNo.length()>=500 || StringUtils.isBlank(ownername)|| StringUtils.isBlank(bankCode) ) {
            return Message.error("参数错误");
        }

        try {
            if (money.compareTo(BigDecimal.ZERO) <= 0) {
                return Message.error("提现金额必须大于0");
            }

            Long userId = 0L;
            if (selfMerchant != null && selfMerchant == 321) {
                Long selfMerchant11 = ApplicationData.getInstance().getConfig().getSelfMerchant();
                if (selfMerchant11 == null) {
                    return Message.error("请联系管理员配置参数.");
                }
                userId = selfMerchant11;
            } else {
                SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
                userId = sysUser.getId();
            }

            SysUser user = sysUserService.querySysUserById(userId);

            if (user.getType().intValue() != 2 && user.getType().intValue() != 1) {
                return Message.error("您的操作已记录.");
            }

            if (StringUtils.isBlank(user.getPayword())) {
                return Message.error("支付密码尚未设置！");
            }
            if (!MD5Util.MD5Encode(payPwd, "UTF-8").equals(user.getPayword())) {
                return Message.error("支付密码错误！");
            }

            BigDecimal minCommission = user.getMinCommission();
            String cname = "下发" ;
            if (channelType.intValue() == 1) {
                minCommission = user.getDfminCommission();
                cname = "代付" ;
            }

            if (minCommission != null && minCommission.compareTo(BigDecimal.ZERO) > 0 && money.compareTo(minCommission) < 0) {
                return Message.error("最小" + cname + "金额为" + minCommission + "元");
            }

            //代理佣金
            BigDecimal proxyCommission = BigDecimal.ZERO;
            //手续费
            BigDecimal commission = BigDecimal.ZERO;

            CashConfig config = new CashConfig();
            String cashConfig = user.getCashConfig();
            if (StringUtils.isNotBlank(cashConfig)) {
                try {
                    config = new GsonBuilder().serializeNulls().create().fromJson(cashConfig, CashConfig.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //获取费率
            PayRatio ratio = new PayRatio();
            if (channelType.intValue() == 0) {//下发
                if (btype.intValue() == 0) {//银行卡
                    ratio = config.getBankRatio();
                } else if (btype.intValue() == 1) {//upi
                    ratio = config.getUpiRatio();
                } else {
                    ratio = config.getUsdtRatio();
                }
            } else {//代付
                if (btype.intValue() == 0) {//银行卡
                    ratio = config.getDfbankRatio();
                } else if (btype.intValue() == 1) {//upi
                    ratio = config.getDfupiRatio();
                } else {
                    ratio = config.getDfusdtRatio();
                }
            }
            BigDecimal h = new BigDecimal(100);
            //计算用户手续费
            commission = money.multiply(ratio.getRatioCommission()).divide(h).add(ratio.getCommission());
            //计算代理佣金
            proxyCommission = money.multiply(ratio.getProxyRatioCommission()).divide(h).add(ratio.getProxyCommission());
            if (proxyCommission.compareTo(commission) > 0) {
                return Message.error(cname + "费率配置有误！");
            }
//			Integer cashMode=0;

//			if(btype.intValue()==0) {//银行卡
//			    commission=user.getCashCommission()==null?BigDecimal.ZERO:user.getCashCommission();
//			    cashMode=user.getCashMode()==null?0:user.getCashMode();
//			    proxyCommission=user.getMcashCommission()==null?BigDecimal.ZERO:user.getMcashCommission();
//			}else if(btype.intValue()==1) {//paytm账号
//			    commission=user.getPtmcashCommission()==null?BigDecimal.ZERO:user.getPtmcashCommission();
//			    cashMode=user.getPtmcashMode()==null?0:user.getPtmcashMode();
//			    proxyCommission=user.getMptmcashCommission()==null?BigDecimal.ZERO:user.getMptmcashCommission();
//			}else {
//			    return Message.error("非法操作");
//			}

//			if(user.getCashCommission()!=null) commission=user.getCashCommission();
//			if(user.getPtmcashCommission()!=null) ptmcommission=user.getPtmcashCommission();

            //计算手续费
//			if(cashMode.intValue()==1) {//按百分比
//			    commission=money.multiply(commission).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//			    proxyCommission=money.multiply(proxyCommission).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//			}

            BigDecimal realMoney = money;
            //实际扣减金额
            money = money.add(commission);

            BigDecimal money1 = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
            BigDecimal frozenMoney = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
            if (money1.compareTo(money) < 0 || money1.subtract(frozenMoney).compareTo(money) < 0) {
                return Message.error("可提现金额不足！");
            }

            //获取卡信息
           // Map<String, Object> params = new HashMap<String, Object>();
            //params.put("id", cardNo);
            /*
            if (btype.intValue() == 0) {
                params.put("id", cardId);
            } else {
                params.put("id", cardId1);
            }
            PayBankCard card = payBankCardService.querySingle(params);

            if (card == null) {
                return Message.error("无效提现账号");
            }
            if (!card.getUserId().equals(user.getId())) {
                return Message.error("非法提现账号.");
            }
            */


            PayCash payCash = new PayCash();
            payCash.setMerchantId(user.getId());
            payCash.setCashType(0);
            payCash.setAmount(money);
            payCash.setMerchantSn("");
            payCash.setSn(SnUtil.createSn(ApplicationData.getInstance().getConfig().getCashsnpre()));

            payCash.setBankIfsc("");//??
            payCash.setBankNation("");//获得银行卡国家
            payCash.setBtype(btype);//收款类型0:银行,1:UPI,2:USDT
            payCash.setChannelType(channelType);

            if (user.getType().intValue() == 1) {
                payCash.setUserType(1);
            } else {
                payCash.setUserType(0);
            }
//            //计算手续费
//          //最低手续费
//            BigDecimal minCommission=user.getCashCommission()==null?BigDecimal.ZERO:user.getCashCommission();
//            //提现费率
//            BigDecimal cashRatio=user.getCashRatio()==null?BigDecimal.ZERO:user.getCashRatio();
//            //提现手续费
//            BigDecimal commission=money.multiply(cashRatio).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//            if(commission.compareTo(minCommission)<0) {
//                commission=minCommission;
//            }
//            System.out.println(amount.compareTo(commission));
//            if(money.compareTo(commission)<=0) {
//                return Message.error("提现金额不能低于手续费!");
//            }

            payCash.setCommission(commission);
            payCash.setRealMoney(realMoney);

            if (user.getProxyId() != null) {
                payCash.setProxyId(user.getProxyId());
                payCash.setProxyMoney(proxyCommission);
            }
            

            payCash.setBankAccno(cardNo.trim());//卡号
            payCash.setBankAccname(ownername.trim());//持有者姓名
            Map<String, Object> searchBankName = new HashMap<String, Object>();
            searchBankName.put("code", bankCode.toString());
            PayBankCode pbc = payBankCodeService.querySingle(searchBankName);
            if (pbc == null) {
                return Message.error("参数错误101");
            }
            payCash.setBankName(pbc.getName());//开户银行
            payCash.setBankSubname("");//支行
            payCash.setBankId("");
            payCash.setBankAccid("");
            payCash.setBankAccmobile("");
            payCash.setBankProvince("");//开户行省份
            payCash.setBankCity("");//开户城市

            payCash.setBankCode(bankCode);//银行编号

            payCash.setNotifyUrl("");
//            payCash.setNotifyStatus(0);
            payCash.setRemark("");
            payCash.setStatus(0);
            payCash.setPayStatus(0);

            payCashService.createCash(payCash, user, user.getType().intValue() == 1 ? 0 : 1, false);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
        return Message.success();
    }

    /**
     * Description:新增保存操作
     */
    @AdminPermission(value = {"admin:paycash:add"})
    @RequestMapping("/addSaveAssign")
    public @ResponseBody
    Message addSaveAssign(Integer btype,String cardOwner,String cardNo,String bankInfo,Integer channelType, BigDecimal money, Long merchantId, HttpServletRequest request) {
        //数据验证
        //String cardId="";
        String bankName="",bankCode="";
        if (money == null || channelType == null || merchantId==0 || cardNo.length()==0 || cardNo.length()>100|| bankInfo.length()==0 || bankInfo.length()>200) {
            return Message.error("参数错误");
        }
       

        try {
            String []bankInfos = bankInfo.split("|");
            bankCode = bankInfos[0];
            bankName = bankInfos[1];

            /*
            bankInfo = bankInfo.replace("&quot;", "\"");
            Map<String,Object> jsonObj = new Gson().fromJson(bankInfo, new TypeToken<HashMap<String, Object>>() {}.getType());
            if(jsonObj.get("cardNo")==""){
                return Message.error("参数bankInfo错误");
            }
            //cardName = jsonObj.get("cardName").toString();
            bankCode = jsonObj.get("bankCode").toString();
            //cardNo = jsonObj.get("cardNo").toString();
            bankName = jsonObj.get("bankName").toString();
  */

            if (money.compareTo(BigDecimal.ZERO) <= 0) {
                return Message.error("提现金额必须大于0");
            }
            //merchantId
            //Map<String,Object> params = new HashMap<String,Object>();
            ///params.put("id",merchantId);
            //payMerchantService.querySingle(params);
            Long userId = merchantId;
            /* 
            Long userId = 0L;
            if (selfMerchant != null && selfMerchant == 321) {
                Long selfMerchant11 = ApplicationData.getInstance().getConfig().getSelfMerchant();
                if (selfMerchant11 == null) {
                    return Message.error("请联系管理员配置参数.");
                }
                userId = selfMerchant11;
            } else {
                SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
                userId = sysUser.getId();
            }
            */
            SysUser user = sysUserService.querySysUserById(userId);

           // return Message.error("您的操作已记录."+user.getName());
        //}catch(Exception e) {
        //}
            
            
            /*
            if (user.getType().intValue() != 2 && user.getType().intValue() != 1) {
                return Message.error("您的操作已记录.");
            }
             */
            /*
            if (StringUtils.isBlank(user.getPayword())) {
                return Message.error("支付密码尚未设置！");
            }
            if (!MD5Util.MD5Encode(payPwd, "UTF-8").equals(user.getPayword())) {
                return Message.error("支付密码错误！");
            }
 */

            BigDecimal minCommission = user.getMinCommission();
            String cname = "下发" ;
            if (channelType.intValue() == 1) {
                minCommission = user.getDfminCommission();
                cname = "代付" ;
            }

            if (minCommission != null && minCommission.compareTo(BigDecimal.ZERO) > 0 && money.compareTo(minCommission) < 0) {
                return Message.error("最小" + cname + "金额为" + minCommission + "元");
            }

            //代理佣金
            BigDecimal proxyCommission = BigDecimal.ZERO;
            //手续费
            BigDecimal commission = BigDecimal.ZERO;

            CashConfig config = new CashConfig();
            String cashConfig = user.getCashConfig();
            if (StringUtils.isNotBlank(cashConfig)) {
                try {
                    config = new GsonBuilder().serializeNulls().create().fromJson(cashConfig, CashConfig.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //获取费率
            PayRatio ratio = new PayRatio();
            if (channelType.intValue() == 0) {//下发
                if (btype.intValue() == 0) {//银行卡
                    ratio = config.getBankRatio();
                } else if (btype.intValue() == 1) {//upi
                    ratio = config.getUpiRatio();
                } else {
                    ratio = config.getUsdtRatio();
                }
            } else {//代付
                if (btype.intValue() == 0) {//银行卡
                    ratio = config.getDfbankRatio();
                } else if (btype.intValue() == 1) {//upi
                    ratio = config.getDfupiRatio();
                } else {
                    ratio = config.getDfusdtRatio();
                }
            }
            BigDecimal h = new BigDecimal(100);

            //Logger.getInstance().Log(Logger.LOG_LEVEL_INFO, "费率参数 金额["+money.toString()+"] - [getRatioCommission:"+ ratio.getRatioCommission().toString()+"getCommission:"+ ratio.getCommission().toString()+"getProxyRatioCommission:"+ratio.getProxyRatioCommission().toString()+"getProxyCommission:"+ratio.getProxyCommission().toString()+"]");
            
            //计算用户手续费
            commission = money.multiply(ratio.getRatioCommission().divide(h)).add(ratio.getCommission());
            if(commission.intValue()<0){
                return Message.error(cname + "费率配置有误不能为负数！");
            }
            //计算代理佣金
            /*
            proxyCommission = money.multiply(ratio.getProxyRatioCommission()).divide(h).add(ratio.getProxyCommission());
            if (proxyCommission.compareTo(commission) > 0) {
                return Message.error(cname + "费率配置有误！");
            }
             */
//			Integer cashMode=0;

//			if(btype.intValue()==0) {//银行卡
//			    commission=user.getCashCommission()==null?BigDecimal.ZERO:user.getCashCommission();
//			    cashMode=user.getCashMode()==null?0:user.getCashMode();
//			    proxyCommission=user.getMcashCommission()==null?BigDecimal.ZERO:user.getMcashCommission();
//			}else if(btype.intValue()==1) {//paytm账号
//			    commission=user.getPtmcashCommission()==null?BigDecimal.ZERO:user.getPtmcashCommission();
//			    cashMode=user.getPtmcashMode()==null?0:user.getPtmcashMode();
//			    proxyCommission=user.getMptmcashCommission()==null?BigDecimal.ZERO:user.getMptmcashCommission();
//			}else {
//			    return Message.error("非法操作");
//			}

//			if(user.getCashCommission()!=null) commission=user.getCashCommission();
//			if(user.getPtmcashCommission()!=null) ptmcommission=user.getPtmcashCommission();

            //计算手续费
//			if(cashMode.intValue()==1) {//按百分比
//			    commission=money.multiply(commission).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//			    proxyCommission=money.multiply(proxyCommission).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//			}

            BigDecimal realMoney = money;
            //实际扣减金额
            money = money.add(commission).add(proxyCommission);

            BigDecimal money1 = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
            BigDecimal frozenMoney = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
            if (money1.compareTo(money) < 0 || money1.subtract(frozenMoney).compareTo(money) < 0) {
                return Message.error("可提现金额不足！");
            }

            //获取卡信息
           // Map<String, Object> params = new HashMap<String, Object>();
            //params.put("id", cardNo);
            /*
            if (btype.intValue() == 0) {
                params.put("id", cardId);
            } else {
                params.put("id", cardId1);
            }
            PayBankCard card = payBankCardService.querySingle(params);

            if (card == null) {
                return Message.error("无效提现账号");
            }
            if (!card.getUserId().equals(user.getId())) {
                return Message.error("非法提现账号.");
            }
            */


            PayCash payCash = new PayCash();
            payCash.setMerchantId(user.getId());
            payCash.setCashType(0);
            payCash.setAmount(money);
            payCash.setMerchantSn("");
            payCash.setSn(SnUtil.createSn(ApplicationData.getInstance().getConfig().getCashsnpre()));

            payCash.setBankIfsc("");//??
            payCash.setBankNation("");//获得银行卡国家
            payCash.setBtype(btype);//收款类型0:银行,1:UPI,2:USDT
            payCash.setChannelType(channelType);

            if (user.getType().intValue() == 1) {
                payCash.setUserType(1);
            } else {
                payCash.setUserType(0);
            }
//            //计算手续费
//          //最低手续费
//            BigDecimal minCommission=user.getCashCommission()==null?BigDecimal.ZERO:user.getCashCommission();
//            //提现费率
//            BigDecimal cashRatio=user.getCashRatio()==null?BigDecimal.ZERO:user.getCashRatio();
//            //提现手续费
//            BigDecimal commission=money.multiply(cashRatio).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
//            if(commission.compareTo(minCommission)<0) {
//                commission=minCommission;
//            }
//            System.out.println(amount.compareTo(commission));
//            if(money.compareTo(commission)<=0) {
//                return Message.error("提现金额不能低于手续费!");
//            }

            payCash.setCommission(commission);
            payCash.setRealMoney(realMoney);

            if (user.getProxyId() != null) {
                payCash.setProxyId(user.getProxyId());
                payCash.setProxyMoney(proxyCommission);
            }
            
            
            payCash.setBankAccno(cardNo.trim());//卡号
            payCash.setBankAccname(cardOwner.trim());//持有者姓名
            /*Map<String, Object> searchBankName = new HashMap<String, Object>();
            searchBankName.put("code", bankCode);
            
            PayBankCode pbc = payBankCodeService.querySingle(searchBankName);
            if (pbc == null) {
                return Message.error("参数错误101");
            }
             */
            payCash.setBankName(bankName.trim());//开户银行
            payCash.setBankSubname("");//支行
            payCash.setBankId("");
            payCash.setBankAccid("");
            payCash.setBankAccmobile("");
            payCash.setBankProvince("");//开户行省份
            payCash.setBankCity("");//开户城市

            payCash.setBankCode(bankCode.trim());//银行编号

            payCash.setNotifyUrl("");
//            payCash.setNotifyStatus(0);
            payCash.setRemark("");
            payCash.setStatus(0);
            payCash.setPayStatus(0);

            payCashService.createCash(payCash, user, user.getType().intValue() == 1 ? 0 : 1, false);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
        return Message.success();
    }

    /**
     * Description:打开编辑页面
     * @author:demo
     * @since 2020-6-29
     * @param id 记录ID
     * @param model model对象
     * @return String
     */
//	@AdminPermission(value={"admin:paycash:edit"})
//	@RequestMapping("/edit")
//	public String edit(Integer id,Model model){
//	    if(id==null){
//	        model.addAttribute("error","请选择需要编辑的记录");
//	        return "admin/pay/paycash/edit";
//	    }
//	    //将查询的参数添加到map
//	    Map<String,Object> params=new HashMap<String, Object>();
//	    params.put("id",id);
//	    //获取需要修改的记录数据
//	    PayCash result=payCashService.querySingle(params);
//	    if(result==null){
//	        model.addAttribute("error","编辑的记录可能已经被删除");
//	    }else{
//			model.addAttribute("result",result);
//		}
//	    return "admin/pay/paycash/edit";
//	}

    /**
     * Description:编辑保存
     * @author:demo
     * @since 2020-6-29
     * @param
     * @param
     * @param id 编辑的记录ID
     * @return Message
     */
//	@AdminPermission(value={"admin:paycash:edit"})
//	@RequestMapping("/editSave")
//	public @ResponseBody Message editSave(PayCash payCash){
//	    //数据验证
//	    if(payCash==null){
//	        return Message.error("参数错误");
//	    }
//
//
//        try{
//
//            payCashService.editSave(payCash);
//        }catch(Exception e){
//            e.printStackTrace();
//            return Message.error();
//        }
//        return Message.success();
//	}

    /**
     * Description:删除记录
     * @author:demo
     * @since 2020-6-29
     * @param ids  需要删除的记录的ID数组
     * @return Message
     */
//	@AdminPermission(value={"admin:paycash:delete"})
//	@RequestMapping("/delete")
//	public @ResponseBody Message delete(@RequestParam(value="ids[]")Long[] ids){
//	    if(ids!=null&&ids.length==0){
//	        return Message.error("请选择要删除的数据");
//	    }
//	    try{
//
//	        payCashService.delete(ids);
//	    }catch(Exception e){
//            e.printStackTrace();
//            return Message.error();
//        }
//        return Message.success();
//	}

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:paycash:merchantList"})
    @RequestMapping("/merchantView")
    public String merchantView() {
        return "admin/pay/paycash/merchant_list" ;
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
    @AdminPermission(value = {"admin:paycash:merchantList"})
    @RequestMapping("/merchantList")
    public @ResponseBody
    PageResult merchantList(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("merchantId", sysUser.getId());
            // 分页查询
            payCashService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }

    /**
     * 审核通过
     *
     * @param id
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:paycash:shtg"})
    @RequestMapping("/shtg")
    public @ResponseBody
    Message shtg(Long id, String remark) {
        if (id == null) {
            return Message.error("参数错误");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayCash cash = payCashService.querySingle(params);
            if (cash == null) {
                return Message.error("提现记录不存在");
            }
            if (cash.getPayStatus().intValue() != 0) {
                return Message.error("只能审核待审核的提现订单");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (cash.getRemark() == null) cash.setRemark("");
            remark = cash.getRemark() + " <span style='color:green;'>" + sdf.format(new Date()) + " 审核通过：" + remark + "</span><br/>" ;
            cash.setPayStatus(3);
            cash.setRemark(remark);
            payCashService.updateCashStatus(cash);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    /**
     * 审核通过
     *
     * @param id
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:paycash:shtg"})
    @RequestMapping("/batchShtg")
    public @ResponseBody
    Message batchShtg(Long[] ids, String remark) {
       /* if(ids==null||ids.length<1) {
            return Message.error("参数错误");
        }
        Set<String> errorSet = new LinkedHashSet<>();
        Long id ;
        for (int i = 0; i < ids.length; i++) {

            id = ids[i];
            try{

                Map < String, Object > params=new HashMap<String, Object>();
                params.put("id", id);
                PayCash cash = payCashService.querySingle(params);
                if(cash==null) {
                    errorSet.add(" 第"+(i+1)+"条记录不存在  跳过处理！");
                    continue;
                }
                if(cash.getPayStatus().intValue()!=0) {
                    errorSet.add(" 第"+(i+1)+"条记录只能审核待审核的提现订单  跳过处理！");
                    continue;
                }
                if (cash.getRealMoney().intValue()>1000) {
                    errorSet.add(" 第"+(i+1)+"条记录金额过大  跳过处理！");
                    continue;
                }

                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(cash.getRemark()==null) cash.setRemark("");
                remark=cash.getRemark()+" <span style='color:green;'>"+sdf.format(new Date())+" 审核通过："+remark+"</span><br/>";
                cash.setPayStatus(3);
                cash.setRemark(remark);
                payCashService.updateCashStatus(cash);
            }catch (Exception e){
                e.printStackTrace();
                return Message.error();
            }

        }*/

        return Message.success("操作完成", null);
    }


    @AdminPermission(value = {"admin:paycash:api"})
    @RequestMapping("/batchShtgView")
    public String batchShtgView(Long[] ids, Model model, HttpServletRequest request) {

        // model.addAttribute("clientList", sysUserService.queryClientInfoListAll());
       /* model.addAttribute("modelList", payCashService.queryClientInfoByModel());

        BigDecimal realMoney = new BigDecimal(0);
        BigDecimal commission = new BigDecimal(0);
        BigDecimal amount = new BigDecimal(0);
        for (Long id : ids) {
            Map < String, Object > params=new HashMap<String, Object>();
            params.put("id", id);
            PayCash cash=payCashService.querySingle(params);

            if (null==cash) {
                throw new RuntimeException("数据已变化，请刷新后重试");
            }

            realMoney = realMoney.add(cash.getRealMoney());
            commission =  commission.add(cash.getCommission());
            amount =  amount.add(cash.getAmount());
        }
        model.addAttribute("ids", org.apache.commons.lang3.StringUtils.join(ids,","));
        model.addAttribute("realMoney", realMoney);
        model.addAttribute("commission", commission);
        model.addAttribute("amount", amount);*/

        return "admin/pay/paycash/batch_cash" ;
    }

    @AdminPermission(value = {"admin:paycash:api"})
    @RequestMapping("/batchCashSubmit")
    public @ResponseBody
    Message batchCashSubmit(Long[] ids, Long modelId, @RequestParam(required = false) String remark, HttpServletRequest request) {
       /* if(ids==null||ids.length<1 || modelId==null ) {
            return Message.error("参数错误");
        }

        Long id ;
        Set<String> errorSet = new LinkedHashSet<>();
        for (int i = 0; i < ids.length; i++) {
            id = ids[i];

            try{

                Map < String, Object > params=new HashMap<String, Object>();
                params.put("id", id);
                PayCash cash = payCashService.querySingle(params);
                if(cash==null) {
                    errorSet.add(" 第"+(i+1)+"条提现记录不存在 跳过处理！");
                    continue;
                }

                if(cash.getPayStatus().intValue()!=3) {
                    errorSet.add(" 第"+(i+1)+"条只能代付审核通过的提现订单  跳过处理！");
                    continue;
                }

                BigDecimal money = cash.getRealMoney();
                if (money.intValue()>1000){
                    errorSet.add(" 第"+(i+1)+"条提现金额过大  跳过处理！");
                    continue;
                }


                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(cash.getRemark()==null) cash.setRemark("");
                remark=cash.getRemark()+" <span style='color:green;'>"+sdf.format(new Date())+" 发起api代付:"+remark+"</span><br/>";
                cash.setRemark(remark);

                //查询上游信息
                SysUser client=sysUserService.querySysUserById(modelId);
                if(client==null) {
                    errorSet.add(" 第"+(i+1)+"条上游不存在   跳过处理！");
                    continue;
                }

                BigDecimal money1=client.getMoney()==null?BigDecimal.ZERO:client.getMoney();
                BigDecimal frozenMoney=client.getFrozenMoney()==null?BigDecimal.ZERO:client.getFrozenMoney();
                if(money1.compareTo(money)<0 || money1.subtract(frozenMoney).compareTo(money)<0) {
                    errorSet.add(" 第"+(i+1)+"条可提现金额不足！  跳过处理！");
                    continue;
                }
                cash.setClientMoney(money);
                //获取模块键名
                String keyname=client.getPaykey();

                //获取模块对象
                Map < String, Object > p=new HashMap<String,Object>();
                p.put("keyname", keyname);
                PayClientModel pcm=payClientModelService.querySingle(p);
                if(pcm==null || pcm.getStatus().intValue()!=1 || pcm.getPayStatus().intValue()!=1) {
                    errorSet.add(" 第"+(i+1)+"条代付模块不可用！  跳过处理！");
                    continue;
                }

                GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(keyname);
                if(gs==null || gs.getPayOrderParams()==null || gs.getDealPayOrderData()==null) {
                    errorSet.add(" 第"+(i+1)+"条代付模块不可用！  跳过处理！");
                    continue;
                }

                Binding binding=new Binding();
                binding.setProperty("cash", cash);
                binding.setProperty("client", client);
                binding.setProperty("domain", request.getAttribute("serverName").toString());
                binding.setProperty("urlcash", client.getUrlcash());
                binding.setProperty("keyname", keyname);

                Object data=GroovyUtil.runScript(gs.getPayOrderParams(), binding);
                if(data==null) {
                    errorSet.add(" 第"+(i+1)+"条签名失败!   跳过处理！");
                    continue;
                }

                //代付参数处理失败
                Map < String, Object > dfParams=(Map < String, Object >)data;
                if(dfParams.get("dferror")!=null) {
                    errorSet.add(" 第"+(i+1)+"条发起代付失败:"+dfParams.get("dferror")+"  跳过处理！");
                    continue;
                }
                //发起请求
                CashInfo cinfo=HttpUtil.requestData_Cash(client.getUrlcash(), RequestType.POST, (Map < String, Object >)data,
                        new HashMap<String,String>(), "utf-8", cash.getSn(), pcm.getReqType().intValue(),gs.getDealPayOrderData());


                if(!cinfo.getStatus()) {
                    errorSet.add(" 第"+(i+1)+"条  "+ cinfo.getMsg() +"   跳过处理！" );
                    continue;
                }
                cash.setClientSn(cinfo.getSn());

                payCashService.merchantClientCashSubmit(cash, client, 1, money, new BigDecimal(0), new BigDecimal(0));

            }catch(Exception e){
                e.printStackTrace();
                return Message.error();
            }

        }*/


        return Message.success("操作完成", null);
    }


    @AdminPermission(value = {"admin:paycash:shjj"})
    @RequestMapping("/shjj")
    public @ResponseBody
    Message shjj(Long id, String remark) {
        if (id == null) {
            return Message.error("参数错误");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayCash cash = payCashService.querySingle(params);
            if (cash == null) {
                return Message.error("提现记录不存在");
            }
            if (cash.getPayStatus().intValue() != 0) {
                return Message.error("只能审核待审核的提现订单");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (cash.getRemark() == null) cash.setRemark("");
            remark = cash.getRemark() + " <span style='color:red;'>" + sdf.format(new Date()) + " 审核拒绝:" + remark + "</span><br/>" ;
            cash.setRemark(remark);
            payCashService.refuseCash(cash, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:paycash:shjj"})
    @RequestMapping("/bh")
    public @ResponseBody
    Message bh(Long id, String remark) {
        if (id == null) {
            return Message.error("参数错误");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayCash cash = payCashService.querySingle(params);
            if (cash == null) {
                return Message.error("提现记录不存在");
            }
            if (cash.getPayStatus().intValue() != 3) {
                return Message.error("只能驳回审核通过的提现订单");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (cash.getRemark() == null) cash.setRemark("");
            remark = cash.getRemark() + " <span style='color:red;'>" + sdf.format(new Date()) + " 驳回申请:" + remark + "</span><br/>" ;
            cash.setRemark(remark);
            payCashService.refuseCash(cash, 3);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    /**
     * 编辑备注
     *
     * @param id
     * @param remark
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:paycash:shjj"})
    @RequestMapping("/edremark")
    public @ResponseBody
    Message edremark(Long id, String remark) {
        if (id == null || StringUtils.isBlank(remark)) {
            return Message.error("参数错误");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayCash cash = payCashService.querySingle(params);
            if (cash == null) {
                return Message.error("提现记录不存在");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (cash.getRemark() == null) cash.setRemark("");
            remark = cash.getRemark() + " <span style='color:blue;'>" + sdf.format(new Date()) + " 添加备注:" + remark + "</span><br/>" ;
            cash.setRemark(remark);
            payCashService.updateCashRemark(cash);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    @AdminPermission(value = {"admin:paycash:api"})
    @RequestMapping("/merchantChangeClientCash")
    public String merchantChangeClientCash(Long id, Model model, HttpServletRequest request) {

        model.addAttribute("clientList", sysUserService.queryClientInfoListAll());
        model.addAttribute("modelList", payCashService.queryClientInfoByModel());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        PayCash cash = payCashService.querySingle(params);
        model.addAttribute("cash", cash);

        return "admin/pay/paycash/client_changecash" ;
    }


    /**
     * 更换上游 - 提交
     *
     * @param id
     * @param type
     * @param clientId
     * @param money
     * @param clientCommission
     * @param clientRealMoney
     * @param keyname
     * @param remark
     * @param request
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:paycash:api"})
    @RequestMapping("/merchantChangeClientCashSubmit")
    public @ResponseBody
    Message merchantChangeClientCashSubmit(Long id, Integer payType, Long clientId, BigDecimal money,
                                           @RequestParam(required = false) BigDecimal clientCommission, @RequestParam(required = false) BigDecimal clientRealMoney,
                                           @RequestParam(required = false) Long modelId,
                                           @RequestParam(required = false) String remark,
                                           HttpServletRequest request) {
        if (id == null || payType == null || clientId == null || money == null) {
            return Message.error("参数错误");
        }

        try {
            if (money.compareTo(BigDecimal.ZERO) <= 0) {
                return Message.error("提现金额有误");
            }

            if (clientCommission == null) clientCommission = BigDecimal.ZERO;
            if (clientRealMoney == null) clientRealMoney = BigDecimal.ZERO;

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayCash cash = payCashService.querySingle(params);
            if (cash == null) {
                return Message.error("提现记录不存在");
            }
            if (cash.getPayStatus().intValue() != 4) {
                return Message.error("只能更换处理中状态的代付上游");
            }

            SysUser client1 = sysUserService.querySysUserById(cash.getClientId());
            if (client1 == null) {
                return Message.error("代付记录异常");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (cash.getRemark() == null) cash.setRemark("");
            remark = cash.getRemark() + " <span style='color:green;'>" + sdf.format(new Date()) + " 更换代付上游，发起" + (payType.intValue() == 0 ? "人工" : "API") + "代付:" + remark + "</span> [原上游:" + client1.getUsername() + ",原代付金额:" + cash.getClientMoney() + "]<br/>" ;
            cash.setRemark(remark);

            if (payType.intValue() == 0) {//人工代付
                //查询上游信息
                SysUser client = sysUserService.querySysUserById(clientId);
                if (client == null) {
                    return Message.error("上游不存在");
                }

                BigDecimal money1 = client.getMoney() == null ? BigDecimal.ZERO : client.getMoney();
                BigDecimal frozenMoney = client.getFrozenMoney() == null ? BigDecimal.ZERO : client.getFrozenMoney();
                if (money1.compareTo(money) < 0 || money1.subtract(frozenMoney).compareTo(money) < 0) {
                    return Message.error("可提现金额不足！");
                }

                payCashService.merchantChangeClientCashSubmit(cash, client, payType, money, clientCommission, clientRealMoney);
            } else if (payType.intValue() == 1) {//API代付
//                if(StringUtils.isBlank(keyname)) {
//                    return Message.error("不支持的代付模式.");
//                }
//                String[] strs=keyname.split("_");
//                keyname=strs[0];
//                clientId=Long.parseLong(strs[1]);
//
//                //查询上游信息
//                SysUser client=sysUserService.querySysUserById(clientId);
//                if(client==null) {
//                    return Message.error("上游不存在");
//                }
//
//                BigDecimal money1=client.getMoney()==null?BigDecimal.ZERO:client.getMoney();
//                BigDecimal frozenMoney=client.getFrozenMoney()==null?BigDecimal.ZERO:client.getFrozenMoney();
//                if(money1.compareTo(money)<0 || money1.subtract(frozenMoney).compareTo(money)<0) {
//                    return Message.error("可提现金额不足！");
//                }
//                cash.setClientMoney(money);
//                //获取通道对象
//                ClientChannel clientChannel = ChannelManager.channels.get(keyname);
//
//              //发起代付请求
//                Map < String,Object > data=new TreeMap<String,Object>();
//
//                CashInfo cinfo=clientChannel.clientCash(data,cash, client, request.getAttribute("serverName").toString(),client.getUrlcash(),keyname);
                if (modelId == null) {
                    return Message.error("不支持的代付模式.");
                }

                clientId = modelId;

                //查询上游信息
                SysUser client = sysUserService.querySysUserById(clientId);
                if (client == null) {
                    return Message.error("上游不存在");
                }

                BigDecimal money1 = client.getMoney() == null ? BigDecimal.ZERO : client.getMoney();
                BigDecimal frozenMoney = client.getFrozenMoney() == null ? BigDecimal.ZERO : client.getFrozenMoney();
                if (money1.compareTo(money) < 0 || money1.subtract(frozenMoney).compareTo(money) < 0) {
                    return Message.error("可提现金额不足！");
                }
                cash.setClientMoney(money);
                //获取模块键名
                String keyname = client.getPaykey();

                //获取模块对象
                Map<String, Object> p = new HashMap<String, Object>();
                p.put("keyname", keyname);
                PayClientModel pcm = payClientModelService.querySingle(p);
                if (pcm == null || pcm.getStatus().intValue() != 1 || pcm.getPayStatus().intValue() != 1) {
                    return Message.error("代付模块不可用！");
                }

                GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(keyname);
                if (gs == null || gs.getPayOrderParams() == null || gs.getDealPayOrderData() == null) {
                    return Message.error("代付模块不可用！");
                }

                Binding binding = new Binding();
                binding.setProperty("cash", cash);
                binding.setProperty("client", client);
                binding.setProperty("domain", request.getAttribute("serverName").toString());
                binding.setProperty("urlcash", client.getUrlcash());
                binding.setProperty("keyname", keyname);

                Object data = GroovyUtil.runScript(gs.getPayOrderParams(), binding);
                if (data == null) {
                    return Message.error("签名失败!");
                }

                //代付参数处理失败
                Map<String, Object> dfParams = (Map<String, Object>) data;
                if (dfParams.get("dferror") != null) {
                    return Message.error("发起代付失败:" + dfParams.get("dferror"));
                }
                		//加入GET方法 20220614 kahn
				RequestType reqType=RequestType.POST;
                if (dfParams.get("reqtype") != null) {
                    reqType=RequestType.GET;
                    dfParams.remove("reqtype");
					System.out.println("RequestType.GET:更改请求类型。删除reqtype"+new Gson().toJson(params));
                }
                //发起请求
                CashInfo cinfo = HttpUtil.requestData_Cash(client.getUrlcash(), reqType, (Map<String, Object>) dfParams,
                        new HashMap<String, String>(), "utf-8", cash.getSn(), pcm.getReqType().intValue(), gs.getDealPayOrderData());

                if (!cinfo.getStatus()) {
                    return Message.error(cinfo.getMsg());
                }
                cash.setClientSn(cinfo.getSn());

                payCashService.merchantChangeClientCashSubmit(cash, client, payType, money, clientCommission, clientRealMoney);
            } else {
                return Message.error("不支持的代付模式");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    @AdminPermission(value = {"admin:paycash:api"})
    @RequestMapping("/merchantClientCash")
    public String merchantClientCash(Long id, Model model, HttpServletRequest request) {

        model.addAttribute("clientList", sysUserService.queryClientInfoListAll());
        model.addAttribute("modelList", payCashService.queryClientInfoByModel());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        PayCash cash = payCashService.querySingle(params);
        model.addAttribute("cash", cash);

        return "admin/pay/paycash/client_cash" ;
    }

    @AdminPermission(value = {"admin:paycash:api"})
    @RequestMapping("/merchantClientCashSubmit")
    public @ResponseBody
    Message merchantClientCashSubmit(Long id, Integer payType, Long clientId, BigDecimal money,
                                     @RequestParam(required = false) BigDecimal clientCommission, @RequestParam(required = false) BigDecimal clientRealMoney,
                                     @RequestParam(required = false) Long modelId,
                                     @RequestParam(required = false) String remark,
                                     HttpServletRequest request) {
        if (id == null || payType == null || clientId == null || money == null) {
            return Message.error("参数错误");
        }

        try {
            if (money.compareTo(BigDecimal.ZERO) <= 0) {
                return Message.error("提现金额有误");
            }

            if (clientCommission == null) clientCommission = BigDecimal.ZERO;
            if (clientRealMoney == null) clientRealMoney = BigDecimal.ZERO;

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayCash cash = payCashService.querySingle(params);
            if (cash == null) {
                return Message.error("提现记录不存在");
            }
            if (cash.getPayStatus().intValue() != 3) {
                return Message.error("只能代付审核通过的提现订单");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (cash.getRemark() == null) {
                cash.setRemark("");
            }
            remark = cash.getRemark() + " <span style='color:green;'>" + sdf.format(new Date()) + " 发起" + (payType.intValue() == 0 ? "人工" : "API") + "代付:" + remark + "</span><br/>" ;
            cash.setRemark(remark);

            if (payType.intValue() == 0) {//人工代付
                //查询上游信息
                SysUser client = sysUserService.querySysUserById(clientId);
                if (client == null) {
                    return Message.error("上游不存在");
                }

                BigDecimal money1 = client.getMoney() == null ? BigDecimal.ZERO : client.getMoney();
                BigDecimal frozenMoney = client.getFrozenMoney() == null ? BigDecimal.ZERO : client.getFrozenMoney();
                if (money1.compareTo(money) < 0 || money1.subtract(frozenMoney).compareTo(money) < 0) {
                    return Message.error("可提现金额不足！");
                }

                payCashService.merchantClientCashSubmit(cash, client, payType, money, clientCommission, clientRealMoney);
            } else if (payType.intValue() == 1) {//API代付
                if (modelId == null) {
                    return Message.error("不支持的代付模式.");
                }

                clientId = modelId;

                //查询上游信息
                SysUser client = sysUserService.querySysUserById(clientId);
                if (client == null) {
                    return Message.error("上游不存在");
                }

                BigDecimal money1 = client.getMoney() == null ? BigDecimal.ZERO : client.getMoney();
                BigDecimal frozenMoney = client.getFrozenMoney() == null ? BigDecimal.ZERO : client.getFrozenMoney();
                if (money1.compareTo(money) < 0 || money1.subtract(frozenMoney).compareTo(money) < 0) {
                    return Message.error("可提现金额不足！");
                }
                cash.setClientMoney(money);
                //获取模块键名
                String keyname = client.getPaykey();

                //获取模块对象
                Map<String, Object> p = new HashMap<String, Object>();
                p.put("keyname", keyname);
                PayClientModel pcm = payClientModelService.querySingle(p);
                if (pcm == null || pcm.getStatus().intValue() != 1 || pcm.getPayStatus().intValue() != 1) {
                    return Message.error("代付模块不可用！");
                }

                GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(keyname);
                if (gs == null || gs.getPayOrderParams() == null || gs.getDealPayOrderData() == null) {
                    return Message.error("代付模块不可用！");
                }

                Binding binding = new Binding();
                binding.setProperty("cash", cash);
                binding.setProperty("client", client);
                if (request != null) {
                    binding.setProperty("domain", request.getAttribute("serverName").toString());
                } else {
                    binding.setProperty("domain", "自动代付-本地发起");
                }
                binding.setProperty("urlcash", client.getUrlcash());
                binding.setProperty("keyname", keyname);

                Object data = GroovyUtil.runScript(gs.getPayOrderParams(), binding);
                if (data == null) {
                    return Message.error("生成代付参数失败!");
                }

                //代付参数处理失败
                Map<String, Object> dfParams = (Map<String, Object>) data;
                if (dfParams.get("dferror") != null) {
                    return Message.error("发起代付失败:" + dfParams.get("dferror"));
                }
                RequestType reqType=RequestType.POST;
                if (dfParams.get("reqtype") != null) {
                    reqType=RequestType.GET;
                    dfParams.remove("reqtype");
					System.out.println("RequestType.GET:更改请求类型。删除reqtype"+new Gson().toJson(params));
                }
                //发起请求
                CashInfo cinfo = HttpUtil.requestData_Cash(client.getUrlcash(), reqType, (Map<String, Object>) dfParams,
                        new HashMap<String, String>(), "utf-8", cash.getSn(), pcm.getReqType().intValue(), gs.getDealPayOrderData());

//                ClientChannel clientChannel = ChannelManager.channels.get(keyname);
//              //发起代付请求
//                Map < String,Object > data=new TreeMap<String,Object>();
//                data.put("money", money);
//                CashInfo cinfo=clientChannel.clientCash(data,cash, client,request.getAttribute("serverName").toString(),client.getUrlcash(),keyname);

                if (!cinfo.getStatus()) {
                    //TODO: 代付请求测试
                    return Message.error(cinfo.getMsg());
                }
                cash.setClientSn(cinfo.getSn());

                payCashService.merchantClientCashSubmit(cash, client, payType, money, clientCommission, clientRealMoney);
            } else {
                return Message.error("不支持的代付模式");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
        return Message.success();
    }


    @AdminPermission(value = {"admin:paycash:suc"})
    @RequestMapping("/cashSuc")
    public @ResponseBody
    Message cashSuc(Long id, String remark) {
        if (id == null) {
            return Message.error("参数错误");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayCash cash = payCashService.querySingle(params);
            if (cash == null) {
                return Message.error("提现记录不存在");
            }
            if (cash.getPayStatus().intValue() != 4) {
                return Message.error("只能将处理中的提现订单设置为成功");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (cash.getRemark() == null) cash.setRemark("");
            remark = cash.getRemark() + " <span style='color:green;'>" + sdf.format(new Date()) + " 后台操作提现成功：" + remark + "</span><br/>" ;
            cash.setRemark(remark);

            payCashService.cashSuc(cash);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:paycash:fail"})
    @RequestMapping("/cashFail")
    public @ResponseBody
    Message cashFail(Long id, String remark) {
        if (id == null) {
            return Message.error("参数错误");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayCash cash = payCashService.querySingle(params);
            if (cash == null) {
                return Message.error("提现记录不存在");
            }
            if (cash.getPayStatus().intValue() != 4) {
                return Message.error("只能将处理中的提现订单设置为失败");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (cash.getRemark() == null) cash.setRemark("");
            remark = cash.getRemark() + " <span style='color:red;'>" + sdf.format(new Date()) + " 提现失败：" + remark + "</span><br/>" ;
            cash.setRemark(remark);

            payCashService.cashFail(cash);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
        return Message.success();
    }
        //kahn 2022.6.17提现驳回
        @AdminPermission(value = {"admin:paycash:refuse"})
        @RequestMapping("/cashRefuse")
        public @ResponseBody
        Message cashRefuse(Long id, String remark) {
            if (id == null) {
                return Message.error("参数错误");
            }
            try {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("id", id);
                PayCash cash = payCashService.querySingle(params);
                if (cash == null) {
                    return Message.error("提现记录不存在");
                }
                if (cash.getPayStatus().intValue() != 4) {
                    return Message.error("只能将处理中的提现订单设置为提现驳回");
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (cash.getRemark() == null) cash.setRemark("");
                remark = cash.getRemark() + " <span style='color:red;'>" + sdf.format(new Date()) + " 提现驳回：" + remark + "</span><br/>" ;
                cash.setRemark(remark);
    
                payCashService.cashRefuse(cash);
            } catch (Exception e) {
                e.printStackTrace();
                return Message.error(e.getMessage());
            }
            return Message.success();
        }

    @AdminPermission(value = {"admin:paycash:xy"})
    @RequestMapping("/sendNotify")
    public @ResponseBody
    Message sendNotify(Long id) {
        if (id == null) {
            return Message.error("参数错误!");
        }
        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayCash po = payCashService.querySingle(params);
            if (po == null) {
                return Message.error("无效订单!");
            }

            if (StringUtils.isBlank(po.getNotifyUrl())) {
                return Message.error("无效通知地址!");
            }

            payCashService.sendCashNotify(po);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success("已通知商户！");
    }


    /**
     * 导出excel
     *
     * @param param
     * @param orders
     * @param excelCols 表头
     * @author:nb
     */
    @AdminPermission(value = {"admin:paycash:export"})
    @RequestMapping("/exportExcel")
    public @ResponseBody
    Message exportExcel(@RequestParam Map<String, Object> param, @RequestParam(value = "orders[]", required = false) String[] orders, String excelCols, HttpServletRequest request) {
        try {

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            
            //request.getAttribute("serverName") + "/"
            new PayExcelportThread("提现订单" + sdf.format(new Date()), (String)  "../../", excelCols, staticFolder, sysUser.getId(), new PayExcelexportDataService() {

                @Override
                public List<Map<String, Object>> getExprotData(Map<String, Object> param, String[] orders) {
                    return payCashService.queryExportList(param, orders);
                }
            }, payExcelexportService, param, orders, 1).start();
            return Message.success("启动成功");
//            List < Map < String, Object > > queryExportList = payCashService.queryExportList(param, orders);
//            Poi4ExcelExportManager pem=Poi4ExcelExportManager.Builder()
//                    .fileName("提现订单")
//                    .outModel(Constants.MODEL_OUTPUT_EXCEL_LARGE)
//                    .excelSaveFolder(staticFolder+"excelExport")
//                    .cellPropertys(excelCols)
//                    .fileModel(Constants.MODEL_FILE_CREATE_RANDOM)
//                    ;
//            String filePath=pem.createExcel(queryExportList);
////            String filePath=ExcelExportManager.Builder().fileName("提现订单").excelType(Constants.EXCEL_TYPE_XSSF).createExcel(staticFolder+"excelExport", excelCols, queryExportList);
//            System.out.println("导出文件路径："+filePath);
//            //将filePath转换为url
////            String urlPre=(String)request.getAttribute("serverName");
//            String url=(String)request.getAttribute("serverName")+"/"+filePath.substring(staticFolder.length()).replace("\\", "/");
//            System.out.println(url);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return Message.error("导出失败");
    }

    /**
     * 商户数据导出
     *
     * @param param
     * @param orders
     * @param excelCols
     * @param request
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:paycash:merchantExport", "admin:payorder:proxyExport"})
    @RequestMapping("/exportExcelMerchant")
    public @ResponseBody
    Message exportExcelMerchant(@RequestParam Map<String, Object> param, @RequestParam(value = "orders[]", required = false) String[] orders, String excelCols, HttpServletRequest request) {

        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            param.put("merchantId", sysUser.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            //request.getAttribute("serverName") + "/"
            new PayExcelportThread("商户提现订单" + sdf.format(new Date()), "../../" , excelCols, staticFolder, sysUser.getId(), new PayExcelexportDataService() {

                @Override
                public List<Map<String, Object>> getExprotData(Map<String, Object> param, String[] orders) {
                    return payCashService.queryExportListMerchant(param, orders);
                }
            }, payExcelexportService, param, orders, 1).start();
            return Message.success("启动成功");

//            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
//            param.put("merchantId", sysUser.getId());
//            List < Map < String, Object > > queryExportList = payCashService.queryExportListMerchant(param, orders);
//
//            Poi4ExcelExportManager pem=Poi4ExcelExportManager.Builder()
//                    .fileName("商户提现订单")
//                    .outModel(Constants.MODEL_OUTPUT_EXCEL_LARGE)
//                    .excelSaveFolder(staticFolder+"excelExport")
//                    .cellPropertys(excelCols)
//                    .fileModel(Constants.MODEL_FILE_CREATE_RANDOM)
//                    ;
//            String filePath=pem.createExcel(queryExportList);
////            String filePath=ExcelExportManager.Builder().fileName("商户提现订单").excelType(Constants.EXCEL_TYPE_XSSF).createExcel(staticFolder+"excelExport", excelCols, queryExportList);
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

    /**
     * 查询提现订单详情
     *
     * @param id
     * @param sn
     * @param model
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:paycash:list"})
    @RequestMapping("/detail")
    public String detail(Long id, String sn, Model model) {
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("sn", sn);

        model.addAttribute("order", payCashService.queryCashDetail(params));
        return "admin/pay/paycash/detail" ;
    }

    @AdminPermission(value = {"admin:paycash:list"})
    @RequestMapping("/autoConfig")
    public String autoConfig(Model model, HttpServletRequest request) {
        model.addAttribute("autoDaiFu", ApplicationData.getInstance().getConfig().getAutoDaiFu() == 1);
        model.addAttribute("curModel", payCashService.queryAutoPayModule());
        model.addAttribute("modelList", payCashService.queryClientInfoByModel());
        return "admin/pay/paycash/autoConfig" ;
    }

    @AdminPermission(value = {"admin:paycash:list"})
    @RequestMapping("/autoConfigSubmit")
    public @ResponseBody
    Message autoConfigSubmit(Long modelId, boolean autoDaiFu, HttpServletRequest request) {

        configService.updateAutoDaiFu(autoDaiFu ? 1 : 0);
        ApplicationData.getInstance().setConfig(configService.querySingle(new HashMap<String, Object>()));

        if (autoDaiFu) {
            //查询上游是否接入自动回调:
            SysUser client = sysUserService.querySysUserById(modelId);
            String keyname = client.getPaykey();
            Map<String, Object> p = new HashMap<String, Object>();
            p.put("keyname", keyname);
            PayClientModel pcm = payClientModelService.querySingle(p);
            if (pcm == null || pcm.getStatus().intValue() != 1 || pcm.getPayStatus().intValue() != 1) {
                return Message.error("代付模块不可用！");
            }

            GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(keyname);
            if (gs == null || gs.getPayOrderParams() == null || gs.getDealPayOrderData() == null) {
                return Message.error("代付模块不可用！");
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("modelID", modelId);
            params.put("date", new Date());

            payCashService.updateAutoModel(params);
        }
        return Message.success();
    }

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:paycash:proxyList"})
    @RequestMapping("/proxyView")
    public String proxyView() {
        return "admin/pay/paycash/proxy_list" ;
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
    @AdminPermission(value = {"admin:paycash:proxyList"})
    @RequestMapping("/proxyList")
    public @ResponseBody
    PageResult proxyList(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("merchantId", sysUser.getId());
            // 分页查询
            payCashService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }


    @AdminPermission(value = {"admin:paycash:add"})
    @RequestMapping("/addBatch")
    public String addBatch(Model model, HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);

            SysUser user = sysUserService.querySysUserById(sysUser.getId());
            BigDecimal money = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
            BigDecimal frozenMoney = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
            model.addAttribute("money", money);
            model.addAttribute("frozenMoney", frozenMoney);
            model.addAttribute("amount", money.subtract(frozenMoney));

            String cashConfig = user.getCashConfig();
            if (StringUtils.isNotBlank(cashConfig)) {
                CashConfig cc = new Gson().fromJson(cashConfig, CashConfig.class);
                model.addAttribute("cashConfig", cc);
            }
            model.addAttribute("minCommission", sysUser.getMinCommission() == null ? 0 : sysUser.getMinCommission());
            model.addAttribute("dfminCommission", sysUser.getDfminCommission() == null ? 0 : sysUser.getDfminCommission());

        } catch (UserNotFoundException e) {

            e.printStackTrace();
            model.addAttribute("error", "操作异常:" + e.getMessage());
        }

        return "admin/pay/paycash/merchant_batch" ;
    }

    @AdminPermission(value = {"admin:paycash:add"})
    @RequestMapping("/addSaveBatch")
    public String addSaveBatch(Model model, @RequestParam(value = "efile", required = true) MultipartFile efile,
                               HttpServletRequest request, String payPwd) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);

            SysUser user = sysUserService.querySysUserById(sysUser.getId());
            BigDecimal money1 = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
            BigDecimal frozenMoney1 = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
            model.addAttribute("money", money1);
            model.addAttribute("frozenMoney", frozenMoney1);
            model.addAttribute("amount", money1.subtract(frozenMoney1));

            String cashConfig = user.getCashConfig();
            if (StringUtils.isNotBlank(cashConfig)) {
                CashConfig cc = new Gson().fromJson(cashConfig, CashConfig.class);
                model.addAttribute("cashConfig", cc);
            }
            model.addAttribute("minCommission", sysUser.getMinCommission() == null ? 0 : sysUser.getMinCommission());
            model.addAttribute("dfminCommission", sysUser.getDfminCommission() == null ? 0 : sysUser.getDfminCommission());

            if (user.getType().intValue() != 2 && user.getType().intValue() != 1) {
                model.addAttribute("errorinfo", "您的操作已记录.");
                return "admin/pay/paycash/merchant_batch" ;
            }

            if (StringUtils.isBlank(user.getPayword())) {
                model.addAttribute("errorinfo", "支付密码尚未设置！");
                return "admin/pay/paycash/merchant_batch" ;
            }
            if (!MD5Util.MD5Encode(payPwd, "UTF-8").equals(user.getPayword())) {
                model.addAttribute("errorinfo", "支付密码错误！");
                return "admin/pay/paycash/merchant_batch" ;
            }

            int index = efile.getOriginalFilename().lastIndexOf(".");
            if (index == -1) {
                model.addAttribute("errorinfo", "你特么上传的什么？");
                return "admin/pay/paycash/merchant_batch" ;
            }
            String type = efile.getOriginalFilename().substring(index + 1).toLowerCase();
            if (",xls,xlsx,".indexOf("," + type + ",") == -1) {
                model.addAttribute("errorinfo", "请上传Excel文件,文件名后缀为xls或者xlsx");
                return "admin/pay/paycash/merchant_batch" ;
            }

            //读取excel
            Workbook workbook = null;
            if (type.equals("xls")) {//03
                workbook = new HSSFWorkbook(efile.getInputStream());
            } else {
                workbook = new XSSFWorkbook(efile.getInputStream());
            }
            Sheet sheet = workbook.getSheetAt(0);
            //获取最后一行
            int endRow = sheet.getLastRowNum();

            int suc = 0;
            int total = 0;

            for (int i = 1; i <= endRow; i++) {//循环获取excel内容并处理
                String errorInfo = "" ;
                total++;
                Row row = sheet.getRow(i);
                try {
                    //银行卡号
                    String cardNo = getCellStringValue(row.getCell(0));
                    if (StringUtils.isBlank(cardNo)) {
                        errorInfo += "为什么不填银行卡号?" ;
                    }
                    System.out.println("卡号:" + cardNo);
                    //持卡人姓名
                    String accountName = getCellStringValue(row.getCell(1));
                    if (StringUtils.isBlank(accountName)) {
                        errorInfo += "为什么不填持卡人姓名?" ;
                    }
                    //银行名
                    String bankName = getCellStringValue(row.getCell(2));
                    if (StringUtils.isBlank(bankName)) {
                        errorInfo += "为什么不填银行名?" ;
                    }
                    //开户行省份
                    String province = getCellStringValue(row.getCell(3));
                    if (StringUtils.isBlank(province)) {
                        errorInfo += "为什么不填开户行省份?" ;
                    }
                    //开户行城市
                    String city = getCellStringValue(row.getCell(4));
                    if (StringUtils.isBlank(city)) {
                        errorInfo += "为什么不填开户行城市?" ;
                    }
                    //支行名
                    String bankSubName = getCellStringValue(row.getCell(5));
                    if (StringUtils.isBlank(bankSubName)) {
                        errorInfo += "为什么不填支行名?" ;
                    }
                    //提现类型
                    String channelTypeStr = getCellStringValue(row.getCell(6));
                    int channelType = 0;
                    if (StringUtils.isBlank(channelTypeStr)) {
                        errorInfo += "为什么不填提现类型?" ;
                    } else {
                        if ("代付".equals(channelTypeStr)) {
                            channelType = 1;
                        } else if ("下发".equals(channelTypeStr)) {
                            channelType = 0;
                        } else {
                            errorInfo += "为什么乱填提现类型?" ;
                        }
                    }

                    Cell cell = row.getCell(7);
                    BigDecimal money = BigDecimal.ZERO;
                    if (cell == null) {
                        errorInfo += "为什么不填提现金额?" ;
                    } else {
                        CellType ctype = cell.getCellType();
                        if (ctype == CellType.NUMERIC) {
                            money = new BigDecimal(cell.getNumericCellValue());
                            if (money.compareTo(BigDecimal.ZERO) <= 0) {
                                errorInfo += "提现金额为什么不大于0,你在想什么?" ;
                            }
                        } else {
                            String mstr = cell.getStringCellValue();
                            if (StringUtils.isBlank(bankSubName)) {
                                errorInfo += "为什么不填提现金额,要我猜?" ;
                            } else {
                                try {
                                    money = new BigDecimal(mstr).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    if (money.compareTo(BigDecimal.ZERO) <= 0) {
                                        errorInfo += "提现金额为什么不大于0,你在想什么?" ;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    errorInfo += "提现金额为什么乱填?" ;
                                }
                            }
                        }
                    }


                    if (StringUtils.isBlank(errorInfo)) {//没有异常数据,创建提现订单
                        String data = createCashOrder(user, cardNo.trim(), accountName.trim(), bankName.trim(), province.trim(), city.trim(), bankSubName.trim(), channelType, money);
                        if (StringUtils.isBlank(data)) {
                            errorInfo = "提交成功!" ;
                            suc++;
                        } else {
                            errorInfo += data;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    errorInfo += "这条数据有问题:" + e.getMessage();
                }

                Cell cell = row.createCell(8);
                cell.setCellValue(errorInfo);
            }


            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + type;

            File f = new File(this.staticFolder + "excelUpload/");
            if (!f.exists()) {
                f.mkdirs();
            }
            String filePath = this.staticFolder + "excelUpload/" + fileName;

            FileOutputStream stream = new FileOutputStream(filePath);
            workbook.write(stream);
            stream.close();
            workbook.close();

            String url =  request.getAttribute("serverName") + "/" + filePath.substring(staticFolder.length()).replace("\\", "/");

            model.addAttribute("downloadUrl", url);
            model.addAttribute("suc", suc);
            model.addAttribute("error", total - suc);
            model.addAttribute("total", total);


            user = sysUserService.querySysUserById(sysUser.getId());
            money1 = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
            frozenMoney1 = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
            model.addAttribute("money", money1);
            model.addAttribute("frozenMoney", frozenMoney1);
            model.addAttribute("amount", money1.subtract(frozenMoney1));

            cashConfig = user.getCashConfig();
            if (StringUtils.isNotBlank(cashConfig)) {
                CashConfig cc = new Gson().fromJson(cashConfig, CashConfig.class);
                model.addAttribute("cashConfig", cc);
            }
            model.addAttribute("minCommission", sysUser.getMinCommission() == null ? 0 : sysUser.getMinCommission());
            model.addAttribute("dfminCommission", sysUser.getDfminCommission() == null ? 0 : sysUser.getDfminCommission());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorinfo", "操作异常：" + e.getMessage().replace("\\", "\\\\"));
        }
        return "admin/pay/paycash/merchant_batch" ;
    }


    public String createCashOrder(SysUser user, String cardNo, String accountName, String bankName,
                                  String province, String city, String bankSubName, int channelType, BigDecimal money) {
        try {
            BigDecimal minCommission = user.getMinCommission();
            String cname = "下发" ;
            if (channelType == 1) {
                minCommission = user.getDfminCommission();
                cname = "代付" ;
            }

            if (minCommission != null && minCommission.compareTo(BigDecimal.ZERO) > 0 && money.compareTo(minCommission) < 0) {
                return "最小" + cname + "金额为" + minCommission + "元" ;
            }

            //代理佣金
            BigDecimal proxyCommission = BigDecimal.ZERO;
            //手续费
            BigDecimal commission = BigDecimal.ZERO;

            CashConfig config = new CashConfig();
            String cashConfig = user.getCashConfig();
            if (StringUtils.isNotBlank(cashConfig)) {
                try {
                    config = new GsonBuilder().serializeNulls().create().fromJson(cashConfig, CashConfig.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //获取费率
            PayRatio ratio = new PayRatio();
            if (channelType == 0) {//下发
                ratio = config.getBankRatio();
            } else {//代付
                ratio = config.getDfbankRatio();
            }
            BigDecimal h = new BigDecimal(100);
            //计算用户手续费
            commission = money.multiply(ratio.getRatioCommission()).divide(h).add(ratio.getCommission());
            //计算代理佣金
            proxyCommission = money.multiply(ratio.getProxyRatioCommission()).divide(h).add(ratio.getProxyCommission());
            if (proxyCommission.compareTo(commission) > 0) {
                return cname + "费率配置有误！" ;
            }

            BigDecimal realMoney = money;
            //实际扣减金额
            money = money.add(commission);

            BigDecimal money1 = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
            BigDecimal frozenMoney = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
            if (money1.compareTo(money) < 0 || money1.subtract(frozenMoney).compareTo(money) < 0) {
                return "可提现金额不足！" ;
            }

            //根据银行名匹配银行编码
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name", bankName);
            PayBankCode bankCode = payBankCodeService.querySingle(params);
            if (bankCode == null) {
                return "银行名不对！" ;
            }

            PayCash payCash = new PayCash();
            payCash.setMerchantId(user.getId());
            payCash.setCashType(0);
            payCash.setAmount(money);
            payCash.setMerchantSn("");
            payCash.setSn(SnUtil.createSn(ApplicationData.getInstance().getConfig().getCashsnpre()));

            payCash.setBankIfsc("");
            payCash.setBankNation("");
            payCash.setBtype(0);
            payCash.setChannelType(channelType);

            if (user.getType().intValue() == 1) {
                payCash.setUserType(1);
            } else {
                payCash.setUserType(0);
            }

            payCash.setCommission(commission);
            payCash.setRealMoney(realMoney);

            if (user.getProxyId() != null) {
                payCash.setProxyId(user.getProxyId());
                payCash.setProxyMoney(proxyCommission);
            }

            payCash.setBankAccno(cardNo);
            payCash.setBankAccname(accountName);
            payCash.setBankName(bankName);
            payCash.setBankSubname(bankSubName);
            payCash.setBankId("");
            payCash.setBankAccid("");
            payCash.setBankAccmobile("");
            payCash.setBankProvince(province);
            payCash.setBankCity(city);

            payCash.setBankCode(bankCode.getCode());

            payCash.setNotifyUrl("");
            payCash.setRemark("");
            payCash.setStatus(0);
            payCash.setPayStatus(0);

            payCashService.createCash(payCash, user, user.getType().intValue() == 1 ? 0 : 1, false);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return "" ;
    }

    private String getCellStringValue(Cell cell) {
        return cell == null ? null : cell.getStringCellValue();
    }
}
