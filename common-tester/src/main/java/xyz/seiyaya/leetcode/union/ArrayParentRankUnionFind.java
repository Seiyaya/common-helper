package xyz.seiyaya.leetcode.union;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 15:44
 */
public class ArrayParentRankUnionFind implements UnionFind {

    /**
     * 存储当前元素的根结点
     */
    private int[] parent;

    /**
     * 表示当前元素为根结点的高度
     */
    private int[] rank;

    public ArrayParentRankUnionFind(int size){
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 1;
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
     * 查找元素
     * @param p
     */
    private int find(int p) {
        if(p < 0 || p > parent.length){
            throw new IllegalArgumentException("p is out of bound.");
        }
        while (p != parent[p]){
            p = parent[p];
        }
        return p;
    }

    @Override
    public void unionElements(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);
        if(pRoot == qRoot){
            return;
        }
        //根据以当前元素为根节点的高度来合并
        //将以当前元素为根节点的高度低的合并到高的集合上面
        if(rank[pRoot] < rank[qRoot]){
            parent[pRoot] = qRoot;
        }else if(rank[pRoot] > rank[qRoot]){
            parent[qRoot] = pRoot;
        }else{
            parent[pRoot] = qRoot;
            rank[qRoot] += 1;
        }
    }
}
