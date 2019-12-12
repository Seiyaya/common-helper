package xyz.seiyaya.shiro.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:34
 */
@Data
public class Role implements Serializable {
    private Long id;
    /**
     * 角色标识
     */
    private String role;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 是否可用
     */
    private Boolean available = Boolean.FALSE;

    public Role(String role, String description, Boolean available) {
        this.role = role;
        this.description = description;
        this.available = available;
    }
}
