package xyz.seiyaya.leetcode;

/**
 * 爱丽丝以 0 分开始，并在她的得分少于 K 分时抽取数字。 抽取时，她从 [1, W] 的范围中随机获得一个整数作为分数进行累计，其中 W 是整数。 每次抽取都是独立的，其结果具有相同的概率。
 * 当爱丽丝获得不少于 K 分时，她就停止抽取数字。 爱丽丝的分数不超过 N 的概率是多少？
 * 0 <= K <= N <= 10000
 * 1 <= W <= 10000
 * @author wangjia
 * @version 1.0
 * @date 2020/6/3 8:47
 */
public class Solution837 {

    private int valid = 0;

    public static void main(String[] args) {
        double v = new Solution837().new21Game1(21, 17, 10);

        System.out.println(v);
    }

    public double new21Game(int N, int K, int W) {
        int[] arr = new int[W];
        for(int i=1;i<=W;i++){
            arr[i-1] = i;
        }
        int total = dfs(arr,0,K,N,0);
        return (double)valid/total;
    }

    public double new21Game1(int N, int K, int W) {
        double[] dp = new double[N+1];
        double sum = 0;
        dp[0] = 1;
        if(K > 0){
            sum += 1;
        }
        for(int i=1;i<=N;i++){
            dp[i] = sum/W;
            if( i < K){
                sum += dp[i];
            }
            if(i >= W){
                sum -= dp[i-W];
            }
        }
        double result = 0;
        for(int i=K;i<=N;i++){
            result += dp[i];
        }
        return result;
    }

    private int dfs(int[] arr, int level, int k, int n,int sum) {
        int result = 0;
        if(sum > n || level == arr.length){
            for(int i=0;i<level;i++){
                System.out.print(arr[i] + " ");
            }
            System.out.println();
            if(sum <= n){
                valid++;
            }
            return 1;
        }
        for(int i=0;i<arr.length;i++){
            swap(arr,i,level);
            sum += arr[i];
            result += dfs(arr,level+1,k,n,sum);
            sum -= arr[i];
        }
        return result;
    }

    private void swap(int[] arr, int i, int level) {
        if(i == level){
            return ;
        }
        int tmp = arr[level];
        arr[level] = arr[i];
        arr[i] = tmp;
    }
}
