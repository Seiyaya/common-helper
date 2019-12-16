package xyz.seiyaya.shiro.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.seiyaya.shiro.realm.MyRealm;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/13 9:36
 */
@Configuration
public class ShiroConfig {

    @Bean
    public MyRealm myRealm(){
        return new MyRealm();
    }

    @Bean
    public SessionManager sessionManager(){
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        sessionManager.setSessionDAO(systemSessionDAO);
    }

    @Bean
    public SecurityManager securityManager(MyRealm myRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);
        securityManager.setSessionManager();
    }
}
