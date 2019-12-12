package xyz.seiyaya.shiro.serivce;

import xyz.seiyaya.shiro.bean.Permission;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:38
 */
public interface PermissionService {

    /**
     * 创建权限
     * @param permission
     */
    void createPermission(Permission permission);

    /**
     * 通过id删除权限
     * @param permissionId
     */
    void deletePermissionById(Long permissionId);
}
