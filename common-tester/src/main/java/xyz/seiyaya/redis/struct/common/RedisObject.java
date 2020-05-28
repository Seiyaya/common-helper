package xyz.seiyaya.redis.struct.common;

/**
 * k-v中的v，简单起见直接以这个表示
 * @author wangjia
 * @date 2020/5/25 16:28
 */
public class RedisObject {

    /**
     * <pre>类型，记录的是对象的类型,可以通过 type {key} 查看
     * REDIS_STRING
     * REDIS_LIST
     * REDIS_HASH
     * REDIS_SET
     * REDIS_ZSET
     * </pre>
     */
    private int type;

    /**
     * <pre>编码,主要有8中编码,可以通过命令 object encoding {key}查看
     * REDIS_STRING
     *      REDIS_ENCODING_INT      long类型
     *      REDIS_ENCODING_EMBSTR   小于等于44个字符(只适用于 > V3.2)
     *      REDIS_ENCODING_RAW      大于44个字符
     * REDIS_LIST   v3.2之后使用替代: quicklist
     *      REDIS_ENCODING_LINKEDLIST
     *      REDIS_ENCODING_ZIPLIST
     * REDIS_HASH
     *      REDIS_ENCODING_HT
     *      REDIS_ENCODING_ZIPLIST
     * REDIS_SET
     *      REDIS_ENCODING_INTSET
     *      REDIS_ENCODING_HT
     * REDIS_ZSET
     *      REDIS_ENCODING_ZIPLIST
     *      REDIS_ENCODING_SKIPLIST
     * 每个类型的对象都至少使用两种编码
     * </pre>
     */
    private int encoding;

    /**
     * 具体的数据结构的实现，由encoding实现
     */
    private Object ptr;

    /**
     * 被引用则+1,=0时回收
     * object refCount查看引用数量
     */
    private int refCount;

    /**
     * 对象最后一次被访问的时间
     */
    private long lru;
}
