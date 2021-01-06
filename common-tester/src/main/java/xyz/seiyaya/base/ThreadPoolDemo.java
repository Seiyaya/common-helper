package xyz.seiyaya.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author wangjia
 * @date 2020/5/14 14:14
 */
@Slf4j
public class ThreadPoolDemo {

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


    @Test
    public void testThreadPoolException() throws InterruptedException, ExecutionException {
        /**
         * 任务代码可能抛出RuntimeException，抛出异常后，线程池可能捕获它，也可能创建一个新的线程来代替异常的线程，我们可能无法感知任务出现了异常，因此我们需要考虑线程池异常情况。
         * submit方法会将异常处理成上述,execute方法不会
         */
        ExecutorService executorService = Executors.newFixedThreadPool(5 , factory ->{
            Thread t = new Thread(factory);
            t.setUncaughtExceptionHandler((t1, e) -> System.out.println(t1.getName() + "线程抛出的异常"+e));
            return t;
        });
        for(int i=0;i<5;i++){
            Future<?> future = executorService.submit(() -> {
                System.out.printf("current thread name :%s", Thread.currentThread().getName());
                System.out.println();
                Object obj = null;
                System.out.println("result ## " + obj.toString());
            });
            try {
                Object o = future.get();
                System.out.println(o);
            }catch (Exception e){
                System.out.println(future.toString() + "==== 出现异常");
            }
        }

        /**
         * submit方法执行流程
         * 构造Future对象
         *      1. submit(task)
         *      2. AbstractExecutorService#newTaskFor(java.lang.Runnable, java.lang.Object)
         *      3. new FutureTask<T>(runnable, value)
         * 线程池执行方法
         *      1. ThreadPoolExecutor#execute(java.lang.Runnable)
         *      2. ThreadPoolExecutor#addWorker(java.lang.Runnable, boolean)
         *      3. 调用线程start方法  --> ThreadPoolExecutor#runWorker(java.util.concurrent.ThreadPoolExecutor.Worker)
         *      4. FutureTask#setException(java.lang.Throwable)，即调用futureTask.get方法可以获取异常
         */

        TimeUnit.SECONDS.sleep(5);
    }


    @Test
    public void testFixedExecutorsOOM() throws InterruptedException {
        // 启动参数设置  -Xmx8m -Xms8m
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i=0;i<Integer.MAX_VALUE;i++){
            executorService.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        TimeUnit.SECONDS.sleep(5000);
    }
}
