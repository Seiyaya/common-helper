package xyz.seiyaya.redis.struct.high.cluster;

import lombok.Data;

/**
 * 消息的正文
 * @author wangjia
 * @date 2020/5/26 14:35
 */
@Data
public class ClusterMsgData {

    private ClusterMsgDataGossip clusterMsgDataGossip;


    /**
     * 节点之间通信使用gossip协议来交换不同节点的状态
     * 主要是由 meet、ping、pong三种消息实现
     */
    @Data
    private static class ClusterMsgDataGossip{
        /**
         * 节点的名字
         */
        private String name;
        /**
         * 最后一次向该节点发送ping消息的时间戳
         */
        private long pingSent;

        /**
         * 最后一次从该节点接收到pong消息的时间戳
         */
        private long pongReceived;
    }
}
