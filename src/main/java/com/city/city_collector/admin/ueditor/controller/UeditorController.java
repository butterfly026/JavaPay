package com.city.city_collector.admin.ueditor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.city.city_collector.admin.ueditor.bean.ActionMap;
import com.city.city_collector.admin.ueditor.bean.BaseState;
import com.city.city_collector.admin.ueditor.bean.State;

@Controller
@RequestMapping({"/admin/ueditor"})
public class UeditorController {
    @RequestMapping({"/upload"})
    @ResponseBody
    public String upload(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        if ((action == null) || (!ActionMap.mapping.containsKey(action))) {
            return new BaseState(false, 101).toJSONString();
        }
        System.out.println("进入了ueditor配置读取方法:" + action);
        int actionCode = ActionMap.getType(action);
        switch (actionCode) {
            case 1:
            case 2:
            case 3:
            case 4:
                MultipartFile upfile = null;
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                upfile = multipartRequest.getFile("upfile");
                if (upfile == null) {
                    return new BaseState(false, 7).toJSONString();
                }
                State state = new BaseState(true);
                state.putInfo("size", 1000L);
                state.putInfo("title", "测试数据");
                state.putInfo("url", "/admin/uploadimage/1/20190615/120190615203922627.jpg");
                state.putInfo("type", "jpg");
                state.putInfo("original", "测试文件路径....");
                return state.toJSONString();
        }
        try {
            File file = ResourceUtils.getFile("classpath:config.json");
            System.out.println("获取到了配置文件:" + file.getAbsolutePath());
            FileInputStream fis = new FileInputStream(file);
            String data = IOUtils.toString(fis);
            IOUtils.closeQuietly(fis);
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
