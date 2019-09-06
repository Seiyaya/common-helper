package xyz.seiyaya.common.base;

import xyz.seiyaya.common.helper.DBParam;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:44
 */
public interface BaseMapper<T,ID extends Serializable> {

    /**
     * 添加一条数据
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 根据主键更新
     * @param t
     * @return
     */
    int update(T t);

    /**
     * 按照指定条件更新
     * @param param
     * @return
     */
    int updateSelective(DBParam param);

    /**
     * 根据条件查找一条数据
     * @param param
     * @return
     */
    T findSelective(DBParam param);

    /**
     * 根据主键查找记录
     * @param primary
     * @return
     */
    T findByPrimary(ID primary);

    /**
     * 根据条件查询相关列表
     * @param param
     * @return
     */
    List<T> listSelective(DBParam param);
}
