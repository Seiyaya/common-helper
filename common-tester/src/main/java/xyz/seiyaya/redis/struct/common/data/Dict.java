package xyz.seiyaya.redis.struct.common.data;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 数据库键空间，存储着所有的键值对
 * @author wangjia
 * @date 2020/5/25 11:20
 */
@Data
public class Dict<V> {

    /**
     * object可以为hash、List、String
     */
    private Map<String,V> maps = new HashMap<>();

    public void put(String key,V value){
        maps.put(key,value);
    }

    public V del(String key){
        return maps.remove(key);
    }

    public V get(String key){

        return maps.get(key);
    }

    public void flushDB(){
        maps.clear();
    }

    public int dbSize(){
        return maps.size();
    }

    public Set<String> keySet(){
        return maps.keySet();
    }
}
