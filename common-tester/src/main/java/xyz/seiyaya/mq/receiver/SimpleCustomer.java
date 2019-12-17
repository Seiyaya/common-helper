package xyz.seiyaya.mq.receiver;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static xyz.seiyaya.mq.config.SimpleRabbitConstant.*;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/17 17:05
 */
@Slf4j
public class SimpleCustomer {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(RABBIT_USERNAME);
        connectionFactory.setPassword(RABBIT_PASSWORD);

        try {
            Connection connection = connectionFactory.newConnection(new Address[]{new Address(HOST_NAME,PORT)});
            Channel channel = connection.createChannel();
            //设置客户端最多接受未被ack的消息个数
            channel.basicQos(64);


            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    log.info("接收到的消息:{}",new String(body));
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        log.error("",e);
                    }
                    channel.basicAck(envelope.getDeliveryTag() ,  false) ;
                }
            };

            channel.basicConsume(QUEUE_NAME,consumer);
            TimeUnit.SECONDS.sleep(5);
            channel.close();
            connection.close();
        } catch (Exception e) {
            log.error("",e);
        }
    }
}
