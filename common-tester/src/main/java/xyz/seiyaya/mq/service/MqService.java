package xyz.seiyaya.mq.service;

import xyz.seiyaya.mybatis.bean.UserBean;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/12/10 10:23
 */
public interface MqService {


    /**
     * 发送消息到指定队列
     * @param key
     * @param userBean
     */
    void sendMsg(String key, Object userBean);

    /**
     * 发送到交换机的消息
     * @param exchange
     * @param key
     * @param userBean
     */
    void sendMsg(String exchange,String key, Object userBean);
}
