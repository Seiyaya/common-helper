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

    void set(String key , String value , int second);

    String get(String key);

    String hget(String hashKey,String key);

    Boolean remove(String key);

    boolean exists(String key);

    void setObject(String key,Object obj, long time);

    <T>T getObject(String s, Class<T> clazz);

    /**
     * 加锁
     * @param key
     * @param value
     * @return
     */
    boolean lock(String key,String value);

    /**
     * 加锁并指定时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    Boolean lock(String key,String value,int time);

    /**
     * 解锁
     * @param key
     * @param value
     * @return
     */
    Boolean unlock(String key, String value);
}
