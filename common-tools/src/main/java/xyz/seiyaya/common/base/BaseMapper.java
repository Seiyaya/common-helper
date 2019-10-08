package xyz.seiyaya.common.base;

import com.github.pagehelper.Page;
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
     * 根据主键查找记录
     * @param primary
     * @return
     */
    T findByPrimary(ID primary);

    /**
     * 获取查询列表
     * @param t
     * @return
     */
    List<T> getList(T t);

    /**
     * 根据条件获取对象
     * @param t
     * @return
     */
    T getByCondition(T t);

    /**
     * 根据指定条件更新
     * @param param
     * @return
     */
    int updateByCondition(DBParam param);
}
