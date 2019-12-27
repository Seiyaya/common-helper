package xyz.seiyaya.mq.receiver;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static xyz.seiyaya.mq.config.SimpleRabbitConstant.*;
import static xyz.seiyaya.mq.sender.DeadMessageSender.QUEUE_DEAD_NAME;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/27 15:54
 */
@Slf4j
public class DeadMessageReceiver {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(RABBIT_USERNAME);
        connectionFactory.setPassword(RABBIT_PASSWORD);
        try {
            Connection connection = connectionFactory.newConnection(new Address[]{new Address(HOST_NAME,PORT)});
            Channel channel = connection.createChannel();

            channel.basicConsume(QUEUE_DEAD_NAME,new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    log.info("接收到的消息:{}",new String(body));
                    channel.basicAck(envelope.getDeliveryTag() ,  false) ;
                }
            });

            TimeUnit.SECONDS.sleep(2);

            channel.close();
            connection.close();
        }catch (Exception e){
            log.error("",e);
        }
    }
}
