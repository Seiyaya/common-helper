package xyz.seiyaya.mq.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static xyz.seiyaya.mq.config.RabbitConfig.ROUTING_KEY_TRADE;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 14:27
 */
@Component
@Slf4j
public class TradeSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendTrade(){
        String context = "hello " + new Date();
        log.info("send message:{}",context);
        rabbitTemplate.convertAndSend(ROUTING_KEY_TRADE, context);
    }
}
