package xyz.seiyaya.mybatis.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/26 15:25
 */
@Data
public class UserBean {

    private Integer id;
    private String name;
    private String password;
    private Integer age;
    private Date birthday;
}
