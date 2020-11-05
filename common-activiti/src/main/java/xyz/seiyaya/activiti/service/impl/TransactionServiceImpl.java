package xyz.seiyaya.activiti.service.impl;

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
import xyz.seiyaya.common.cache.helper.SpringHelper;

import javax.annotation.Resource;
import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

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
            Arrays.stream(Thread.currentThread().getClass().getDeclaredFields()).forEach(model-> log.info("field :" + model.getName()));
            Field threadLocals = Thread.currentThread().getClass().getDeclaredField("threadLocals");
            threadLocals.setAccessible(true);
            Object threadLocalMap = threadLocals.get(Thread.currentThread());
            Class threadLocalMapClazz = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            Field tableField = threadLocalMapClazz.getDeclaredField("table");
            tableField.setAccessible(true);


            Class entryClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap$Entry");
            Object[] objects = (Object[]) tableField.get(threadLocalMap);
            Field entryValueField = entryClass.getDeclaredField("value");
            entryValueField.setAccessible(true);
            Field referenceField = Reference.class.getDeclaredField("referent");
            referenceField.setAccessible(true);

            Arrays.stream(objects).filter(Objects::nonNull).forEach((obj) -> {
                try {
                    Object value = entryValueField.get(obj);
                    if (value != null) {
                        if (value instanceof Reference) {
                            Reference ref = (Reference) value;
                            log.info(" ref {} ref to {}",ref.getClass().getName(),ref.get());
                        } else {
                            log.info("{}",value);
                        }
                    }
                } catch (IllegalAccessException e) {
                    log.error("",e);
                }
            });
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
