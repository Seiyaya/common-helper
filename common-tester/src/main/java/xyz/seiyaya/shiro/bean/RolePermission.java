package xyz.seiyaya.shiro.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:35
 */
@Data
public class RolePermission implements Serializable {

    private Long permissionId;
    private Long roleId;
}
