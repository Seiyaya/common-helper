package xyz.seiyaya.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 10:29
 */
@Slf4j
public class RoleTest {


    @Test
    public void testHasRole(){
        commonLogin("classpath:shiro/shiro-role.ini","zhang","123");

        Subject subject = SecurityUtils.getSubject();

        log.info("has role1:{}",subject.hasRole("role1"));

        log.info("has all role :{}",subject.hasAllRoles(Arrays.asList("role1", "role2")));

        log.info("==================");

        boolean[] booleans = subject.hasRoles(Arrays.asList("role1", "role2", "role3"));
        for(int i=0;i<booleans.length;i++){
            log.info("has role{} : {}",i+1,booleans[i]);
        }
        // subject.checkRole(); 会抛出异常
    }

    @Test
    public void testPermission(){
        commonLogin("classpath:shiro/shiro-permission.ini","zhang","123");

        Subject subject = SecurityUtils.getSubject();
        log.info("has permission user:create :{}",subject.isPermitted("user:create"));

        log.info("has permission user:update :{}",subject.isPermittedAll("user:create","user:delete"));

        log.info("has permission user:view:{}",subject.isPermitted("user:view"));
    }

    @Test
    public void testIsPermitted(){
        commonLogin("classpath:shiro/three/shiro-authorizer.ini","zhang","123");

        Subject subject = SecurityUtils.getSubject();
        log.info("has permission user1:update --> {}",subject.isPermitted("user1:update"));
        log.info("has permission user2:update --> {}",subject.isPermitted("user2:update"));

        //通过二进制的方式表示权限
        /**
         * 0000
         * 第一位表示查看权限
         * 第二位表示删除权限
         * 第三位表示新增权限
         */
        log.info("是否具有新增权限:0010 --> {}",subject.isPermitted("+user1+2"));
        log.info("是否具有查看权限:1000 --> {}",subject.isPermitted("+user1+8"));
        log.info("是否具有查看和新增权限:1010 --> {}",subject.isPermitted("+user1+10"));
        log.info("是否具有删除权限:0100 --> {}",subject.isPermitted("+user1+4"));
    }

    private void commonLogin(String iniFilePath,String username,String password) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(iniFilePath);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            Assert.assertEquals(true, subject.isAuthenticated());
            log.info("登录成功");
        } catch (AuthenticationException e) {
            log.error("登录失败，认证错误", e);
        }
    }
}
