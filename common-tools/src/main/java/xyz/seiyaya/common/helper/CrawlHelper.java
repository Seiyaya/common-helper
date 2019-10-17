package xyz.seiyaya.common.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import xyz.seiyaya.common.annotation.CrawlAttribute;

import java.lang.reflect.Field;

/**
 * 爬虫工具类
 * @author wangjia
 * @version 1.0
 * @date 2019/10/17 17:52
 */
@Slf4j
public class CrawlHelper {

    /**
     * 从json转对应的属性到bean上面
     * @param clazz
     * @param json
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getT(Class<T> clazz,Object json) throws Exception {
        T t = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        if(fields.length == 0){
            return null;
        }
        for(Field field : fields){
            field.setAccessible(true);

            CrawlAttribute crawlAttribute = field.getAnnotation(CrawlAttribute.class);
            if(crawlAttribute != null){
                String key = crawlAttribute.value();
                //从json对象中搜索key为value的属性
                Object value = OgnlCache.getValue(key, json);
                if(value != null){
                    field.set(t,value);
                }
            }
        }
        return t;
    }
}
