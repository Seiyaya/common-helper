package xyz.seiyaya.base;

import xyz.seiyaya.common.helper.ThreadPoolHelper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/15 11:41
 */
@SuppressWarnings({"PWD"})
public class EasyLock extends AbstractQueuedSynchronizer {

    public void lock(){
        this.acquire(1);
    }

    public boolean tryLock(){
        return this.tryAcquire(1);
    }

    public void unlock(){
        this.release(1);
    }

    public boolean isLocked(){
        return this.isHeldExclusively();
    }


    @Override
    protected boolean tryAcquire(int arg) {
        if(compareAndSetState(0,1)){
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    @Override
    protected boolean tryRelease(int arg) {
        setExclusiveOwnerThread(null);
        setState(0);
        return true;
    }

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        EasyLock easyLock = new EasyLock();
        ExecutorService executorService = ThreadPoolHelper.getPool();
        int clientTotal = 3000;
        CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    easyLock.lock();
                    count++;
                    easyLock.unlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            ThreadPoolHelper.printPoolInfo();
        }
        ThreadPoolHelper.printPoolInfo();
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("统计次数：" + count);
    }
}
