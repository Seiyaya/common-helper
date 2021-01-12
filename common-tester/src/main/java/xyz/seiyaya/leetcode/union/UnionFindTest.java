package xyz.seiyaya.leetcode.union;

import java.util.Random;

/**
 * 并查集相关测试
 * @author wangjia
 * @version v1.0
 * @date 2021/1/11 9:50
 */
public class UnionFindTest {

    public static void main(String[] args) {
        int n = 100000;
        int size = 100000;

        // 测试并查集耗时
        UnionFind arrayUnionFund = new ArrayUnionFind(size);
        double arrayResult = testUnionFind(arrayUnionFund,n);

        System.out.printf("数组并查集:%s ms",arrayResult);
        System.out.println();

        UnionFind arrayParentUnionFind = new ArrayParentUnionFind(size);
        double arrayParentResult = testUnionFind(arrayParentUnionFind,n);
        System.out.printf("直接指向根结点:%s ms",arrayParentResult);
        System.out.println();

        UnionFind arrayParentSizeUnionFind = new ArrayParentSizeUnionFind(size);
        double arrayParentSizeResult = testUnionFind(arrayParentSizeUnionFind,n);
        System.out.printf("直接指向根结点,按照元素个数合并:%s ms",arrayParentSizeResult);
        System.out.println();

        UnionFind arrayParentRankUnionFind = new ArrayParentRankUnionFind(size);
        double arrayParentRankResult = testUnionFind(arrayParentRankUnionFind,n);
        System.out.printf("直接指向根结点,按照集合高度合并:%s ms",arrayParentRankResult);
        System.out.println();

        UnionFind arrayParentPathUnionFind = new ArrayParentPathUnionFind(size);
        double arrayParentPathResult = testUnionFind(arrayParentPathUnionFind,n);
        System.out.printf("直接指向根结点,路径压缩，按照集合高度合并:%s ms",arrayParentPathResult);
        System.out.println();

        UnionFind arrayParentPathRankUnionFind = new ArrayParentPathRankUnionFind(size);
        double arrayParentPathRankResult = testUnionFind(arrayParentPathRankUnionFind,n);
        System.out.printf("直接指向根结点,路径压缩，按照集合高度合并:%s ms",arrayParentPathRankResult);
        System.out.println();
    }

    /**
     * 测试并查集
     * @param n
     * @return
     */
    private static float testUnionFind(UnionFind unionFind, int n) {
        int size = unionFind.getSize();
        Random random = new Random();

        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            int a = random.nextInt(size);
            int b = random.nextInt(size);
            unionFind.unionElements(a,b);
        }

        long end = System.currentTimeMillis();

        return (end - start);
    }
}
