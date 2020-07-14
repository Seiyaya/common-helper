package xyz.seiyaya.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/7 15:50
 */
public class ZLock implements Watcher {

    @Override
    public void process(WatchedEvent watchedEvent) {
        /**
         * watcher通知事件
         * @see org.apache.zookeeper.Watcher.Event.KeeperState  通知状态
         * @see org.apache.zookeeper.Watcher.Event.EventType  事件类型，只有客户端和服务端正常连接的时候才会产生有效的事件
         *
         * KeeperState = SyncConnected  此时客户端和服务端处于连接状态
         * 只会收到通知，需要调用其他api获取修改后的数据或者根据版本号获取数据
         * EventType = NodeDataChanged  监听的是节点version的变化
         * EventType = NodeChildrenChanged 删除或者新增节点
         */
    }
}
