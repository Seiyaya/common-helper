package xyz.seiyaya.common.base.impl;

import xyz.seiyaya.common.base.BaseMapper;
import xyz.seiyaya.common.base.BaseService;

import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:52
 */
public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    private BaseMapper<T,ID> mapper;

    @Override
    public int insert(T t) {
        return mapper.insert(t);
    }

    @Override
    public T getById(ID id) {
        return mapper.findByPrimary(id);
    }

    @Override
    public int updateById(T t) {
        return mapper.update(t);
    }

    /**
     * 子类需要实现的，直接调用mapper操作公用方法
     * @return
     */
    public abstract BaseMapper<T, ID> getMapper();
}
