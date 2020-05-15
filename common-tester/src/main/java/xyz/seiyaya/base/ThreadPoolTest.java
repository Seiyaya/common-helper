package xyz.seiyaya.base;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @date 2020/5/14 14:14
 */
@Slf4j
public class ThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService execute = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        execute.execute(() -> log.info("====11====="));

        TimeUnit.SECONDS.sleep(5);
        execute.execute(()->{
            int count = 0;
            while (true) {
                count++;
                log.info("-------222-------------{}", count);

                if (count == 10) {
                    System.out.println(1 / 0);
                    try {
                    } catch (Exception e) {
                        log.error("Exception",e);
                    }
                }

                if (count == 20) {
                    log.info("count={}", count);
                    break;
                }
            }
        });
    }

}
