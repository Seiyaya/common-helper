package xyz.seiyaya.collection.data;

/**
 * @author wangjia
 * @version 1.0
 * @date 2019/11/1 17:17
 */
public class RedBlackTree<K extends Comparable<K>,V> {

    public static final boolean RED = true;

    public static final boolean BLACK = false;

    private RBNode root;

    private int size;

    public RedBlackTree(){
        root = null;
        size = 0;
    }


    //   node                     x
    //  /   \     左旋转         /  \
    // T1   x   --------->   node   T3
    //     / \              /   \
    //    T2 T3            T1   T2
    /**
     * 左旋转
     * @param node
     * @return
     */
    private RBNode leftRotate(RBNode node){
        RBNode x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }

    /**
     * 颜色翻转
     * @param node
     */
    private void flipColors(RBNode node){
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }


    //     node                   x
    //    /   \     右旋转       /  \
    //   x    T2   ------->   y   node
    //  / \                       /  \
    // y  T1                     T1  T2
    /**
     * 右旋转
     * @param node
     * @return
     */
    private RBNode rightRotate(RBNode node){
        RBNode x = node.left;
        //右旋转操作
        node.left = x.right;
        x.right = node;

        x.color = node.color;
        node.color = RED;
        return x;
    }


    public boolean isRed(RBNode node){
        if(node == null){
            return BLACK;
        }
        return node.color;
    }

    /**
     * 向红黑树中添加元素
     * @param key
     * @param value
     */
    public void add(K key,V value){
        root = add(root,key,value);
        // 根结点为黑色
        root.color = BLACK;
    }

    private RBNode add(RBNode node, K key, V value) {
        if(node == null){
            size++;
            return new RBNode(key,value);
        }

        if(key.compareTo(node.key) < 0 ){
            node.left = add(node.left,key,value);
        }else if(key.compareTo(node.key) > 0){
            node.right = add(node.right,key,value);
        }else{
            node.value = value;
        }


        // 维护红黑树的性质

        // 判断是否需要左旋转
        if(isRed(node.right) && !isRed(node.left)){
            node = leftRotate(node);
        }

        // 判断是否需要右旋转
        if(isRed(node.left) && isRed(node.left.left)){
            node = rightRotate(node);
        }

        // 判断是否需要颜色翻转
        if(isRed(node.left) && isRed(node.right)){
            flipColors(node);
        }
        return node;
    }

    /**
     * 查找红黑树的最小值
     * @return
     */
    public V minimum(){
        return minimum(root).value;
    }

    /**
     * 查找红黑树上的最小值
     * @param root
     * @return
     */
    private RBNode minimum(RBNode root) {
        if(isEmpty()){
            throw new IllegalArgumentException("BinarySearchTree is empty !");
        }
        if(root.left == null){
            return root;
        }
        return minimum(root.left);
    }

    /**
     * 查找红黑树的最大值
     * @return
     */
    public V maximize(){
        return maximize(root).value;
    }

    private RBNode maximize(RBNode root) {
        if(isEmpty()){
            throw new IllegalArgumentException("BinarySearchTree is empty !");
        }
        if(root.right == null){
            return root;
        }
        return maximize(root.right);
    }

    /**
     * 删除红黑树的最大值
     * @return
     */
    public V removeMax(){
        V maximize = maximize();
        removeMax(root);
        return maximize;
    }

    /**
     * 删除红黑树的最大值
     * @param root
     */
    private RBNode removeMax(RBNode root) {
        if(root.right == null){
            // 当前节点即为最大值
            RBNode leftNode = root.left;
            root.left = null;
            size--;
            return leftNode;
        }
        root.right = removeMax(root.right);
        return root;
    }

    /**
     * 删除红黑树的最小值
     * @param root
     */
    public RBNode removeMin(RBNode root){
        if(root.left == null){
            RBNode rightNode = root.right;
            root.right = null;
            size--;
            return rightNode;
        }
        root.left = removeMin(root.left);
        return root;
    }

    /**
     * 删除指定元素的key
     * @param key
     * @return
     */
    public V removeKey(K key){
        RBNode node = getNode(root,key);
        if(node != null){
            root = remove(root,key);
            return node.value;
        }
        return null;
    }

    private RBNode remove(RBNode root, K key) {
        if(root == null){
            return null;
        }

        if(key.compareTo(root.key) < 0){
            root.left = remove(root.left,key);
            return root;
        }else if(key.compareTo(root.key) > 0){
            root.right = remove(root.right,key);
            return root;
        }else{
            if(root.right == null){
                // 删除右子节点为空
                RBNode leftNode = root.left;
                root.left = null;
                size--;
                return leftNode;
            }else if(root.left == null){
                // 删除左子树为空
                RBNode rightNode = root.right;
                root.right = null;
                size--;
                return rightNode;
            }else{
                // 左右子树均不为空
                RBNode successor = minimum(root.right);
                // 删除后继节点，并让待删除节点的右子树称为后继节点的右子树
                successor.right = removeMin(root);
                // 待删除节点的左子树称为后继节点的左子树
                successor.left = root.left;
                // 将待删除的节点左右子树都置为空
                root.left = root.right = null;
                return successor;
            }
        }
    }

    public boolean contains(K key){
        return getNode(root,key) != null;
    }

    public void set(K key,V value){
        RBNode node = getNode(root,key);
        if(node == null){
            throw new IllegalArgumentException("Set failed. key is not exists!");
        }
        node.value = value;
    }

    public int getSize() {
        return size;
    }

    private RBNode getNode(RBNode root, K key) {
        if(root == null){
            return null;
        }
        if(key.compareTo(root.key) < 0){
            // 左子树遍历
            return getNode(root.left,key);
        }else if(key.compareTo(root.key) > 0){
            // 右子树遍历
            return getNode(root.right,key);
        }else{
            return root;
        }
    }

    public boolean isEmpty(){
        return size == 0;
    }


    /**
     * 红黑树节点
     */
    private class RBNode{

        public K key;
        public V value;

        public RBNode left;
        public RBNode right;

        public boolean color;

        public RBNode(K key,V v){
            this.key = key;
            this.value = v;
            left = null;
            right = null;
            color = RED;
        }
    }
}
