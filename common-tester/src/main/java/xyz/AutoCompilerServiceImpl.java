package xyz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.seiyaya.mybatis.bean.UserBean;
import xyz.seiyaya.mybatis.mapper.UserBeanMapper;

import javax.annotation.Resource;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/19 9:09
 */
@Service(value = "AutoCompilerServiceImpl")
//@Transactional(rollbackFor = Exception.class)
public class AutoCompilerServiceImpl {

    @Resource
    private UserBeanMapper userBeanMapper;

    public void setUserBeanMapper(UserBeanMapper userBeanMapper) {
        this.userBeanMapper = userBeanMapper;
    }

    public void testInsert(){
        UserBean userBean = new UserBean();
        userBean.setName(Math.random()*100+"");
        userBean.setAge((int) (Math.random()*100));
        userBeanMapper.insertSelective(userBean);

        int zero = 1/0;

        userBean = new UserBean();
        userBean.setName(Math.random()+"");
        userBean.setAge((int) (Math.random()*100));
        userBeanMapper.insertSelective(userBean);
    }
}
