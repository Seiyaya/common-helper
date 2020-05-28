package xyz.seiyaya.redis.struct.common.data;

import lombok.Data;

/**
 * 有序集合的底层实现方式之一
 * @author wangjia
 * @version 1.0
 * @date 2020/5/28 17:01
 */
@Data
public class SkipList {

    /**
     * ele = null ,score=0，不计入跳跃表总长度，是一个空节点
     */
    SkipListNode head;

    /**
     * 跳跃表尾节点
     */
    SkipListNode tail;

    /**
     * 跳跃表高度
     */
    int level;

    /**
     * 跳跃表长度
     */
    int length;

    @Data
    private static class SkipListNode{
        /**
         * 字符串类型的数据
         */
        SimpleDynamicString simpleDynamicString;

        /**
         * 存储排序的分值
         */
        double source;

        /**
         * 后退指针
         */
        SkipListNode backward;

        /**
         * 柔性数组，纵向看每一个层级表示一个level
         */
        SkipListLevel[] skipListLevels;
    }

    @Data
    private static class SkipListLevel{

        /**
         * 指向本层的下一个节点
         */
        SkipListNode forward;

        /**
         * forward指向的节点与本节点之间的元素个数
         */
        int span;
    }
}
