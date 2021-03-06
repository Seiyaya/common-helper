package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.*;

/**
 * 树的层序遍历
 * @author wangjia
 * @date 2020/5/13 8:57
 */
public class Solution102 {

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        treeNode.left = new TreeNode(2);

        foreachTreeNode(treeNode);


        List<List<Integer>> lists = new Solution102().levelOrder(treeNode);
        System.out.println(lists);
    }

    /**
     * 使用队列遍历二叉树更简单
     * @param root
     */
    public static void foreachTreeNode(TreeNode root){
        if(root == null){
            return ;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        List<List<Integer>> list = new ArrayList<>();
        while(!queue.isEmpty()){
            List<Integer> innerList = new ArrayList<>();
            int forCount = queue.size();
            for(int i=0;i<forCount;i++){
                TreeNode poll = queue.poll();
                innerList.add(poll.val);
                if(poll.left != null){
                    queue.add(poll.left);
                }
                if(poll.right != null){
                    queue.add(poll.right);
                }
            }
            list.add(innerList);
        }
        System.out.println(list);
    }


    public List<List<Integer>> levelOrder(TreeNode root) {
        Map<Integer,List<Integer>> maps = new HashMap<>();
        List<List<Integer>> list = new ArrayList<>();
        if(root == null){
            return list;
        }
        list.add(Collections.singletonList(root.val));
        TreeNode tmp = root;
        forTreeNode(tmp,maps,0);
        for(int i=1;;i++){
            List<Integer> integers = maps.get(i);
            if(integers == null){
                break;
            }
            list.add(integers);
        }
        return list;
    }

    private void forTreeNode(TreeNode tmp, Map<Integer,List<Integer>> maps, int level) {
        List<Integer> innerList = new ArrayList<>();
        if(tmp.left == null && tmp.right == null){
            return ;
        }
        if(tmp.left != null){
            innerList.add(tmp.left.val);
        }
        if(tmp.right != null){
            innerList.add(tmp.right.val);
        }
        List<Integer> mapList = maps.getOrDefault(level, innerList);
        if(mapList != innerList){
            mapList.addAll(innerList);
        }
        maps.put(level,mapList);
        if(tmp.left != null){
            forTreeNode(tmp.left,maps,level+1);
        }
        if(tmp.right != null){
            forTreeNode(tmp.right,maps,level+1);
        }
    }
}
