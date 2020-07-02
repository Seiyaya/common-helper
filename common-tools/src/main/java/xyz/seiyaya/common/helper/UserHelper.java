package xyz.seiyaya.common.helper;

import com.alibaba.fastjson.JSON;
import xyz.seiyaya.common.bean.LoginUserInfo;
import xyz.seiyaya.common.cache.service.CacheService;
import xyz.seiyaya.common.config.Constant;

/**
 * @author wangjia
 * @version 1.0
 */
public class UserHelper {

    private static CacheService cacheService = SpringHelper.getBean(CacheService.class);

    public static LoginUserInfo getLoginUserInfo(String token){
        String loginUserStr = cacheService.get(Constant.UserConstant.USER_KEY + ":" + token);
        if(StringHelper.isBlank(loginUserStr)){
            return null;
        }
        return JSON.parseObject(loginUserStr,LoginUserInfo.class);
    }
}
