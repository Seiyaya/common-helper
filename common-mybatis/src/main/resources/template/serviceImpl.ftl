package ${packageName}.${moduleName}.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ${commonName}.mapper.BaseMapper;
import ${commonName}.service.impl.BaseServiceImpl;
import ${packageName}.${moduleName}.mapper.${ClassName}Mapper;
import ${packageName}.${moduleName}.bean.${ClassName};
import ${packageName}.${moduleName}.service.${ClassName}Service;

import java.util.List;
import java.util.Map;

/**
 * ${functionName}ServiceImpl
 *
 * @author ${classAuthor}
 * @version 1.0.0
 * @date ${classDate}
 */
@Service
public class ${ClassName}ServiceImpl extends BaseServiceImpl<${ClassName}, Long> implements ${ClassName}Service {
    @Resource
    private ${ClassName}Mapper ${className}Mapper;

    @Override
    public BaseMapper<${ClassName}, Long> getMapper() {
        return ${className}Mapper;
    }
}