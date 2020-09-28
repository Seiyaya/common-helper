package xyz.seiyaya.leetcode;

import xyz.seiyaya.leetcode.common.TreeNode;

import java.util.*;

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
@SuppressWarnings("all")
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
        System.out.println("反序列化之后:");
        deserialize.foreachTreeNode();
    }


    public String serialize(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        foreachTreeNode(root,list);
        System.out.println(list);
        return list.toString();
    }

    private void foreachTreeNode(TreeNode root, List<Integer> list) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            int size = queue.size();
            for(int i=0;i<size;i++){
                TreeNode poll = queue.poll();
                list.add(poll == null ? null : poll.val);
                if(poll != null){
                    if (poll.left == null) {
                        queue.add(null);
                    } else {
                        queue.add(poll.left);
                    }
                    if (poll.right == null) {
                        queue.add(null);
                    } else {
                        queue.add(poll.right);
                    }
                }
            }
        }
        for(int i=list.size()-1;i>=0;i--){
            if(list.get(i) == null){
                list.remove(i);
            }else{
                break;
            }
        }
    }

    public TreeNode deserialize(String data) {
        data = data.substring(1, data.length() - 1);
        String[] split = data.split(",");
        List<String> nodeList = new LinkedList<>(Arrays.asList(split));
        if("null".equals(split[0])){
            return null;
        }
        TreeNode root = new TreeNode(Integer.valueOf(nodeList.get(0)));
        nodeList.remove(0);

        return new TreeNode(1);
    }
}
