package xyz.seiyaya.leetcode;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 根据一棵树的前序遍历与中序遍历构造二叉树。
 *
 * @author wangjia
 * @date 2020/5/22 8:37
 */
public class Solution105 {

    public static void main(String[] args) {
        TreeNode treeNode = new Solution105().buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        System.out.println(JSON.toJSONString(treeNode));
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return build(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
    }

    private TreeNode build(int[] preorder, int preStart, int preEnd, int[] inorder, int inStart, int inEnd) {
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }
        int root = preorder[preStart];
        // 在中序遍历中查找根结点的位置，可以划分出左子树和右子树
        // 对于前序遍历的左子树和右子树划分
        //   左子树   [preStart+1, preStart+1+(mid-1-inStart+1)] ==> [preStart,preStart+mid-inStart]
        //   右子树   [preStart+mid-inStart+(inEnd-mid-1+1),preEnd] ==> [preStart-inStart+inEnd ,preEnd]
        // 对于中序遍历的左子树和右子树划分 [inStart,mid-1]  [mid+1,inEnd]
        // 对于后序遍历
        //   左子树      [backStart,backStart+(mid-1-inStart+1)] ==> [backStart,backStart+mid-inStart]
        //   右子树      [backStart+(mid-1-inStart+1)+(inEnd-mid-1+1),backEnd] ==> [backStart
        int mid = inStart;
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder[i] == root) {
                mid = i;
                break;
            }
        }
        TreeNode treeNode = new TreeNode(root);
        treeNode.left = build(preorder, preStart + 1, preStart + mid - inStart, inorder, inStart, mid - 1);
        treeNode.right = build(preorder, preStart + mid - inStart + 1, preEnd, inorder, mid + 1, inEnd);
        return treeNode;
    }

    private int findInOrder(int item, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) {
            if (item == inorder[i]) {
                return i;
            }
        }
        return 0;
    }

    @Data
    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
