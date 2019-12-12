package xyz.seiyaya.shiro.serivce.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.seiyaya.shiro.bean.User;
import xyz.seiyaya.shiro.helper.PasswordHelper;
import xyz.seiyaya.shiro.mapper.UserMapper;
import xyz.seiyaya.shiro.serivce.UserService;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/12 16:44
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private PasswordHelper passwordHelper;

    @Resource
    private UserMapper userMapper;

    @Override
    public User createUser(User user) {
        passwordHelper.encryptPassword(user);
        userMapper.insertSelective(user);
        return user;
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user = userMapper.selectByPrimaryKey(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);

        User updateUser = new User(userId,user.getPassword());
        userMapper.updateByPrimaryKey(updateUser);
    }

    @Override
    public void correlationRoles(Long userId, Long... roleIds) {

    }

    @Override
    public void unCorrelationRoles(Long userId, Long... roleIds) {

    }

    @Override
    public User findByUsername(String username) {
        User queryUser = new User();
        queryUser.setUsername(username);
        return userMapper.selectOne(queryUser);
    }

    @Override
    public Set<String> findRoles(String username) {
        User user = findByUsername(username);
        return userMapper.findRoles(user.getId());
    }

    @Override
    public Set<String> findPermissions(String username) {
        User user = findByUsername(username);
        return userMapper.findPermissions(user.getId());
    }
}
