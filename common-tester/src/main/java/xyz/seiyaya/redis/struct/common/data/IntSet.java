package xyz.seiyaya.redis.struct.common.data;

import lombok.Data;

/**
 * 整数集合
 * @author wangjia
 * @version 1.0
 * @date 2020/5/28 17:36
 */
@Data
public class IntSet {

    /**
     * 编码类型
     */
    int encoding;

    /**
     * 元素个数
     */
    int length;

    /**
     * 具体的存储内容，根据encoding决定几个字节表示一个数
     */
    int[] contents;
}
