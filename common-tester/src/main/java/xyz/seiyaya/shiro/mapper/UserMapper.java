package xyz.seiyaya.shiro.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.seiyaya.shiro.bean.User;

import java.util.List;
import java.util.Set;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:47
 */
@Mapper
public interface UserMapper extends tk.mybatis.mapper.common.Mapper<User> {

    /**
     * 查找角色信息
     * @param userId
     * @return
     */
    Set<String> findRoles(Long userId);

    /**
     * 查找用户的所有权限
     * @param id
     * @return
     */
    Set<String> findPermissions(Long id);
}
