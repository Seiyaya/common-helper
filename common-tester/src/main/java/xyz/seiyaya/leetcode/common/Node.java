package xyz.seiyaya.leetcode.common;

import java.util.ArrayList;
import java.util.List;

/**
 * N叉树
 * @author wangjia
 * @version 1.0
 * @date 2020/9/27 18:16
 */
public class Node {

    public int val;

    public List<Node> children;

    public Node(int val){
        this.val = val;
    }

    public Node(int val, int[] array){
        this.val = val;

        children = new ArrayList<>(array.length);
        for(int ele : array){
            children.add(new Node(ele));
        }
    }

    public Node getNodeByValue(int value){
        return this.children.stream().filter(model -> model.val == value).findFirst().orElse(null);
    }

    public void addChild(int[] array){
        children = new ArrayList<>(array.length);
        for(int ele : array){
            children.add(new Node(ele));
        }
    }
}
