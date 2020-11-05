package xyz.seiyaya.common.annotation;

import java.lang.annotation.*;

/**
 * 用来做返回给前端的列表字段，指定哪些列是要返回的或者给这些列加上权限
 * @author wangjia
 * @version 1.0
 * @date 2019/11/27 11:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface AuthorityTableData {

    /**
     * 指定前端表格上显示的文案
     * @return
     */
    String title();

    /**
     * 权限标识，可以配置在权限表中，然后关联角色
     * @return
     */
    String authority();

    /**
     * 如果是字典类型，则通过dictType和对象的值进行转换成中文意思
     * 避免前端也要加上判断条件
     * @return
     */
    String dictType();

    /**
     * 对应数据库的字段，查询表的时候会使用到
     * @return
     */
    String dbField();
}
