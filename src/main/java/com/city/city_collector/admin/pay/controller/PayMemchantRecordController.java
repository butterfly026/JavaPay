package com.city.city_collector.admin.pay.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.city.city_collector.admin.pay.service.PayExcelexportService;
import com.city.city_collector.common.util.GoogleGenerator;
import com.city.city_collector.common.util.PayExcelexportDataService;
import com.city.city_collector.common.util.PayExcelportThread;
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
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayMemchantRecord;
import com.city.city_collector.admin.pay.service.PayMemchantRecordService;
import com.city.city_collector.admin.pay.service.PayMerchantService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;

/**
 * Description:商户流水-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payMemchantRecord")
public class PayMemchantRecordController {
    @Autowired
    PayMemchantRecordService payMemchantRecordService;

    @Autowired
    PayMerchantService payMerchantService;
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
    @AdminPermission(value = {"admin:paymemchantrecord:list"})
    @RequestMapping("/view")
    public String view(Model model) {
        model.addAttribute("merchantList", payMerchantService.querySelectList());
        return "admin/pay/paymemchantrecord/list";
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
    @AdminPermission(value = {"admin:paymemchantrecord:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payMemchantRecordService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:paymemchantrecord:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("merchantList", payMerchantService.querySelectList());
        return "admin/pay/paymemchantrecord/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payMemchantRecord
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymemchantrecord:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayMemchantRecord payMemchantRecord, HttpServletRequest request) {

        try {
            if (payMemchantRecord == null) {
                return Message.error("参数错误");
            }

            if(org.apache.commons.lang3.StringUtils.isBlank(payMemchantRecord.getKey()) || payMemchantRecord.getKey() == "")
                return Message.error("fuck-谷歌参数为空你不怕吗!");

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            if (org.apache.commons.lang3.StringUtils.isBlank(payMemchantRecord.getKey()) || !new GoogleGenerator().check_code(sysUser.getValidCode(), payMemchantRecord.getKey(), System.currentTimeMillis())) {
                return Message.error("谷歌验证动态口令错误！");
            }

            if (payMemchantRecord.getMerchantId() == null) {
                return Message.error("请选择商户");
            }
            if (payMemchantRecord.getMoney() == null || payMemchantRecord.getMoney().compareTo(BigDecimal.ZERO) >= 0) {
                return Message.error("调账金额只能为负数");
            }
            if (payMemchantRecord.getOrderSn() == null) payMemchantRecord.setOrderSn("");


            payMemchantRecord.setNo(UUID.randomUUID().toString().replace("-", ""));
            payMemchantRecord.setType(6);
            SysUser u1 = sysUserService.querySysUserById(payMemchantRecord.getMerchantId());
            if (u1 == null) {
                return Message.error("商户不存在");
            }

            BigDecimal money1 = u1.getMoney() == null ? BigDecimal.ZERO : u1.getMoney();
            BigDecimal frozenMoney = u1.getFrozenMoney() == null ? BigDecimal.ZERO : u1.getFrozenMoney();
            if (money1.compareTo(payMemchantRecord.getMoney().abs()) < 0 || money1.subtract(frozenMoney).compareTo(payMemchantRecord.getMoney().abs()) < 0) {
                return Message.error("扣减金额不能大于商户可用余额");
            }

            payMemchantRecord.setMerchantNo(u1.getUsername());
            payMemchantRecord.setMerchantName(u1.getName());
//	        payMemchantRecordService.addSave(payMemchantRecord);
            payMemchantRecordService.updateMerchantMoney(payMemchantRecord, u1);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    /**
     * 增加商户余额
     *
     * @param model
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:paymemchantrecord:add"})
    @RequestMapping("/add_add")
    public String add_add(Model model) {
        model.addAttribute("merchantList", payMerchantService.querySelectList());
        return "admin/pay/paymemchantrecord/edit_add";
    }

    @AdminPermission(value = {"admin:paymemchantrecord:add"})
    @RequestMapping("/addSave_add")
    public @ResponseBody
    Message addSave_add(PayMemchantRecord payMemchantRecord, HttpServletRequest request) {

        try {
            if (payMemchantRecord == null) {
                return Message.error("参数错误");
            }

            if(org.apache.commons.lang3.StringUtils.isBlank(payMemchantRecord.getKey()) || payMemchantRecord.getKey() == "")
                return Message.error("fuck-谷歌参数为空你不怕吗!");

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            if (org.apache.commons.lang3.StringUtils.isBlank(payMemchantRecord.getKey()) || !new GoogleGenerator().check_code(sysUser.getValidCode(), payMemchantRecord.getKey(), System.currentTimeMillis())) {
                return Message.error("谷歌验证动态口令错误！");
            }

            if (payMemchantRecord.getMerchantId() == null) {
                return Message.error("请选择商户");
            }
            if (payMemchantRecord.getMoney() == null || payMemchantRecord.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
                return Message.error("调账金额不能为负数");
            }
            if (payMemchantRecord.getOrderSn() == null) payMemchantRecord.setOrderSn("");


            payMemchantRecord.setNo(UUID.randomUUID().toString().replace("-", ""));
            payMemchantRecord.setType(6);
            SysUser u1 = sysUserService.querySysUserById(payMemchantRecord.getMerchantId());
            if (u1 == null) {
                return Message.error("商户不存在");
            }

//            BigDecimal money1=u1.getMoney()==null?BigDecimal.ZERO:u1.getMoney();
//            BigDecimal frozenMoney=u1.getFrozenMoney()==null?BigDecimal.ZERO:u1.getFrozenMoney();
//            if(money1.compareTo(payMemchantRecord.getMoney().abs())<0 || money1.subtract(frozenMoney).compareTo(payMemchantRecord.getMoney().abs())<0) {
//                return Message.error("扣减金额不能大于商户可用余额");
//            }

            payMemchantRecord.setMerchantNo(u1.getUsername());
            payMemchantRecord.setMerchantName(u1.getName());
//          payMemchantRecordService.addSave(payMemchantRecord);
            payMemchantRecordService.updateMerchantMoney_add(payMemchantRecord, u1);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

//	/**
//	 * Description:打开编辑页面
//	 * @author:demo
//	 * @since 2020-6-29
//	 * @param id 记录ID
//	 * @param model model对象
//	 * @return String
//	 */
//	@AdminPermission(value={"admin:paymemchantrecord:edit"})
//	@RequestMapping("/edit")
//	public String edit(Integer id,Model model){
//	    if(id==null){
//	        model.addAttribute("error","请选择需要编辑的记录");
//	        return "admin/pay/paymemchantrecord/edit";
//	    }
//	    //将查询的参数添加到map
//	    Map<String,Object> params=new HashMap<String, Object>();
//	    params.put("id",id);
//	    //获取需要修改的记录数据
//	    PayMemchantRecord result=payMemchantRecordService.querySingle(params);
//	    if(result==null){
//	        model.addAttribute("error","编辑的记录可能已经被删除");
//	    }else{
//			model.addAttribute("result",result);
//		}
//	    return "admin/pay/paymemchantrecord/edit";
//	}
//
//	/**
//	 * Description:编辑保存
//	 * @author:demo
//	 * @since 2020-6-29
//	 * @param
//     * @param
//	 * @param id 编辑的记录ID
//	 * @return Message
//	 */
//	@AdminPermission(value={"admin:paymemchantrecord:edit"})
//	@RequestMapping("/editSave")
//	public @ResponseBody Message editSave(PayMemchantRecord payMemchantRecord){
//	    //数据验证
//	    if(payMemchantRecord==null){
//	        return Message.error("参数错误");
//	    }
//
//
//        try{
//
//            payMemchantRecordService.editSave(payMemchantRecord);
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
//	@AdminPermission(value={"admin:paymemchantrecord:delete"})
//	@RequestMapping("/delete")
//	public @ResponseBody Message delete(@RequestParam(value="ids[]")Long[] ids){
//	    if(ids!=null&&ids.length==0){
//	        return Message.error("请选择要删除的数据");
//	    }
//	    try{
//
//	        payMemchantRecordService.delete(ids);
//	    }catch(Exception e){
//            e.printStackTrace();
//            return Message.error();
//        }
//        return Message.success();
//	}


    /**
     * Description: 商户-数据列表
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:paymemchantrecord:merchantList"})
    @RequestMapping("/merchantView")
    public String merchantView(Model model) {

        return "admin/pay/paymemchantrecord/merchant_list";
    }

    /**
     * Description:分页查询-商户
     *
     * @param map      参数组
     * @param pageNo   当前页码
     * @param pageSize 每页记录数，为null使用默认值20
     * @param orders   排序列表
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paymemchantrecord:merchantList"})
    @RequestMapping("/merchantList")
    public @ResponseBody
    PageResult merchantList(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("merchantId", sysUser.getId());
            // 分页查询
            payMemchantRecordService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }


    /**
     * 导出excel
     *
     * @param param
     * @param orders
     * @param excelCols 表头
     * @author:nb
     */
    @AdminPermission(value = {"admin:paymemchantrecord:export"})
    @RequestMapping("/exportExcel")
    public @ResponseBody
    Message exportExcel(@RequestParam Map<String, Object> param, @RequestParam(value = "orders[]", required = false) String[] orders, String excelCols, HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            //request.getAttribute("serverName") + "/"
            //localhost/login/main -> localhost/exportExcel
            new PayExcelportThread("流水明细" + sdf.format(new Date()), "../../" , excelCols, staticFolder, sysUser.getId(), new PayExcelexportDataService() {
                @Override
                public List<Map<String, Object>> getExprotData(Map<String, Object> param, String[] orders) {
                    return payMemchantRecordService.queryExportList(param, orders);
                }
            }, payExcelexportService, param, orders, 0).start();

            return Message.success("导出成功");
        } catch (Exception e) {

            e.printStackTrace();

        }
        return Message.error("导出失败");
    }


    /**
     * 导出excel
     *
     * @param param
     * @param orders
     * @param excelCols 表头
     * @author:nb
     */
    @AdminPermission(value = {"admin:paymemchantrecord:export_merchant"})
    @RequestMapping("/exportExcelMerchant")
    public @ResponseBody
    Message exportExcelMerchant(@RequestParam Map<String, Object> param, @RequestParam(value = "orders[]", required = false) String[] orders, String excelCols, HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            //request.getAttribute("serverName") + "/"
            new PayExcelportThread("流水明细" + sdf.format(new Date()), "../../", excelCols, staticFolder, sysUser.getId(), new PayExcelexportDataService() {
                @Override
                public List<Map<String, Object>> getExprotData(Map<String, Object> param, String[] orders) {
                    param.put("merchantId", sysUser.getId());
                    return payMemchantRecordService.queryExportList(param, orders);
                }
            }, payExcelexportService, param, orders, 2).start();

            return Message.success("导出成功");
        } catch (Exception e) {

            e.printStackTrace();

        }
        return Message.error("导出失败");
    }


}
