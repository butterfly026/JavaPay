package com.city.city_collector.admin.pay.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.city.adminpermission.exception.UserNotFoundException;
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.util.SnUtil;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayBankCard;
import com.city.city_collector.admin.pay.entity.PayCash;
import com.city.city_collector.admin.pay.entity.PayRecharge;
import com.city.city_collector.admin.pay.service.PayBankCardService;
import com.city.city_collector.admin.pay.service.PayMerchantService;
import com.city.city_collector.admin.pay.service.PayRechargeService;
import com.city.city_collector.admin.system.entity.SysUser;
import com.city.city_collector.admin.system.service.SysUserService;

/**
 * Description:商户充值-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payRecharge")
public class PayRechargeController {
    @Autowired
    PayRechargeService payRechargeService;

    @Autowired
    PayMerchantService payMerchantService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    PayBankCardService payBankCardService;

    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:payrecharge:list"})
    @RequestMapping("/view")
    public String view(Model model, HttpServletRequest request) {
        model.addAttribute("merchantList", payMerchantService.querySelectList());
        return "admin/pay/payrecharge/list";
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
    @AdminPermission(value = {"admin:payrecharge:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payRechargeService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }
//	/**
//	 * Description:打开新增页面
//	 * @author:demo
//	 * @since 2020-6-29
//	 * @return String
//	 */
//	@AdminPermission(value={"admin:payrecharge:add"})
//	@RequestMapping("/add")
//	public String add(Model model,HttpServletRequest request){
//        try {
//            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
//            SysUser user=sysUserService.querySysUserById(sysUser.getId());
//
//        }
//        catch (UserNotFoundException e) {
//            e.printStackTrace();
//        }
//
//	    return "admin/pay/payrecharge/edit";
//	}
//	/**
//	 * Description:新增保存操作
//	 * @author:demo
//	 * @since 2020-6-29
//	 * @param payRecharge
//	 * @return Message
//	 */
//	@AdminPermission(value={"admin:payrecharge:add"})
//	@RequestMapping("/addSave")
//	public @ResponseBody Message addSave(PayRecharge payRecharge){
//	    //数据验证
//	    if(payRecharge==null){
//	        return Message.error("参数错误");
//	    }
//
//	    try{
//
//	        payRechargeService.addSave(payRecharge);
//	    }catch(Exception e){
//	        e.printStackTrace();
//	        return Message.error();
//	    }
//	    return Message.success();
//	}

    /**
     * Description:打开编辑页面
     *
     * @param id    记录ID
     * @param model model对象
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payrecharge:edit"})
    @RequestMapping("/edit")
    public String edit(Integer id, Model model, HttpServletRequest request) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/pay/payrecharge/edit";
        }
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        //获取需要修改的记录数据
        PayRecharge result = payRechargeService.querySingle(params);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {
            try {
                SysUser user = (SysUser) AdminPermissionManager.getUserObject(request);

                params = new HashMap<String, Object>();
                params.put("userId", user.getId());
                params.put("btype", 0);
                model.addAttribute("cardList", payBankCardService.queryList(params));

                params.put("btype", 1);
                model.addAttribute("ptmList", payBankCardService.queryList(params));
            } catch (UserNotFoundException e) {

                e.printStackTrace();
            }

            model.addAttribute("result", result);
        }
        return "admin/pay/payrecharge/edit_fpzh";
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
    @AdminPermission(value = {"admin:payrecharge:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(PayRecharge payRecharge, Long cardId, Long cardId1) {
        //数据验证
        if (payRecharge == null) {
            return Message.error("参数错误");
        }
        if (payRecharge.getSmodel() == null) {
            return Message.error("请选择账号录入模式");
        }
        if (payRecharge.getAccType() == null) {
            return Message.error("请选择账号类型");
        }

        try {
            Integer smodel = payRecharge.getSmodel();
            Integer accType = payRecharge.getAccType();
            if (smodel.intValue() == 0) {//账号选择模式
                if ((accType.intValue() == 0 && cardId == null) || (accType.intValue() == 1 && cardId1 == null)) {
                    return Message.error("请选择您的账号!");
                }
                Map<String, Object> params = new HashMap<String, Object>();
                if (accType.intValue() == 0) {
                    params.put("id", cardId);
                } else {
                    params.put("id", cardId1);
                }
                PayBankCard card = payBankCardService.querySingle(params);

                if (card == null) {
                    return Message.error("无效提现账号");
                }
                payRecharge.setCardNo(card.getCardNo());
                payRecharge.setCardName(card.getCardName());
                payRecharge.setBankName(card.getBankName());
                payRecharge.setBankIfsc(card.getBankIfsc());
                payRecharge.setBankNation(card.getBankNation());
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", payRecharge.getId());
            PayRecharge pr = payRechargeService.querySingle(params);

            String remark = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (pr.getRemark() == null) pr.setRemark("");
            remark = pr.getRemark() + " <span style='color:green;'>" + sdf.format(new Date()) + " 分配充值账号:" + payRecharge.getRemark() + "</span><br/>";

            payRecharge.setRemark(remark);
            payRecharge.setStatus(2);
            payRechargeService.editSave(payRecharge);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

//	/**
//	 * Description:删除记录
//	 * @author:demo
//	 * @since 2020-6-29
//	 * @param ids  需要删除的记录的ID数组
//	 * @return Message
//	 */
//	@AdminPermission(value={"admin:payrecharge:delete"})
//	@RequestMapping("/delete")
//	public @ResponseBody Message delete(@RequestParam(value="ids[]")Long[] ids){
//	    if(ids!=null&&ids.length==0){
//	        return Message.error("请选择要删除的数据");
//	    }
//	    try{
//
//	        payRechargeService.delete(ids);
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
    @AdminPermission(value = {"admin:payrecharge:merchantList"})
    @RequestMapping("/merchantView")
    public String merchantView(Model model, HttpServletRequest request) {
        model.addAttribute("merchantList", payMerchantService.querySelectList());
        return "admin/pay/payrecharge/merchant_list";
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
    @AdminPermission(value = {"admin:payrecharge:merchantList"})
    @RequestMapping("/merchantList")
    public @ResponseBody
    PageResult merchantList(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders, HttpServletRequest request) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);

            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            map.put("merchantId", sysUser.getId());
            // 分页查询
            payRechargeService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:payrecharge:merchantAdd"})
    @RequestMapping("/merchantAdd")
    public String merchantAdd(Model model, HttpServletRequest request) {
        return "admin/pay/payrecharge/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payRecharge
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payrecharge:merchantAdd"})
    @RequestMapping("/merchantAddSave")
    public @ResponseBody
    Message merchantAddSave(PayRecharge payRecharge, HttpServletRequest request) {
        //数据验证
        if (payRecharge == null) {
            return Message.error("参数错误");
        }

        if (payRecharge.getMoney() == null || payRecharge.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
            return Message.error("充值金额有误");
        }
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            payRecharge.setMerchantId(sysUser.getId());
            payRecharge.setSn(SnUtil.createSn("RPG"));
            payRecharge.setStatus(0);
            payRechargeService.addSave(payRecharge);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    /**
     * 审核拒绝
     *
     * @param id
     * @param remark
     * @return
     * @author:nb
     */
    @AdminPermission(value = {"admin:payrecharge:jj"})
    @RequestMapping("/auditJj")
    public @ResponseBody
    Message auditJj(Long id, String remark) {
        if (id == null) {
            return Message.error("参数错误");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayRecharge pr = payRechargeService.querySingle(params);
            if (pr == null) {
                return Message.error("充值记录不存在");
            }
            if (pr.getStatus().intValue() != 0) {
                return Message.error("只能拒绝发起充值状态的充值记录");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (pr.getRemark() == null) pr.setRemark("");
            remark = pr.getRemark() + " <span style='color:red;'>" + sdf.format(new Date()) + " 拒绝提现申请:" + remark + "</span><br/>";
            pr.setRemark(remark);
            pr.setStatus(1);
            payRechargeService.editSave(pr);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:payrecharge:merchantPaysuc"})
    @RequestMapping("/paysuc")
    public @ResponseBody
    Message paysuc(Long id, String remark) {
        if (id == null) {
            return Message.error("参数错误");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayRecharge pr = payRechargeService.querySingle(params);
            if (pr == null) {
                return Message.error("充值记录不存在");
            }
            if (pr.getStatus().intValue() != 2) {
                return Message.error("只能处理待充值的订单");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (pr.getRemark() == null) pr.setRemark("");
            remark = pr.getRemark() + " <span style='color:blue;'>" + sdf.format(new Date()) + " 充值成功:" + remark + "</span><br/>";
            pr.setRemark(remark);
            pr.setStatus(3);
            payRechargeService.editSave(pr);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }


    @AdminPermission(value = {"admin:payrecharge:detail"})
    @RequestMapping("/detail")
    public String detail(Long id, Model model) {
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        model.addAttribute("order", payRechargeService.querySingle(params));
        return "admin/pay/payrecharge/detail";
    }


    @AdminPermission(value = {"admin:payrecharge:sb"})
    @RequestMapping("/rechargeFail")
    public @ResponseBody
    Message rechargeFail(Long id, String remark) {
        if (id == null) {
            return Message.error("参数错误");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayRecharge pr = payRechargeService.querySingle(params);
            if (pr == null) {
                return Message.error("充值记录不存在");
            }
            if (pr.getStatus().intValue() != 3 && pr.getStatus().intValue() != 2) {
                return Message.error("只能处理待充值和已充值的订单");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (pr.getRemark() == null) pr.setRemark("");
            remark = pr.getRemark() + " <span style='color:red;'>" + sdf.format(new Date()) + " 充值失败:" + remark + "</span><br/>";
            pr.setRemark(remark);
            pr.setStatus(5);
            payRechargeService.editSave(pr);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:payrecharge:cg"})
    @RequestMapping("/rechargeSuc")
    public @ResponseBody
    Message rechargeSuc(Long id, String remark) {
        if (id == null) {
            return Message.error("参数错误");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayRecharge pr = payRechargeService.querySingle(params);
            if (pr == null) {
                return Message.error("充值记录不存在");
            }
            if (pr.getStatus().intValue() != 3) {
                return Message.error("只能处理已充值的订单");
            }
            SysUser merchant = sysUserService.querySysUserById(pr.getMerchantId());
            if (merchant == null) {
                return Message.error("商户不存在");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (pr.getRemark() == null) pr.setRemark("");
            remark = pr.getRemark() + " <span style='color:green;'>" + sdf.format(new Date()) + " 充值成功:" + remark + "</span><br/>";
            pr.setRemark(remark);
            pr.setStatus(4);
//            payRechargeService.editSave(pr);

            payRechargeService.paySuc(pr, merchant);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:payrecharge:merchantdetail"})
    @RequestMapping("/merchantDetail")
    public String merchantDetail(Long id, Model model, HttpServletRequest request) {
        try {
            SysUser sysUser = (SysUser) AdminPermissionManager.getUserObject(request);
            //将查询的参数添加到map
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            params.put("merchantId", sysUser.getId());

            model.addAttribute("order", payRechargeService.querySingle(params));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return "admin/pay/payrecharge/detail";
    }
}
