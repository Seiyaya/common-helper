package xyz.seiyaya.shiro.bean;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:36
 */
@Data
public class User implements Serializable {

    @Id
    private Long id;
    private String username;
    private String password;
    private String salt;
    private Boolean locked = Boolean.FALSE;

    public User(){

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id,String password){
        this.id = id;
        this.password = password;
    }

    public String getCredentialsSalt() {
        return username + salt;
    }
}
