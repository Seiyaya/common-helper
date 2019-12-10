package xyz.seiyaya.mq.receiver;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import xyz.seiyaya.mybatis.bean.UserBean;

import static xyz.seiyaya.mq.config.RabbitConfig.ROUTING_KEY_TRADE;

/**
 * 收到成交消息进行指标计算
 * @author wangjia
 * @version 1.0
 * @date 2019/12/10 10:30
 */
@Component
@RabbitListener(queues = {ROUTING_KEY_TRADE})
@Slf4j
public class IndexCalcReceiver {

    @RabbitHandler
    public void process(UserBean userBean, Message message, Channel channel){
        log.info("收到成交消息{}，进行指标计算",userBean);
    }
}
