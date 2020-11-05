package xyz.seiyaya.common.cache.helper;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/12 14:05
 */
public class ObjectHelper {

    /**
     * 如果为空取默认值
     * @param t
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T ifNull(T t,T defaultValue){
        if( t == null){
            return defaultValue;
        }
        return t;
    }
}
