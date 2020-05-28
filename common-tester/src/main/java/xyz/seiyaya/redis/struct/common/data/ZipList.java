package xyz.seiyaya.redis.struct.common.data;

import lombok.Data;

/**
 * 本质上是一个字节数组,有序集合、散列、和列表的底层实现 <br/>
 * 有序集合或散列的元素较少且是短字符串的时候使用zipList <br/>
 * 列表使用快速列表(quickList)数据结构存储
 * @author wangjia
 * @version 1.0
 * @date 2020/5/28 17:18
 */
@Data
public class ZipList {

    /**
     * zlbytes: 前4个字节表示压缩列表的长度，也就是压缩列表最多有 (2 << 8 -1 )个字节  <br/>
     * zltail: 占有4个字节，压缩列表尾元素相对其实地址的偏移量
     * zllen: 占有2个字节，表示压缩列表的元素个数
     * entryX: 压缩列表存储的可以是字节数组或者整数
     * zlend: 压缩列表尾，占用1个字节
     */
    byte[] zipArray;
}
