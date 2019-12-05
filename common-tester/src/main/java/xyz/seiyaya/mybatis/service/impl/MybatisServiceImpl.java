package xyz.seiyaya.mybatis.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.mybatis.bean.UserBean;
import xyz.seiyaya.mybatis.mapper.UserBeanMapper;
import xyz.seiyaya.mybatis.service.MybatisService;

import javax.annotation.Resource;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/5 15:56
 */
@Service
@Slf4j
public class MybatisServiceImpl implements MybatisService {

    @Resource
    private UserBeanMapper userBeanMapper;


    @Override
    public UserBean findUserBeanById(Integer id){
        UserBean user = userBeanMapper.findUser(id);
        log.info("第一次查询出来的user-->{} hashCode:{}",user,user.hashCode());

        user = userBeanMapper.findUser(id);
        log.info("第二次查询出来的user-->{} hashCode:{}",user,user.hashCode());

        return user;
    }

    /**
     * 在该方法中调用两次判断是否会经过sqlSession的一级缓存
     * @param id   用户id
     * @return  用户
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public UserBean findUserBeanRepeatById(Integer id){
        UserBean user = userBeanMapper.findUser(id);
        log.info("第一次查询出来的user-->{} hashCode:{}",user,user.hashCode());

        user = userBeanMapper.findUser(id);
        log.info("第二次查询出来的user-->{} hashCode:{}",user,user.hashCode());

        return user;
    }
}
