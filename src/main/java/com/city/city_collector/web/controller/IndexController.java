
package com.city.city_collector.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nb
 * @Description:
 */
@Controller
@RequestMapping("/")
public class IndexController {
    /**
     * 缓存名前缀
     */
//    public final static String CACHE_PRE = "SHCLER_";
    /**
     * 主页缓存
     */
//    public final static String DOMAIN_CACHE = "SHCLER_VIDEO_INDEXLIST";


    /**
     * 首页
     *
     * @param request
     * @param model
     * @return
     * @author:nb
     */
    @RequestMapping("/")
    public @ResponseBody
    String index(HttpServletRequest request, Model model) {
//        String ua = request.getHeader("User-Agent");
//        boolean flag=false;
//        if (ua!=null && !ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
//            // 排除 苹果桌面系统
//            if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
//                for (String item : agent) {
//                    if (ua.contains(item)) {
//                        flag = true;
//                        break;
//                    }
//                }
//            }
//        }
//        if(flag) {
//            return "redirect:mindex.html";
//        }else {
//            return "redirect:index.html";
//        }
        return "";
    }


//    private final static String[] agent = {"Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser"};

    @RequestMapping("/test")
    public @ResponseBody
    String test() {
        return "success";
    }

//    @RequestMapping("/success1")
//    public String success(HttpServletRequest request, Model model) {
//        return "/admin/success";
//    }
//
//    @RequestMapping("/error1")
//    public String error(HttpServletRequest request, Model model) {
//        return "/admin/error";
//    }
}
