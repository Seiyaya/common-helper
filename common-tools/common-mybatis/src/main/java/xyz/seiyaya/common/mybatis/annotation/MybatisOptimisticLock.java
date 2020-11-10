package xyz.seiyaya.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * mybatis乐观锁注解，用来判断是否经过乐观锁拦截器
 * @author wangjia
 * @version v1.0
 * @date 2020/11/10 10:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface MybatisOptimisticLock {

    /**
     * 版本控制字段，默认为version
     * @return
     */
    String value() default "version";

    /**
     * 版本控制字段类型
     * 0 string
     * 1 int
     * 2 long
     * @return
     */
    int versionType() default 0;
}
