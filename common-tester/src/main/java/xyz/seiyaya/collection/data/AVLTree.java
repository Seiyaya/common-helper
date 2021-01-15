package xyz.seiyaya.collection.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * avl平衡二叉树
 * 难点主要还是在增删节点维护树的高度平衡上面
 * @author wangjia
 * @version 1.0
 * @date 2019/11/1 17:17
 */
public class AVLTree<K extends Comparable<K>,V> {

    private AVLNode root;

    private int size;

    public AVLTree(){
        root = null;
        size = 0;
    }

    /**
     * 获取某个节点的高度
     * @param node
     * @return
     */
    private int getHeight(AVLNode node){
        if(node == null){
            return 0;
        }

        return node.height;
    }

    /**
     * 获取某个节点的平衡因子
     * @param node
     * @return
     */
    private int getBalanceFactor(AVLNode node){
        if(node == null){
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }


    /**
     * 判断当前树是否是二叉查找树
     * @return
     */
    private boolean isBinarySearchTree(){
        List<K> keys = new ArrayList<>();
        inOrder(root,keys);
        for(int i=1;i<keys.size();i++){
            if(keys.get(i-1).compareTo(keys.get(i)) > 0){
                return false;
            }
        }
        return true;
    }

    private boolean isBalanced(){
        return isBalanced(root);
    }

    /**
     * 判断是否是平衡二叉树
     * @param root
     * @return
     */
    private boolean isBalanced(AVLNode root) {
        if(root == null){
            return true;
        }
        int balanceFactor = getBalanceFactor(root);
        if(Math.abs(balanceFactor) > 1){
            return false;
        }
        return isBalanced(root.left) && isBalanced(root.right);
    }

    // 对节点y进行向右旋转操作，返回旋转后新的根节点x
    //        y                              x
    //       / \                           /   \
    //      x   T4     向右旋转 (y)        z     y
    //     / \       - - - - - - - ->    / \   / \
    //    z   T3                       T1  T2 T3 T4
    //   / \
    // T1   T2
    /**
     * 右旋转操作
     * @param y
     * @return
     */
    private AVLNode rightRotate(AVLNode y){
        AVLNode x = y.left;
        AVLNode t3 = x.right;

        x.right = y;
        y.right = t3;

        y.height = Math.max(getHeight(y.left),getHeight(y.right))+1;
        x.height = Math.max(getHeight(x.left),getHeight(x.right))+1;
        return x;
    }

    // 对节点y进行向左旋转操作，返回旋转后新的根节点x
    //    y                             x
    //  /  \                          /   \
    // T1   x      向左旋转 (y)       y     z
    //     / \   - - - - - - - ->   / \   / \
    //   T2  z                     T1 T2 T3 T4
    //      / \
    //     T3 T4
    /**
     * 左旋转
     * @param y
     * @return
     */
    private AVLNode leftRotate(AVLNode y){
        AVLNode x = y.right;
        AVLNode t2 = x.left;

        x.left = y;
        y.right = t2;

        y.height = Math.max(getHeight(y.left),getHeight(y.right))+1;
        x.height = Math.max(getHeight(x.left),getHeight(x.right))+1;
        return x;
    }

    /**
     * 中序遍历
     * @param root
     * @param keys
     */
    private void inOrder(AVLNode root, List<K> keys) {
        if(root == null){
            return;
        }
        inOrder(root.left,keys);
        keys.add(root.key);
        inOrder(root.right,keys);
    }

    /**
     * 向平衡二叉树添加元素
     * @param key
     * @param value
     */
    public void add(K key,V value){
        root = add(root,key,value);
    }

    private AVLNode add(AVLNode root,K key, V value){
        if(root == null){
            size++;
            return new AVLNode(key,value);
        }

        if(key.compareTo(root.key) < 0){
            // 添加到左子树
            root.left = add(root.left,key,value);
        }else if(key.compareTo(root.key) > 0){
            // 添加到右子树
            root.right = add(root.right,key,value);
        }else{
            root.value = value;
        }

        // 维护树的高度平衡
        root.height = 1 + Math.max(getHeight(root.left),getHeight(root.right))+1;

        int balanceFactor = getBalanceFactor(root);

        // LL左孩子节点的左侧产生不平衡
        if(balanceFactor > 1 && getBalanceFactor(root.left) >= 0){
            return rightRotate(root);
        }

        // RR右孩子子节点的右侧产生不平衡
        if(balanceFactor < -1 && getBalanceFactor(root.right) >= 0){
            return leftRotate(root);
        }

        // LR左孩子节点的右侧产生不平衡
        if(balanceFactor > 1 && getBalanceFactor(root.left) < 0){
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // RL右孩子节点的左侧产生不平衡
        if(balanceFactor < -1 && getBalanceFactor(root.left) > 0){
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }

    public V minimum(){
        return minimum(root).value;
    }

    /**
     * 查找以node为根结点avl平衡二叉树的最小节点
     * @param root
     * @return
     */
    private AVLNode minimum(AVLNode root) {
        if(isEmpty()){
            throw new IllegalArgumentException("BinarySearchTree is empty !");
        }
        if(root.left == null){
            return root;
        }
        return minimum(root.left);
    }

    public V maximize(){
        return maximize(root).value;
    }

    private AVLNode maximize(AVLNode root) {
        if(isEmpty()){
            throw new IllegalArgumentException("BinarySearchTree is empty !");
        }
        if(root.right == null){
            return root;
        }
        return maximize(root.right);
    }

    /**
     * 删除平衡二叉树的最大值
     * @return
     */
    public V removeMax(){
        V max = maximize();
        removeMax(root);
        return max;
    }

    /**
     * 删除平衡二叉树的最大值
     * @param root
     */
    private AVLNode removeMax(AVLNode root) {
        if(root.right == null){
            // 右子树为空说明当前节点即为最大值
            AVLNode left = root.left;
            root.left = null;
            size--;
            return left;
        }
        root.right = removeMin(root.right);
        return root;
    }

    private V removeMin() {
        V minimum = minimum();
        removeMin(root);
        return minimum;
    }

    /**
     * 删除平衡二叉树的最小值
     * @param root
     * @return
     */
    private AVLNode removeMin(AVLNode root) {
        if(root.left == null){
            AVLNode rightNode = root.right;
            root.right = null;
            size--;
            return rightNode;
        }

        root.left = removeMin(root.left);
        return root;
    }

    public V remove(K key){
        AVLNode node = getNode(root,key);
        if(node != null){
            root = remove(root,key);
            return node.value;
        }
        return null;
    }

    private AVLNode remove(AVLNode root, K key) {
        if(root == null){
            return null;
        }

        AVLNode resultNode = null;
        if(key.compareTo(root.key) < 0){
            // 删除的节点在左子树
            root.left = remove(root.left,key);
            resultNode = root;
        }else if(key.compareTo(root.key) > 0){
            // 删除的节点在右子树
            root.right = remove(root.right,key);
            resultNode = root;
        }else{
            if(root.right == null){
                // 删除右子树为空的情况
                AVLNode leftNode = root.left;
                root.left = null;
                size--;
                resultNode = leftNode;
            }else if(root.left == null){
                // 删除左子树为空的情况
                AVLNode rightNode = root.right;
                root.right = null;
                size--;
                resultNode = rightNode;
            }else{
                // 删除左子树和右子树都不为空
                AVLNode successor = minimum(root.right);
                successor.right = remove(root.right,successor.key);
                successor.left = root.left;
                root.left = root.right = null;
                resultNode = successor;
            }
        }

        // 开始维护高度平衡
        resultNode.height = 1 + Math.max(getHeight(resultNode.left), getHeight(resultNode.right));
        //计算平衡因子
        int balanceFactor = getBalanceFactor(resultNode);
        //LL左孩子节点的左侧产生不平衡
        if(balanceFactor > 1 && getBalanceFactor(resultNode.left) >= 0){
            //右旋转操作
            return rightRotate(resultNode);
        }
        //RR右孩子节点的右侧产生不平衡
        if (balanceFactor < -1 && getBalanceFactor(resultNode.right) <= 0){
            //左旋转操作
            return leftRotate(resultNode);
        }
        //LR左孩子节点的右侧产生不平衡
        if(balanceFactor > 1 && getBalanceFactor(resultNode.left) < 0){
            resultNode.left = leftRotate(resultNode.left);
            //右旋转操作
            return rightRotate(resultNode);
        }
        //RL右孩子节点的左侧产生不平衡
        if(balanceFactor <-1 && getBalanceFactor(resultNode.right) > 0){
            resultNode.right = rightRotate(resultNode.right);
            //右旋转操作
            return leftRotate(resultNode);
        }
        return resultNode;
    }

    public boolean contains(K key){
        return getNode(root,key) != null;
    }

    public V get(K key){
        AVLNode node = getNode(root,key);
        return node != null ? node.value : null;
    }

    public void set(K key,V value){
        AVLNode node = getNode(root, key);
        if(node == null){
            throw new IllegalArgumentException("Set failed. key is not exists!");
        }
        node.value = value;
    }

    /**
     * 获取二叉查找树的节点
     * @param root
     * @param key
     * @return
     */
    private AVLNode getNode(AVLNode root, K key) {
        if(root == null){
            return null;
        }
        if(key.compareTo(root.key) == 0){
            return root;
        }else if(key.compareTo(root.key) < 0){
            return getNode(root.left,key);
        }else{
            return getNode(root.right,key);
        }
    }

    public int getSize() {
        return size;
    }

    private boolean isEmpty() {
        return size == 0;
    }


    private class AVLNode{
        private K key;
        private V value;
        private AVLNode left;
        private AVLNode right;
        private int height;

        public AVLNode(K key,V value){
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            height = 1;
        }
    }


    public static void main(String[] args) {
        ArrayList<String> words = new ArrayList<>();
        List<String> menus = Arrays.asList("apple", "banana", "water");
        for(int i=0;i<200;i++){
            words.add(menus.get((int)(Math.random()*100)%3));
        }
        AVLTree<String,Integer> wordsMap = new AVLTree<>();
        for (String word : words) {
            if(wordsMap.contains(word)){
                wordsMap.set(word,wordsMap.get(word)+1);
            }else {
                wordsMap.add(word,1);
            }
        }
        System.out.println("Total different words: " + wordsMap.getSize());
        System.out.println("Frequency of PRIDE "+wordsMap.get("pride"));
        System.out.println("Frequency of is "+wordsMap.get("is"));
        System.out.println("Frequency of of "+wordsMap.get("of"));
        System.out.println("isBinarySearchTree:"+wordsMap.isBinarySearchTree());
        System.out.println("isBalanced:"+wordsMap.isBalanced());
        for (String word : words) {
            wordsMap.remove(word);
            if(!wordsMap.isBinarySearchTree() || !wordsMap.isBalanced()){
                throw new RuntimeException("Error");
            }
        }
    }
}
