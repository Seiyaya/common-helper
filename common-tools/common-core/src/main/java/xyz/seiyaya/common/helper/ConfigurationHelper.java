package xyz.seiyaya.common.helper;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置列表
 * @author wangjia
 * @version v1.0
 * @date 2020/12/21 16:41
 */
public class ConfigurationHelper {

    public static final String BATCH_INSERT_SIZE = "batch_insert_size";

    public static final String BATCH_UPDATE_SIZE = "batch_insert_size";


    private static ConcurrentHashMap<String,Object> paramMap = new ConcurrentHashMap<>();


    public static Integer getInteger(String key,Integer defaultValue){
        Object value = paramMap.getOrDefault(key, defaultValue);
        try {
            return Integer.valueOf(value.toString());
        }catch (Exception ignored){

        }
        return defaultValue;
    }
}
