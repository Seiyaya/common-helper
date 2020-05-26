package xyz.seiyaya.redis.struct.common;

import lombok.Data;
import xyz.seiyaya.redis.struct.client.RedisClient;

/**
 * 模式订阅关系
 * @author wangjia
 * @date 2020/5/26 16:43
 */
@Data
public class PubSubPattern {

    /**
     * 与server建立模式连接的服务
     */
    RedisClient redisClient;

    /**
     * 订阅的模式
     */
    String pattern;
}
