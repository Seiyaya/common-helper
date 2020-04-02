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

/**
 * redis相关测试
 * @author wangjia
 * @version 1.0
 * @date 2020/3/31 9:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesterApplication.class)
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
}
