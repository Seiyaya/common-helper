package xyz.seiyaya.common.annotation.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
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

    private static ExpressionParser parser = new SpelExpressionParser();

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
                Object value = parser.parseExpression(key).getValue(json);
                if(value != null){
                    field.set(t,value);
                }
            }
        }
        return t;
    }
}
