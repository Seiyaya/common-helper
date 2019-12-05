package xyz.seiyaya.common.base;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

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

    /**
     * 获取分页
     * @param t
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageInfo<T> page(T t, Integer currentPage, Integer pageSize);

    /**
     * 根据指定条件获取列表
     * @param t
     * @return
     */
    List<T> getList(T t);

    /**
     * 通过指定条件获取对象
     * @param t
     * @return
     */
    T getByCondition(T t);
}
