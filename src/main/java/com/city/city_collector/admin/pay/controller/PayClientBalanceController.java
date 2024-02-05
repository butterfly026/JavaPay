package com.city.city_collector.admin.pay.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayClientBalance;
import com.city.city_collector.admin.pay.service.PayClientBalanceService;
import com.city.city_collector.admin.pay.service.PayClientService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;

/**
 * Description:上游余额明细-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payClientBalance")
public class PayClientBalanceController {
    @Autowired
    PayClientBalanceService payClientBalanceService;

    @Autowired
    PayClientService payClientService;

    @Autowired
    SysUserService sysUserService;

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:payclientbalance:list"})
    @RequestMapping("/view")
    public String view(Model model) {
        model.addAttribute("clientList", payClientService.querySelectList());
        return "admin/pay/payclientbalance/list";
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
    @AdminPermission(value = {"admin:payclientbalance:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payClientBalanceService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:payclientbalance:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("clientList", payClientService.querySelectList());
        return "admin/pay/payclientbalance/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payClientBalance
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payclientbalance:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayClientBalance payClientBalance) {


        try {
            if (payClientBalance == null) {
                return Message.error("参数错误");
            }
            if (payClientBalance.getClientId() == null) {
                return Message.error("请选择商户");
            }
            if (payClientBalance.getMoney() == null || payClientBalance.getMoney().compareTo(BigDecimal.ZERO) >= 0) {
                return Message.error("调账金额只能为负数");
            }
            if (payClientBalance.getOrderSn() == null) payClientBalance.setOrderSn("");


            payClientBalance.setNo(UUID.randomUUID().toString().replace("-", ""));
            payClientBalance.setType(6);
            SysUser u1 = sysUserService.querySysUserById(payClientBalance.getClientId());
            if (u1 == null) {
                return Message.error("上游不存在");
            }

            BigDecimal money1 = u1.getMoney() == null ? BigDecimal.ZERO : u1.getMoney();
            BigDecimal frozenMoney = u1.getFrozenMoney() == null ? BigDecimal.ZERO : u1.getFrozenMoney();
            if (money1.compareTo(payClientBalance.getMoney().abs()) < 0 || money1.subtract(frozenMoney).compareTo(payClientBalance.getMoney().abs()) < 0) {
                return Message.error("扣减金额不能大于商户可用余额");
            }

            payClientBalance.setClientNo(u1.getUsername());
            payClientBalance.setClientName(u1.getProxyName());
            payClientBalanceService.updateClientMoney(payClientBalance, u1);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:payclientbalance:add"})
    @RequestMapping("/add_add")
    public String add_add(Model model) {
        model.addAttribute("clientList", payClientService.querySelectList());
        return "admin/pay/payclientbalance/edit_add";
    }

    /**
     * Description:新增保存操作
     *
     * @param payClientBalance
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payclientbalance:add"})
    @RequestMapping("/addSave_add")
    public @ResponseBody
    Message addSave_add(PayClientBalance payClientBalance) {


        try {
            if (payClientBalance == null) {
                return Message.error("参数错误");
            }
            if (payClientBalance.getClientId() == null) {
                return Message.error("请选择商户");
            }
            if (payClientBalance.getMoney() == null || payClientBalance.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
                return Message.error("调账金额不能为负数");
            }
            if (payClientBalance.getOrderSn() == null) payClientBalance.setOrderSn("");


            payClientBalance.setNo(UUID.randomUUID().toString().replace("-", ""));
            payClientBalance.setType(6);
            SysUser u1 = sysUserService.querySysUserById(payClientBalance.getClientId());
            if (u1 == null) {
                return Message.error("上游不存在");
            }

//            BigDecimal money1=u1.getMoney()==null?BigDecimal.ZERO:u1.getMoney();
//            BigDecimal frozenMoney=u1.getFrozenMoney()==null?BigDecimal.ZERO:u1.getFrozenMoney();
//            if(money1.compareTo(payClientBalance.getMoney().abs())<0 || money1.subtract(frozenMoney).compareTo(payClientBalance.getMoney().abs())<0) {
//                return Message.error("扣减金额不能大于商户可用余额");
//            }

            payClientBalance.setClientNo(u1.getUsername());
            payClientBalance.setClientName(u1.getProxyName());
            payClientBalanceService.updateClientMoney(payClientBalance, u1);
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
//	@AdminPermission(value={"admin:payclientbalance:edit"})
//	@RequestMapping("/edit")
//	public String edit(Integer id,Model model){
//	    if(id==null){
//	        model.addAttribute("error","请选择需要编辑的记录");
//	        return "admin/pay/payclientbalance/edit";
//	    }
//	    //将查询的参数添加到map
//	    Map<String,Object> params=new HashMap<String, Object>();
//	    params.put("id",id);
//	    //获取需要修改的记录数据
//	    PayClientBalance result=payClientBalanceService.querySingle(params);
//	    if(result==null){
//	        model.addAttribute("error","编辑的记录可能已经被删除");
//	    }else{
//			model.addAttribute("result",result);
//		}
//	    return "admin/pay/payclientbalance/edit";
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
//	@AdminPermission(value={"admin:payclientbalance:edit"})
//	@RequestMapping("/editSave")
//	public @ResponseBody Message editSave(PayClientBalance payClientBalance){
//	    //数据验证
//	    if(payClientBalance==null){
//	        return Message.error("参数错误");
//	    }
//        
//        
//        try{
//            
//            payClientBalanceService.editSave(payClientBalance);
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
//	@AdminPermission(value={"admin:payclientbalance:delete"})
//	@RequestMapping("/delete")
//	public @ResponseBody Message delete(@RequestParam(value="ids[]")Long[] ids){
//	    if(ids!=null&&ids.length==0){
//	        return Message.error("请选择要删除的数据");
//	    }
//	    try{
//			
//	        payClientBalanceService.delete(ids);
//	    }catch(Exception e){
//            e.printStackTrace();
//            return Message.error();
//        }
//        return Message.success();
//	}
}
