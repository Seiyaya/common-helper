package xyz.seiyaya.shiro.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:37
 */
@Data
public class UserRole implements Serializable {

    private Long userId;
    private Long roleId;
}
