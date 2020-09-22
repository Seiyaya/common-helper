package xyz.seiyaya.leetcode;

import java.util.Arrays;

/**
 * 在本问题中，有根树指满足以下条件的有向图。该树只有一个根节点，所有其他节点都是该根节点的后继。每一个节点只有一个父节点，除了根节点没有父节点。
 * <p>
 * 输入一个有向图，该图由一个有着N个节点 (节点值不重复1, 2, ..., N) 的树及一条附加的边构成。附加的边的两个顶点包含在1到N中间，这条附加的边不属于树中已存在的边。
 * <p>
 * 结果图是一个以边组成的二维数组。 每一个边 的元素是一对 [u, v]，用以表示有向图中连接顶点 u 和顶点 v 的边，其中 u 是 v 的一个父节点。
 * <p>
 * 返回一条能删除的边，使得剩下的图是有N个节点的有根树。若有多个答案，返回最后出现在给定二维数组的答案。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/redundant-connection-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author wangjia
 * @version 1.0
 * @date 2020/9/17 9:11
 */
public class Solution685 {

    public static void main(String[] args) {
        int[] result = new Solution685().findRedundantDirectedConnection(new int[][]{
                {1, 2}, {1, 3}, {2, 3}
        });

        System.out.println(Arrays.toString(result));
    }

    static class UnionFind {
        int[] parent;
        int[] rank;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < parent.length; i++) {
                parent[i] = i;
            }
        }

        public boolean union(int a, int b) {
            int pa = find(a);
            int pb = find(b);
            if (pa == pb){
                return false;
            }
            if (rank[pa] < rank[pb]) {
                parent[pa] = pb;
            } else {
                parent[pb] = pa;
                if (rank[pa] == rank[pb]) {
                    rank[pa]++;
                }
            }
            return true;
        }

        int find(int a) {
            while (a != parent[a]) {
                parent[a] = parent[parent[a]];
                a = parent[a];
            }
            return a;
        }
    }

    private final int MAX_EDGE_NUM = 1000;

    /**
     * @author li
     * @version 1.0
     * @date 2019-05-09 10:03
     * @link https://leetcode-cn.com/problems/redundant-connection-ii/comments/83972
     *
     * 有向图带来了一个变化
     * 就是形成的环有可能是环上每个边remove都可以形成valid tree,
     * 也可能环上有一个奇葩node有两个爹。
     * 因而
     * 1：如果没有这种奇葩node，那同志们就按照上一题来做就好了，一模一样
     * 2：如果出现了这种奇葩node，那结果一定在这个奇葩node跟他爹们的两条边之中
     * 再细分为两种情况：当这个奇葩node出现的时候，他和他两个爹形成的两条边能构成两种情况
     * 2.1. 两个边都在这个环上，根据题意选在数组中后出现的边。
     * 2.2. 只有一个边在这个环上，那就选在环上的这条边
     **/
    public int[] findRedundantDirectedConnection(int[][] edges) {
        if (edges == null || edges.length == 0) {
            return null;
        }
        int[] c1 = new int[2];
        int[] c2 = new int[2];

        int[] parent = new int[edges.length + 1];
        for (int[] edge : edges) {
            if (parent[edge[1]] != 0) {
                c1 = new int[]{parent[edge[1]], edge[1]};
                c2 = new int[]{edge[0], edge[1]};
                break;
            }
            parent[edge[1]] = edge[0];
        }
        UnionFind uf = new UnionFind(edges.length + 1);
        for (int[] e : edges) {
            if ((e[0] == c1[0] && e[1] == c1[1]) || (e[0] == c2[0] && e[1] == c2[1])) {
                continue;
            }
            if (!uf.union(e[0], e[1])) {
                return e;
            }
        }
        if (!uf.union(c1[0], c1[1])) {
            return c1;
        }
        return c2;
    }
}
