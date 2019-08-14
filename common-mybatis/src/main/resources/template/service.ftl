package ${packageName}.${moduleName}.service;

import ${commonName}.service.BaseService;
import ${packageName}.${moduleName}.${ClassName};
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * ${functionName}Service
 *
 * @author ${classAuthor}
 * @version 1.0.0
 * @date ${classDate}

 *
 *
 */
public interface ${ClassName}Service extends BaseService<${ClassName}, Long>{
    /**
     * 保存
     * @param ${className}
     * @return int
     * */
    int save(${ClassName} ${className});

    /**
     * 更新
     * @param ${className}
     * @return int
     * */
    int update(${ClassName} ${className});

    /**
     * 更新
     * @param params
     * @return int
     * */
    int updateByMap(Map<String, Object> params);

    /**
     * 查询集合
     * @param params
     * @return  List<${ClassName}>
    * */
    List<${ClassName}> select(Map<String, Object> params);

    /**
    * 分页查询
    * @param current
    * @param pageSize
    * @param params
    * @return  Page<${ClassName}>
    * */
    Page<${ClassName}> page(Integer current, Integer pageSize , Map<String, Object> params);

    /**
    * 单数查询
    * @param params
    * @return  ${ClassName}
    * */
    ${ClassName} find(Map<String, Object> params);

    /**
    * 根据主键查询
    * @param id
    * @return  ${ClassName}
    * */
    ${ClassName} findByPrimary(Long id);
}
