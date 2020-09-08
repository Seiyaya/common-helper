package xyz.seiyaya.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(path,data);
    }

    public void deleteNode(String path,int version) throws Exception {
        client.delete().guaranteed().withVersion(version).inBackground((client, event) -> {
            log.info("删除成功:{} resultCode:{}",client,event.getResultCode());
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

    public void addNodeDataWatcher(String path) throws Exception {
        NodeCache nodeCache = new NodeCache(client, path);
        nodeCache.start(true);

        nodeCache.getListenable().addListener(()->{
            byte[] data = nodeCache.getCurrentData().getData();
            log.info("result:{}",data);
        });
    }

    public static void main(String[] args) throws Exception {
        /**
         * 异步操作inbackground,传递一个回调函数
         */
        CuratorExample example = new CuratorExample();
        Scanner scanner = new Scanner(System.in);
        int switchInt = scanner.nextInt();
        switch (switchInt){
            case 0:
                example.createNode("/curator/node1","123".getBytes());
                break;
            case 1:
                example.deleteNode("/demo/per",0);
                break;
            case 2:
                example.updateNode("/demo/abc","456".getBytes(),-1);
                break;
            case 3:
                example.readNode("/demo");
                break;
            case 4:
                example.getChildren("/demo");
                break;
            case 5:
                example.addNodeDataWatcher("/demo");
                break;
            default:
        }

        TimeUnit.SECONDS.sleep(3);
    }
}
