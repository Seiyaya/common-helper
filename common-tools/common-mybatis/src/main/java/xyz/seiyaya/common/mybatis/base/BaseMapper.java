package xyz.seiyaya.common.mybatis.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 * baseMapper
 * @param <T>
 * @author seiyaya
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
