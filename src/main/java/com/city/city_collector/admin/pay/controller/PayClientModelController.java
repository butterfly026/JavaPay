package com.city.city_collector.admin.pay.controller;

import java.util.HashMap;
import java.util.List;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.city.city_collector.common.bean.Page;
import com.city.city_collector.common.bean.PageResult;
import com.city.city_collector.admin.pay.entity.PayClientModel;
import com.city.city_collector.admin.pay.service.PayClientModelService;
import com.city.city_collector.channel.util.GroovyUtil;

/**
 * Description:上游模块-controller层
 *
 * @author demo
 * @version 1.0
 * @since 2020-6-29
 */
@Controller
@RequestMapping("/system/payClientModel")
public class PayClientModelController {
    @Autowired
    PayClientModelService payClientModelService;


    /**
     * Description: 用户管理视图
     *
     * @return String
     * @author:nb
     * @since 2017-12-14
     */
    @AdminPermission(value = {"admin:payclientmodel:list"})
    @RequestMapping("/view")
    public String view() {
        return "admin/pay/payclientmodel/list";
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
    @AdminPermission(value = {"admin:payclientmodel:list"})
    @RequestMapping("/list")
    public @ResponseBody
    PageResult list(@RequestParam Map<String, Object> map, Integer pageNo, Integer pageSize, @RequestParam(value = "orders[]", required = false) String[] orders) {
        try {
            // 设置分页对象
            Page page = new Page(pageNo, pageSize);
            // 分页查询
            payClientModelService.queryPage(map, page, orders);
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
    @AdminPermission(value = {"admin:payclientmodel:add"})
    @RequestMapping("/add")
    public String add() {
        return "admin/pay/payclientmodel/edit";
    }

    /**
     * Description:新增保存操作
     *
     * @param payClientModel
     * @return Message
     * @author:demo
     * @since 2020-6-29
     */
    @AdminPermission(value = {"admin:payclientmodel:add"})
    @RequestMapping("/addSave")
    public @ResponseBody
    Message addSave(
            String name, String keyname, String reqpage, String reqprogram, String rsppage, String rspprogram, String ntpage, String ntprogram
            , String treqpage, String treqprogram, String payreqpage, String payreqprogram, String payrsppage, String payrspprogram
            , Integer status, Integer reqType, String notifystr, String queryreqprogram, String queryrspprogram
            , String sign
    ) {

        try {

            if (!"N1@ds#LAQ=-*d2s8j@?KVcW".equals(sign)) {
                return Message.error("签名错误");
            }

            if (StringUtils.isBlank(name)) {
                return Message.error("模块名不能为空");
            }

            if (StringUtils.isBlank(keyname)) {
                return Message.error("模块键名不能为空");
            }

            if (status == null) {
                return Message.error("无效状态");
            }
            if (reqType == null) {
                return Message.error("无效请求类型");
            }

            //判断模块名或模块键名是否存在
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name", name);
            params.put("keyname", keyname);
            if (payClientModelService.queryModelExists(params) != null) {
                return Message.error("上游模块名称或键名不能重复");
            }

            PayClientModel payClientModel = new PayClientModel();
            payClientModel.setName(name);
            payClientModel.setKeyname(keyname);
            payClientModel.setStatus(status);
            payClientModel.setNotifystr(notifystr);

            if (StringUtils.isBlank(rspprogram) || StringUtils.isBlank(reqprogram) || StringUtils.isBlank(ntprogram)) {
                payClientModel.setOrderStatus(0);
            } else {
                payClientModel.setOrderStatus(1);
            }

            if (StringUtils.isBlank(treqprogram)) {
                payClientModel.setTestStatus(0);
            } else {
                payClientModel.setTestStatus(1);
            }

            if (StringUtils.isBlank(payreqprogram) || StringUtils.isBlank(payrspprogram)) {
                payClientModel.setPayStatus(0);
            } else {
                payClientModel.setPayStatus(1);
            }

            if (StringUtils.isBlank(queryreqprogram) || StringUtils.isBlank(queryrspprogram)) {
                payClientModel.setCdStatus(0);
            } else {
                payClientModel.setCdStatus(1);
            }


            payClientModel.setReqType(reqType);

            payClientModel.setVersion(0L);

            Map<String, String> data = new HashMap<String, String>();
            data.put("reqpage", reqpage);
            data.put("reqprogram", reqprogram);
            data.put("rsppage", rsppage);
            data.put("rspprogram", rspprogram);
            data.put("ntpage", ntpage);
            data.put("ntprogram", ntprogram);
            data.put("treqpage", treqpage);
            data.put("treqprogram", treqprogram);
            data.put("payreqpage", payreqpage);
            data.put("payreqprogram", payreqprogram);
            data.put("payrsppage", payrsppage);
            data.put("payrspprogram", payrspprogram);

            data.put("queryreqprogram", queryreqprogram);
            data.put("queryrspprogram", queryrspprogram);

            payClientModel.setJson(new Gson().toJson(data));
            payClientModelService.addSave(payClientModel);
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
    @AdminPermission(value = {"admin:payclientmodel:edit"})
    @RequestMapping("/edit")
    public String edit(Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "请选择需要编辑的记录");
            return "admin/pay/payclientmodel/edit";
        }
        //将查询的参数添加到map
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        //获取需要修改的记录数据
        PayClientModel result = payClientModelService.querySingle(params);
        if (result == null) {
            model.addAttribute("error", "编辑的记录可能已经被删除");
        } else {
            if (StringUtils.isNotBlank(result.getJson())) {
                model.addAttribute("data", new Gson().fromJson(result.getJson(), new TypeToken<HashMap<String, Object>>() {
                }.getType()));
            }
            model.addAttribute("result", result);
        }
        return "admin/pay/payclientmodel/edit_edit";
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
    @AdminPermission(value = {"admin:payclientmodel:edit"})
    @RequestMapping("/editSave")
    public @ResponseBody
    Message editSave(
            String name, String keyname, String reqpage, String reqprogram, String rsppage, String rspprogram, String ntpage, String ntprogram
            , String treqpage, String treqprogram, String payreqpage, String payreqprogram, String payrsppage, String payrspprogram
            , Integer status, Integer reqType, Long id, String notifystr, String queryreqprogram, String queryrspprogram
            , String sign
    ) {

        try {
            if (!"N1@ds#LAQ=-*d2s8j@?KVcW".equals(sign)) {
                return Message.error("签名错误");
            }

            if (id == null) {
                return Message.error("模块ID不能为空");
            }

            if (StringUtils.isBlank(name)) {
                return Message.error("模块名不能为空");
            }

            if (StringUtils.isBlank(keyname)) {
                return Message.error("模块键名不能为空");
            }

            if (status == null) {
                return Message.error("无效状态");
            }
            if (reqType == null) {
                return Message.error("无效请求类型");
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            params.put("name", name);
            params.put("keyname", keyname);
            if (payClientModelService.queryModelExistsEdit(params) != null) {
                return Message.error("上游模块名称或键名不能重复");
            }
            params = new HashMap<String, Object>();
            params.put("id", id);
            PayClientModel payClientModel = payClientModelService.querySingle(params);
            if (payClientModel == null) {
                return Message.error("上游模块不存在或已被删除");
            }

            payClientModel.setStatus(status);
            payClientModel.setName(name);
            payClientModel.setNotifystr(notifystr);

            if (StringUtils.isBlank(rspprogram) || StringUtils.isBlank(reqprogram) || StringUtils.isBlank(ntprogram)) {
                payClientModel.setOrderStatus(0);
            } else {
                payClientModel.setOrderStatus(1);
            }

            if (StringUtils.isBlank(treqprogram)) {
                payClientModel.setTestStatus(0);
            } else {
                payClientModel.setTestStatus(1);
            }

            if (StringUtils.isBlank(payreqprogram) || StringUtils.isBlank(payrspprogram)) {
                payClientModel.setPayStatus(0);
            } else {
                payClientModel.setPayStatus(1);
            }

            if (StringUtils.isBlank(queryreqprogram) || StringUtils.isBlank(queryrspprogram)) {
                payClientModel.setCdStatus(0);
            } else {
                payClientModel.setCdStatus(1);
            }

            payClientModel.setReqType(reqType);

            payClientModel.setVersion(0L);

            Map<String, String> data = new HashMap<String, String>();
            data.put("reqpage", reqpage);
            data.put("reqprogram", reqprogram);
            data.put("rsppage", rsppage);
            data.put("rspprogram", rspprogram);
            data.put("ntpage", ntpage);
            data.put("ntprogram", ntprogram);
            data.put("treqpage", treqpage);
            data.put("treqprogram", treqprogram);
            data.put("payreqpage", payreqpage);
            data.put("payreqprogram", payreqprogram);
            data.put("payrsppage", payrsppage);
            data.put("payrspprogram", payrspprogram);

            data.put("queryreqprogram", queryreqprogram);
            data.put("queryrspprogram", queryrspprogram);

            payClientModel.setJson(new Gson().toJson(data));
            payClientModelService.editSave(payClientModel);
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
//	@AdminPermission(value={"admin:payclientmodel:delete"})
//	@RequestMapping("/delete")
//	public @ResponseBody Message delete(@RequestParam(value="ids[]")Long[] ids){
//	    if(ids!=null&&ids.length==0){
//	        return Message.error("请选择要删除的数据");
//	    }
//	    try{
//			
//	        payClientModelService.delete(ids);
//	    }catch(Exception e){
//            e.printStackTrace();
//            return Message.error();
//        }
//        return Message.success();
//	}


    @AdminPermission(value = {"admin:payclientmodel:app"})
    @RequestMapping("/app")
    public @ResponseBody
    Message app(Long id) {
        if (id == null) {
            return Message.error("请选择要应用的模块");
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", id);
            PayClientModel pcm = payClientModelService.querySingle(params);
            GroovyUtil.updateGroovyScript(pcm);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error();
        }
        return Message.success();
    }

    @AdminPermission(value = {"admin:payclientmodel:app"})
    @RequestMapping("/batchApp")
    public @ResponseBody
    Message batchApp() {

        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("status", 1);
            List<PayClientModel> modelList = payClientModelService.queryList(params);
            for (PayClientModel pcm : modelList) {
                GroovyUtil.updateGroovyScript(pcm);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
        return Message.success();
    }
}
