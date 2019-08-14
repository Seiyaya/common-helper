package ${packageName}.${moduleName}.service.impl;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ${commonName}.mapper.BaseMapper;
import ${commonName}.service.impl.BaseServiceImpl;
import ${packageName}.${moduleName}.mapper.${ClassName}Mapper;
import ${packageName}.${moduleName}.${ClassName};
import ${packageName}.${moduleName}.service.${ClassName}Service;

import java.util.List;
import java.util.Map;

/**
 * ${functionName}ServiceImpl
 *
 * @author ${classAuthor}
 * @version 1.0.0
 * @date ${classDate}

 *
 *
 */

@Service("${className}Service")
public class ${ClassName}ServiceImpl extends BaseServiceImpl<${ClassName}, Long> implements ${ClassName}Service {

    private static final Logger logger = LoggerFactory.getLogger(${ClassName}ServiceImpl.class);

    @Resource
    private ${ClassName}Mapper ${className}Mapper;

	@Override
	public BaseMapper<${ClassName}, Long> getMapper() {
		return ${className}Mapper;
	}

	@Override
	public int save(${ClassName} ${className}){
		return ${className}Mapper.save(${className});
	}

	@Override
	public int update(${ClassName} ${className}){
		return ${className}Mapper.update(${className});
	}

	@Override
	public int updateByMap(Map<String, Object> params){
		return ${className}Mapper.updateSelective(params);
	}

	@Override
	public List<${ClassName}> select(Map<String, Object> params){
        return ${className}Mapper.listSelective(params);
    }

    @Override
    public Page<${ClassName}> page(Integer current, Integer pageSize , Map<String, Object> params){
        PageHelper.startPage(current, pageSize);
        return (Page<${ClassName}>)${className}Mapper.listSelective(params);
    }

    @Override
    public ${ClassName} find(Map<String, Object> params){
    return ${className}Mapper.findSelective(params);
    }

    @Override
    public ${ClassName} findByPrimary(Long id){
    return ${className}Mapper.findByPrimary(id);
    }

}