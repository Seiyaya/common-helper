package xyz.seiyaya.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 */
@Data
public class LoginUserInfo implements Serializable {

    private Long id;
    private String username;
    private String name;
    private String phone;
}
