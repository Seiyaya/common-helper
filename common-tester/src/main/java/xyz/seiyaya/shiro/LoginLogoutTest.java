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

/**
 * 用来测试登录和登出
 * @author wangjia
 * @version 1.0
 * @date 2019/12/11 14:47
 */
@Slf4j
public class LoginLogoutTest {

    @Test
    public void testLogin(){
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro/shiro.ini");
        commonLoginAndLogout(factory);
        /**
         * 常见的异常       AuthenticationException及其子类
         * DisabledAccountException 禁用的帐户
         * LockedAccountException   锁定的帐户
         * UnknownAccountException  错误的帐户
         * ExcessiveAttemptsException   登录失败次数过多
         * IncorrectCredentialsException    错误的凭证
         * ExpiredCredentialsException      过期的凭证
         * 最好使用如"用户名/密码错误"而不是"用户名错误"或者"密码错误"，防止一些恶意用户非法扫描帐号库
         *
         * 上面存储在的问题
         * 1. 账户密码硬编码在ini文件中，需要改为数据库存储且为加密存储密码
         * 2. 用户登录允许手机号和邮箱同时登录
         */
    }

    /**
     * 调用自己的realm进行认证
     */
    @Test
    public void testLoginByMyRealm() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro/shiro-realm.ini");
        commonLoginAndLogout(factory);
    }

    private void commonLoginAndLogout(Factory<SecurityManager> factory) {
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");

        try {
            subject.login(token);
            Assert.assertEquals(true, subject.isAuthenticated());

            log.info("登录成功");
            subject.logout();
        } catch (AuthenticationException e) {
            log.error("登录失败，认证错误", e);
        }
    }
}
