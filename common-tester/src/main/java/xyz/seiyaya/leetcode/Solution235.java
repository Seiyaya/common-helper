package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

/**
 * 二叉搜索树的公共祖先
 * @author wangjia
 * @version 1.0
 * @date 2020/9/27 13:49
 */
@SuppressWarnings("all")
public class Solution235 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(6);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(0);
        root.left.right = new TreeNode(4);
        root.left.right.left = new TreeNode(3);
        root.left.right.right = new TreeNode(5);

        root.right = new TreeNode(8);
        root.right.left = new TreeNode(7);
        root.right.right = new TreeNode(9);
        TreeNode treeNode = new Solution235().lowestCommonAncestor(root, new TreeNode(2), new TreeNode(8));
        System.out.println("公共祖先:"+treeNode.val);
    }

    /**
     * 根据二叉搜索树的性质: 左子树的值小于右子树
     * 如果当前遍历的值大于需要查找的两个值，说明两个节点在左子树
     * 如果当前遍历的值小于需要查找的两个值，说明两个节点在右子树
     * @param root
     * @param p
     * @param q
     * @return
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root.val > p.val && root.val > q.val){
            return  lowestCommonAncestor(root.left,p,q);
        }
        if(root.val < p.val && root.val < q.val){
            return lowestCommonAncestor(root.right,p,q);
        }
        return root;
    }

    /**
     * 利用迭代
     * @param root
     * @param p
     * @param q
     * @return
     */
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p,TreeNode q){
        while(root != null){
            if(root.val < p.val && root.val < q.val){
                root = root.right;
            }else if(root.val > p.val && root.val > root.val){
                root = root.left;
            }else{
                break;
            }
        }
        return root;
    }
}
