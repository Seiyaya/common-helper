package xyz.seiyaya.common.annotation;

import java.lang.annotation.*;

/**
 * 当前登录用户信息
 * @author wangjia
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER})
@Documented
public @interface LoginUser {
}
