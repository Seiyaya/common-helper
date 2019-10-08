package xyz.seiyaya.common.cache.bean;

import xyz.seiyaya.common.cache.task.CleanExpiredDataTask;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 具有过期功能的cache,不想引入redis，可以使用该类来完成
 * @author wangjia
 * @version 1.0
 * @date 2019/10/8 18:32
 */
public class TimerCacheBean {

    /**
     * value开头是时间戳+保存的字符串
     * System.currentTimeMillis()+itemString
     */
    private static ConcurrentHashMap<String,String> TIMER_CACHE = null;

    static{
        TIMER_CACHE = new ConcurrentHashMap<>();
        new CleanExpiredDataTask().start();
    }

    /**
     * 清理过期数据
     */
    public static void cleanExpiredData(){
    }
}
