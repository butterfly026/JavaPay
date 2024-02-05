
package com.city.city_collector.channel.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author nb
 * @Description:
 */
public class TimeUtil {
    /**
     * 时间字符串
     * @author:nb
     * @param timeStr
     * @return
     */
//    public static long timestrToLong(String timeStr) {
//        String[] strs=timeStr.split(":");
//    }

//    public static void main(String[] args) {
////        Long l1=Long.parseLong("001000");
////        System.out.println(l1);
//        System.out.println(getDateMinuteBySS(600L));
//    }

    /**
     * 获取时间：秒
     *
     * @return
     * @author:nb
     */
    public static long getSystemTimeSecond() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取xx秒之前的分钟时间
     *
     * @param time
     * @return
     */
    public static String getDateMinuteBySS(long time) {
        long l = System.currentTimeMillis();
        l = l - time * 1000;
        Date date = new Date(l);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date) + ":00";
    }

    public static List<String> getDateMinuteByMM(int minute) {
        long l = System.currentTimeMillis();
        List<String> list = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int i = minute; i > 0; i--) {
            long l1 = l - i * 60000;
            Date date = new Date(l1);
            list.add(sdf.format(date));
        }

        return list;
    }

    public static String getDateSSMinuteBySS(long time) {
        long l = System.currentTimeMillis();
        l = l - time * 1000;
        Date date = new Date(l);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
