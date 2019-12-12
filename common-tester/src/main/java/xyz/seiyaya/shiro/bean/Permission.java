package xyz.seiyaya.shiro.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:33
 */
@Data
public class Permission implements Serializable {

    private Long id;
    /**
     * 权限标识
     */
    private String permission;
    /**
     * 权限描述
     */
    private String description;
    /**
     * 是否可用
     */
    private Boolean available = Boolean.FALSE;

    public Permission(String permission, String description, Boolean available) {
        this.permission = permission;
        this.description = description;
        this.available = available;
    }
}
