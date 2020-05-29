package xyz.seiyaya.redis.struct.server;

import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Type;

/**
 * 延时队列
 * @author wangjia
 * @version 1.0
 * @date 2020/5/29 15:41
 */
public class RedisDelayingQueue<T> {

    @Data
    private static class TaskItem<T>{
        String id;
        T msg;
    }

    Type type = new TypeReference<TaskItem<TaskItem<T>>>() {}.getType();

    Jedis jedis;

    String queueKey;


}
