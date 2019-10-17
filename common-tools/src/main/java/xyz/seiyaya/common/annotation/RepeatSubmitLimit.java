package xyz.seiyaya.common.annotation;

import java.lang.annotation.*;

/**
 *
 * @author wangjia
 * @version 1.0
 * @date 2019/10/16 9:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface RepeatSubmitLimit {

    /**
     * 缓存的key，还可能是前缀
     * @return
     */
    String value();

    /**
     * 时间单位为秒
     * @return
     */
    int time() default 60;

    /**
     * 表达式，请求参数中提取对应的表达式
     * 最后转换为key
     * userId+"_"+customerId
     * @return
     */
    String[] expression() default {};
}
