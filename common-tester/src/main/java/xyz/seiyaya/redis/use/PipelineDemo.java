package xyz.seiyaya.redis.use;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * pipeline和一条一条保存和阻塞队列保存比较
 *
 * @author wangjia
 * @version 1.0
 */
@Slf4j
public class PipelineDemo {

    /**
     * 测试结果
     * <pre>
     * ordinary set :361165
     * pipeline set :192
     * java memory set:4
     * </pre>
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis("redis.seiyaya.com", 6388);
        jedis.select(1);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            jedis.set(String.valueOf(i), String.valueOf(i));
        }
        long end = System.currentTimeMillis();
        log.info("ordinary set :{}", end - start);

        Pipeline pipelined = jedis.pipelined();
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            pipelined.set("key"+i, String.valueOf(i));
        }
        pipelined.sync();
        end = System.currentTimeMillis();
        log.info("pipeline set :{}", end - start);

        BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            logQueue.put("i=" + i);
        }
        end = System.currentTimeMillis();
        log.info("java memory set:{}", end - start);
    }
}
