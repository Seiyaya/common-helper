package xyz.seiyaya.common.business;

import xyz.seiyaya.common.bean.LoginUserInfo;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/18 15:49
 */
public interface BaseUserService {

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    LoginUserInfo getById(Long userId);

    /**
     * 根据登录帐号获取用户信息
     * @param username
     * @return
     */
    LoginUserInfo getByUserName(String username);
}
