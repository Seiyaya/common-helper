package xyz.seiyaya.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 */
@Slf4j
public class WatcherRegister {

    private ZooKeeper zooKeeper;

    public WatcherRegister(Watcher watcher){
        try {
            zooKeeper = new ZooKeeper("localhost:2181",10000,watcher);
        } catch (IOException e) {
            log.error("init zk error ",e);
        }
    }

    public static void main(String[] args) throws Exception {
        WatcherExample watcher = new WatcherExample();
        WatcherRegister watcherRegister = new WatcherRegister(watcher);
        watcherRegister.zooKeeper.getData("/demo",watcher,null);


        TimeUnit.SECONDS.sleep(3000);
    }
}
