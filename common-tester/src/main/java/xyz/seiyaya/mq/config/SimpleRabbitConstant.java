package xyz.seiyaya.mq.config;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/17 17:09
 */
public interface SimpleRabbitConstant {
    String EXCHANGE_NAME = "exchange_name";

    String QUEUE_NAME = "queue_name";

    String ROUTING_KEY = "routing_key";

    String HOST_NAME = "127.0.0.1";

    Integer PORT = 5672;

    String RABBIT_USERNAME = "root";

    String RABBIT_PASSWORD = "123456";
}
