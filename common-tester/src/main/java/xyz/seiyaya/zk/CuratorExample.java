package xyz.seiyaya.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/15 15:45
 */
@Slf4j
public class CuratorExample {

    private CuratorFramework client = null;

    public CuratorExample(){
        this.client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .sessionTimeoutMs(6000)
                .connectionTimeoutMs(6000)
                .retryPolicy(new RetryNTimes(3,300))
                .build();
        client.start();
    }

    public void closeClient(){
        if(client != null){
            this.client.close();
        }
    }

    public void createNode(String path,byte[] data) throws Exception {
        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(path,data);
    }

    public void deleteNode(String path,int version) throws Exception {
        client.delete().guaranteed().withVersion(version).inBackground((client, event) -> {

        }).forPath(path);
    }

    public void readNode(String path) throws Exception {
        Stat stat = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat).forPath(path);
        log.info("读取节点[ {} ]的数据:{}",path,new String(bytes));
        log.info("statInfo:{}",stat);
    }

    public void updateNode(String path,byte[] data,int version) throws Exception {
        client.setData().withVersion(version).forPath(path,data);
    }

    public void getChildren(String path) throws Exception {
        List<String> strings = client.getChildren().forPath(path);
        for (String string : strings) {
            log.info("child:{}",string);
        }
    }

    public void addNodeDataWatcher(String path){
    }

    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", 6000, 6000, new RetryNTimes(3, 300));

    }
}
