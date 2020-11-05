package xyz.seiyaya.common.cache.helper;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/15 13:56
 */
@SuppressWarnings("all")
@Slf4j
public class ThreadPoolHelper {

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(10,20,1, TimeUnit.SECONDS,new LinkedBlockingQueue<>());

    public static ThreadPoolExecutor getPool(){
        return THREAD_POOL_EXECUTOR;
    }

    public static void execute(Runnable run){
        THREAD_POOL_EXECUTOR.execute(run);
    }

    public static void printPoolInfo(){
        log.info("active:{} complete:{} queueSize:{} total:{}",THREAD_POOL_EXECUTOR.getActiveCount()
                ,THREAD_POOL_EXECUTOR.getCompletedTaskCount()
                ,THREAD_POOL_EXECUTOR.getQueue().size()
                ,THREAD_POOL_EXECUTOR.getTaskCount());
    }

}
