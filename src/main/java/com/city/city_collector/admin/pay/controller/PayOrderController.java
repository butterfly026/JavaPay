package com.city.city_collector.admin.pay.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.city.service.ConfigService;
import com.city.city_collector.admin.city.util.Logger;
import com.city.city_collector.admin.pay.entity.*;
import com.city.city_collector.admin.pay.service.*;
import com.city.city_collector.common.util.*;
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
import com.city.adminpermission.exception.UserNotFoundException;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.channel.ChannelManager1;
import com.city.city_collector.channel.PaytmChannelManager;
import com.city.city_collector.channel.bean.GroovyScript;
import com.city.city_collector.channel.bean.QueryOrderInfo;
import com.city.city_collector.channel.bean.QueryOrderResult;
import com.city.city_collector.channel.util.GroovyUtil;
import com.city.city_collector.channel.util.HttpUtil;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.queryorder.QueryOrderManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import groovy.lang.Binding;

/**
 * Description:支付订单-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payOrder")
public class PayOrderController {
    @Autowired
    PayOrderService payOrderService;

    @Autowired
    PayChannelTypeService payChannelTypeService;

    @Autowired
    PayMerchantService payMerchantService;

    @Autowired
    PayClientService payClientService;

    @Autowired
    PayProxyService payProxyService;

    @Autowired
    PayExcelexportService payExcelexportService;

    @Value("${static.folder}")
    private String staticFolder;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    private PaySensitiveLogService paySensitiveLogService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    ConfigService configService;

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:payorder:list"})
    @RequestMapping("/view")
    public String view(Model model) {
        Map<String, Object> params = new HashMap<String, Object>();

        model.addAttribute("clientList", payClientService.querySelectList());
        model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));
        model.addAttribute("merchantList", payMerchantService.querySelectList());
        model.addAttribute("proxyList", payProxyService.querySelectList());
        model.addAttribute("channelNameList", payClientChannelService.queryAllList(params));
        return "admin/pay/payorder/list";
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
    @AdminPermission(value = {"admin:payorder:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payOrderService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:payorder:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        return "admin/pay/payorder/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payOrder
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payorder:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayOrder payOrder) {
        //数据验证
        if (payOrder == null) {
            return Message.error("参数错误");
        }

        try {

            payOrderService.addSave(payOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
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
    @AdminPermission(value = {"admin:payorder:edit"})
    @RequestMapping("/edit")
    public String edit(Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/pay/payorder/edit";
        }
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        //获取需要修改的记录数据
        PayOrder result = payOrderService.querySingle(params);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {
            model.addAttribute("result", result);
        }
        return "admin/pay/payorder/edit";
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
    @AdminPermission(value = {"admin:payorder:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(PayOrder payOrder) {
        //数据验证
        if (payOrder == null) {
            return Message.error("参数错误");
        }


        try {

            payOrderService.editSave(payOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    /**
     * Description:删除记录
     *
     * @param ids 需要删除的记录的ID数组
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payorder:delete"})
    @RequestMapping("/delete")
    public @ResponseBody
    Message delete(@RequestParam(value = "ids[]") Long[] ids) {
        if (ids != null && ids.length == 0) {
            return Message.error("请选择要删除的数据");
        }
        try {

            payOrderService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:payorder:detail"})
    @RequestMapping("/detail")
    public String detail(Long id, String sn, Model model) {
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("sn", sn);
        //获取需要修改的记录数据
        PayOrder result = payOrderService.querySingle(params);
        if (result != null) {
            if (result.getOrderStatus() != null && result.getOrderStatus().intValue() == 1) {
                model.addAttribute("order", payOrderService.queryOrderDetailSuccess(id, sn));
            } else if (result.getOrderStatus() != null && result.getOrderStatus().intValue() == 0) {
                model.addAttribute("order", payOrderService.queryOrderDetailNoPay(id, sn));
            } else {
                model.addAttribute("order", result);
            }
        }

        return "admin/pay/payorder/detail";
    }

    /**
     * 补单
     *
     * @param ids
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:payorder:bd"})
    @RequestMapping("/bd")
    public @ResponseBody
    Message bd(Long id, String key) {
        if (id == null) {
            return Message.error("参数错误!");
        }
        if(key == null || key == "")
            return Message.error("fuck-谷歌参数为空你不怕吗!");
        

        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            String roleid = sysUserService.queryUserRoleIds(sysUser.getId());
            //cynb修改 大于1，2角色类型的都不允许操作补单
            //6,7,10:非平台下发，平台运营，商务不允许补单
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"BD - roleid1:"+roleid);
            roleid = roleid.split(",")[0];
            Logger.getInstance().Log(Logger.LOG_LEVEL_INFO,"BD - roleid2:"+roleid);
            Integer rid=Integer.parseInt(roleid);

            if(rid>2 && rid!=6 && rid!=7 && rid!=10)
            {
                return Message.error("补单失败102！");
            }

           
            if (org.apache.commons.lang3.StringUtils.isBlank(key) || !new GoogleGenerator().check_code(sysUser.getValidCode(), key, System.currentTimeMillis())) {
                return Message.error("谷歌验证动态口令错误！");
            }

            Map<String, Object> params1 = new HashMap<String, Object>();
            params1.put("id", id);
            //获取需要修改的记录数据
            Config result = configService.querySingle(params1);
            if (result == null || result.getBd() == 1) {
                return Message.error("补单关闭！");
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayOrder po = payOrderService.queryMendOrder(params);
            if (po == null) {
                return Message.error("无效订单!");
            }

            po.setOrderStatus(1);
            po.setMend(1);
            po.setPayTime(new Date());
            payOrderService.updateOrderMendStatus(po);

            PaySensitiveLog paySensitiveLog = new PaySensitiveLog(sysUser.getId(), sysUser.getUsername(), new Date(), "bd", "操作补单", po.getSn(), CommonUtil.getIp(request));
            paySensitiveLogService.insertLog(paySensitiveLog);

//            payOrderService.mendOrderStatus(po);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success("补单成功！");
        
    }

    /**
     * 远程查单
     *
     * @param id
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:payorder:bd"})
    @RequestMapping("/cd")
    public @ResponseBody
    Message cd(Long id) {
        if (id == null) {
            return Message.error("参数错误!");
        }
        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayOrder po = payOrderService.querySingle1(params);
            if (po == null) {
                return Message.error("无效订单!");
            }
            if (po.getOrderStatus().intValue() != 0) {
                return Message.error("只能查询待支付状态的订单!");
            }

            if (po.getOrderType() == null || po.getOrderType() == 0) {
                params.put("id", po.getClientChannelId());
                PayClientChannel pcc = payClientChannelService.querySingle(params);
                if (pcc == null) {
                    return Message.error("查单通道不存在!");
                }

                Map<String, Object> channelParams = new HashMap<String, Object>();
                if (StringUtils.isNotBlank(pcc.getParams())) {
                    try {
                        channelParams = new Gson().fromJson(pcc.getParams(), new TypeToken<HashMap<String, Object>>() {
                        }.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                SysUser user = sysUserService.querySysUserById(po.getClientId());
                if (user == null) {
                    return Message.error("上游不存在!");
                }

                String keyname = pcc.getKeyname();
                GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(keyname);
                if (gs == null || gs.getQueryOrderData() == null || gs.getDealQueryOrderData() == null) {
                    return Message.error("此上游查单模块暂不可用，请去上游后台查单！");
                }
                //获取上游通道的处理脚本
                //订单对象，通道对象，上游对象，通道参数
                Binding binding = new Binding();
                binding.setProperty("order", po);
                binding.setProperty("clientChannel", pcc);
                binding.setProperty("client", user);
                binding.setProperty("channelParams", channelParams);

                Object data = GroovyUtil.runScript(gs.getQueryOrderData(), binding);
                if (data == null) {
                    return Message.error("数据异常，查单失败!");
                }
                QueryOrderInfo qoi = (QueryOrderInfo) data;
                if (!qoi.isNeedRequest()) {//不需要发起请求

                    if (qoi.isOrderSuccess()) {//订单支付成功
                        po.setPayTime(new Date());
                        po.setClientSn(qoi.getClientSn());
                        po.setOrderStatus(1);

                        payOrderService.updateOrderPayStatus(po);
                        return Message.success("查单成功,订单已支付!");
                    } else {
                        return Message.error("订单未支付!");
                    }
                } else {

                    QueryOrderResult qos = HttpUtil.requestData_QueryOrder(user.getUrlquery(), qoi.getRequestType(), qoi.getParams(), new HashMap<String, String>(), qoi.getCharset(),
                            qoi.getReqType(), gs.getDealQueryOrderData(), po, pcc, user, channelParams);
                    if (qos.isStatus()) {//订单支付成功
                        po.setPayTime(new Date());
                        po.setClientSn(qoi.getClientSn());
                        po.setOrderStatus(1);

                        payOrderService.updateOrderPayStatus(po);
                        return Message.success("查单成功,订单已支付!");
                    } else {
                        return Message.error("订单未支付:" + qos.getMsg());
                    }
                }
//                return Message.error("暂时只有Paytm类型订单提供查单功能!");
            } else {
                params.put("id", po.getClientChannelId());
                PayClientChannel pcc = payClientChannelService.querySingle(params);
                if (pcc == null) {
                    return Message.error("通道不存在!");
                }

                //获取秘钥
                String md5 = pcc.getPaytmmd5();
                String uuid = pcc.getPaytmuid();

                String pwd = md5.substring(md5.length() - 7) + uuid.substring(uuid.length() - 4) + PaytmClientChannelController.KEY;

                String key = AESUtil.decrypt(pcc.getPaytmkey(), pwd);

                Map<String, Object> datas = PaytmChannelManager.getInstance().queryOrderStatus(pcc.getPaytmid(), key, po.getSn());
                System.out.println("订单状态:" + datas);

                BigDecimal amount = new BigDecimal((String) datas.get("amount"));

                if ("01".equals(datas.get("code")) && amount.compareTo(po.getAmount()) == 0) {
                    po.setPayTime(new Date());
                    po.setClientSn(datas.get("txnId").toString());
                    po.setOrderStatus(1);

                    payOrderService.updateOrderPayStatus(po);
                    return Message.success("查单成功,订单已支付!");
                } else {
                    String msg = "";
                    if (amount.compareTo(po.getAmount()) != 0) {
                        msg = "(金额不匹配，应付" + amount + ",实付" + po.getAmount() + ")";
                    }
                    return Message.success("查单成功,订单未支付:" + datas.get("msg") + " " + msg);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
    }

    /**
     * 下游
     *
     * @param id
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:payorder:xy"})
    @RequestMapping("/xy")
    public @ResponseBody
    Message xy(Long id) {
        if (id == null) {
            return Message.error("参数错误!");
        }
        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayOrder po = payOrderService.querySingle1(params);
            if (po == null) {
                return Message.error("无效订单!");
            }
            if (po.getOrderStatus() == null || po.getOrderStatus().intValue() != 1) {
                return Message.error("只能通知已成功的订单!");
            }

            payOrderService.sendOrderNotify(po);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success("已通知商户！");
    }

    /**
     * 商户自行通知
     *
     * @param id
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:payorder:merchantXy"})
    @RequestMapping("/merchantXy")
    public @ResponseBody
    Message merchatnXy(Long id, HttpServletRequest request) {
        if (id == null) {
            return Message.error("参数错误!");
        }
        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayOrder po = payOrderService.querySingle1(params);
            if (po == null) {
                return Message.error("无效订单!");
            }
            //判断用户是否拥有订单权限
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            if (po.getMerchantId() == null || !po.getMerchantId().equals(sysUser.getId())) {
                return Message.error("what are you do?");
            }

            if (po.getOrderStatus() == null || po.getOrderStatus().intValue() != 1) {
                return Message.error("只能通知已成功的订单!");
            }

            payOrderService.sendOrderNotify(po);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success("已通知商户！");
    }

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:payorder:merchantList"})
    @RequestMapping("/merchantView")
    public String merchantView(Model model) {
        Map<String, Object> params = new HashMap<String, Object>();

//        model.addAttribute("clientList", payClientService.querySelectList());
        model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));
//        model.addAttribute("proxyList", payProxyService.querySelectList());
        return "admin/pay/payorder/merchant_list";
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
    @AdminPermission(value = {"admin:payorder:merchantList"})
    @RequestMapping("/merchantList")
    public @ResponseBody
    PageResult merchantList(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("merchantId", sysUser.getId());
            map.put("ismerchant", 1);

            // 分页查询
            payOrderService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }

    @AdminPermission(value = {"admin:payorder:merchantList"})
    @RequestMapping("/merchantDetail")
    public String merchantDetail(Long id, String sn, Model model, HttpServletRequest request) {
        try {
            //将查询的参数添加到map
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            params.put("sn", sn);
            //获取需要修改的记录数据
            PayOrder result = payOrderService.querySingle(params);
            if (result != null) {
                SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
                if (sysUser.getId().equals(result.getMerchantId())) {
                    if (result.getOrderStatus() != null && result.getOrderStatus().intValue() == 1) {
                        model.addAttribute("order", payOrderService.queryOrderDetailSuccess(id, sn));
                    } else {
                        model.addAttribute("order", payOrderService.queryOrderDetailNoPay(id, sn));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin/pay/payorder/merchant_detail";
    }

    /**
     * 导出excel
     *
     * @param param
     * @param orders
     * @param excelCols 表头
     * @author:nb
     */
    @AdminPermission(value = {"admin:payorder:export"})
    @RequestMapping("/exportExcel")
    public @ResponseBody
    Message exportExcel(@RequestParam Map<String, Object> param, @RequestParam(value = "orders[]", required = false) String[] orders, String excelCols, HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            //request.getAttribute("serverName") + "/"
            new PayExcelportThread("交易订单" + sdf.format(new Date()), "../../", excelCols, staticFolder, sysUser.getId(), new PayExcelexportDataService() {

                @Override
                public List<Map<String, Object>> getExprotData(Map<String, Object> param, String[] orders) {
                    return payOrderService.queryExportList(param, orders);
                }
            }, payExcelexportService, param, orders, 0).start();

            return Message.success("启动成功");
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
    @AdminPermission(value = {"admin:payorder:merchantExport"})
    @RequestMapping("/exportExcelMerchant")
    public @ResponseBody
    Message exportExcelMerchant(@RequestParam Map<String, Object> param, @RequestParam(value = "orders[]", required = false) String[] orders, String excelCols, HttpServletRequest request) {

        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            param.put("merchantId", sysUser.getId());
            param.put("ismerchant", 1);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            //request.getAttribute("serverName") + "/"
            new PayExcelportThread("交易订单" + sdf.format(new Date()), "../../", excelCols, staticFolder, sysUser.getId(), new PayExcelexportDataService() {
                @Override
                public List<Map<String, Object>> getExprotData(Map<String, Object> param, String[] orders) {
                    return payOrderService.queryExportListMerchant(param, orders);
                }
            }, payExcelexportService, param, orders, 0).start();
            return Message.success("启动成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error("导出失败");
    }

    @AdminPermission(value = {"admin:payorder:list"})
    @RequestMapping("/queryTotalAdmin")
    public @ResponseBody
    Message queryTotalAdmin(@RequestParam Map<String, Object> map) {
        try {
            return Message.success("操作成功", payOrderService.queryTotalAdmin(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error("汇总失败");
    }

    @AdminPermission(value = {"admin:payorder:merchantList"})
    @RequestMapping("/queryTotalMerchant")
    public @ResponseBody
    Message queryTotalMerchant(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        try {

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("merchantId", sysUser.getId());

            map.put("ismerchant", 1);

            return Message.success("操作成功", payOrderService.queryTotalMerchant(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error("汇总失败");
    }

    @Autowired
    PayClientChannelService payClientChannelService;

    @Autowired
    PayMerchantChannelService payMerchantChannelService;

    @AdminPermission(value = {"admin:payorder:nullorder"})
    @RequestMapping("/nullOrder")
    public String nullOrder(Model model) {

        model.addAttribute("clientChannelList", payClientChannelService.querySelectList());
        model.addAttribute("merchantChannelList", payMerchantChannelService.querySelectList());

        return "admin/pay/payorder/null_order";
    }

    @AdminPermission(value = {"admin:payorder:nullorder"})
    @RequestMapping("/createNullOrder")
    public @ResponseBody
    Message createNullOrder(Long merchantChannelId, Long clientChannelId, String sn, BigDecimal money, HttpServletRequest httpRequest) {
        try {

            if (merchantChannelId == null || clientChannelId == null || StringUtils.isBlank(sn) || money == null) {
                return Message.error("必填参数不能为空");
            }

            //判断商户订单号是否已存在
            Map<String, Object> p1 = new HashMap<String, Object>();
            p1.put("sn", sn);
            if (payOrderService.querySingle1(p1) != null) {
                return Message.error("订单号已存在!");
            }

            //获取通道信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", clientChannelId);
            PayClientChannel clientChannel = payClientChannelService.querySingle(params);
            if (clientChannel == null) {
                return Message.error("上游通道不存在");
            }

            params.put("id", merchantChannelId);
            PayMerchantChannel merchantChannel = payMerchantChannelService.querySingle(params);
            if (merchantChannel == null) {
                return Message.error("商户通道不存在");
            }

            params.put("id", merchantChannel.getChannelTypeId());
            PayChannelType ct = payChannelTypeService.querySingle(params);
            if (ct == null) {
                return Message.error("通道类型不存在");
            }

            SysUser client = sysUserService.querySysUserById(clientChannel.getClientId());
            SysUser merchant = sysUserService.querySysUserById(merchantChannel.getMerchantId());

            //保存订单信息
            PayOrder order = new PayOrder();

            order.setAmount(money);
            order.setChannelTypeId(clientChannel.getChannelTypeId().longValue());
            order.setClientId(clientChannel.getClientId());
            order.setClientNo(client == null ? clientChannel.getName() : client.getName() + "[空单]");
            order.setClientChannelId(clientChannelId);
            order.setClientInfo("");
            order.setClientSn(SnUtil.createSn("BDC"));

            order.setMerchantId(merchantChannel.getMerchantId());
            order.setMerchantNo(merchant == null ? merchantChannel.getName() : merchant.getName() + "[空单]");
            order.setMerchantChannelId(merchantChannelId);
            order.setMerchantSn(SnUtil.createSn("BDM"));

            order.setNotifyStatus(0);//通知状态
            order.setNotifyUrl(httpRequest.getAttribute("serverName").toString() + "/api/paytm/testnotify");
            order.setOrderStatus(0);//订单状态，未支付
            order.setPayTime(null);
            order.setPayUrl("");
            order.setProductName("预留商品名");
            order.setRemark(null);
            order.setSn(sn);
            order.setMoney(money);
            order.setTname(ct.getName());

            order.setClientRatio(clientChannel.getRatio());
            order.setProxyRatio(merchantChannel.getProxyRatio());
            order.setMerchantRatio(merchantChannel.getMerchantRatio());

            payOrderService.addSave(order);
            return Message.success("下单成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error("创建空单失败");
    }


    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:payorder:proxyList"})
    @RequestMapping("/proxyView")
    public String proxyView(Model model, HttpServletRequest request) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            model.addAttribute("channelTypeList", payChannelTypeService.queryAllList(params));
            model.addAttribute("merchantList", payMerchantService.queryMerchantByProxy(sysUser.getId()));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        return "admin/pay/payorder/proxy_list";
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
    @AdminPermission(value = {"admin:payorder:proxyList"})
    @RequestMapping("/proxyList")
    public @ResponseBody
    PageResult proxyList(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);

            List<Map<String, Object>> merchatList = payMerchantService.queryMerchantByProxy(sysUser.getId());
            String ids = "";
            for (Map<String, Object> m : merchatList) {
                ids += m.get("id") + ",";
            }
            if (!"".equals(ids)) {
                ids = ids.substring(0, ids.length() - 1);
            } else {
                ids = "-1";
            }

            map.put("merchantIds", ids);

            // 分页查询
            payOrderService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }

    /**
     * 代理数据导出
     *
     * @param param
     * @param orders
     * @param excelCols
     * @param request
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:payorder:proxyExport"})
    @RequestMapping("/exportExcelProxy")
    public @ResponseBody
    Message exportExcelProxy(@RequestParam Map<String, Object> param, @RequestParam(value = "orders[]", required = false) String[] orders, String excelCols, HttpServletRequest request) {

        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            List<Map<String, Object>> merchatList = payMerchantService.queryMerchantByProxy(sysUser.getId());
            String ids = "";
            for (Map<String, Object> m : merchatList) {
                ids += m.get("id") + ",";
            }
            if (!"".equals(ids)) {
                ids = ids.substring(0, ids.length() - 1);
            } else {
                ids = "-1";
            }

            param.put("merchantIds", ids);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            //request.getAttribute("serverName") + "/"
            new PayExcelportThread("交易订单" + sdf.format(new Date()), "../../", excelCols, staticFolder, sysUser.getId(), new PayExcelexportDataService() {
                @Override
                public List<Map<String, Object>> getExprotData(Map<String, Object> param, String[] orders) {
                    return payOrderService.queryExportListMerchant(param, orders);
                }
            }, payExcelexportService, param, orders, 0).start();
            return Message.success("启动成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error("导出失败");
    }

    @AdminPermission(value = {"admin:payorder:proxyList"})
    @RequestMapping("/queryTotalProxy")
    public @ResponseBody
    Message queryTotalProxy(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        try {

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
//            map.put("merchantId", sysUser.getId());
            List<Map<String, Object>> merchatList = payMerchantService.queryMerchantByProxy(sysUser.getId());
            String ids = "";
            for (Map<String, Object> m : merchatList) {
                ids += m.get("id") + ",";
            }
            if (!"".equals(ids)) {
                ids = ids.substring(0, ids.length() - 1);
            } else {
                ids = "-1";
            }


            map.put("merchantIds", ids);

            return Message.success("操作成功", payOrderService.queryTotalMerchant(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error("汇总失败");
    }

    /**
     * 进入批量查单页面
     *
     * @param model
     * @return
     */
    @AdminPermission(value = {"admin:payorder:queryorder"})
    @RequestMapping("/batchQueryOrder")
    public String batchQueryOrder(Model model) {
        //启用查单的上游
        List<Map<String, Object>> clientModelList = payOrderService.queryClientModelList();

        model.addAttribute("modelList", clientModelList);

        model.addAttribute("run", QueryOrderManager.getInstance().isRun());

        return "admin/pay/payorder/queryorder";
    }

    /**
     * 获取正在运行的查单信息
     *
     * @param map
     * @param request
     * @return
     */
    @AdminPermission(value = {"admin:payorder:queryorder"})
    @RequestMapping("/queryOrderInfo")
    public @ResponseBody
    Message queryOrderInfo(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        try {
            Map<String, Object> queryInfo = QueryOrderManager.getInstance().getQueryInfo();
            queryInfo.put("errorLogs", QueryOrderManager.getInstance().getErrorLogs());
            return Message.success("操作成功", queryInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.error("查询失败");
    }

    /**
     * 开启查单
     *
     * @param map
     * @param request
     * @return
     */
    @AdminPermission(value = {"admin:payorder:queryorder"})
    @RequestMapping("/startQueryOrderInfo")
    public @ResponseBody
    Message startQueryOrderInfo(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank((String) map.get("startTime"))) {
                return Message.error("开始时间不能为空");
            }
            if (StringUtils.isBlank((String) map.get("endTime"))) {
                return Message.error("结束时间不能为空");
            }
            if (StringUtils.isBlank((String) map.get("clientId"))) {
                return Message.error("开始时间不能为空");
            }
            Long clientId = Long.parseLong((String) map.get("clientId"));
            //获取上游
            SysUser client = sysUserService.querySysUserById(clientId);
            if (client == null) {
                return Message.error("上游有误");
            }

            //查询需要查单的订单
            List<PayOrder> orders = payOrderService.queryNeedQueryOrdersList(map);
            if (orders == null || orders.isEmpty()) {
                return Message.error("此时间段没有支付失败的订单");
            }

            PayOrder order = orders.get(0);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", order.getClientChannelId());
            PayClientChannel payClientChannel = payClientChannelService.querySingle(params);
            if (payClientChannel == null) {
                return Message.error("查单通道不存在!");
            }
            String keyname = payClientChannel.getKeyname();
            GroovyScript gs = ChannelManager1.getInstance().getGroovyScriptByKeyname(keyname);
            if (gs == null || gs.getQueryOrderData() == null || gs.getDealQueryOrderData() == null) {
                return Message.error("此上游查单模块暂不可用！");
            }

            QueryOrderManager.getInstance().startQueryOrder(payOrderService, gs, payClientChannel, client, new HashMap<String, Object>(), orders);

            return Message.success("启动批量查单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("启动查单失败:" + e.getMessage());
        }

    }

    @AdminPermission(value = {"admin:payorder:queryorder"})
    @RequestMapping("/stopQueryOrderInfo")
    public @ResponseBody
    Message stopQueryOrderInfo() {
        try {
            QueryOrderManager.getInstance().shutDown();
            return Message.success("停止查单成功,正在停止");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("启动查单失败:" + e.getMessage());
        }

    }


    @AdminPermission(value = {"admin:payorder:notifyorder"})
    @RequestMapping("/batchNotifyMerchant")
    public String batchNotifyMerchant(Model model) {
        //商户列表
        List<SysUser> userList = payMerchantService.querySelectList();

        model.addAttribute("modelList", userList);

        return "admin/pay/payorder/notifymerchant";
    }

    /**
     * 开始批量通知
     *
     * @param map
     * @param request
     * @return
     */
    @AdminPermission(value = {"admin:payorder:notifyorder"})
    @RequestMapping("/startBatchNotifyMerchant")
    public @ResponseBody
    Message startBatchNotifyMerchant(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        try {
            if (map.get("startTime") == null) {
                return Message.error("开始时间不能为空");
            }
            if (map.get("endTime") == null) {
                return Message.error("结束时间不能为空");
            }

            payOrderService.batchNotifyMerchant(map);

            return Message.success("批量通知成功，订单将在30秒内通知商户");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("启动通知失败:" + e.getMessage());
        }
    }
}
