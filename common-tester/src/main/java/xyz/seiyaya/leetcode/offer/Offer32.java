package xyz.seiyaya.leetcode.offer;

import org.junit.Before;
import org.junit.Test;
import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.*;

/**
 * 从上到下打印树
 * @author wangjia
 * @version 1.0
 * @date 2020/9/23 10:08
 */
@SuppressWarnings("all")
public class Offer32 {

    private TreeNode root;

    @Before
    public void init(){
        root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
    }

    /**
     * 从上到下打印出二叉树的每个节点，同一层的节点按照从左到右的顺序打印
     */
    @Test
    public void printTree(){
        int[] ints = levelOrder(root);
        System.out.println(Arrays.toString(ints));
    }

    public int[] levelOrder(TreeNode root) {
        if(root == null){
            return new int[]{};
        }
        List<Integer> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
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
                result.add(poll.val);
            }
        }
        int[] ints = new int[result.size()];
        for(int i=0;i<result.size();i++){
            ints[i] = result.get(i);
        }
        return ints;
    }

    /**
     * 从上到下按层打印二叉树，同一层的节点按从左到右的顺序打印，每一层打印到一行。
     */
    @Test
    public void printTree2(){
        List<List<Integer>> lists = levelOrder2(root);
        System.out.println(lists);
    }

    public List<List<Integer>> levelOrder2(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if(root == null){
            return result;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            int size = queue.size();
            List<Integer> innerResult = new ArrayList<>(size);
            for(int i=0;i<size;i++){
                TreeNode poll = queue.poll();
                if(poll.left != null){
                    queue.add(poll.left);
                }
                if(poll.right != null){
                    queue.add(poll.right);
                }
                innerResult.add(poll.val);
            }
            result.add(innerResult);
        }
        return result;
    }

    /**
     * 请实现一个函数按照之字形顺序打印二叉树，即第一行按照从左到右的顺序打印，第二层按照从右到左的顺序打印，第三行再按照从左到右的顺序打印，其他行以此类推。
     */
    @Test
    public void printTree3(){
        List<List<Integer>> lists = levelOrder3(root);
        System.out.println(lists);
    }

    public List<List<Integer>> levelOrder3(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if(root == null){
            return result;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int level = 1;
        while(!queue.isEmpty()){
            int size = queue.size();
            List<Integer> innerResult = new ArrayList<>(size);
            // true表示从右往左  false表示从左往右
            boolean flag = level % 2 == 0;
            for(int i=0;i<size;i++){
                TreeNode poll = queue.poll();
                if(poll.left != null){
                    queue.add(poll.left);
                }
                if(poll.right != null){
                    queue.add(poll.right);
                }
                innerResult.add(poll.val);
            }
            if(flag){
                int start = 0;
                int end = size-1;
                while(start < end){
                    Integer tmp = innerResult.get(start);
                    innerResult.set(start,innerResult.get(end));
                    innerResult.set(end,tmp);
                    start++;
                    end--;
                }
            }
            result.add(innerResult);
            level++;
        }
        return result;
    }
}
