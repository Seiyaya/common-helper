package xyz.seiyaya.common.bean;

import lombok.Data;

/**
 * 前端的表头
 * @author wangjia
 * @version 1.0
 * @date 2019/11/27 15:10
 */
@Data
public class AuthorityHeadTitle {

    /**
     * 字段名称
     */
    private String filed;

    /**
     * 字段文案，显示的表头
     */
    private String fieldName;

    /**
     * 对应数据库的字段
     */
    private String dbField;
}
