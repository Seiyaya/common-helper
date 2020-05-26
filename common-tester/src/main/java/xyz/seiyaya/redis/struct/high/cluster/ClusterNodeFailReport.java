package xyz.seiyaya.redis.struct.high.cluster;

import lombok.Data;

/**
 * @author wangjia
 * @date 2020/5/26 16:36
 */
@Data
public class ClusterNodeFailReport {

    /**
     * 报告目标节点已经下线的节点
     */
    ClusterNode clusterNode;

    /**
     * 最后一次从node节点收到下线报告的时间，用来检测下线报告是否过期
     */
    long time;
}
