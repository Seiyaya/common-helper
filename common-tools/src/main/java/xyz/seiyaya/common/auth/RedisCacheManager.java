package xyz.seiyaya.common.auth;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.seiyaya.common.cache.service.CacheService;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/18 17:46
 */
public class RedisCacheManager implements CacheManager {


    @Autowired
    private CacheService cacheService;


    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return null;
    }

}
