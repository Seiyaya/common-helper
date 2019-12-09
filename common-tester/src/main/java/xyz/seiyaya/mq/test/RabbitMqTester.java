package xyz.seiyaya.mq.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.seiyaya.mq.sender.TradeSender;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 14:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqTester {

    @Autowired
    private TradeSender tradeSender;

    @Test
    public void sendMsg(){
        tradeSender.sendTrade();
    }
}
