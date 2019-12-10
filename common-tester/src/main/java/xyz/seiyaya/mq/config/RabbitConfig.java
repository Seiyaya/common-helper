package xyz.seiyaya.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
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
     * 成交后推送消息
     */
    public static final String ROUTING_KEY_TRADE_SEND_MSG = "routing_key_trade_send_msg";

    /**
     * 成交的交换机
     */
    public static final String EXCHANGE_TRADE = "exchange_trade";

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE_TRADE, true, false);
    }

    /**
     * 成交信息
     * @return
     */
    @Bean
    public Queue tradeQueue(){
        return new Queue(ROUTING_KEY_TRADE,true);
    }

    @Bean
    public Binding bindTradeToExchange(){
        return BindingBuilder.bind(tradeQueue()).to(fanoutExchange());
    }

    @Bean
    public Queue tradeSendMsgQueue(){
        return new Queue(ROUTING_KEY_TRADE_SEND_MSG,true);
    }

    @Bean
    public Binding bindTradeSendMsgToExchange(){
        return BindingBuilder.bind(tradeSendMsgQueue()).to(fanoutExchange());
    }
}
