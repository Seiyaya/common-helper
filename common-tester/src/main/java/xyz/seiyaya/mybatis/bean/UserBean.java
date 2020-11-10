package xyz.seiyaya.mybatis.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/26 15:25
 */
@Data
@Table(name = "t_test_user")
public class UserBean implements Serializable {

    @Id
    private Integer id;
    private String name;
    private String password;
    private Integer age;
    /**
     * 性别0男1女
     */
    private String sex;
    private String[] status;
    private Date birthday;

    private Integer version;


    public UserBean(String name, String password, Integer age, Date birthday) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.birthday = birthday;
    }

    public UserBean(){

    }
}
