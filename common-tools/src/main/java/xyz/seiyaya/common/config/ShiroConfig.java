package xyz.seiyaya.common.config;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.seiyaya.common.auth.RedisCacheManager;
import xyz.seiyaya.common.auth.SystemAuthRealm;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/18 15:31
 */
@Configuration
public class ShiroConfig {
//
//    @Bean
//    public CacheManager cacheManager(){
//        CacheManager redisCacheManager = new RedisCacheManager();
//        return redisCacheManager;
//    }
//
//    @Bean
//    public SecurityManager securityManager(SystemAuthRealm systemAuthRealm, SessionManager sessionManager, CacheManager cacheManager, RememberMeManager rememberMeManager) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(systemAuthRealm);
//        securityManager.setSessionManager(sessionManager);
//        securityManager.setCacheManager(cacheManager);
//        securityManager.setRememberMeManager(rememberMeManager);
//        return securityManager;
//    }
//
//    @Bean
//    public SystemAuthRealm systemAuthorizingRealm() {
//        SystemAuthRealm systemAuthorizingRealm = new SystemAuthRealm();
//        systemAuthorizingRealm.setAuthorizationCacheName("authorizationCache");
//        systemAuthorizingRealm.setAuthenticationCacheName("authenticationCache");
//        return systemAuthorizingRealm;
//    }
}
