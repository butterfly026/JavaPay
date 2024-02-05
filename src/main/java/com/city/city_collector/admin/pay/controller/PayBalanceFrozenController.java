package com.city.city_collector.admin.pay.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.city.adminpermission.annotation.AdminPermission;
import com.city.city_collector.admin.pay.entity.PayBalanceFrozen;
import com.city.city_collector.admin.pay.service.PayBalanceFrozenService;
import com.city.city_collector.admin.pay.service.PayClientService;
import com.city.city_collector.admin.pay.service.PayMerchantService;
import com.city.city_collector.admin.pay.service.PayProxyService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.common.util.Message;

/**
 * Description:余额冻结-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payBalanceFrozen")
public class PayBalanceFrozenController {
    @Autowired
    PayBalanceFrozenService payBalanceFrozenService;

    @Autowired
    PayMerchantService payMerchantService;
    @Autowired
    PayClientService payClientService;
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
    @AdminPermission(value = {"admin:paybalancefrozen:list"})
    @RequestMapping("/view")
    public String view(Model model) {
        model.addAttribute("merchantList", payMerchantService.querySelectList());
        model.addAttribute("proxyList", payProxyService.querySelectList());

        return "admin/pay/paybalancefrozen/list";
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
    @AdminPermission(value = {"admin:paybalancefrozen:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payBalanceFrozenService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:paybalancefrozen:add"})
    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("merchantList", payMerchantService.querySelectList());
        model.addAttribute("proxyList", payProxyService.querySelectList());

        return "admin/pay/paybalancefrozen/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payBalanceFrozen
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paybalancefrozen:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(PayBalanceFrozen payBalanceFrozen, Long merchantId, Long proxyId) {


        try {
            //数据验证
            if (payBalanceFrozen == null) {
                return Message.error("参数错误");
            }

            if (payBalanceFrozen.getType() == null) {
                return Message.error("用户类型错误");
            }

            if (payBalanceFrozen.getMoney() == null) {
                return Message.error("金额有误");
            }

            if (payBalanceFrozen.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
                return Message.error("冻结金额必须大于0,不然你冻结个屁");
            }

            if (StringUtils.isNotBlank(payBalanceFrozen.getRemark())) {
                payBalanceFrozen.setRemark("冻结原因:" + payBalanceFrozen.getRemark());
            }
            SysUser user = null;
            if (payBalanceFrozen.getType().intValue() == 1) {
                if (merchantId == null) {
                    return Message.error("请选择商户");
                }
                user = sysUserService.querySysUserById(merchantId);
                if (user == null) {
                    return Message.error("商户不存在");
                }
                payBalanceFrozen.setUserId(merchantId);
            } else {
                if (proxyId == null) {
                    return Message.error("请选择代理");
                }
                user = sysUserService.querySysUserById(proxyId);
                if (user == null) {
                    return Message.error("代理不存在");
                }
                payBalanceFrozen.setUserId(proxyId);
            }

            BigDecimal money1 = user.getMoney() == null ? BigDecimal.ZERO : user.getMoney();
            BigDecimal frozenMoney = user.getFrozenMoney() == null ? BigDecimal.ZERO : user.getFrozenMoney();
            if (money1.compareTo(payBalanceFrozen.getMoney()) < 0 || money1.subtract(frozenMoney).compareTo(payBalanceFrozen.getMoney()) < 0) {
                return Message.error("请仔细瞅瞅,有没有这么多钱可以冻结!OK?");
            }

            payBalanceFrozenService.createBalanceFrozen(payBalanceFrozen);
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
//	@AdminPermission(value={"admin:paybalancefrozen:edit"})
//	@RequestMapping("/edit")
//	public String edit(Integer id,Model model){
//	    if(id==null){
//	        model.addAttribute("error","请选择需要编辑的记录");
//	        return "admin/pay/paybalancefrozen/edit";
//	    }
//	    //将查询的参数添加到map
//	    Map<String,Object> params=new HashMap<String, Object>();
//	    params.put("id",id);
//	    //获取需要修改的记录数据
//	    PayBalanceFrozen result=payBalanceFrozenService.querySingle(params);
//	    if(result==null){
//	        model.addAttribute("error","编辑的记录可能已经被删除");
//	    }else{
//			model.addAttribute("result",result);
//		}
//	    return "admin/pay/paybalancefrozen/edit";
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
//	@AdminPermission(value={"admin:paybalancefrozen:edit"})
//	@RequestMapping("/editSave")
//	public @ResponseBody Message editSave(PayBalanceFrozen payBalanceFrozen){
//	    //数据验证
//	    if(payBalanceFrozen==null){
//	        return Message.error("参数错误");
//	    }
//        
//        
//        try{
//            
//            payBalanceFrozenService.editSave(payBalanceFrozen);
//        }catch(Exception e){
//            e.printStackTrace();
//            return Message.error();
//        }
//        return Message.success();
//	}

//	/**
//	 * Description:删除记录
//	 * @author:demo
//	 * @since 2020-6-29
//	 * @param ids  需要删除的记录的ID数组
//	 * @return Message
//	 */
//	@AdminPermission(value={"admin:paybalancefrozen:delete"})
//	@RequestMapping("/delete")
//	public @ResponseBody Message delete(@RequestParam(value="ids[]")Long[] ids){
//	    if(ids!=null&&ids.length==0){
//	        return Message.error("请选择要删除的数据");
//	    }
//	    try{
//			
//	        payBalanceFrozenService.delete(ids);
//	    }catch(Exception e){
//            e.printStackTrace();
//            return Message.error();
//        }
//        return Message.success();
//	}

    @AdminPermission(value = {"admin:paybalancefrozen:status"})
    @RequestMapping("/upStatus")
    public @ResponseBody
    Message upStatus(Long id, Integer status) {
        //数据验证
        if (id == null || status == null) {
            return Message.error("参数错误");
        }

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayBalanceFrozen pbf = payBalanceFrozenService.querySingle(params);
            if (pbf == null) {
                return Message.error("无效的冻结数据");
            }
            if (pbf.getStatus().intValue() != 0) {
                return Message.error("此数据非冻结状态");
            }
            if (pbf.getFrozenType().intValue() != 6) {
                return Message.error("此数据无法人工解冻");
            }

            //status没有意义
            payBalanceFrozenService.updateStatus(pbf);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }
}
