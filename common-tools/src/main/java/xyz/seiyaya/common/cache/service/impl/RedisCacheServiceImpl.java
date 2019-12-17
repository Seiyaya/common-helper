package xyz.seiyaya.common.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.seiyaya.common.cache.service.CacheService;

import static xyz.seiyaya.common.cache.service.CacheService.CACHE_SERVICE_REDIS;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/25 13:44
 */
@Service(CACHE_SERVICE_REDIS)
@Lazy
public class RedisCacheServiceImpl implements CacheService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String value) {

    }

    @Override
    public void set(String key) {

    }

    @Override
    public void set(String key, int second) {

    }

    @Override
    public void set(String key, String value, int second) {

    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public boolean exists(String key) {
        return false;
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        String result = stringRedisTemplate.opsForValue().get(key);
        return JSONObject.parseObject(result,clazz);
    }
}
