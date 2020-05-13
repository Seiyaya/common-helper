package xyz.seiyaya.base;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @date 2020/5/11 15:05
 */
@SuppressWarnings("all")
public class ThreadLocalDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            Map<Integer,ThreadLocal> map = new HashMap<>();
            for (int i = 0; i < 20; i++) {
                ThreadLocal threadLocal = new ThreadLocal();
                threadLocal.set(i);
                map.put(i, threadLocal);
            }
            map.forEach((k, v) -> {
                System.out.println(String.format("%s --> %s", k, v.get()));
            });
        });
        thread.start();

        TimeUnit.SECONDS.sleep(3);

        System.gc();
        System.out.println("gc after");
    }

    /**
     * 子线程共享父线程的数据
     */
    @Test
    public void testInheritableThreadLocal() throws InterruptedException {
        ThreadLocal threadLocal = new ThreadLocal();
        threadLocal.set("threadLocal");
        InheritableThreadLocal inheritableThreadLocal = new InheritableThreadLocal();
        inheritableThreadLocal.set("inheritableThreadLocal");
        new Thread(()->{
            System.out.println("threadLocal:"+threadLocal.get());
            System.out.println("inheritableThreadLocal:"+inheritableThreadLocal.get());
        }).start();


        TimeUnit.SECONDS.sleep(2);
    }


    /**
     * 线程池的环境验证是否能子线程共享父线程的数据
     * 父线程将ThreadLocal的数据传递给子线程是在Thread的构造函数中，由于线程池不需要构造函数，所以不能共享
     */
    @Test
    public void threadPoolInheritableThreadLocal() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3,20,1,TimeUnit.SECONDS,new LinkedBlockingQueue<>());
        for(int i=0;i<10;i++){
            executor.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread name :" + Thread.currentThread().getName());
            });
        }
        ThreadLocal threadLocal = new ThreadLocal();
        threadLocal.set("threadLocal");
        InheritableThreadLocal inheritableThreadLocal = new InheritableThreadLocal();
        inheritableThreadLocal.set("inheritableThreadLocal");
        executor.execute(()->{
            System.out.println("threadLocal thread name :" + Thread.currentThread().getName());
            System.out.println("threadLocal:"+threadLocal.get());
            System.out.println("inheritableThreadLocal:"+inheritableThreadLocal.get());
        });

        TimeUnit.SECONDS.sleep(10);
    }

}
