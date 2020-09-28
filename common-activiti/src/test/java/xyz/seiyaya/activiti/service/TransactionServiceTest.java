package xyz.seiyaya.activiti.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.boot.ProcessApplication;

import javax.annotation.Resource;

/**
 * 事务测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProcessApplication.class)
@EnableAutoConfiguration
@Slf4j
public class TransactionServiceTest {

    @Resource
    private TransactionService transactionService;

    @Test
    public void testTransaction(){
        transactionService.test1();
    }
}
