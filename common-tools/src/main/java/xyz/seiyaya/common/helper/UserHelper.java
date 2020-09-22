package xyz.seiyaya.common.helper;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import xyz.seiyaya.common.bean.LoginUserInfo;
import xyz.seiyaya.common.business.BaseUserService;
import xyz.seiyaya.common.cache.service.CacheService;
import xyz.seiyaya.common.constant.Constant;

/**
 * @author wangjia
 * @version 1.0
 */
public class UserHelper {

    private static CacheService cacheService = SpringHelper.getBean(CacheService.class);

    private static BaseUserService baseUserService;

    static{
        baseUserService = SpringHelper.getBean(BaseUserService.class);
    }

    public static LoginUserInfo getLoginUserInfo(String token){
        String loginUserStr = cacheService.get(Constant.User.USER_KEY + ":" + token);
        if(StringHelper.isBlank(loginUserStr)){
            return null;
        }
        return JSON.parseObject(loginUserStr,LoginUserInfo.class);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public static LoginUserInfo getUser(){
        Subject subject = SecurityUtils.getSubject();
        LoginUserInfo user = null;
        if(subject != null){
            Long userId = (Long)subject.getPrincipal();
            user = getUser(userId);
        }
        return user;
    }

    /**
     * 根据userId获取用户信息
     * @param userId
     * @return
     */
    public static LoginUserInfo getUser(Long userId){
        String userKey = Constant.User.USER_KEY + userId;
        LoginUserInfo user = cacheService.getObject(userKey, LoginUserInfo.class);
        if(user == null){
            user = baseUserService.getById(userId);
            if(user == null){
                return null;
            }
            cacheService.setObject(userKey,user,Constant.Time.D1);
        }
        return user;
    }
}
