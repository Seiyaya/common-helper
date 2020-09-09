package xyz.seiyaya.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/25 18:12
 */
@Slf4j
public class ThreadDemo {


    @Test
    public void testStop() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("sleep 2s");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();



        TimeUnit.SECONDS.sleep(20);

        thread.stop();

        System.out.println("stop method call");

        TimeUnit.SECONDS.sleep(5);
        System.out.println("end");
    }


    /**
     * 单元测试并不能测试出效果
     * 使用main方法会主线程结束  但是子线程仍然在运行
     */
    @Test
    public void testThreadDaemon(){
        Thread thread = new Thread(() -> {
            while(true){
                System.out.println("sub thread is running");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        System.out.println("current thread name :"+Thread.currentThread().getName());

        System.out.println("current thread isDaemon:"+Thread.currentThread().isDaemon());

        System.out.println("main exit");
    }

    @Test
    public void testYield() throws InterruptedException {
        Runnable run = ()->{
          for(int i=0;i<30;i++){
              System.out.println(Thread.currentThread().getName()+"-->"+i);
              if(i == 20){
                  System.out.println(Thread.currentThread().getName()+" 执行yield");
                  Thread.yield();
              }
          }
        };

        new Thread(run).start();
        new Thread(run).start();

        Thread.currentThread().join();
    }

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
