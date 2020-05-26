package xyz.seiyaya.redis.struct.high.cluster;

import lombok.Data;

/**
 * @author wangjia
 * @date 2020/5/26 14:27
 */
@Data
public class ClusterMsg {

    /**
     * 消息长度
     */
    private int totLen;

    /**
     * 消息类型,可以用来判断消息是ping、pong、meet
     */
    private int type;

    /**
     * 发送者所处的配置纪元
     */
    private int currentEpoch;

    /**
     * 发送者是一个主节点: =发送者的配置纪元
     * 发送者是一个从节点: =发送者正在复制的主节点的配置纪元
     */
    private int configEpoch;

    /**
     * 发送者名字(id)
     */
    private String name;

    /**
     * 发送者的槽指派信息
     */
    private byte[] mySlots;

    /**
     * 如果发送者是主节点: =这里记录的是 REDIS_NODE_NULL_NAME
     * 如果发送者是从节点: =发送者正在复制的主节点的名字
     */
    private String slaveOf;

    /**
     * 发送者的端口号
     */
    private int port;

    /**
     * 发送者的标识值
     */
    private int flags;

    /**
     * 发送者所处的集群状态
     */
    private int state;

    /**
     * 消息的正文
     */
    private ClusterMsgData clusterMsgData;
}
