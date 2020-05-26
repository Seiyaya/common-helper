package xyz.seiyaya.redis.struct.high.cluster;

import lombok.Data;

/**
 * 保存连接节点所需要的有关信息
 * 与redisClient的区别: 都有自己的fd、输入输出缓冲区
 *  区别在于redisClient的输入输出是连接的客户端
 *  ClusterLink的输入输出是连接服务端
 * @author wangjia
 * @date 2020/5/26 14:13
 */
@Data
public class ClusterLink {

    /**
     * 连接创建的时间
     */
    private long createTime;

    /**
     * tcp套接字描述符
     */
    private int fd;

    /**
     * 输出缓冲区
     */
    private String sndBuf;

    /**
     * 输入缓冲区
     */
    private String rcvBuf;

    /**
     * 与这个连接关联的节点
     */
    private ClusterNode clusterNode;
}
