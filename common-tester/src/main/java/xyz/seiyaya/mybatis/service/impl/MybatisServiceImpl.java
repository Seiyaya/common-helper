package xyz.seiyaya.mybatis.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.common.helper.DBParam;
import xyz.seiyaya.common.helper.SpringHelper;
import xyz.seiyaya.mybatis.bean.PrintBean;
import xyz.seiyaya.mybatis.bean.UserBean;
import xyz.seiyaya.mybatis.mapper.UserBeanMapper;
import xyz.seiyaya.mybatis.service.MybatisService;
import xyz.seiyaya.shiro.bean.User;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

        UserBean user2 = userBeanMapper.findUser(id);
        log.info("第二次查询出来的user-->{} hashCode:{}",user2,user2.hashCode());

        log.info("{}",user == user2);

        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public void parentInsert() {
        UserBean userBean = new UserBean("seiyaya","123456",24,new Date());
        userBeanMapper.insertSelective(userBean);

        try {
            MybatisService bean = SpringHelper.getBean(MybatisService.class);
            bean.sonInsert();
        }catch (Exception e){
            log.error("子插入异常");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void sonInsert() {
        UserBean userBean = new UserBean("seiyayaSon","123456",2,new Date());
        userBeanMapper.insertSelective(userBean);
        int result = 1/0;
        log.info("{}",result);
    }

    @Override
    public void updateUserById(Integer model) {
        UserBean userBean = new UserBean();
        userBean.setId(model);
        userBean.setBirthday(new Date());
        userBeanMapper.updateByPrimaryKeySelective(userBean);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void findAndUpdateByStream() {
        MybatisService proxyBean = SpringHelper.getBean(MybatisService.class);
        ArrayList<UserBean> idList = Lists.newArrayList();
        for(int i=0;i<20;i++){
            UserBean userBean = new UserBean();
            userBean.setId(i+1);
            idList.add(userBean);
        }

        idList.parallelStream().forEach(model->{
            proxyBean.updateUserById(model.getId());
        });
    }

    @Override
    public void printBean() {
        PrintBean printBean = new PrintBean();
        printBean.print();
    }

    @Override
    public UserBean getUserByLike(String name) {
        DBParam dbParam = new DBParam().set("name","zhang");
        return userBeanMapper.findUserByCondition(dbParam,null);
    }
}
