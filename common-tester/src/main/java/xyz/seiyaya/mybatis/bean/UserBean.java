package xyz.seiyaya.mybatis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date: 2019/9/26 15:25
 */
@Data
@Table(name = "t_test_user")
public class UserBean {

    @Id
    private Integer id;
    private String name;
    private String password;
    private Integer age;
    private Date birthday;


    public UserBean(String name, String password, Integer age, Date birthday) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.birthday = birthday;
    }

    public UserBean(){

    }
}
