package xyz.seiyaya.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.seiyaya.common.base.BaseMapper;
import xyz.seiyaya.common.bean.RequestLog;

/**
 * 请求日志Dao
 * @author seiyaya
 * @version 1.0.0
 * @date 2019-10-25
 */
@Mapper
public interface RequestLogMapper extends BaseMapper<RequestLog, Integer> {

}
