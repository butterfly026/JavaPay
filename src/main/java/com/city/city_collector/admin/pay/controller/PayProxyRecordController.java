package com.city.city_collector.admin.pay.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.city.city_collector.admin.pay.entity.PayProxyRecord;
import com.city.city_collector.admin.pay.service.PayProxyRecordService;
import com.city.city_collector.admin.pay.service.PayProxyService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;

/**
 * Description:代理流水-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payProxyRecord")
public class PayProxyRecordController {
    @Autowired
    PayProxyRecordService payProxyRecordService;
    @Autowired
    PayProxyService payProxyService;

    @Autowired
    SysUserService sysUserService;

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:payproxyrecord:list"})
    @RequestMapping("/view")
    public String view(Model model) {
        model.addAttribute("proxyList", payProxyService.querySelectList());
        return "admin/pay/payproxyrecord/list";
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
    @AdminPermission(value = {"admin:payproxyrecord:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payProxyRecordService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:payproxyrecord:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("proxyList", payProxyService.querySelectList());
        return "admin/pay/payproxyrecord/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payProxyRecord
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payproxyrecord:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayProxyRecord payProxyRecord) {

        try {
            if (payProxyRecord == null) {
                return Message.error("参数错误");
            }

            if (payProxyRecord.getProxyId() == null) {
                return Message.error("请选择代理");
            }
            if (payProxyRecord.getMoney() == null || payProxyRecord.getMoney().compareTo(BigDecimal.ZERO) >= 0) {
                return Message.error("调账金额只能为负数");
            }
            if (payProxyRecord.getOrderSn() == null) payProxyRecord.setOrderSn("");


            payProxyRecord.setNo(UUID.randomUUID().toString().replace("-", ""));
            payProxyRecord.setType(6);
            SysUser u1 = sysUserService.querySysUserById(payProxyRecord.getProxyId());
            if (u1 == null) {
                return Message.error("代理不存在");
            }

            BigDecimal money1 = u1.getMoney() == null ? BigDecimal.ZERO : u1.getMoney();
            BigDecimal frozenMoney = u1.getFrozenMoney() == null ? BigDecimal.ZERO : u1.getFrozenMoney();
            if (money1.compareTo(payProxyRecord.getMoney().abs()) < 0 || money1.subtract(frozenMoney).compareTo(payProxyRecord.getMoney().abs()) < 0) {
                return Message.error("扣减金额不能大于商户可用余额");
            }

            payProxyRecord.setProxyNo(u1.getUsername());
            payProxyRecord.setProxyName(u1.getName());
//          payMemchantRecordService.addSave(payMemchantRecord);     
            payProxyRecordService.updateProxyMoney(payProxyRecord, u1);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    /**
     * 增加代理余额
     *
     * @param model
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:payproxyrecord:add"})
    @RequestMapping("/add_add")
    public String add_add(Model model) {
        model.addAttribute("proxyList", payProxyService.querySelectList());
        return "admin/pay/payproxyrecord/edit_add";
    }

    @AdminPermission(value = {"admin:payproxyrecord:add"})
    @RequestMapping("/addSave_add")
    public @ResponseBody
    Message addSave_add(PayProxyRecord payProxyRecord) {

        try {
            if (payProxyRecord == null) {
                return Message.error("参数错误");
            }
            if (payProxyRecord.getProxyId() == null) {
                return Message.error("请选择商户");
            }
            if (payProxyRecord.getMoney() == null || payProxyRecord.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
                return Message.error("调账金额不能为负数");
            }
            if (payProxyRecord.getOrderSn() == null) payProxyRecord.setOrderSn("");


            payProxyRecord.setNo(UUID.randomUUID().toString().replace("-", ""));
            payProxyRecord.setType(6);
            SysUser u1 = sysUserService.querySysUserById(payProxyRecord.getProxyId());
            if (u1 == null) {
                return Message.error("商户不存在");
            }

//            BigDecimal money1=u1.getMoney()==null?BigDecimal.ZERO:u1.getMoney();
//            BigDecimal frozenMoney=u1.getFrozenMoney()==null?BigDecimal.ZERO:u1.getFrozenMoney();
//            if(money1.compareTo(payMemchantRecord.getMoney().abs())<0 || money1.subtract(frozenMoney).compareTo(payMemchantRecord.getMoney().abs())<0) {
//                return Message.error("扣减金额不能大于商户可用余额");
//            }

            payProxyRecord.setProxyNo(u1.getUsername());
            payProxyRecord.setProxyName(u1.getName());
//          payMemchantRecordService.addSave(payMemchantRecord);     
            payProxyRecordService.updateProxyMoney_add(payProxyRecord, u1);
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
//	@AdminPermission(value={"admin:payproxyrecord:edit"})
//	@RequestMapping("/edit")
//	public String edit(Integer id,Model model){
//	    if(id==null){
//	        model.addAttribute("error","请选择需要编辑的记录");
//	        return "admin/pay/payproxyrecord/edit";
//	    }
//	    //将查询的参数添加到map
//	    Map<String,Object> params=new HashMap<String, Object>();
//	    params.put("id",id);
//	    //获取需要修改的记录数据
//	    PayProxyRecord result=payProxyRecordService.querySingle(params);
//	    if(result==null){
//	        model.addAttribute("error","编辑的记录可能已经被删除");
//	    }else{
//			model.addAttribute("result",result);
//		}
//	    return "admin/pay/payproxyrecord/edit";
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
//	@AdminPermission(value={"admin:payproxyrecord:edit"})
//	@RequestMapping("/editSave")
//	public @ResponseBody Message editSave(PayProxyRecord payProxyRecord){
//	    //数据验证
//	    if(payProxyRecord==null){
//	        return Message.error("参数错误");
//	    }
//        
//        
//        try{
//            
//            payProxyRecordService.editSave(payProxyRecord);
//        }catch(Exception e){
//            e.printStackTrace();
//            return Message.error();
//        }
//        return Message.success();
//	}
//	
//	/**
//	 * Description:删除记录
//	 * @author:demo
//	 * @since 2020-6-29
//	 * @param ids  需要删除的记录的ID数组
//	 * @return Message
//	 */
//	@AdminPermission(value={"admin:payproxyrecord:delete"})
//	@RequestMapping("/delete")
//	public @ResponseBody Message delete(@RequestParam(value="ids[]")Long[] ids){
//	    if(ids!=null&&ids.length==0){
//	        return Message.error("请选择要删除的数据");
//	    }
//	    try{
//			
//	        payProxyRecordService.delete(ids);
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
    @AdminPermission(value = {"admin:payproxyrecord:proxyList"})
    @RequestMapping("/proxyView")
    public String proxyView(Model model) {
        return "admin/pay/payproxyrecord/proxy_list";
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
    @AdminPermission(value = {"admin:payproxyrecord:proxyList"})
    @RequestMapping("/proxyList")
    public @ResponseBody
    PageResult proxyList(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("proxyId", sysUser.getId());
            // 分页查询
            payProxyRecordService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }
}
