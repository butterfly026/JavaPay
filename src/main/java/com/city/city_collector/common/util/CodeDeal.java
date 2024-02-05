
package com.city.city_collector.common.util;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @author nb
 * @Description:
 */
public class CodeDeal {
    public static String Base64Encode(String code) {
        try {
            byte[] bytes = B4.encode(code.getBytes("UTF-8"), 0);
//            bytes=B4.encode(bytes, 0);
            return new String(bytes, "UTF-8").replace("\r", "").replace("\n", "");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        return "";
    }

    public static String Base64Decode(String code) {
        byte[] bytes = B4.decode(code, 0);
//        bytes=B4.decode(bytes, 0);
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定长度的随机字符串
     *
     * @param len
     * @return
     * @author:nb
     */
    public static String getRandomStr(int len) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, len);
    }

    /**
     * 编码URL
     *
     * @param url
     * @return
     * @author:nb
     */
    public static String encodeUrl(String url) {
        return getRandomStr(3) + Base64Encode(url);
    }

    /**
     * 解码URL
     *
     * @return
     * @author:nb
     */
    public static String decodeUrl(String url) {
        return Base64Decode(url.substring(3));
    }

    /**
     * 编码视频地址URL
     *
     * @param url
     * @return
     * @author:nb
     */
    public static String encodeVideoUrl(String url) {
        return getRandomStr(4) + Base64Encode(url);
    }

    /**
     * 解码视频地址URL
     *
     * @return
     * @author:nb
     */
    public static String decodeVideoUrl(String url) {
        return Base64Decode(url.substring(4));
    }

    /**
     * 获取真实的ID
     *
     * @param id
     * @return
     * @author:nb
     */
    public static String getRealId(String id) {
        return id.substring(4);
    }
}
