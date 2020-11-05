package xyz.seiyaya.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.seiyaya.common.cache.helper.DBParam;
import xyz.seiyaya.mybatis.bean.UserBean;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/9/26 15:32
 */
@Mapper
public interface UserBeanMapper extends tk.mybatis.mapper.common.Mapper<UserBean> {

    UserBean findUser(@Param("id") Integer id);

    UserBean findUserByCondition(DBParam param,@Param("name") String name);

    void updateUserById(UserBean userBean);

    UserBean findExUser(@Param("id") Integer id);

    UserBean sqlExecuteWithMapParamsAndForeach(DBParam list);

    List<UserBean> findUserList();
}
