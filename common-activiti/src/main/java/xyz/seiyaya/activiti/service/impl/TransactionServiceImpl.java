package xyz.seiyaya.activiti.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import xyz.seiyaya.activiti.bean.TransactionUser;
import xyz.seiyaya.activiti.mapper.TransactionUserMapper;
import xyz.seiyaya.activiti.service.TransactionService;
import xyz.seiyaya.common.base.impl.BaseServiceImpl;
import xyz.seiyaya.common.helper.SpringHelper;

import javax.annotation.Resource;
import java.lang.reflect.Field;

@Service
@Slf4j
public class TransactionServiceImpl extends BaseServiceImpl<TransactionUser,TransactionUserMapper> implements TransactionService {

    @Resource
    private TransactionUserMapper transactionUserMapper;

    @Resource
    private TransactionUserDetailServiceImpl transactionUserDetail;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void test1() {
        TransactionUser transactionUser = new TransactionUser();
        transactionUser.setId(1L);
        transactionUser.setName("123");
        transactionUserMapper.updateByPrimaryKeySelective(transactionUser);

        try {
            ThreadLocal<String> objectThreadLocal = new ThreadLocal<>();
            objectThreadLocal.set("String");
            Field threadLocals = Thread.currentThread().getClass().getField("threadLocals");
            threadLocals.setAccessible(true);
            Object o = threadLocals.get(Thread.currentThread());
            log.info("{}", JSON.toJSONString(o));
        }catch (Exception e){
            log.error("",e);
        }

        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("当前  TransactionUserDetailServiceImpl  transactionName: {}",transactionName);
        try {
            transactionUserDetail.update(transactionUser);
            TransactionTemplate template = SpringHelper.getBean(TransactionTemplate.class);
            log.info("{}",template.getName());
        }catch (Exception e){
            log.error("",e);
        }
    }
}
