package xyz.seiyaya.leetcode.union;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 11:09
 */
public class ArrayParentUnionFind implements UnionFind {

    /**
     * 存储当前位置元素的根节点
     */
    private int[] parent;

    public ArrayParentUnionFind(int size){
        parent = new int[size];
        for(int i=0;i<size;i++){
            parent[i] = i;
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

    /**
     * 查找p所在的根结点
     * @param p
     */
    private int find(int p) {
        if(p < 0 || p > parent.length){
            throw new IllegalArgumentException("p is out of bound.");
        }
        // 如果当前节点的父节点不是指向自己说明它所在的集合它不是作为根结点
        while( p != parent[p]){
            p = parent[p];
        }
        return p;
    }

    @Override
    public void unionElements(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);
        if(pRoot == qRoot){
            // p和q同在一个集合中
            return ;
        }
        // p和q不在同一个集合，直接把p集合指向q集合
        parent[pRoot] = qRoot;
    }
}
