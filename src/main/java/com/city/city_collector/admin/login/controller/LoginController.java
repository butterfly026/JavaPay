package com.city.city_collector.admin.login.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.AdminPermissionManager;
import com.city.adminpermission.exception.PermissionValidateFailException;
import com.city.adminpermission.exception.UserNotFoundException;
import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.city.util.ApplicationData;
import com.city.city_collector.admin.pay.dao.PayCashDao;
import com.city.city_collector.admin.pay.service.PayCashService;
import com.city.city_collector.admin.pay.service.PayClientChannelService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.util.TimeUtil;
import com.city.city_collector.common.bean.SystemConfig;
import com.city.city_collector.common.util.CommonUtil;
import com.city.city_collector.common.util.GoogleGenerator;
import com.city.city_collector.common.util.MD5Util;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.util.SingleLoginUtil;
import com.city.city_collector.common.util.ValidateCodeUtil;
import com.city.city_collector.common.util.ZxingUtil;
import com.city.city_collector.configuration.SpringUtil;
import com.city.city_collector.paylog.PayLogManager;
import com.google.gson.Gson;

@Controller
@RequestMapping({"/login"})
public class LoginController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    PayClientChannelService payClientChannelService;

    @Autowired
    PayCashService payCashService;

//    private final static int IMG_COUNT=40;

    @RequestMapping(value = {"/login"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String loginGet(HttpServletRequest request, Model model) {
        if (needValidateCode(request)) {
            ValidateCodeUtil codeUtil = new ValidateCodeUtil();
            codeUtil.createRndImage();
            HttpSession session = request.getSession();
            session.setAttribute("USER_LOGIN_VALIDATECODE", codeUtil.getResult());
            session.setMaxInactiveInterval(120);
            model.addAttribute("validateCode", codeUtil.getBaseString());
        }

        model.addAttribute("bg", getImgname());

        return "/admin/login" + ApplicationData.getInstance().getConfig().getIndexmodel();
    }

    @RequestMapping({"/login"})
    @ResponseBody
    public Message loginPost(String userName, String pwd, String validCode, String key, HttpServletRequest request,
                             HttpServletResponse response) {

        String ip = CommonUtil.getIp(request);
        PayLogManager.getInstance().createLoginLog("用户" + userName + "登录,IP:" + ip, "{userName:'" + userName + "',ip:'" + ip + "'}");

        if ((StringUtils.isBlank(userName)) || (StringUtils.isBlank(pwd)))
            return Message.error("用户名、密码不能为空");
        try {
            ValidateCodeUtil codeUtil = new ValidateCodeUtil();

            HttpSession session = request.getSession();
            if (session.getAttribute("USER_LOGIN_VALIDATECODE") != null) {
                if (StringUtils.isBlank(validCode)) {
                    return Message.error("请输入验证码");
                }
                if (!validCode.equals(session.getAttribute("USER_LOGIN_VALIDATECODE").toString())) {
                    dealValidateCode(request, response, codeUtil);
                    return Message.error("验证码输入有误，请重新输入！", codeUtil.getBaseString());
                }
                session.removeAttribute("USER_LOGIN_VALIDATECODE");
            }

            SysUser sysUser = this.sysUserService.querySysUserByUsername(userName);
            if (sysUser == null) {
                dealValidateCode(request, response, codeUtil);
                return Message.error("用户名或密码错误，登录失败", codeUtil.getBaseString());
            }
            //判断用户IP是否合法
//            if (sysUser.getType() != null && sysUser.getType() == 2) {
//
//                if (StringUtils.isNotBlank(sysUser.getAdminip())) {
//                    String[] ips = sysUser.getAdminip().split(",");
//                    String uip = CommonUtil.getIp(request);
//                    boolean flag = false;
//                    for (int i = 0; i < ips.length; i++) {
//                        if (ips[i].equals(uip)) {
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if (!flag) {
//                        return Message.error("IP禁止访问");
//                    }
//                }
//            }

            if (StringUtils.isNotBlank(sysUser.getAdminip())) {
                String[] ips = sysUser.getAdminip().split(",");
                String uip = CommonUtil.getIp(request);
                boolean flag = false;
                for (int i = 0; i < ips.length; i++) {
                    if (ips[i].equals(uip)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return Message.error("IP禁止访问");
                }
            }

            //开启谷歌验证
            if (sysUser.getValidStatus() != null && sysUser.getValidStatus().intValue() == 1) {
                if (StringUtils.isBlank(key) || !new GoogleGenerator().check_code(sysUser.getValidCode(), key, System.currentTimeMillis())) {
                    return Message.error("谷歌验证动态口令错误！");
                }
            }

            if (!MD5Util.MD5Encode(pwd, "UTF-8").equals(sysUser.getPassword())) {
                dealValidateCode(request, response, codeUtil);
                PayLogManager.getInstance().createLoginFailLog("用户" + userName + "登录失败,IP:" + ip + ",密码:" + pwd, "{userName:'" + userName + "',ip:'" + ip + "',password:'" + pwd + "'}");
                return Message.error("用户名或密码错误，登录失败", codeUtil.getBaseString());
            }
            if ((sysUser.getStatus() != null) && (!"1".equals(sysUser.getStatus()))) {
                return Message.error("用户被禁用，无法登录");
            }

            if (SystemConfig.getInstance().isSingleUser()) {
                sysUser.setVersion(
                        Integer.valueOf(sysUser.getVersion() == null ? 1 : sysUser.getVersion().intValue() + 1));
                if (sysUser.getVersion().intValue() > 99999999) {
                    sysUser.setVersion(Integer.valueOf(0));
                }
            }

            SysUser updUser = new SysUser();
            updUser.setLastLoginDate(new Date());
            updUser.setVersion(sysUser.getVersion());
            updUser.setId(sysUser.getId());
            this.sysUserService.editSave(updUser);

            if (SystemConfig.getInstance().isSingleUser()) {
                SingleLoginUtil.updateUserVersion(request, sysUser.getId(), sysUser.getVersion());
            }

            List permissions = this.sysUserService.queryUserPermission(sysUser.getId());
            String[] pms = null;
            if ((permissions != null) && (!permissions.isEmpty())) {
                for (int i = 0; i < permissions.size(); i++) {
                    if ((null == permissions.get(i)) || ("".equals(permissions.get(i)))) {
                        permissions.remove(i);
                        i--;
                    }
                }
                pms = (String[]) permissions.toArray(new String[0]);
            }
            AdminPermissionManager.putAdminUser(sysUser, pms, request);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("系统异常，操作失败");
        }
        return Message.success("登录成功");
    }

    @RequestMapping({"/main"})
    public String main(Model model, HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            model.addAttribute("name", sysUser.getName());

            String ip = CommonUtil.getIp(request);
            PayLogManager.getInstance().createLoginLog("用户" + sysUser.getName() + "访问首页,IP:" + ip, "");

            if (sysUser.getType() == null) {
                model.addAttribute("url", "normal");
            } else {
                if (sysUser.getType().intValue() == 0) {
                    model.addAttribute("url", "adminIndex");
                } else if (sysUser.getType().intValue() == 1) {
                    model.addAttribute("url", "proxyIndex");
                } else if (sysUser.getType().intValue() == 2) {
                    model.addAttribute("url", "merchantIndex");
                } else if (sysUser.getType().intValue() == 3) {
                    model.addAttribute("url", "normal");
                }
            }

        } catch (UserNotFoundException e) {
            model.addAttribute("bg", getImgname());
            return "/admin/login" + ApplicationData.getInstance().getConfig().getIndexmodel();
        }
        return "/admin/main" + ApplicationData.getInstance().getConfig().getIndexmodel();
    }

    @RequestMapping({"/normal"})
    public String normal(HttpServletRequest request) {
        return "/admin/normal";
    }

    @RequestMapping({"/adminIndex"})
    public String adminIndex(Model model, HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SysUser user = sysUserService.querySysUserById(sysUser.getId());
//            if(sysUser.getType()==null || sysUser.getType().intValue()!=0) {
//                Map < String,Object > params=new HashMap<String,Object>();
//                Map < String,Object > data=sysUserService.queryAdminInfo(params);
//                
//                //当日订单成交额
//                model.addAttribute("data", data);
//                
//                model.addAttribute("dealOrderCount", sysUserService.querySysDealOrder());
//                
//                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                model.addAttribute("time", sdf.format(new Date()));
//                model.addAttribute("channelList", payClientChannelService.queryIndexEcharts());
            model.addAttribute("validStatus", user.getValidStatus());

//                List < String > list=TimeUtil.getDateMinuteByMM(15);
//                StringBuffer sbuf=new StringBuffer(" ");
//                for(String str:list) {
//                    sbuf.append(" union all select '");
//                    sbuf.append(str);
//                    sbuf.append("' d1,0 cou from dual ");
//                }
////                System.out.println(sbuf.toString());
//                sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                params.put("endTime", sdf.format(new Date()));
//                params.put("startTime", list.get(0));
//                params.put("sql", sbuf.toString());
//                model.addAttribute("ordercount", new Gson().toJson(sysUserService.queryOrderCountCharts(params)));
            //cyber 修改 返回内容填充为无意义值
            if (user.getValidStatus() != null && user.getValidStatus().intValue() == 1) {
                //model.addAttribute("img", ZxingUtil.createQRCodeStr(GoogleGenerator.getQRBarcode(user.getUsername(), user.getValidCode())));
                model.addAttribute("img","aaaabbbbccccdddd");
            }
//            }
        } catch (UserNotFoundException e) {
            model.addAttribute("bg", getImgname());
            return "/admin/login" + ApplicationData.getInstance().getConfig().getIndexmodel();
        }
        return "/admin/adminIndex";
    }

    @RequestMapping("/adminData")
    public @ResponseBody
    Message adminData(HttpServletRequest request) {
        try {
            AdminPermissionManager.getUserObject(request);
//            SysUser user=sysUserService.querySysUserById(sysUser.getId());

            Map<String, Object> result = new HashMap<String, Object>();

            Map<String, Object> params = new HashMap<String, Object>();
            Map<String, Object> data = sysUserService.queryAdminInfo(params);

            //当日订单成交额
            result.put("data", data);
            //待处理订单数
            result.put("dealOrderCount", sysUserService.querySysDealOrder());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //最后更新时间
            result.put("time", sdf.format(new Date()));
            //通道数据图表
            result.put("channelList", payClientChannelService.queryIndexEcharts());

            List<String> list = TimeUtil.getDateMinuteByMM(20);
            StringBuffer sbuf = new StringBuffer(" ");
            for (String str : list) {
                sbuf.append(" union all select '");
                sbuf.append(str);
                sbuf.append("' d1,0 cou,0 acou from dual ");
            }
//                System.out.println(sbuf.toString());
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            params.put("endTime", sdf.format(new Date()));
            params.put("startTime", list.get(0));
            params.put("sql", sbuf.toString());
            //订单并发数
            result.put("ordercount", sysUserService.queryOrderCountCharts(params));
            //待处理订单数
            result.put("cashCount", payCashService.queryWaitDealCount());

            //预警数据
            Config config = ApplicationData.getInstance().getConfig();
            if (config.getStaticstime().intValue() == 0) {
                result.put("staticsList", new ArrayList<Integer>());
            } else {
                params = new HashMap<String, Object>();
                String startTime = TimeUtil.getDateSSMinuteBySS(config.getBeforetime() + config.getStaticstime());
                String endTime = TimeUtil.getDateSSMinuteBySS(config.getBeforetime());
                params.put("startTime", startTime);
                params.put("endTime", endTime);

                result.put("staticsList", sysUserService.queryOrderCountAlarm(params));
                result.put("staticsStime", startTime);
                result.put("staticsEtime", endTime);
            }

            return Message.success("操作成功", result);
        } catch (UserNotFoundException e) {
            return Message.error("请登录再试");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error();
    }

    @RequestMapping({"/proxyIndex"})
    public String proxyIndex(Model model, HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SysUser user = sysUserService.querySysUserById(sysUser.getId());

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", sysUser.getId());
            Map<String, Object> data = sysUserService.queryProxyInfo(params);

            //当日订单成交额
            model.addAttribute("data", data);

            model.addAttribute("validStatus", user.getValidStatus());
        } catch (UserNotFoundException e) {
            model.addAttribute("bg", getImgname());
            return "/admin/login" + ApplicationData.getInstance().getConfig().getIndexmodel();
        }
        return "/admin/proxyIndex";
    }

    @RequestMapping({"/merchantIndex"})
    public String merchantIndex(Model model, HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SysUser user = sysUserService.querySysUserById(sysUser.getId());

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", sysUser.getId());
            Map<String, Object> data = sysUserService.queryMerchantInfo(params);

            //当日订单成交额
            model.addAttribute("data", data);

            model.addAttribute("validStatus", user.getValidStatus());
        } catch (UserNotFoundException e) {
            model.addAttribute("bg", getImgname());
            return "/admin/login" + ApplicationData.getInstance().getConfig().getIndexmodel();
        }
        return "/admin/merchantIndex";
    }

    @RequestMapping({"/updateLoginPwd"})
    public @ResponseBody
    Message updateLoginPwd(String password, String password1, String password2, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(password)) {
                return Message.error("原密码不能为空！");
            }
            if (StringUtils.isBlank(password1)) {
                return Message.error("新密码不能为空！");
            }
            if (StringUtils.isBlank(password2)) {
                return Message.error("确认密码不能为空！");
            }
            if (!password1.equals(password2)) {
                return Message.error("新密码和确认密码不一致！");
            }

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SysUser user = sysUserService.querySysUserById(sysUser.getId());

            if (!MD5Util.MD5Encode(password, "UTF-8").equals(user.getPassword())) {
                return Message.error("原密码错误！");
            }

            SysUser upUser = new SysUser();
            upUser.setId(user.getId());
            upUser.setPassword(MD5Util.MD5Encode(password1, "UTF-8"));
            sysUserService.editSave(upUser);
            return Message.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("更新登录密码失败：" + e.getMessage());
        }
    }

    @RequestMapping({"/updatePaywordPwd"})
    public @ResponseBody
    Message updatePaywordPwd(String payword, String payword1, String payword2, HttpServletRequest request) {
        try {

            if (StringUtils.isBlank(payword1)) {
                return Message.error("新密码不能为空！");
            }
            if (StringUtils.isBlank(payword2)) {
                return Message.error("确认密码不能为空！");
            }
            if (!payword1.equals(payword2)) {
                return Message.error("新密码和确认密码不一致！");
            }

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SysUser user = sysUserService.querySysUserById(sysUser.getId());

            if (StringUtils.isNotBlank(user.getPayword())) {
                if (StringUtils.isBlank(payword)) {
                    return Message.error("原密码不能为空！");
                }
//                System.out.println(MD5Util.MD5Encode(payword, "UTF-8"));
                if (!MD5Util.MD5Encode(payword, "UTF-8").equals(user.getPayword())) {
                    return Message.error("原密码错误！");
                }
            }


            SysUser upUser = new SysUser();
            upUser.setId(user.getId());
            upUser.setPayword(MD5Util.MD5Encode(payword1, "UTF-8"));
            sysUserService.editSave(upUser);
            return Message.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("更新支付密码失败：" + e.getMessage());
        }
    }

    @RequestMapping({"/logout"})
    public String logout(HttpServletRequest request, Model model) {
        AdminPermissionManager.removeAdminUser(request);

        model.addAttribute("bg", getImgname());

        return "/admin/login" + ApplicationData.getInstance().getConfig().getIndexmodel();
    }

    @RequestMapping({"/noPermissionMsg"})
    public String noPermissionMsg() {
        return "/admin/nolimit_msg";
    }

    @RequestMapping({"/singleUserLogout"})
    public String singleUserLogout() {
        return "/admin/userLogout_msg";
    }

    @RequestMapping({"/goLogin"})
    public String goLogin() {
        return "/admin/goLogin_msg";
    }

    @RequestMapping({"/validateImage"})
    public void validateImage(HttpServletRequest request, HttpServletResponse response) {
        try {
            ValidateCodeUtil codeUtil = new ValidateCodeUtil();
            codeUtil.createRndImage();
            HttpSession session = request.getSession();
            session.setAttribute("USER_LOGIN_VALIDATECODE", codeUtil.getResult());
            codeUtil.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean needValidateCode(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        long time = 0L;
        int count = 0;
        if (cookies == null) {
            return false;
        }
        for (Cookie cookie : cookies) {
            try {
                if ("JHJASAYSAK".equals(cookie.getName()))
                    time = Long.parseLong(cookie.getValue());
                else if ("HAGHAGSAISGAAHH".equals(cookie.getName()))
                    count = Integer.parseInt(cookie.getValue());
            } catch (NumberFormatException localNumberFormatException) {
            }
        }
        return (count > 3) && (time != 0L) && (System.currentTimeMillis() - time < 1800000L);
    }

    public boolean dealValidateCode(HttpServletRequest request, HttpServletResponse response,
                                    ValidateCodeUtil codeUtil) {
        boolean flag = false;
        Cookie[] cookies = request.getCookies();
        long time = 0L;
        int count = 0;
        if (cookies != null) {
            for (Cookie cookie : cookies)
                try {
                    if ("JHJASAYSAK".equals(cookie.getName()))
                        time = Long.parseLong(cookie.getValue());
                    else if ("HAGHAGSAISGAAHH".equals(cookie.getName()))
                        count = Integer.parseInt(cookie.getValue());
                } catch (NumberFormatException localNumberFormatException) {
                }
        }
        long notime = System.currentTimeMillis();
        if (time != 0L) {
            if ((notime - time < 1800000L) && (count >= 3)) {
                codeUtil.createRndImage();
                request.getSession().setAttribute("USER_LOGIN_VALIDATECODE", codeUtil.getResult());
                request.getSession().setMaxInactiveInterval(120);
                flag = true;
            } else if (notime - time > 1800000L) {
                count = 0;
            }
        }

        count++;
        Cookie cookie = new Cookie("JHJASAYSAK", notime + "");
        response.addCookie(cookie);
        cookie = new Cookie("HAGHAGSAISGAAHH", count + "");
        response.addCookie(cookie);
        return flag;
    }

    private String getImgname() {
//        Random rd=new Random();
//        int cu=rd.nextInt(IMG_COUNT);
//        if(cu==7 || cu==8 || cu==11 || cu==12 || cu==14) {
//            return cu+".gif";
//        }else {
//            return cu+".jpg";
//        }
        return ApplicationData.getInstance().getIndexImgUrl();
    }
}
