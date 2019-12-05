package xyz.seiyaya.common.base.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.common.Mapper;
import xyz.seiyaya.common.base.BaseService;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:52
 */
public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    @Override
    public int insert(T t) {
        return getMapper().insertSelective(t);
    }

    @Override
    public T getById(ID id) {
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public int updateById(T t) {
        return getMapper().updateByPrimaryKeySelective(t);
    }

    @Override
    public PageInfo<T> page(T t, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<T> result = getMapper().select(t);
        PageInfo<T> pageInfo = new PageInfo<>(result);
        return pageInfo;
    }

    @Override
    public List<T> getList(T t) {
        return getMapper().select(t);
    }

    @Override
    public T getByCondition(T t) {
        return getMapper().selectOne(t);
    }

    /**
     * 子类需要实现的，直接调用mapper操作公用方法
     * @return
     */
    public abstract Mapper<T> getMapper();
}
