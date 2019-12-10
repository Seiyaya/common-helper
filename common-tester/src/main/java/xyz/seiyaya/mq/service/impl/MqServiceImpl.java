package xyz.seiyaya.mq.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.seiyaya.mq.service.MqService;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/10 10:24
 */
@Service
@Slf4j
public class MqServiceImpl implements MqService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void sendMsg(String key, Object userBean) {
        log.info("发送消息  key-->{}   object-->{}",key,userBean);
        rabbitTemplate.convertAndSend(key,userBean);
    }

    @Override
    public void sendMsg(String exchange, String key, Object userBean) {
        log.info("发送消息 exchange:{}  key-->{}   object-->{}",exchange,key,userBean);
        rabbitTemplate.convertAndSend(exchange,key,userBean);
    }
}
