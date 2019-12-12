package xyz.seiyaya.shiro.serivce;

import xyz.seiyaya.shiro.bean.User;

import java.util.Set;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:41
 */
public interface UserService {

    /**
     * 创建用户
     * @param user
     * @return
     */
    User createUser(User user);

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     */
    void changePassword(Long userId, String newPassword);

    /**
     * 添加用户和角色的关系
     * @param userId
     * @param roleIds
     */
    void correlationRoles(Long userId, Long... roleIds);

    /**
     * 移除用户和角色的关系
     * @param userId
     * @param roleIds
     */
    void unCorrelationRoles(Long userId, Long... roleIds);

    /**
     * 查找用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 用户下的角色信息
     * @param username
     * @return
     */
    Set<String> findRoles(String username);

    /**
     * 用户下的权限信息
     * @param username
     * @return
     */
    Set<String> findPermissions(String username);
}
