package xyz.seiyaya.common.cache.service.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import xyz.seiyaya.common.cache.service.CacheService;

import static xyz.seiyaya.common.cache.service.CacheService.CACHE_SERVICE_FILE;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/25 13:45
 */
@Service(CACHE_SERVICE_FILE)
@Lazy
public class FileCacheServiceImpl implements CacheService {
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
    public <T> T getObject(String s, Class<T> clazz) {
        T t = null;
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
