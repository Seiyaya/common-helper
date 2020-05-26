package xyz.seiyaya.redis.struct.high.sentinel;

import lombok.Data;

/**
 * 实例地址
 * @author wangjia
 * @date 2020/5/26 13:56
 */
@Data
public class SentinelAddr {
    String ip;
    int port;
}
