package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.*;

/**
 * 求二叉查找树的众数
 * @author wangjia
 * @version 1.0
 * @date 2020/9/24 8:47
 */
public class Solution501 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(2);
        int[] mode = new Solution501().findMode(root);
        System.out.println("利用层序遍历暴力求众数:" + Arrays.toString(mode));

        int[] mode2 = new Solution501().findMode2(root);
        System.out.println("利用中序遍历求众数:" + Arrays.toString(mode2));
    }

    /**
     * 上一个遍历的节点
     */
    private TreeNode pre = null;

    private int[] ret;

    /**
     * ret数组的索引
     */
    private int retCount = 0;

    /**
     * 出现次数最多的次数的值
     */
    private int maxCount = 0;

    /**
     * 当前值出现的次数
     */
    private int currCount = 0;

    /**
     * 二叉查找树的中序遍历就是按照升序排列
     * @param root
     * @return
     */
    public int[] findMode2(TreeNode root) {
        dfs(root);
        pre = null;
        ret = new int[retCount];
        retCount = 0;
        currCount = 0;
        dfs(root);
        return ret;
    }

    private void dfs(TreeNode root) {
        if(root == null){
            return ;
        }
        dfs(root.left);
        if( pre!= null && pre.val == root.val){
            currCount++;
        }else{
            currCount = 1;
        }
        if(currCount > maxCount){
            maxCount = currCount;
            retCount = 1;
        }else if(currCount == maxCount){
            if(ret != null){
                ret[retCount] = root.val;
            }
            retCount++;
        }
        pre = root;
        dfs(root.right);
    }

    private int max = Integer.MIN_VALUE;

    public int[] findMode(TreeNode root) {
        if(root == null){
            return new int[]{};
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        Map<Integer,Integer> maps = new HashMap<>();
        while(!queue.isEmpty()){
            int size = queue.size();
            for(int i=0;i<size;i++){
                TreeNode poll = queue.poll();
                if(poll.left != null){
                    queue.add(poll.left);
                }
                if(poll.right != null){
                    queue.add(poll.right);
                }
                maps.put(poll.val,maps.getOrDefault(poll.val,0)+1);
            }
        }
        maps.forEach((k,v)->{
            if(v > max){
                max = v;
            }
        });
        List<Integer> result = new ArrayList<>();
        maps.forEach((k,v)->{
            if(v == max){
                result.add(k);
            }
        });
        int[] resultArray = new int[result.size()];
        for(int i=0;i<result.size();i++){
            resultArray[i] = result.get(i);
        }
        return resultArray;
    }
}
