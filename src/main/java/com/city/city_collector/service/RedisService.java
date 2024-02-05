//package com.city.city_collector.service;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Service;
//
//@Service("redisService")
//public class RedisService {
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    public void addCacheValue(String key, String value, Long expireTime) {
//        if (expireTime != null)
//            this.stringRedisTemplate.opsForValue().set(key, value, expireTime.longValue(), TimeUnit.SECONDS);
//        else
//            this.stringRedisTemplate.opsForValue().set(key, value);
//    }
//
//    public void addCacheValue(String key, String value) {
//        addCacheValue(key, value, null);
//    }
//
//    public String getCacheValue(String key) {
//        if (key == null)
//            return null;
//        return (String) this.stringRedisTemplate.opsForValue().get(key);
//    }
//
//    public void removeCacheValue(String key) {
//        this.stringRedisTemplate.delete(key);
//    }
//
//    public String getUqCookieValue() {
//        if (this.stringRedisTemplate.opsForValue().get("ADMIN_SESSIONCOOKIE") == null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
//            this.stringRedisTemplate.opsForValue().set("ADMIN_SESSIONCOOKIE", sdf.format(new Date()));
//        }
//
//        Long ids = this.stringRedisTemplate.opsForValue().increment("ADMIN_SESSIONCOOKIE", 1L);
//        return UUID.randomUUID().toString().replace("-", "").substring(0, 6) + ids;
//    }
//}