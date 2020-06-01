package xyz.seiyaya.redis.application;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 简单限流
 *
 * @author wangjia
 * @version 1.0
 * @date 2020/6/1 10:38
 */
public class SimpleRateLimiter {

    private Jedis jedis;

    public SimpleRateLimiter(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean isActionAllowed(String userId, String actionKey, int period, int maxCount) {
        String key = String.format("hist:%s:%s", userId, actionKey);
        long nowTs = System.currentTimeMillis();
        Pipeline pipelined = jedis.pipelined();
        pipelined.multi();
        pipelined.zadd(key, nowTs, String.valueOf(nowTs));
        pipelined.zremrangeByScore(key, 0, nowTs - period * 1000);
        Response<Long> count = pipelined.zcard(key);
        pipelined.expire(key, period + 1);
        pipelined.exec();
        pipelined.close();
        return count.get() <= maxCount;
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis.seiyaya.com", 6388);
        SimpleRateLimiter simpleRateLimiter = new SimpleRateLimiter(jedis);

        for (int i = 0; i < 20; i++) {
            System.out.println(simpleRateLimiter.isActionAllowed("zhangsan", "login", 60, 5));
        }
    }
}
