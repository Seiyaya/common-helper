package xyz.seiyaya.activiti.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import xyz.seiyaya.activiti.bean.TransactionUser;
import xyz.seiyaya.activiti.mapper.TransactionUserMapper;
import xyz.seiyaya.activiti.service.TransactionUserDetailService;

import javax.annotation.Resource;

@Component
@Slf4j
public class TransactionUserDetailServiceImpl implements TransactionUserDetailService {

    @Resource
    private TransactionUserMapper transactionUserMapper;

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void update(TransactionUser transactionUser) {
        transactionUser.setId(2L);
        transactionUser.setName(transactionUser.getName()+"淦");
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("当前TransactionUserDetailServiceImpl   transactionName: {}",transactionName);
        transactionUserMapper.updateByPrimaryKeySelective(transactionUser);
        int i = 1/0;
    }
}
