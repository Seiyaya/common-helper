package xyz.seiyaya.redis.struct.high.cluster;

import lombok.Data;

import java.util.List;

/**
 * @author wangjia
 * @date 2020/5/26 14:11
 */
@Data
public class ClusterNode {

    /**
     * 节点创建时间
     */
    private long createTime;

    /**
     * 节点名字
     */
    private String name;

    /**
     * 节点标识，节点的角色(主从)和状态(在线下线)
     * REDIS_NODE_MASTER    主节点
     */
    private int flags;

    /**
     * 当前节点配置纪元，用于实现故障转移
     */
    private int configEpoch;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口号
     */
    private int port;

    /**
     * 记录槽指派信息
     * 这里本来是移动14位，但是1byte=8bit，所以只移动11位
     */
    private byte[] slots = new byte[2 >> 11];

    /**
     * 节点处理的槽数量
     */
    private int numSlots;

    /**
     * 保存连接节点有关的信息
     */
    private ClusterLink clusterLink;

    /**
     * 关联该节点的集群状态
     */
    private ClusterState clusterState;

    /**
     * 如果这个节点是从节点，那么这个指向主节点
     */
    private ClusterNode slaveOf;


    /**
     * 正在复制这个主节点的从节点数量
     */
    private int numSlaves;

    /**
     * 指向正在复制这个主节点的从节点
     */
    private ClusterNode slave;

    /**
     * 记录了所有节点对该节点的下线报告
     * ABC节点，B ping C没有响应，B判断C疑似下线，A节点会收到B的疑似下线
     */
    private List<ClusterNodeFailReport> failReports;


    /**
     * A向B发送meet命令
     *  A在自身的nodes添加B节点的ClusterNode，然后发送meet,B接收到之后添加A节点的node到nodes中，然后响应pong命令， 然后A在发送ping命令表示自己收到
     * @param otherNode
     */
    public void meet(ClusterNode otherNode){
        this.clusterState.getNodes().put(otherNode.getName(),otherNode);
    }
}
