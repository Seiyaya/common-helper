package xyz.seiyaya.common.annotation;

import java.lang.annotation.*;

/**
 * 格式化字典，返回的时候字典key转换成对应的value
 * @author wangjia
 * @version 1.0
 * @date 2020/11/3 16:46
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.TYPE})
@Documented
public @interface DictFormat {

    /**
     * 是否显示原始的key
     * 比如 status=1 是否显示
     * @return
     */
    boolean showOriginal() default false;

    /**
     * 字典type
     * @return
     */
    String type();

    /**
     * 默认取 字段+Str，showOriginal=true的时候才使用
     * @return
     */
    String fieldName() default "";

    /**
     * 字典默认值
     * @return
     */
    String defaultValue() default "";
}
