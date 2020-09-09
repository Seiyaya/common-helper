package xyz.seiyaya.base;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/9 17:21
 */
public class ThreadPoolExecutorDemo {

    @Test
    public void testSubmit() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,10,1, TimeUnit.SECONDS,new LinkedBlockingQueue<>());

        Future<String> submit = threadPoolExecutor.submit(() -> "string");

        String s = submit.get();

        System.out.println(s);
    }
}
