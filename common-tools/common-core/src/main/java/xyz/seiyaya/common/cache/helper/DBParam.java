package xyz.seiyaya.common.cache.helper;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * 简便的操作hashmap
 */
@Slf4j
public class DBParam extends HashMap<String, Object> {

    public DBParam set(String key, Object value) {
        put(key, value);
        return this;
    }

    public String getString(String key) {
        return get(key).toString();
    }

    public Integer getInt(String key) {
        Object value ;
        try {
            value =  get(key);
            if(value == null){
                return 0;
            }
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            log.error("int转化异常，默认返回0");
            return 0;
        }
    }

    public double getDouble(String key) {
        Object value;
        try {
            value = get(key);
            if(value == null){
                return 0;
            }
            return Double.parseDouble(String.valueOf(value));
        } catch (Exception e) {
            log.error("double转化异常，默认返回0");
            return 0;
        }
    }

    public <T>T getT(String key,Class<T> clazz){
        return (T)get(key);
    }

    public Long getLong(String key) {
        Object value;
        try {
            value = get(key);
            if(value == null){
                return 0L;
            }
            return Long.parseLong(String.valueOf(value));
        } catch (Exception e) {
            log.error("long转化异常，默认返回0");
            return 0L;
        }
    }
}
