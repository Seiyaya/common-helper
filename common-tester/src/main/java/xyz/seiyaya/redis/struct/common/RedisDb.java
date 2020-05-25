package xyz.seiyaya.redis.struct.common;

import lombok.Data;
import xyz.seiyaya.redis.struct.server.Dict;

/**
 * @author wangjia
 * @date 2020/5/25 11:15
 */
@Data
public class RedisDb {

    /**
     * 字典，存储具体的键值对
     */
    Dict<Object> dict;

    /**
     * 过期字典，存储过期时间
     */
    Dict<Long> expires;

    public Object get(String key){
        if(expireIfNeeded(key)){
            return null;
        }
        return dict.get(key);
    }

    public int size(){
        return dict.dbSize();
    }

    public int expireSize(){
        return expires.dbSize();
    }

    public void delExpire(String key){
        dict.del(key);
        expires.del(key);
    }


    /**
     * 惰性删除
     * @param key
     * @return
     */
    private boolean expireIfNeeded(String key){
        Long expire = expires.get(key);
        if(expire == null){
            return true;
        }
        if(expire < System.currentTimeMillis()){
            // 已经过期
            dict.del(key);
            return true;
        }
        return false;
    }
}
