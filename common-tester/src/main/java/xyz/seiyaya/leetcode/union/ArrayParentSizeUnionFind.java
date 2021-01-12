package xyz.seiyaya.leetcode.union;

/**
 * 基于size优化的并查集
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 15:14
 */
public class ArrayParentSizeUnionFind implements UnionFind {

    /**
     * 存储当前位置的根结点
     */
    private int[] parent;

    /**
     * 表示当前根结点的集合中的个数
     */
    private int[] quantity;

    public ArrayParentSizeUnionFind(int size){
        parent = new int[size];
        quantity = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            quantity[i] = 1;
        }
    }

    @Override
    public int getSize() {
        return parent.length;
    }

    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    private int find(int p) {
        if(p < 0 || p > parent.length){
            throw new IllegalArgumentException("p is out of bound.");
        }
        while( p != parent[p]){
            p = parent[p];
        }
        return  p;
    }

    @Override
    public void unionElements(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);
        if(pRoot == qRoot){
            return ;
        }
        //根据以当前元素为根节点的集合中元素个数的大小来合并
        //将以当前元素为根节点的集合中元素个数小的合并到大的集合上面
        if(quantity[pRoot] < quantity[qRoot]){
            parent[pRoot] = qRoot;
            quantity[qRoot] += quantity[pRoot];
        }else{
            parent[qRoot] = pRoot;
            quantity[pRoot] += quantity[qRoot];
        }
    }
}
