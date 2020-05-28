package xyz.seiyaya.redis.struct.high.cluster;

import lombok.Data;
import xyz.seiyaya.redis.struct.common.data.Dict;

/**
 * 每个节点都有的集群状态
 * @author wangjia
 * @date 2020/5/26 14:18
 */
@Data
public class ClusterState {

    /**
     * 关联状态的节点
     */
    private ClusterNode clusterNode;

    /**
     * 集群关联的当前纪元
     */
    private int currentEpoch;

    /**
     * 集群状态，用于实现上下线
     */
    private int state;

    /**
     * 集群至少处理着一个槽的节点的数量
     * size=0表示集群没有处理槽的节点，也就是集群此时的状态是下线的
     */
    private int size;

    /**
     * 集群节点名单,包含了自身的clusterNode
     */
    private Dict<ClusterNode> nodes;

    /**
     * 集群中所有的槽指派信息
     * 这里不同于clusterNode.slots，这里是保存了所有槽指派信息，可以快速找到某个槽被哪个节点使用，不需要遍历clusterNode.nodes
     * clusterNode。slots的好处在于meet过程中发送自己的槽指派情况只需要发送clusterNode.slots即可
     */
    private ClusterNode[] slots = new ClusterNode[2 << 14];

    /**
     * importingSlotsFrom[i]当前节点正在从clusterNode所代表的节点导入槽i
     */
    private ClusterNode[] importingSlotsFrom = new ClusterNode[2 << 14];


    /**
     * migratingSlotsTo[i]当前节点正在将槽i迁移至clusterNode所代表的节点
     */
    private ClusterNode[] migratingSlotsTo = new ClusterNode[2 << 14];
}
