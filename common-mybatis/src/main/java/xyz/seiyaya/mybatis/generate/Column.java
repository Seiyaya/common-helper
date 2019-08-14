package xyz.seiyaya.mybatis.generate;

import lombok.Data;

/**
 * @author seiyaya
 * @date 2019/8/13 15:20
 */
@Data
public class Column {

    /**
     * 列名
     */
    private String columnName;
    /**
     * 数据库的数据类型
     */
    private String dataType;
    /**
     * mapper中的数据类型
     */
    private String jdbcType;
    /**
     * 列的备注
     */
    private String columnComment;

    /**
     * 首字母大写列名
     */
    private String columnNameUpper;

    /**
     * 数据无转换列名
     */
    private String typeName;
}
