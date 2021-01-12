package xyz.seiyaya.leetcode.union;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 9:51
 */
public interface UnionFind {


    /**
     * 获取并查集大小
     * @return
     */
    int getSize();

    /**
     * 判断两个集合是否相连
     * @param p
     * @param q
     * @return
     */
    boolean isConnected(int p,int q);

    /**
     * 合并两个集合
     * @param p
     * @param q
     */
    void unionElements(int p,int q);
}
