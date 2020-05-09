package xyz.seiyaya.leetcode;

/**
 * 判断一个树是否是另外一个树的子树
 * @author wangjia
 * @date 2020/5/7 9:46
 */
public class Solution572 {

    public static void main(String[] args) {
        /**
         * 要么两个子树一模一样
         * 要么一个树是另外一个树的左支树
         * 要么一个树是另外一个树的右支树
         */
        new Solution572().isSubtree(new TreeNode(1),new TreeNode(2));
    }


    public boolean isSubtree(TreeNode s, TreeNode t) {
        // 判断树是否相等
        if(s == null  && t == null){
            return true;
        }
        if(s == null && t != null){
            return false;
        }
        return isSameTree(s,t) || isSameTree(s.left,t.left) || isSameTree(s.right,t.right);
    }

    private boolean isSameTree(TreeNode s, TreeNode t){
        if(s == null && t == null){
            return true;
        }
        return s != null && t!=null && s.val == t.val && isSameTree(s.left, t.left) && isSameTree(s.right,t.right);
    }

    static class TreeNode{
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x){
            this.val = x;
        }
    }
}
