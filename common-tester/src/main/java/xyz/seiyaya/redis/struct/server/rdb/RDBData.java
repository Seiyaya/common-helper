package xyz.seiyaya.redis.struct.server.rdb;

import lombok.Data;

import java.util.Map;

/**
 * RDB的database部分
 * @author wangjia
 * @date 2020/5/25 14:41
 */
@Data
public class RDBData {

    private int selectDb;

    private int dbNumber;

    /**
     * 保存一个或以上的键值对，如果有过期时间也会被保存在其中
     */
    private Map<String,Object> keyValuePairs;
}
