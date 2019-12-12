package xyz.seiyaya.shiro.serivce;

import xyz.seiyaya.shiro.bean.Role;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:39
 */
public interface RoleService {

    /**
     * 创建角色
     * @param role
     * @return
     */
    Role createRole(Role role);

    /**
     * 通过roleId删除角色
     * @param roleId
     */
    void deleteRoleById(Long roleId);

    /**
     * 添加权限和角色之间的关系
     * @param roleId
     * @param permissionIds
     */
    void correlationPermissions(Long roleId, Long... permissionIds);

    /**
     * 移除权限和角色之间的关系
     * @param roleId
     * @param permissionIds
     */
    void unCorrelationPermissions(Long roleId, Long... permissionIds);
}
