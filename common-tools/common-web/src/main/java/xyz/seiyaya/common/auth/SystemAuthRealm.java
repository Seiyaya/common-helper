package xyz.seiyaya.common.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import xyz.seiyaya.common.bean.LoginUserInfo;
import xyz.seiyaya.common.business.BaseUserService;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/18 15:35
 */
public class SystemAuthRealm extends AuthorizingRealm {

    private BaseUserService baseUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 授权时回调
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 登录时回调
        // 登录的时候回调
        if( !( token instanceof  SystemUsernamePasswordToken)){
            return null;
        }
        SystemUsernamePasswordToken passwordToken = (SystemUsernamePasswordToken)token;

        LoginUserInfo dbUser = baseUserService.getByUserName(passwordToken.getUsername());

        if(dbUser != null){
            return new SimpleAuthenticationInfo(dbUser.getId(), dbUser.getPassword(), ByteSource.Util.bytes(dbUser.getSalt()), getName());
        }else{
            throw new AuthenticationException("用户名或者密码错误");
        }
    }
}
