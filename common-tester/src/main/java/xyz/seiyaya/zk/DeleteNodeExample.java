package xyz.seiyaya.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/15 9:14
 */
@Slf4j
public class DeleteNodeExample {

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 6000, null);

        /**
         * getData
         * path: 需要获取数据的路径
         * watcher: 设置watcher后发生数据变化时会受到通知
         * watch: 是否使用默认watcher
         * stat: 指定数据节点状态信息
         * cb: 回调函数
         * ctx: 上下文信息
         */

        zooKeeper.delete("/demo/bcd",1, (rc, path, ctx) -> log.info("删除节点:{}  上下文信息:{}",path,ctx),"上下文对象");
    }
}
