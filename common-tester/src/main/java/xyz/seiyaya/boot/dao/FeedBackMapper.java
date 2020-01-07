package xyz.seiyaya.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.seiyaya.boot.bean.Feedback;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/7 11:17
 */
@Mapper
public interface FeedBackMapper extends tk.mybatis.mapper.common.Mapper<Feedback> {
}
