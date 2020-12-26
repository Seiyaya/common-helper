package xyz.seiyaya.common.annotation.handle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.common.annotation.CrawlAttribute;
import xyz.seiyaya.common.bean.NoneCrawlHandler;
import xyz.seiyaya.common.helper.CheckConditionHelper;
import xyz.seiyaya.common.helper.CollectionHelper;
import xyz.seiyaya.common.helper.DateHelper;
import xyz.seiyaya.common.helper.NumberHelper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 爬虫工具类
 * @author wangjia
 * @version 1.0
 * @date 2019/10/17 17:52
 */
@Slf4j
public class CrawlHelper<T> {

    /**
     * 从json转对应的属性到bean上面
     * @param clazz
     * @param obj
     * @return
     * @throws Exception
     */
    public T getT(Class<T> clazz,JSONObject obj) {
        T t = null;
        try {
            t = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            if(fields.length == 0){
                return null;
            }
            for(Field field : fields){
                field.setAccessible(true);
                // 设置属性的值
                CrawlAttribute crawlAttribute = field.getAnnotation(CrawlAttribute.class);
                if(crawlAttribute != null){
                    String key = crawlAttribute.value();
                    Object o = obj.get(key);
                    field.set(t,o);
                }
            }
        }catch (Exception e){
            log.error("",e);
        }
        return t;
    }

    /**
     * 获取list T
     * @param clazz
     * @param jsonArray
     * @return
     */
    public List<T> getListT(Class<T> clazz, JSONArray jsonArray) {
        List<Field> fieldList = checkCrawlAnnotation(clazz);
        CheckConditionHelper.checkParams(CollectionHelper.isEmpty(fieldList),String.format("解析的对象没有标注相关注解@CrawlAttribute,clazz:%s",clazz.getName()));

        List<T> list = new ArrayList<>(jsonArray.size());
        // 缓存handler对象
        Map<String,CrawlHandler> handlerMap = Maps.newHashMap();
        // 暂时不考虑list里面包含嵌套对象的情况
        jsonArray.stream().filter(model -> model instanceof JSONObject).forEach(model -> {
            JSONObject obj = (JSONObject) model;
            try {
                T t = clazz.newInstance();
                for (Field field : fieldList) {
                    CrawlAttribute crawlAttribute = field.getAnnotation(CrawlAttribute.class);
                    String value = crawlAttribute.value();
                    Object o = obj.get(value);
                    if(!NoneCrawlHandler.class.equals(crawlAttribute.handleClass())){
                        o = dealSpecialField(handlerMap,value,o,crawlAttribute);
                    } else if( Date.class.equals(field.getType())){
                        o = dealDateType(crawlAttribute, o);
                    }else if(BigDecimal.class.equals(field.getType())){
                        o = NumberHelper.parseBigDecimal(o.toString());
                    }else if(Byte.class.equals(field.getType())){
                        o = NumberHelper.parseByte(o.toString());
                    }
                    field.set(t, o);
                }
                list.add(t);
            } catch (Exception e) {
                log.error("",e);
            }
        });

        return list;
    }

    /**
     * 特殊字段处理
     * @param handlerMap     缓存map
     * @param value          缓存的key,表示的是映射的属性名
     * @param o              映射的属性值
     * @param crawlAttribute    注解
     * @return
     */
    private Object dealSpecialField(Map<String, CrawlHandler> handlerMap, String value,Object o,CrawlAttribute crawlAttribute) throws Exception {
        Class<? extends CrawlHandler> crawlHandlerClass = crawlAttribute.handleClass();
        CrawlHandler crawlHandler;
        if(handlerMap.containsKey(value)){
            crawlHandler = handlerMap.get(value);
        }else{
            crawlHandler = crawlHandlerClass.newInstance();
            handlerMap.put(value,crawlHandler);
        }
        return crawlHandler.handle(o);
    }

    /**
     * 日期类型处理
     * @param crawlAttribute
     * @param o
     * @return
     * @throws IllegalAccessException
     */
    private Date dealDateType(CrawlAttribute crawlAttribute, Object o){
        // 日期类型特殊处理
        Date date = null;
        try {
            switch (crawlAttribute.dateFormat()){
                case 0:
                    String format = DateHelper.formatDate(new Date(),"yyyy-") +  o.toString();
                    date = DateHelper.parseDate(format, DateHelper.YYYY_MM_DD);
                    break;
                default:
                    date = DateHelper.parseDate(o.toString(),DateHelper.YYYY_MM_DD);
            }
        }catch (Exception ignored){
        }
        return date;
    }

    /**
     * 校验是否有crawl注解
     * @param clazz
     * @return
     */
    private List<Field> checkCrawlAnnotation(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        // 过滤得到标注有@CrawlAttribute的属性
        List<Field> fieldList = new ArrayList<>(fields.length);
        for(Field field : fields){
            field.setAccessible(true);
            CrawlAttribute crawlAttribute = field.getAnnotation(CrawlAttribute.class);
            if(crawlAttribute != null){
                fieldList.add(field);
            }
        }
        return fieldList;
    }
}
