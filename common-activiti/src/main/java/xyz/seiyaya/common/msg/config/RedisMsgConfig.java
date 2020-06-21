package xyz.seiyaya.common.msg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import xyz.seiyaya.common.msg.processor.ProcessProcessor;

/**
 * @author wangjia
 * @version 1.0
 */
@Configuration
public class RedisMsgConfig {

    /**
     * 绑定消息监听者和接收监听的方法
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(ProcessProcessor processProcessor) {
        return new MessageListenerAdapter(processProcessor, "startProcess");
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(MsgConstant.PROCESS));
        return container;
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }


    @Bean
    ProcessProcessor processProcessor() {
        return new ProcessProcessor();
    }
}
