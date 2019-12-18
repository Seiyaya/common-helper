package xyz.seiyaya.mq.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;

import static xyz.seiyaya.mq.config.SimpleRabbitConstant.*;

/**
 * 实用原生api进行操作mq
 *
 * @author wangjia
 * @version 1.0
 * @date 2019/12/17 16:52
 */
@Slf4j
public class SimpleProducer {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST_NAME);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername(RABBIT_USERNAME);
        connectionFactory.setPassword(RABBIT_PASSWORD);
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            log.info("connection.isOpen:{}   -->  channel.isOpen:{}",connection.isOpen(),channel.isOpen());
            // 创建一个 type = direct ,持久化的、非自动删除的交换机
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
            // 创建一个 持久化、非排他的、非自动删除的队列
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            // 将队列绑定到交换机上
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            String msg = "hello";
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
            channel.close();
            connection.close();
        } catch (Exception e) {
            log.info("获取连接信息异常", e);
        }
    }
}
