package com.city.city_collector.admin.city.controller;

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
import com.city.city_collector.admin.city.entity.Config;
import com.city.city_collector.admin.city.service.ConfigService;
import com.city.city_collector.admin.city.util.ApplicationData;

/**
 * Description:栏目-controller层
 *
 * @author BG
 * @version 1.0
 * @since 2020-11-12
 */
@Controller
@RequestMapping("/system/config")
public class ConfigController {
    @Autowired
    ConfigService configService;


    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:config:list"})
    @RequestMapping("/view")
    public String view() {
        return "admin/city/config/list";
    }

    /**
     * Description:分页查询
     *
     * @param map      参数组
     * @param pageNo   当前页码
     * @param pageSize 每页记录数，为null使用默认值20
     * @param orders   排序列表
     * @return String
     * @author:BG
     * @since 2020-11-12
     */
    @AdminPermission(value = {"admin:config:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            configService.queryPage(map, page, orders);
            return new PageResult(PageResult.PAGE_SUCCESS, "操作成功", page.getTotal(), page.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(PageResult.PAGE_ERROR, "操作失败", 0L, null);
    }
//	/**
//	 * Description:打开新增页面
//	 * @author:BG
//	 * @since 2020-11-12
//	 * @return String
//	 */
//	@AdminPermission(value={"admin:config:add"})
//	@RequestMapping("/add")
//	public String add(){
//	    return "admin/city/config/edit";
//	}
//	/**
//	 * Description:新增保存操作
//	 * @author:BG
//	 * @since 2020-11-12
//	 * @param config
//	 * @return Message
//	 */
//	@AdminPermission(value={"admin:config:add"})
//	@RequestMapping("/addSave")
//	public @ResponseBody Message addSave(Config config){
//	    //数据验证
//	    if(config==null){
//	        return Message.error("参数错误");
//	    }
//
//	    try{
//			
//	        configService.addSave(config);	        
//	    }catch(Exception e){
//	        e.printStackTrace();
//	        return Message.error();
//	    }
//	    return Message.success();
//	}
//	

    /**
     * Description:打开编辑页面
     *
     * @param id    记录ID
     * @param model model对象
     * @return String
     * @author:BG
     * @since 2020-11-12
     */
    @AdminPermission(value = {"admin:config:edit"})
    @RequestMapping("/edit")
    public String edit(Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/city/config/edit";
        }
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        //获取需要修改的记录数据
        Config result = configService.querySingle(params);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {
            model.addAttribute("result", result);
        }
        return "admin/city/config/edit";
    }

    /**
     * Description:编辑保存
     *
     * @param
     * @param
     * @param id 编辑的记录ID
     * @return Message
     * @author:BG
     * @since 2020-11-12
     */
    @AdminPermission(value = {"admin:config:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(Config config) {
        //数据验证
        if (config == null) {
            return Message.error("参数错误");
        }

        try {
            configService.editSave(config);
            ApplicationData.getInstance().setConfig(configService.querySingle(new HashMap<String, Object>()));
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

//	/**
//	 * Description:删除记录
//	 * @author:BG
//	 * @since 2020-11-12
//	 * @param ids  需要删除的记录的ID数组
//	 * @return Message
//	 */
//	@AdminPermission(value={"admin:config:delete"})
//	@RequestMapping("/delete")
//	public @ResponseBody Message delete(@RequestParam(value="ids[]")Long[] ids){
//	    if(ids!=null&&ids.length==0){
//	        return Message.error("请选择要删除的数据");
//	    }
//	    try{
//			
//	        configService.delete(ids);
//	    }catch(Exception e){
//            e.printStackTrace();
//            return Message.error();
//        }
//        return Message.success();
//	}
}
