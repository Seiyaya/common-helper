package xyz.seiyaya.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 线程测试
 * @author wangjia
 * @version 1.0
 * @date 2020/6/1 9:00
 */
@Slf4j
public class ThreadTest {

    @Test
    public void testJoin() throws InterruptedException {
        Thread start = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
                log.info("sleep three second");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        start.start();
        // 等待start线程终止
        start.join();
    }
}
