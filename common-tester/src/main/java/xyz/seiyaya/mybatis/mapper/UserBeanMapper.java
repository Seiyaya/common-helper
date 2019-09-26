package xyz.seiyaya.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import xyz.seiyaya.mybatis.bean.UserBean;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/9/26 15:32
 */
public interface UserBeanMapper {

    UserBean findUser(@Param("id") Integer id);
}
