package xyz.seiyaya.leetcode;

import java.util.*;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/6 9:16
 */
public class Solution399 {

    public static void main(String[] args) {
        double[] doubles = new Solution399().calcEquation(Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("b", "c")), new double[]{2.0, 3.0},
                Arrays.asList(Arrays.asList("a", "c"), Arrays.asList("b", "a"), Arrays.asList("a", "e"), Arrays.asList("a", "a"), Arrays.asList("x", "x")));
        System.out.println(Arrays.toString(doubles));
    }

    private static class Node{
        double value;
        String str;
        public Node(double value,String str){
            this.value = value;
            this.str = str;
        }
    }

    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String,List<Node>> maps = new HashMap<>();


        for(int i=0;i<equations.size();i++){
            String left = equations.get(i).get(0);
            String right = equations.get(i).get(1);
            List<Node> leftNodes = maps.get(left);
            if(leftNodes == null){
                leftNodes = new ArrayList<>();
            }
            // left = values[i] * right
            leftNodes.add(new Node(values[i],right));
            maps.put(left,leftNodes);
            if(values[i] == 0){
                continue;
            }
            List<Node> rightNodes = maps.get(right);
            if(rightNodes == null){
                rightNodes = new ArrayList<>();
            }
            // right = left / values[i]
            rightNodes.add(new Node(1.0/values[i],left));
            maps.put(right,rightNodes);

        }
        double[] res = new double[queries.size()];
        for(int i=0;i<queries.size();i++){
            res[i] = dfs(queries.get(i).get(1), maps, queries.get(i).get(0), new HashSet<String>());
        }
        return res;
    }

    /**
     * 计算left到right的距离
     * @param right
     * @param maps
     * @param left
     * @param visited
     * @return
     */
    public static double dfs(String right,Map<String,List<Node>> maps,String left,Set<String> visited){
        if(!maps.containsKey(left) || !maps.containsKey(right)){
            // 没有记录left和right的关系
            return -1;
        }

        if(left.equals(right)){
            return 1;
        }

        if(visited.contains(left)){
            return -1;
        }
        visited.add(left);

        List<Node> nodes = maps.get(left);
        for(Node n: nodes){
            double res = dfs(right, maps, n.str, visited);
            if(res != -1.0) {
                return res * n.value;
            }
        }
        return -1;
    }
}
