package xyz.seiyaya.leetcode.union;

/**
 * 利用数组进行并查集
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 10:28
 */
public class ArrayUnionFind implements UnionFind {

    /**
     * 存储当前位置元素的id
     */
    private int[] ids;

    public ArrayUnionFind(int size){
        ids = new int[size];
        for(int i=0;i<size;i++){
            ids[i] = i;
        }
    }

    @Override
    public int getSize() {
        return ids.length;
    }

    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * 查询p所对应的元素编号
     * @param p
     * @return
     */
    private int  find(int p) {
        if(p < 0 || p > ids.length){
            throw new IllegalArgumentException("p is out of bound.");
        }
        return ids[p];
    }

    @Override
    public void unionElements(int p, int q) {
        int pId = find(p);
        int qId = find(q);
        // p集合和q集合的父类是同一个
        if(pId == qId){
            return ;
        }

        // 非同一个进行合并，这里处理的是将p的父节点指向q
        for(int i=0;i< ids.length;i++){
            if(ids[i] == pId){
                ids[i] = qId;
            }
        }
    }
}
