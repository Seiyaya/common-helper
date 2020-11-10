package xyz.seiyaya.shiro.resolver;

import org.apache.shiro.authz.Permission;
import xyz.seiyaya.common.helper.StringHelper;

import static xyz.seiyaya.common.constant.ConstantBean.NUMBER_THREE;
import static xyz.seiyaya.common.constant.ConstantBean.NUMBER_TWO;
import static xyz.seiyaya.common.constant.SymbolConstant.SYMBOL_ASTERISK;


/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 11:01
 */
public class BitPermission implements Permission {

    private String resourceIdentify;

    private int permissionBit;

    private String instanceId;

    public BitPermission(String permission){
        String[] array = permission.split("\\+");
        if(array.length > 1){
            resourceIdentify = array[1];
        }

        if(StringHelper.isEmpty(resourceIdentify)) {
            resourceIdentify = SYMBOL_ASTERISK;
        }

        if(array.length > NUMBER_TWO){
            permissionBit = Integer.parseInt(array[2]);
        }

        if(array.length > NUMBER_THREE){
            instanceId = array[3];
        }

        if(StringHelper.isEmpty(instanceId)) {
            instanceId = SYMBOL_ASTERISK;
        }
    }

    @Override
    public boolean implies(Permission p) {
        if(!(p instanceof BitPermission)){
            return false;
        }
        BitPermission other = (BitPermission) p;
        if(!(SYMBOL_ASTERISK.equals(this.resourceIdentify)  || this.resourceIdentify.equals(other.resourceIdentify) )){
            return false;
        }

        if(!(this.permissionBit ==0 || (this.permissionBit & other.permissionBit) != 0)) {
            return false;
        }

        if(!(SYMBOL_ASTERISK.equals(this.instanceId) || this.instanceId.equals(other.instanceId))) {
            return false;
        }

        return true;
    }
}
