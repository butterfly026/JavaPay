
package com.city.city_collector.channel.util;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.city.city_collector.common.util.MD5Util;
import com.google.gson.Gson;

/**
 * @author nb
 * @Description: 签名工具类
 */
public class SignUtil {

    public static long getSysTime() {
        return System.currentTimeMillis();
    }

    public static String getSysTimeStr() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 获取MD5签名
     *
     * @param parameterMap
     * @param appKey
     * @return
     * @author:nb
     */
    public static String getMD5Sign(Map<String, Object> parameterMap, String appKey) {
//        System.out.println(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, "&key=" + appKey, "&", true, "sign_type", "sign"));
//        System.out.println(DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, "&key=" + appKey, "&", true, "sign_type", "sign")).toUpperCase());
        System.out.println("req order signStr=" + joinKeyValue(new TreeMap<String, Object>(parameterMap), null, "&key=" + appKey, "&", true, "sign_type", "sign"));
        return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, "&key=" + appKey, "&", true, "sign_type", "sign")).toUpperCase();
    }

    public static String getMD5SignLower(Map<String, Object> parameterMap, String appKey) {
//        System.out.println(joinKeyValue(new TreeMap<String, Object>(parameterMap), null,"&key="+appKey, "&", true, "sign_type", "sign"));
//        System.out.println(DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null,"&key="+appKey, "&", true, "sign_type", "sign")).toLowerCase());
        return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, "&key=" + appKey, "&", true, "sign_type", "sign")).toLowerCase();
    }

    /**
     * 平台签名
     *
     * @param parameterMap
     * @param appKey
     * @return
     * @author:nb
     */
    public static String getMd5SignPay(Map<String, Object> parameterMap, String appKey) {
        return DigestUtils.md5Hex(joinKeyValue(new TreeMap<String, Object>(parameterMap), null, appKey, "&", true, "sign_type", "sign")).toLowerCase();
    }

    /**
     * 连接Map键值对
     *
     * @param map              Map
     * @param prefix           前缀
     * @param suffix           后缀
     * @param separator        连接符
     * @param ignoreEmptyValue 忽略空值
     * @param ignoreKeys       忽略Key
     * @return 字符串
     */
    public static String joinKeyValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
        List<String> list = new ArrayList<String>();
        if (map != null) {
            for (Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = ConvertUtils.convert(entry.getValue());
                if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
                    list.add(key + "=" + (value != null ? value : ""));
                }
            }
        }
        return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
    }

    public static String sortSign(TreeMap<String, String> map) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String value = entry.getValue();
            if (!value.isEmpty()) {
                sb.append(String.format("%s%s", entry.getKey(), value));
            }
        }
        return sb.toString();
    }

    public static String genNonceStr() {
        Random random = new Random();
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "utf-8");
    }

    /**
     * <p><b>Description: </b>计算签名摘要
     * <p>2018年9月30日 上午11:32:46
     *
     * @param map 参数Map
     * @param key 商户秘钥
     * @return
     */
    public static String getSign(Map<String, Object> map, String key) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (null != entry.getValue() && !"".equals(entry.getValue())) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
//        result = md5(result, encodingCharset).toUpperCase();
        return result;
    }
}
