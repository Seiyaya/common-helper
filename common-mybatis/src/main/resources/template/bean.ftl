package ${packageName}.${moduleName}.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ${functionName}实体
 * 
 * @author ${classAuthor}
 * @version 1.0.0
 * @date ${classDate}
 */
@Data
public class ${ClassName} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list list as item>
    <#if item.columnName == "id">
    /**
    * 主键Id
    */
    private Long id;

    <#else >
    /**
    * ${item.columnComment}
    */
    private ${item.dataType} ${item.columnName};

    </#if >
</#list>
}