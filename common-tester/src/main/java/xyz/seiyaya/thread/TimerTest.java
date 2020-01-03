package xyz.seiyaya.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 用来测试定时任务
 * @author wangjia
 * @version 1.0
 * @date 2020/1/2 9:20
 */
@Slf4j
@SuppressWarnings("all")
public class TimerTest {

    public static void main(String[] args) {
        /**
         * 实现原理，内部使用了 TaskQueue 来进行存储任务，本质上是一个数组实现的最小堆
         * 每次添加的任务都按照时间排序，每次执行任务从堆顶取出任务，添加的任务需要重构堆，复杂度为O(logn)
         * 初始化Timer对象的时候会在内部初始化一个TimerThread,调度任务和执行任务是一个线程
         *   存在的问题： 导致如果执行的任务时间过长会影响下一个定时任务
         *      没有异常捕获，导致出现异常(比如空指针)
         *
         *  ScheduledThreadPoolExecutor 用来代替Timer  继承自ThreadPoolExecutor，所以自身就带了多线程属性
         *      阻塞队列使用的是延迟队列
         */
        log.info("start");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("test1");
            }
        },5000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("test2");
            }
        },3000);

        log.info("end");
    }
}
