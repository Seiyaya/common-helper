package xyz.seiyaya.leetcode;

import lombok.extern.slf4j.Slf4j;
import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * 二叉搜索树插入节点
 * @author wangjia
 * @version 1.0
 * @date 2020/9/30 9:08
 */
@Slf4j
@SuppressWarnings("all")
public class Solution701 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(7);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
//        TreeNode treeNode = new Solution701().insertIntoBST(root, 5);
//        System.out.println("=======================");
//        treeNode.foreachTreeNode();

        TreeNode result = new Solution701().insertIntoBST2(root, 5);
        result.foreachTreeNode();
    }

    /**
     * 递归版本
     * @param root
     * @param val
     * @return
     */
    public TreeNode insertIntoBST2(TreeNode root,int val){
        if(root == null){
            return new TreeNode(val);
        }
        if(root.val > val){
            root.left = insertIntoBST2(root.left,val);
        }else{
            root.right = insertIntoBST2(root.right,val);
        }
        return root;
    }

    public TreeNode insertIntoBST(TreeNode root, int val) {
        TreeNode tmp = root;

        while(tmp != null){
            log.info("root:{} val:{} left:{}",tmp.val,val,tmp.val>val);
            if(tmp.val < val){
                if(tmp.right == null){
                    tmp.right = new TreeNode(val);
                    break;
                }
                tmp = tmp.right;
            }else{
                if(tmp.left == null){
                    tmp.left = new TreeNode(val);
                    break;
                }
                tmp = tmp.left;
            }
        }
        if(tmp == null){
            root = new TreeNode(val);
        }
        return root;
    }
}
