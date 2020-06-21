package xyz.seiyaya.common.msg.service;

import xyz.seiyaya.common.msg.bean.JSONMsg;

/**
 * 消息服务
 * @author wangjia
 * @version 1.0
 */
public interface MsgService {

    /**
     * 发送消息到指定频道
     * @param channel
     * @param msg
     */
    void sendMsg(String channel, JSONMsg msg);
}
