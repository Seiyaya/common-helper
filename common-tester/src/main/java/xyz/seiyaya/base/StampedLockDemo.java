package xyz.seiyaya.base;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/4 10:02
 */
@Slf4j
@SuppressWarnings("all")
public class StampedLockDemo {

    public static void main(String[] args) throws InterruptedException {
        Point point = new Point();
        Thread thread2 = new Thread(()->{
            point.move(1,1);
        });
        Thread thread3 = new Thread(()->{
            double v = point.distanceFromOrigin();
            log.info("point 距离:{}",v);
        });
        Thread thread1 = new Thread(()->{
            point.move(1,1);
        });
        Thread thread4 = new Thread(()->{
            double v = point.distanceFromOrigin();
            log.info("point 距离:{}",v);
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        Thread.currentThread().join();
    }

    private static class Point {
        private double x, y;

        public Point(){
            this.x = 0;
            this.y = 0;
        }

        private final StampedLock stampedLock = new StampedLock();

        void move(double deltaX, double deltaY) {
            long stamp = stampedLock.writeLock();

            try {
                x += deltaX;
                y += deltaY;
            } finally {
                stampedLock.unlockWrite(stamp);
            }
        }

        /**
         * 多个线程调用该函数求距离
         * @return
         */
        double distanceFromOrigin() {
            // 使用乐观读
            long stamp = stampedLock.tryOptimisticRead();
            // 共享变量拷贝到线程栈
            double currentX = x, currentY = y;

            try {
                log.info("休眠前x:{} y:{} stamp:{}",x,y,stamp);
                TimeUnit.SECONDS.sleep(1);
                log.info("休眠后x:{} y:{} stamp:{}",x,y,stamp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 读的期间有其他线程修改数据
            if (!stampedLock.validate(stamp)) {
                // 版本号被修改，升级为悲观读
                stamp = stampedLock.readLock();
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    stampedLock.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }
    }
}
