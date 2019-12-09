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

    /**
     * 父类插入，用来测试事务
     */
    void parentInsert();

    /**
     * 子类插入
     */
    void sonInsert();

    /**
     * 通过id更新
     * @param model
     */
    void updateUserById(Integer model);

    /**
     * 查找和更新通过stream
     */
    void findAndUpdateByStream();
}
