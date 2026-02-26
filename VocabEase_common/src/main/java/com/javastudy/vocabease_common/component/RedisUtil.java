package com.javastudy.vocabease_common.component;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component("redisUtil")
public class RedisUtil<V> {

    @Resource
    private RedisTemplate<String, V> redisTemplate;

    public static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * 删除缓存
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1)
                redisTemplate.delete(key[0]);
            else
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
        }
    }

    public V get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     */
    public boolean set(String key, V value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }
    /**
     * 普通缓存放入并设置时间
     */
    public boolean sets(String key, V value, long time) {
        try {
            if (time > 0)
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            else
                set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }

    /**
     * 计数器
     */
    public long increment(String key, long delta, long time) {
        if (delta < 0)
            throw  new RuntimeException("递增因子必须大于0");
        long result = redisTemplate.opsForValue().increment(key, delta);
        if (result == 1)
            expire(key, time);
        return result;
    }
    /**
     * 过期操作
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0)
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}





















