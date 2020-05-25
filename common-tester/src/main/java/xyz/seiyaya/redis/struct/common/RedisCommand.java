package xyz.seiyaya.redis.struct.common;

import lombok.Data;

/**
 * 根据redisClient的argv[0]得到对应解析命令的方法
 * @author wangjia
 * @date 2020/5/25 16:12
 */
@Data
public class RedisCommand {

    /**
     * 命令的名称，比如set
     */
    private String name;

    /**
     * 具体命令的处理器
     */
    private RedisCommandProcessor redisCommandProcessor;

    /**
     * 记录命令的参数个数，可以用来检验命令格式是否正确
     */
    private int arity;

    /**
     * 对sFlag二进制的标识
     */
    private int sFlags;

    /**
     * 服务器总共执行多少次这个命令
     */
    private long calls;

    /**
     * 服务器执行这个命令耗费总时长
     */
    private long millSeconds;
}
