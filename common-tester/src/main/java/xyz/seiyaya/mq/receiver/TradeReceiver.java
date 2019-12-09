package xyz.seiyaya.mq.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static xyz.seiyaya.mq.config.RabbitConfig.ROUTING_KEY_TRADE;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 14:29
 */
@Component
@RabbitListener(queues = {ROUTING_KEY_TRADE})
@Slf4j
public class TradeReceiver {

    @RabbitHandler
    public void process(String msg){
        log.info("接受到的消息:{}",msg);
    }
}
