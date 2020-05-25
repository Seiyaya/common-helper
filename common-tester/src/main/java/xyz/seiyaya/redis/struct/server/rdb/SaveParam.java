package xyz.seiyaya.redis.struct.server.rdb;

import lombok.Data;

/**
 * 保存rdb时间间隔的配置项
 * @author wangjia
 * @date 2020/5/25 14:24
 */
@Data
public class SaveParam {

    /**
     * time时间内修改 updateCount 次数据库的话执行保存数据库命令
     */
    int time;
    int updateCount;
}
