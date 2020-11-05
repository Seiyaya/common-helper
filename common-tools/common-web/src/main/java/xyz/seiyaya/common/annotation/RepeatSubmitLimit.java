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
     * 时间单位为秒
     * @return
     */
    int time() default 60;
}
