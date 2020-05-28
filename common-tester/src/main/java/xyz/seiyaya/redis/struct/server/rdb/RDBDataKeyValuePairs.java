package xyz.seiyaya.redis.struct.server.rdb;

import lombok.Data;

/**
 * @author wangjia
 * @date 2020/5/25 14:44
 */
@Data
public class RDBDataKeyValuePairs {

    /**
     * 过期时间
     */
    private long expireTime;
    /**
     * <pre>每个type表示一种编码， 根据不同的type决定读入的方式
     * REDIS_RDB_TYPE_STRING
     * REDIS_RDB_TYPE_LIST
     * REDIS_RDB_TYPE_SET
     * REDIS_RDB_TYPE_ZSET
     * REDIS_RDB_TYPE_HASH
     * REDIS_RDB_TYPE_LIST_ZIPLIST
     * REDIS_RDB_TYPE_SET_INTSET
     * REDIS_RDB_TYPE_ZSET_ZIPLIST
     * REDIS_RDB_TYPE_HASH_ZIPLIST
     * </pre>
     */
    private byte type;

    private String key;

    private Object value;
}
