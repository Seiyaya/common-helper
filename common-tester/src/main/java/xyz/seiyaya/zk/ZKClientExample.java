package xyz.seiyaya.zk;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/15 14:18
 */
public class ZKClientExample {

    ZkClient zkClient ;


    public ZKClientExample(){
        this.zkClient = new ZkClient("localhost:2181",5000);
    }

    public static void main(String[] args) {
        ZKClientExample zkClientExample = new ZKClientExample();
        zkClientExample.zkClient.create("/zkClient/node2/node3","123", CreateMode.PERSISTENT);
    }
}
