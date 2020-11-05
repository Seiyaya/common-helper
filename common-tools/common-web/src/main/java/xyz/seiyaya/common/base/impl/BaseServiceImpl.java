package xyz.seiyaya.common.base.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.common.Mapper;
import xyz.seiyaya.common.base.BaseService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:52
 */
public abstract class BaseServiceImpl<T, M extends Mapper<T>> implements BaseService<T> {

//    @Resource
    protected M mapper;

    private Class<T> clazz;

    public BaseServiceImpl(){
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType pType = (ParameterizedType)type;
        this.clazz = (Class)pType.getActualTypeArguments()[0];
    }

    @Override
    public int insert(T t) {
        return mapper.insertSelective(t);
    }

    @Override
    public T getById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateById(T t) {
        return mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public PageInfo<T> page(T t, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<T> result = mapper.select(t);
        PageInfo<T> pageInfo = new PageInfo<>(result);
        return pageInfo;
    }

    @Override
    public List<T> getList(T t) {
        return mapper.select(t);
    }

    @Override
    public T getByCondition(T t) {
        return mapper.selectOne(t);
    }
}
