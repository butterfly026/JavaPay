
package com.city.city_collector.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author nb
 * @Description:订单号
 */
public class SnUtil {
    /**
     * 获取订单编号
     *
     * @param pre
     * @return
     * @author:nb
     */
    public static synchronized String createSn(String pre) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
        String sn = pre + sdf.format(new Date()) + getRandomStr(5);
        int i1 = 0;
        while (true) {
            if (SnCacheManager.getInstance().isValidSn(sn)) {
                break;
            }
            i1++;
            if (i1 > 5) {
                return pre + sdf.format(new Date()) + getRandomStr(8);
            }
            sn = pre + sdf.format(new Date()) + getRandomStr(5);
        }
        return sn;
    }

    /**
     * 获取指定长度字符串
     *
     * @param len
     * @return
     * @author:nb
     */
    public static String getRandomStr(int len) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, len);
    }
}
