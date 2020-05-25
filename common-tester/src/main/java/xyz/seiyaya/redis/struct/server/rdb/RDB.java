package xyz.seiyaya.redis.struct.server.rdb;

import lombok.Data;

/**
 * @author wangjia
 * @date 2020/5/25 14:34
 */
@Data
public class RDB {

    /**
     * 文件的前缀，固定是"redis"字符串，用来快速判断是否是rdb文件
     */
    private String prefix;

    /**
     * 用来记录版本号
     */
    private int dbVersion;

    /**
     * 保存非空数据库的部分
     */
    private RDBData[] rdbData;

    private byte eof;

    /**
     * 校验和 8个字节
     */
    private int checkSum;
}
