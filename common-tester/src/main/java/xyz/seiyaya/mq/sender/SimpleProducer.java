package xyz.seiyaya.mq.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.common.cache.helper.DBParam;

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


    private static String ALTERNATE_EXCHANGE_NAME = "alternate_exchange_name";

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
            DBParam dbParam = new DBParam().set("alternate-exchange",ALTERNATE_EXCHANGE_NAME);
            // 创建一个 type = direct ,持久化的、非自动删除的交换机
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, dbParam);
            // 声明上面的备机
            channel.exchangeDeclare(ALTERNATE_EXCHANGE_NAME,"fanout",true,false,null);


            // 创建一个 持久化、非排他的、非自动删除的队列
            // 设置消息6s过期
            DBParam ttlParam = new DBParam().set("x-message-ttl",6000);
            channel.queueDeclare(QUEUE_NAME, true, false, false, ttlParam);
            // 将队列绑定到交换机上
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);


            // 生成消费路由到备机交换机的队列
            channel.queueDeclare("unRoutedQueue",true,false,false,null);
            channel.queueBind("unRoutedQueue",ALTERNATE_EXCHANGE_NAME,"");

            String msg = "hello";
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,true,false, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());

            // 当消息在对应的交换机找不到对应的队列，消息将会被退回
            channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
                String message = new String(body);
                log.info("没有找到对应队列的消息:{}  exchange:{}  routingKey:{}",message,exchange,routingKey);
            });
            channel.close();
            connection.close();
        } catch (Exception e) {
            log.info("获取连接信息异常", e);
        }
    }
}
