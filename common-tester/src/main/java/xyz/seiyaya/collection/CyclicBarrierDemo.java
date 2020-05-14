package xyz.seiyaya.collection;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 多线程来的进行阻塞，等待某一个临界值条件满足后，同时执行
 * @author wangjia
 * @date 2020/5/14 10:02
 */
public class CyclicBarrierDemo {

    @Test
    public void testCyclicBarrier() throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        ExecutorService executorPools = new ThreadPoolExecutor(5,10,1, TimeUnit.SECONDS,new LinkedBlockingQueue<>());

        executorPools.submit(new Runner("张三", cyclicBarrier));
        executorPools.submit(new Runner("李四", cyclicBarrier));
        executorPools.submit(new Runner("王五", cyclicBarrier));

        Thread.currentThread().join();
        executorPools.shutdown();
    }

    private static class Runner implements Runnable{

        private String name;

        private CyclicBarrier cyclicBarrier;

        public Runner(String name ,CyclicBarrier cyclicBarrier){
            this.name = name;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                System.out.println("运动员：" + this.name + "进行准备工作！");
                TimeUnit.SECONDS.sleep((new Random().nextInt(5)));
                System.out.println("运动员：" + this.name + "准备完成！");
                this.cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("运动员" + this.name + "开始起跑！！！");
        }
    }
}
