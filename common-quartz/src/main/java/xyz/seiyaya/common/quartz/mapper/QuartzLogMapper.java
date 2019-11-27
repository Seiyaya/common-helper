package xyz.seiyaya.common.quartz.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.seiyaya.common.quartz.bean.QuartzLog;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:44
 */
@Mapper
public interface QuartzLogMapper extends tk.mybatis.mapper.common.Mapper<QuartzLog> {
}
