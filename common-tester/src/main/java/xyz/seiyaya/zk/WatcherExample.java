package xyz.seiyaya.zk;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/7 16:19
 */
@Slf4j
public class WatcherExample implements Watcher {

    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) throws Exception{
        WatcherExample example = new WatcherExample();
        zooKeeper = new ZooKeeper(example.getZkPath(),10000,example);
        zooKeeper.getChildren("/demo",false);
        TimeUnit.SECONDS.sleep(300);
    }

    private String getZkPath() {
        return "localhost:2181";
    }

    @SneakyThrows
    @Override
    public void process(WatchedEvent event) {
        log.info("watcher={}  path={}  eventType={}",event.getClass().getName(),event.getPath(),event.getType());

        WatcherExample example = new WatcherExample();
        zooKeeper.getData("/demo",example,null);
    }
}
