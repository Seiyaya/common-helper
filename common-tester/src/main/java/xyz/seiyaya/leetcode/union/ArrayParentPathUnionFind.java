package xyz.seiyaya.leetcode.union;

/**
 * 基于路径压缩的优化
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 15:59
 */
@SuppressWarnings("all")
public class ArrayParentPathUnionFind implements UnionFind {

    private int[] parent;

    private int[] rank;

    @Override
    public int getSize() {
        return parent.length;
    }

    public ArrayParentPathUnionFind(int size){
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * 查找元素的根结点
     * @param p
     */
    private int find(int p) {
        if(p < 0 || p > parent.length){
            throw new IllegalArgumentException("p is out of bound.");
        }
        while( p != parent[p]){
            // 路径压缩,所有节点都指向根结点，不再需要进行循环查找根结点
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    @Override
    public void unionElements(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);
        if( pRoot == qRoot){
            return;
        }

        if(rank[pRoot] < rank[qRoot]){
            parent[pRoot] = qRoot;
        }else if(rank[pRoot] > rank[qRoot]) {
            parent[qRoot] = pRoot;
        }else {
            parent[pRoot] = qRoot;
            rank[qRoot] += 1;
        }
    }
}
