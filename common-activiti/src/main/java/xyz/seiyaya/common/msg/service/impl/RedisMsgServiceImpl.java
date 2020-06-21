package xyz.seiyaya.common.msg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import xyz.seiyaya.common.msg.bean.JSONMsg;
import xyz.seiyaya.common.msg.service.MsgService;

/**
 * @author wangjia
 * @version 1.0
 */
@Component
public class RedisMsgServiceImpl implements MsgService {

    @Autowired
    private StringRedisTemplate template;

    @Override
    public void sendMsg(String channel, JSONMsg msg){
        template.convertAndSend(channel, msg.toJSONString());
    }
}
