package xyz.seiyaya.collection;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @date 2020/5/14 9:28
 */
public class CountDownLatchDemo {

    /**
     * 测试countDownLatch，保证主线程在子线程执行完之后再结束
     * 本质上还是使用的aqs的方法
     */
    @Test
    public void testCountDownLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        // 保证线程按照顺序执行
        new Thread(() ->{
            System.out.println(Thread.currentThread().getName()+"开始执行");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName()+"执行结束");
        }).start();

        new Thread(() ->{
            System.out.println(Thread.currentThread().getName()+"开始执行");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName()+"执行结束");
        }).start();

        new Thread(() ->{
            System.out.println(Thread.currentThread().getName()+"开始执行");
            try {
                TimeUnit.SECONDS.sleep(999);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName()+"执行结束");
        }).start();


        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"执行结束");
    }
}
