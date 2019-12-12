package xyz.seiyaya.shiro.resolver;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;
import xyz.seiyaya.common.bean.SymbolConstant;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 11:37
 */
public class BitAndWildPermissionResolver implements PermissionResolver {
    @Override
    public Permission resolvePermission(String permissionString) {
        if(permissionString.startsWith(SymbolConstant.SYMBOL_ADD)) {
            return new BitPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}
