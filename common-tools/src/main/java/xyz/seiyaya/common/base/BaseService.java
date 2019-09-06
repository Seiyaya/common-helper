package xyz.seiyaya.common.base;

import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:00
 */
public interface BaseService<T,ID extends Serializable> {

    /**
     * 插入数据
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 根据主键获取实体信息
     * @param id
     * @return
     */
    T getById(ID id);

    /**
     * 根据主键更新数据
     * @param t
     * @return
     */
    int updateById(T t);
}
