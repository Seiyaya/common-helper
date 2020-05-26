package xyz.seiyaya.redis.struct.high.sentinel;

import lombok.Data;

/**
 * 被sentinel监控的实例，可以是主、从、sentinel
 * @author wangjia
 * @date 2020/5/26 13:48
 */
@Data
public class SentinelRedisInstance {

    /**
     * 当前实例类型以及实例状态
     */
    int flags;

    /**
     * 实例的名字
     * 主服务器的名字由用户在配置文件中配置
     * 从服务器以及Sentinel的名字由sentinel自动配置
     * 格式ip:port
     */
    String name;

    /**
     * 实例运行Id
     */
    String runId;

    /**
     * 配置纪元，用于实现故障转移
     */
    int configEpoch;

    /**
     * 实例地址
     */
    SentinelAddr sentinelAddr;

    /**
     * 实例无响应多少毫秒之后会被判断为下线
     */
    long downAfterPeriod;

    /**
     * 判断这个实例下线所需要的支持投票数量
     */
    int quorum;

    /**
     * 在实行故障转移时，可以同时对新的主服务器进行同步的从服务器数量
     */
    int parallelSyncs;

    /**
     * 刷新故障转移状态的最大时限
     */
    long failOverTimeout;
}
