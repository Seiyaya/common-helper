package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 序列化是将一个数据结构或者对象转换为连续的比特位的操作，进而可以将转换后的数据存储在一个文件或者内存中，同时也可以通过网络传输到另一个计算机环境，采取相反方式重构得到原数据。
 *
 * 请设计一个算法来实现二叉树的序列化与反序列化。这里不限定你的序列 / 反序列化算法执行逻辑，你只需要保证一个二叉树可以被序列化为一个字符串并且将这个字符串反序列化为原始的树结构。
 *
 * 你可以将以下二叉树：
 *
 *     1
 *    / \
 *   2   3
 *      / \
 *     4   5
 * 序列化为 "[1,2,3,null,null,4,5]"
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/serialize-and-deserialize-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author wangjia
 * @version 1.0
 * @date 2020/6/16 8:45
 */
public class Solution297 {

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        treeNode.left = new TreeNode(2);
        treeNode.right = new TreeNode(3);
        treeNode.right.left = new TreeNode(4);
        treeNode.right.right = new TreeNode(5);

        Solution297 solution = new Solution297();

        String serialize = solution.serialize(treeNode);
        System.out.println("序列化之后:"+serialize);

        TreeNode deserialize = solution.deserialize(serialize);
        System.out.println("反序列化之后:"+serialize);
    }


    public String serialize(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        foreachTreeNode(root,list);
        System.out.println(list);
        return list.toString();
    }

    private void foreachTreeNode(TreeNode root, List<Integer> list) {
        list.add(root == null ? null : root.val);
        if(root == null){
            return ;
        }
        foreachTreeNode(root.left,list);
        foreachTreeNode(root.right,list);
    }

    public TreeNode deserialize(String data) {
        return null;
    }
}
