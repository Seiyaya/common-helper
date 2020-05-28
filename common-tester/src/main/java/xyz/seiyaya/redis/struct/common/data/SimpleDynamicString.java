package xyz.seiyaya.redis.struct.common.data;

import lombok.Data;

/**
 * 对应redis的sds
 * @author wangjia
 * @version 1.0
 * @date 2020/5/28 16:44
 */
@Data
public class SimpleDynamicString {

    /**
     * buf中已用字节数
     */
    private int len;

    /**
     * buf中剩余可用字节数
     */
    private int free;

    /**
     * 具体存储的值
     */
    private char[] buf;
}
