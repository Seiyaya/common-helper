package xyz.seiyaya.collection;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangjia
 * @version v3.9.3
 * @date 2020/5/9 17:23
 */
public class ReentrantLockDemo {

    private static ReentrantLock reentrantLock = new ReentrantLock();

    @SuppressWarnings("all")
    public static void main(String[] args) throws InterruptedException {
        Condition condition = reentrantLock.newCondition();

        new Thread(()->{
            reentrantLock.lock();
            try {
                System.out.println("线程1加锁成功");
                System.out.println("线程1执行await方法");
                condition.await();
                System.out.println("线程1唤醒成功");
            }catch (Exception e){

            }finally {
                reentrantLock.unlock();
                System.out.println("线程1释放锁成功");
            }
        }).start();

        TimeUnit.SECONDS.sleep(3);

        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("线程二加锁成功");
                condition.signal();
                System.out.println("线程二唤醒线程一");
            } finally {
                reentrantLock.unlock();
                System.out.println("线程二释放锁成功");
            }
        }).start();
        TimeUnit.SECONDS.sleep(3);
    }
}
