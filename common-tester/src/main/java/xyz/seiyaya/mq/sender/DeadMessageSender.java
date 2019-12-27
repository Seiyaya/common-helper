package xyz.seiyaya.mq.sender;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.common.helper.DBParam;

import static xyz.seiyaya.mq.config.SimpleRabbitConstant.*;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/27 15:42
 */
@Slf4j
public class DeadMessageSender {

    private static final String QUEUE_NORMAL_NAME = "QUEUE_NORMAL_NAME";

    private static final String EXCHANGE_NORMAL_NAME = "EXCHANGE_NORMAL_NAME";

    public static final String QUEUE_DEAD_NAME = "QUEUE_DEAD_NAME";

    public static final String EXCHANGE_DEAD_NAME = "EXCHANGE_DEAD_NAME";

    private static final String DEAD_MESSAGE_ROUTING_KEY = "DEAD_MESSAGE_ROUTING_KEY";


    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST_NAME);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername(RABBIT_USERNAME);
        connectionFactory.setPassword(RABBIT_PASSWORD);
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            // 死信交换机
            channel.exchangeDeclare(EXCHANGE_DEAD_NAME,"direct",true);

            // 正常的交换机
            channel.exchangeDeclare(EXCHANGE_NORMAL_NAME,"fanout",true);

            DBParam normalArgs = new DBParam()
                    .set("x-message-ttl",10000)
                    .set("x-dead-letter-exchange",EXCHANGE_DEAD_NAME)
                    .set("x-dead-letter-routing-key",DEAD_MESSAGE_ROUTING_KEY);

            channel.queueDeclare(QUEUE_DEAD_NAME,true,false,false,null);
            channel.queueBind(QUEUE_DEAD_NAME,EXCHANGE_DEAD_NAME,DEAD_MESSAGE_ROUTING_KEY);

            channel.queueDeclare(QUEUE_NORMAL_NAME,true,false,false,normalArgs);
            channel.queueBind(QUEUE_NORMAL_NAME,EXCHANGE_NORMAL_NAME,"");


            channel.basicPublish(EXCHANGE_NORMAL_NAME,"rk", MessageProperties.PERSISTENT_TEXT_PLAIN,"dlx".getBytes());

        }catch (Exception e){
            log.error("",e);
        }
    }
}
