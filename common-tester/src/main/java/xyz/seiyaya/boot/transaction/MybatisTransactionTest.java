package xyz.seiyaya.boot.transaction;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import xyz.seiyaya.boot.bean.Feedback;
import xyz.seiyaya.boot.dao.FeedBackMapper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * mybatis事务测试
 * @author wangjia
 * @version 1.0
 * @date 2020/1/13 11:12
 */
@Slf4j
@SuppressWarnings("all")
public class MybatisTransactionTest {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private FeedBackMapper feedBackMapper;


    @Test
    public void testForUpdate(){
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> transactionTemplate.execute(transactionStatus -> {
                // select * from forupdate where name = #{name} for update
                Feedback feedback = feedBackMapper.findByUserName("testforupdate");
                log.info("========ok:{} result:{} ",atomicInteger.getAndIncrement(),feedback);
                return null;
            })).start();
        }
    }
}
