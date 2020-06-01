package xyz.seiyaya.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.TesterApplication;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * redis相关测试
 * @author wangjia
 * @version 1.0
 * @date 2020/3/31 9:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesterApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
@Slf4j
@SuppressWarnings("all")
public class RedisTemplateTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 测试hash结构
     */
    @Test
    public void testSetHash(){
        // 超过512个元素  ziplist 转为  hashtable
        for(int i = 1 ; i <= 512 ;i++){
            stringRedisTemplate.opsForHash().put("profile","name" + i ,"tom");
        }
    }

    @Test
    public void testGetHash(){
        Set<Object> profile = stringRedisTemplate.opsForHash().keys("profile");
        profile.forEach(model->{
            Object value = stringRedisTemplate.opsForHash().get("profile", model);
            log.info("k:{} --> v:{}",model,value);
        });
    }

    /**
     * 从99开始出现误差
     */
    @Test
    public void testHyperLogLog(){
        String key = "hyper_log";
        for(int i=0;;i++){
            stringRedisTemplate.opsForHyperLogLog().add(key,"user"+i);
            Long size = stringRedisTemplate.opsForHyperLogLog().size(key);
            if(size != i+1){
                System.out.println(String.format("difference hyperLogLog size:%s --> realSize:%s",size,i+1));
                break;
            }
        }
    }

    /**
     * 对于100w数据的误差
     */
    @Test
    public void testHyperLogLogMistake() throws InterruptedException {
        String key = "hyper_log1";
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        CountDownLatch countDownLatch = new CountDownLatch(8);
        for(int i=0;i<8;i++){
            final int start = i * 10000 + 1;
            final int end = (i+1) * 10000;
            executorService.execute(()->{
                for(int j=start;j<end;j++){
                    stringRedisTemplate.opsForHyperLogLog().add(key,"user"+j);
                    if((j-start) % 1000 == 0){
                        System.out.println(stringRedisTemplate.opsForHyperLogLog().size(key));
                    }
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        executorService.shutdown();
        System.out.println(String.format("mistake redisDbSize:%s --->  realSize:80000",stringRedisTemplate.opsForHyperLogLog().size(key)));
    }
}
