package xyz.seiyaya.common.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.common.cache.boot.Application;
import xyz.seiyaya.common.cache.service.CacheService;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/14 10:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CacheServiceTester {

    @Resource
    private CacheService cacheService;

    @Test
    public void testLock(){
        String key = "user:1";
        String value = UUID.randomUUID().toString();
        try {
            boolean lock = cacheService.lock(key, value);
            System.out.println("first lock result:"+lock);

            lock = cacheService.lock(key,value);
            System.out.println("second lock result:"+lock);
        }finally {
            cacheService.unlock(key,value);
        }
    }
}
