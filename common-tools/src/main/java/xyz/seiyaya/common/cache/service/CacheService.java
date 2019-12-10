package xyz.seiyaya.common.cache.service;

/**
 * 缓存服务，可能有local、file、redis的实现
 * @author wangjia
 * @version 1.0
 * @date 2019/10/25 13:39
 */
public interface CacheService {

    String CACHE_SERVICE_REDIS = "redisCacheService";

    String CACHE_SERVICE_FILE = "fileCacheService";

    String CACHE_SERVICE_LOCAL = "localCacheService";

    void set(String key,String value);

    void set(String key);

    void set(String key , int second);

    void set(String key , String value , int second);

    String get(String key);

    void remove(String key);

    boolean exists(String key);
}
