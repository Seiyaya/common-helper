package xyz.seiyaya.collection.data;

/**
 * 二叉查找树
 * @author wangjia
 * @version 1.0
 * @date 2019/11/1 10:41
 */
public class BinarySearchTree<T extends Comparable<? super T>> {

    private BSTNode<T> root;

    public BinarySearchTree(){

    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(1);
        tree.insert(3);
        tree.insert(2);
    }

    public void insert(T t){
        BSTNode<T> node  = new BSTNode<T>(t,null,null,null);
        BSTNode<T> insertRoot = root;
        //保存父节点
        BSTNode<T> insertNode = null;

        while(insertRoot!=null){
            insertNode = insertRoot;
            int result = node.key.compareTo(insertNode.key);
            insertRoot = result < 0 ? insertRoot.left : insertRoot.right;
        }

        if(insertNode == null){
            this.root = node;
        }else{
            node.parent = insertNode;
            int result = node.key.compareTo(insertNode.key);
            if(result < 0){
                insertNode.left = node;
            }else{
                insertNode.right = node;
            }
        }

    }

    public void delete(T key){
        BSTNode<T> search = search(root, key);
        if(search!=null){
            delete(search);
        }
        search = null;
    }

    /**
     * 删除节点
     * @param search
     */
    private void delete(BSTNode<T> search) {
        if(search.left != null && search.right != null){

        }
    }

    public BSTNode<T> search(BSTNode<T> x,T key){
        if(x == null){
            return null;
        }
        int result = key.compareTo(x.key);
        if(result < 0){
            //左支树遍历
            return search(x.left,key);
        }else if(result > 0){
            return search(x.right,key);
        }
        return x;
    }

    public BSTNode<T> search(T key){
        return search(root,key);
    }

    class BSTNode<T>{
        T key;
        BSTNode<T> parent;
        BSTNode<T> left;
        BSTNode<T> right;

        BSTNode(T key, BSTNode<T> parent, BSTNode<T> left, BSTNode<T> right){
            this.key = key;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
    }
}
