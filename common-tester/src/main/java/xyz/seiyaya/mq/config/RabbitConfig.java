package xyz.seiyaya.mq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/9 14:24
 */
@Configuration
public class RabbitConfig {


    public static final String ROUTING_KEY_TRADE = "routing_key_trade";

    /**
     * 成交信息
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(ROUTING_KEY_TRADE);
    }
}
