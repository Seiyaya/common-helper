package xyz.seiyaya.mybatis.service;

import xyz.seiyaya.mybatis.bean.UserBean;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/5 13:58
 */
public interface MybatisService {

    UserBean findUserBeanById(Integer id);

    UserBean findUserBeanRepeatById(Integer id);
}
