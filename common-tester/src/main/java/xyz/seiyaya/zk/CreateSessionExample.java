package xyz.seiyaya.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author wangjia
 * @version 1.0
 */
@Slf4j
public class CreateSessionExample {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 6000, event -> {
            log.info("receive watched event {}", event);
            if (Watcher.Event.KeeperState.SyncConnected.equals(event.getState())) {
                connectedSemaphore.countDown();
            }
        });

        connectedSemaphore.await();
        log.info("end zookeeper state:{}",zooKeeper.getState());
    }
}
