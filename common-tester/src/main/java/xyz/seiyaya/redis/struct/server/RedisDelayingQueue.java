package xyz.seiyaya.redis.struct.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列
 *
 * @author wangjia
 * @version 1.0
 * @date 2020/5/29 15:41
 */
@Slf4j
public class RedisDelayingQueue<T> {

    @Data
    private static class TaskItem<T> {
        String id;
        T msg;
    }

    Type type = new TypeReference<TaskItem<T>>() {
    }.getType();

    Jedis jedis;

    String queueKey;

    public RedisDelayingQueue(Jedis jedis, String queueKey) {
        this.jedis = jedis;
        this.queueKey = queueKey;
    }

    public void delay(T msg) {
        TaskItem<T> taskItem = new TaskItem<>();
        taskItem.id = UUID.randomUUID().toString();
        taskItem.msg = msg;
        String s = JSON.toJSONString(taskItem);
        jedis.zadd(queueKey, System.currentTimeMillis() + 5000, s);
    }

    public void loop() throws InterruptedException {
        while (!Thread.interrupted()) {
            Set<String> values = jedis.zrangeByScore(queueKey, 0, System.currentTimeMillis(), 0, 1);
            if (values.isEmpty()) {
                TimeUnit.SECONDS.sleep(5);
                continue;
            }

            String s = values.iterator().next();
            if (jedis.zrem(queueKey, s) > 0) {
                TaskItem<T> taskItem = JSON.parseObject(s, type);
                handlerMsg(taskItem.msg);
            }
        }
    }

    private void handlerMsg(T msg) {
        System.out.println(msg);
    }


    public static void main(String[] args) {
        Jedis jedis = new Jedis("www.seiyaya.xyz",6388);
        jedis.connect();
        if(!jedis.isConnected()){
            log.error("connect error");
            return ;
        }
        RedisDelayingQueue<Object> delayingQueue = new RedisDelayingQueue<>(jedis, "msg_queue");
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                delayingQueue.delay("msg : " + i);
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                delayingQueue.loop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();


        try {
            producer.join();
            TimeUnit.SECONDS.sleep(6);
            consumer.interrupt();
            consumer.join();
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
