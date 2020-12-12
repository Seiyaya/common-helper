package xyz.seiyaya.common.annotation;

import xyz.seiyaya.common.annotation.handle.CrawlHandler;
import xyz.seiyaya.common.bean.NoneCrawlHandler;

import java.lang.annotation.*;

/**
 * 爬虫注解，主要用来做爬取的JsonObject映射到对应的bean上面
 * 标注在对应bean的属性上面
 * @author wangjia
 * @version 1.0
 * @date 2019/10/17 17:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface CrawlAttribute {

    /**
     * 映射到该属性上面JsonObject的key
     * @return
     */
    String value();

    /**
     * 日期格式化策略
     * @return
     */
    int dateFormat() default -1;

    /**
     * 特殊字段的处理
     * 比如2020-09-30表示的是第四季度
     * @return
     */
    Class<? extends CrawlHandler> handleClass() default NoneCrawlHandler.class;
}
