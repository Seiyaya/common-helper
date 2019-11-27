package xyz.seiyaya.common.quartz.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.seiyaya.common.quartz.bean.QuartzInfo;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/6 15:44
 */
@Mapper
public interface QuartzInfoMapper extends tk.mybatis.mapper.common.Mapper<QuartzInfo> {


    List<QuartzInfo> getRealList(QuartzInfo quartzInfo);
}
