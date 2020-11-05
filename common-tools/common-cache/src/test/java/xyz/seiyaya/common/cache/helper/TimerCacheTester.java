package xyz.seiyaya.common.cache.helper;

import org.junit.Test;
import xyz.seiyaya.common.cache.bean.TimerCacheBean;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/10/9 14:14
 */
public class TimerCacheTester {

    @Test
    public void testSet() throws InterruptedException {
        TimerCacheBean.set("last_game",19);
        System.out.println(TimerCacheBean.exists("last_game"));

        TimeUnit.SECONDS.sleep(20);

        System.out.println(TimerCacheBean.exists("last_game"));
    }

    @Test
    public void testSystem() throws InterruptedException {
        long time = System.currentTimeMillis();
        System.out.println(time+"__>"+String.valueOf(time).length());
        TimeUnit.SECONDS.sleep(2);
        time = System.currentTimeMillis();
        System.out.println(time+"__>"+String.valueOf(time).length());

        System.out.println("1570602953501".substring(0,13));
    }
}
