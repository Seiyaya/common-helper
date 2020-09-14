package xyz.seiyaya.common.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import xyz.seiyaya.common.cache.service.CacheService;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;

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

    private DefaultRedisScript<Long> script;

    @PostConstruct
    public void init(){
        script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptText("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end");
    }

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void set(String key, String value, int second) {
        stringRedisTemplate.opsForValue().set(key,value,Duration.ofSeconds(second));
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public String hget(String hashKey, String key) {
        return String.valueOf(stringRedisTemplate.opsForHash().get(hashKey,key));
    }

    @Override
    public Boolean remove(String key) {
        return stringRedisTemplate.delete(key);
    }

    @Override
    public boolean exists(String key) {
        Boolean result = stringRedisTemplate.hasKey(key);
        return result != null && result ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        String result = stringRedisTemplate.opsForValue().get(key);
        return JSONObject.parseObject(result,clazz);
    }

    @Override
    public boolean lock(String key, String value) {
        return lock(key,value,60);
    }

    @Override
    public Boolean lock(String key, String value, int time) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key,value, Duration.ofSeconds(time));
    }

    @Override
    public Boolean unlock(String key, String value) {
        Long result = stringRedisTemplate.execute(script, Collections.singletonList(key), value);
        return result != null && result == 1;
    }
}
