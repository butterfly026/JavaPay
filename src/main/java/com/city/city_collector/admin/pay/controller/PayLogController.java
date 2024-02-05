package com.city.city_collector.admin.pay.controller;

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
import com.city.city_collector.common.util.Message;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayLog;
import com.city.city_collector.admin.pay.service.PayLogService;

/**
 * Description:系统日志-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payLog")
public class PayLogController {
    @Autowired
    PayLogService payLogService;


    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:paylog:list"})
    @RequestMapping("/view")
    public String view() {
        return "admin/pay/paylog/list";
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
    @AdminPermission(value = {"admin:paylog:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payLogService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }

    /**
     * Description:打开详情页面
     *
     * @return String
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:paylog:list"})
    @RequestMapping("/detail")
    public String detail(Long id, Model model) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        model.addAttribute("data", payLogService.queryDetail(params));
        return "admin/pay/paylog/detail";
    }
//	/**
//	 * Description:新增保存操作
//	 * @author:demo
//	 * @since 2020-6-29
//	 * @param payLog
//	 * @return Message
//	 */
//	@AdminPermission(value={"admin:paylog:add"})
//	@RequestMapping("/addSave")
//	public @ResponseBody Message addSave(PayLog payLog){
//	    //数据验证
//	    if(payLog==null){
//	        return Message.error("参数错误");
//	    }
//
//	    try{
//
//	        payLogService.addSave(payLog);
//	    }catch(Exception e){
//	        e.printStackTrace();
//	        return Message.error();
//	    }
//	    return Message.success();
//	}
//
//	/**
//	 * Description:打开编辑页面
//	 * @author:demo
//	 * @since 2020-6-29
//	 * @param id 记录ID
//	 * @param model model对象
//	 * @return String
//	 */
//	@AdminPermission(value={"admin:paylog:edit"})
//	@RequestMapping("/edit")
//	public String edit(Integer id,Model model){
//	    if(id==null){
//	        model.addAttribute("error","请选择需要编辑的记录");
//	        return "admin/pay/paylog/edit";
//	    }
//	    //将查询的参数添加到map
//	    Map<String,Object> params=new HashMap<String, Object>();
//	    params.put("id",id);
//	    //获取需要修改的记录数据
//	    PayLog result=payLogService.querySingle(params);
//	    if(result==null){
//	        model.addAttribute("error","编辑的记录可能已经被删除");
//	    }else{
//			model.addAttribute("result",result);
//		}
//	    return "admin/pay/paylog/edit";
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
//	@AdminPermission(value={"admin:paylog:edit"})
//	@RequestMapping("/editSave")
//	public @ResponseBody Message editSave(PayLog payLog){
//	    //数据验证
//	    if(payLog==null){
//	        return Message.error("参数错误");
//	    }
//
//
//        try{
//
//            payLogService.editSave(payLog);
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
//	@AdminPermission(value={"admin:paylog:delete"})
//	@RequestMapping("/delete")
//	public @ResponseBody Message delete(@RequestParam(value="ids[]")Long[] ids){
//	    if(ids!=null&&ids.length==0){
//	        return Message.error("请选择要删除的数据");
//	    }
//	    try{
//
//	        payLogService.delete(ids);
//	    }catch(Exception e){
//            e.printStackTrace();
//            return Message.error();
//        }
//        return Message.success();
//	}

    @AdminPermission(value = {"admin:paylog:delete"})
    @RequestMapping("/delete")
    public @ResponseBody
    Message delete() {
        try {
//            payLogService.truncateLogTable();
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }
}
