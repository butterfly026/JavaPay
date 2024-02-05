package com.city.city_collector.common.util;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class SingleLoginUtil {
    public static void updateUserVersion(HttpServletRequest request, Long id, Integer version) {
        ServletContext servletContext = request.getServletContext();
        Map<Long, Integer> map = new HashMap<Long, Integer>();
        if (servletContext.getAttribute("ALL_LOGIN_USER") == null)
            servletContext.setAttribute("ALL_LOGIN_USER", map);
        else {
            map = (Map) servletContext.getAttribute("ALL_LOGIN_USER");
        }
        map.put(id, version);
    }

    public static boolean validUserLoginException(HttpServletRequest request, Long id, Integer version) {
        ServletContext servletContext = request.getServletContext();
        Map<Long, Integer> map = (Map) servletContext.getAttribute("ALL_LOGIN_USER");
        if (map == null)
            return false;
        Integer stu = (Integer) map.get(id);
        if (stu == null)
            return false;
        return !stu.equals(version);
    }
}
