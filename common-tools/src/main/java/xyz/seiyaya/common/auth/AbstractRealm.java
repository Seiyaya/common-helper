package xyz.seiyaya.common.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/18 15:38
 */
public abstract class AbstractRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 权限校验
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 登录的时候回调
        if( !( token instanceof  SystemUsernamePasswordToken)){
            return null;
        }
        SystemUsernamePasswordToken passwordToken = (SystemUsernamePasswordToken)token;
        return null;
    }
}
