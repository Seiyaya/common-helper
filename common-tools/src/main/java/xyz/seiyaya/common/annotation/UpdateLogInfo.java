package xyz.seiyaya.common.annotation;

import java.lang.annotation.*;

/**
 * 修改操作记录，类似禅道的   修改了 解决方案，旧值为 ""，新值为 "fixed"
 * @author wangjia
 * @version 1.0
 * @date 2020/10/31 17:03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.TYPE})
@Documented
public @interface UpdateLogInfo {

    /**
     * 修改的字段类型，对应上面的 解决方案
     * @return
     */
    String value();

    /**
     * 修改描述的格式
     * 对应上面 旧值为 ""，新值为 "fixed"
     * @return
     */
    String updatePattern() default "旧值为\"%s\"，新值为\"%s\"";

    /**
     * 日期的比较格式，对于用户来说不需要精确到毫秒，直接比较格式化后的字符串即可
     * @return
     */
    String datePattern() default "yyyy-MM-dd";
}
