package xyz.seiyaya.redis.struct.high.sentinel;

import lombok.Data;
import xyz.seiyaya.redis.struct.common.data.Dict;

import java.util.List;

/**
 * 应用了Sentinel的专用代码之后，服务器会初始化一个哨兵状态
 * 保存和服务器中和Sentinel功能的有关状态
 * @author wangjia
 * @date 2020/5/26 10:00
 */
@Data
public class SentinelState {

    /**
     * 当前纪元，用来实现故障转移
     */
    int currentEpoch;

    /**
     * 保存了所有被这个sentinel监控的主服务器
     * k->v   主服务器的名字 --> sentinelRedisInstance实例
     */
    Dict<SentinelRedisInstance> masters;

    /**
     * 是否进入tilt模式
     */
    int tilt;

    /**
     * 正在执行的脚本数量
     */
    int runningScripts;

    /**
     * 进入tilt模式的时间
     */
    long tiltStartTime;

    /**
     * 最后一次处理执行服务器的时间
     */
    long previousTime;

    /**
     * 一个fifo队列，包含了所有需要执行的用户脚本
     */
    List<String> scriptsQueue;
}
