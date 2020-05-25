package xyz.seiyaya.redis.struct.client;

import lombok.Data;
import xyz.seiyaya.redis.struct.common.RedisCommand;
import xyz.seiyaya.redis.struct.common.RedisDb;

/**
 * @author wangjia
 * @date 2020/5/25 11:14
 */
@Data
public class RedisClient {

    /**
     * 指向redisServer数组的一个元素
     */
    private RedisDb redisDb;

    /**
     * 客户端正在使用的套接字描述符
     * 可以为-1或者>-1的整数
     * 伪客户端的时候fd=-1，主要应用在载入AOF文件还原数据库状态以及 lua 脚本中包含的redis命令
     */
    private int fd;

    /**
     * 默认没有名字
     */
    private String name;

    /**
     * 记录了客户端的角色
     */
    private int flags;

    /**
     * 输入缓冲区
     */
    private String queryBuf;

    /**
     * 参数个数
     */
    private int argc;

    /**
     * 参数
     * argv[0]表示执行的命令
     * 比如set key value
     * argv[0] = "set" argv[1]="key" argv[2]="value"
     */
    private String[] argv;

    /**
     * redis命令 argv[0]
     */
    private RedisCommand redisCommand;

    /**
     * 输出缓冲区，用来接收服务端的回复
     */
    private String outBuf;

    /**
     * 回复的内容,上面的定额缓冲区满后使用这个缓冲区
     */
    private String[] reply;

    /**
     * 认证状态 =1表示通过验证
     */
    private int authenticated;

    /**
     * 创建客户端的时间
     */
    private long createTime;

    /**
     * 最后一次交互的时间，可以用来计算客户端的空转时间
     */
    private long lastInteraction;

    /**
     * 记录了输出缓冲区第一次达到软限制的时间
     * 软限制也不像硬限制一样达到阈值直接关闭客户端，而是根据配置通过超过的频繁程度来决定是否关闭客户端
     */
    private long outSoftLimitReachedTime;

    /**
     * 初始化的时候创建lua脚本的客户端
     */
    private RedisClient luaClient;
}
