package xyz.seiyaya.common.cache.bean;

import xyz.seiyaya.common.cache.helper.StringHelper;
import xyz.seiyaya.common.cache.task.CleanExpiredDataTask;

import java.util.Iterator;
import java.util.Map;
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
    
    public static final int END_POSITION = 13;

    static{
        TIMER_CACHE = new ConcurrentHashMap<>();
        new CleanExpiredDataTask().start();
    }

    /**
     * 清理过期数据
     */
    public static void cleanExpiredData(){
        if (TIMER_CACHE != null) {
            // 计算过期时间
            long time = System.currentTimeMillis();
            Iterator<Map.Entry<String, String>> it = TIMER_CACHE.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, String> next = it.next();
                String value = next.getValue();
                if(StringHelper.isNotEmpty(value)){
                    String timeStr = value.substring(0,END_POSITION);
                    long endTime = Long.parseLong(timeStr);
                    if (time > endTime) {
                        it.remove();
                    }
                }
            }
        }
    }

    /**
     * 设置数据
     * @param key
     * @param value
     */
    public static void set(String key , String value){
        //默认为1分钟
        set(key,value,60);
    }

    /**
     * 设置数据
     * @param key
     */
    public static void set(String key){
        set(key,null,60);
    }

    /**
     * 设置数据
     * @param key
     * @param second
     */
    public static void set(String key , int second){
        set(key,null,second);
    }

    /**
     * 设置数据
     * @param key
     * @param value
     */
    public static void set(String key , String value , int second){
        if (value == null) {
            value = "";
        }
        long needTime = second * 1000 + System.currentTimeMillis() ;
        TIMER_CACHE.put(key,needTime + value);
    }

    /**
     * 获取数据
     * @param key
     */
    public static String get(String key){
        String value = TIMER_CACHE.get(key);
        if (value == null) {
            return null;
        }
        if (judgeExpired(key, value)){
            return null;
        }
        return value.substring(END_POSITION);
    }

    /**
     * 移除元素
     * @param key
     */
    public static void remove(String key){
        TIMER_CACHE.remove(key);
    }

    /**
     * 判断是否包含元素
     * @param key
     * @return
     */
    public static boolean exists(String key){
        String value = TIMER_CACHE.get(key);
        if (value == null) {
            return false;
        }
        if (judgeExpired(key, value)){
            return false;
        }
        return true;
    }

    /**
     * 判断数据是否过期
     * @param key
     * @param value
     * @return
     */
    private static boolean judgeExpired(String key, String value) {
        String endTime = value.substring(0, END_POSITION);
        if(System.currentTimeMillis() > Long.parseLong(endTime)){
            //说明已经过期了
            TIMER_CACHE.remove(key);
            return true;
        }
        return false;
    }
}
