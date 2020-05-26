package xyz.seiyaya.redis.struct.server;

import lombok.Data;
import xyz.seiyaya.redis.struct.client.RedisClient;
import xyz.seiyaya.redis.struct.common.PubSubPattern;
import xyz.seiyaya.redis.struct.common.RedisDb;
import xyz.seiyaya.redis.struct.server.rdb.RDB;
import xyz.seiyaya.redis.struct.server.rdb.SaveParam;

import java.util.List;

/**
 * @author wangjia
 * @date 2020/5/25 11:14
 */
@Data
public class RedisServer {

    /**
     * 保存所有的数据库
     */
    RedisDb[] redisDbs;

    /**
     * 服务器的数据库数量
     */
    int dbNum;

    /**
     * rdb文件保存的规则
     */
    SaveParam[] saveParams;

    /*****************RDB功能用到的属性========start****************/

    /**
     * 修改的计数器，可以用来判断是否保存RDB文件
     */
    int dirty;

    /**
     * 上次保存时间，指的是执行SAVE命令或者BGSAVE的命令
     */
    long lastSaveTime;

    RDB rdb;

    /**
     * 执行bgSave的子进程id
     */
    int rdbChildPid;

    /*****************RDB功能用到的属性========end****************/





    /*****************AOF功能用到的属性========start****************/

    /**
     * 执行set命令会将命令添加到该缓冲区末尾
     * AOF重写的不是AOF文件，而是数据库生成AOF文件
     * 命令写入aof文件的时机是在时间循环中根据指定条件(flushAppendOnlyFile)判断是否将缓冲区中的数据写入到aof
     * flushAppendOnlyFile函数的行为会依赖 redis.conf的  appendfsync 选项来决定
     *      always: 将aofBuf的所有内容写入并同步到aof文件中
     *      *everysec:   将aofBuf的所有内容写入到aof文件，如果上次同步AOF的时间超过1s，则对aof文件进行同步(由一个专门的线程实现)
     *      no: 将aofBuf的内容写入到aof文件，但是何时同步由操作系统决定
     * 上面文件与写入的原因:  操作系统会有缓存，待到缓存达到指定条件时进行磁盘写入。也有fsync和fdatasync强制写入
     */
    String aofBuf;

    /**
     * 执行BGREWRITEAOF的子进程id
     */
    int aofChildPid;

    /*****************AOF功能用到的属性========end****************/





    /*****************client用到的属性========start****************/

    List<RedisClient> redisClientList;

    /*****************client用到的属性========start****************/


    /**
     * 保存所有模式订阅关系
     */
    private Dict<PubSubPattern> pubSubPatternDict;

    /**
     * 保存了毫秒级精度的系统时间戳，由serverCron进行更新
     * 只能用在对时间精度不高的功能上
     */
    private long msTime;

    /**
     * 可以用来计算对象的空转时间
     * lruClock-redisObject.lru  即为对象空转时间
     * object idleTime key  命令的实现
     * 每10秒更新一次，只是一个估值
     */
    private long lruClock;

    /**
     * serverCron函数执行的次数
     */
    private int cronLoops;

    /*****************多机数据库用到的属性========start****************/

    /**
     * 主机地址
     * slaveof 127.0.0.1 6379
     */
    private String masterHost;

    /**
     * 主机端口
     */
    private int masterPort;

    /*****************多机数据库用到的属性========end****************/


    /*****************特有功能用到的属性========start****************/

    /**
     * 监听redisSever执行了哪些命令的监听器
     */
    private List<RedisClient> monitors;

    /*****************特有功能用到的属性========end****************/


    public RedisServer(){
        this(16);
    }

    public RedisServer(int dbNum){
        this.dbNum = dbNum;
        redisDbs = new RedisDb[dbNum];
        loadRDB();
    }

    /**
     * 加载RDB文件，如果是主服务器模式运行，会忽略掉主服务器的过期键
     * 如果是从服务器模式运行，不会处理过期键
     */
    public void loadRDB(){
        rdb = new RDB();
    }

    /**
     * 保存RDB文件会忽略掉已经过期的键
     * bgSave会创建子进程进行创建RDB文件
     * 默认优先使用AOF还原数据，如果AOF是关闭的采用RDB文件还原数据
     * BGREWRITEAOF和BGSAVE命令不能同时执行，如果通知执行后发送的命令会排队在另外一个后面
     */
    public void bgSave(){
        // 保存文件
        this.dirty = 0;
        this.lastSaveTime = System.currentTimeMillis();
    }
}
