# 算法
## 题目列表
+ BFS
+ DFS(回溯相关算法)
+ 二分查找
    - Solution50: 实现pow(x,n)
    - Solution69: 实现根号x
+ 数组
+ 排序算法
+ HashSet
+ HashMap
+ 双指针
+ 动态规划
    - Solution221:求最大矩形面积
    - Solution197:小偷偷得最大金额
    - Solution983: 最低票价
+ 深度优先遍历
    - Solution46: 没有重复字符串的全排列
    - Solution47：有重复字符串的权排列
+ 字符串
+ 回文
    - Solution5: 判断回文串
+ 栈
    - Solution84: 数组下标长度*其中最小的元素的最大值
+ 链表
+ 树
    - Solution94: 树的中序遍历(循环与递归)
    - Solution98: 验证二叉查找树
    - Solution101: 判断一个树是否是镜像对称
    - Solution102: 树的层序遍历(自顶向下)
    - Solution103: z字形打印二叉树
    - Solution104: 二叉树的最大深度
    - Solution105: 根据前序、中序遍历得到树
    - Solution106: 根据中序、后序遍历得到树
    - Solution107: 树的层次遍历 II  (自底向上)
    - Solution108: 将有序数组转换为二叉搜索树
    - Solution112: 路径总和
    - Solution144: 二叉树的前序遍历
    - Solution145: 二叉树的后序遍历
    - Solution226: 二叉树的翻转
    - Solution235: 二叉查找树的最近公共祖先
    - Solution236: 二叉树的最近公共祖先
    - Solution257: 树的所有路径
    - Solution297: 二叉树的序列化与反序列化  
    - Solution501: 二叉搜索树的众数
    - Solution637: 二叉树层的平均值  
    - Solution655: 输出二叉树
    - <font color="#dd0000">Solution968</font>: 安装监视器
+ 数字问题
    - Solution136: 异或的使用
+ 设计
    - Solution146: 实现lru缓存

## 动态规划
动态规划的重点是找到平衡转移方程，本质上还是进行枚举出最优解，但是枚举的过程中可以简化已经做过的操作降低复杂度
```
// 凑硬币问题，coins数组硬币的面值凑出amount金额，使用的最小硬币数量
// 1.直接使用递归，递归复杂度的计算 = 子问题个数*解决子问题需要的时间
public static int coinChange(int[] coins, int amount){
    if(amount == 0) return 0;  // 硬币刚刚好凑出整数解
    if(amount < 0)  return -1; // 硬币凑不出该金额
    int result = Integer.MAX_VALUE;
    for( int coin : coins){
        tmpResult = coinChange(coins, amount-coin);
        if( result < 0) continue;
        result = Math.min(result,tmpResult);
    }
    return result == Integer.MAX_VALUE ? -1 : result;
}
//2. 使用辅助数组,利用memo字典记录amount金额需要的最少硬币
public static int coinChange(int[] coins, int amount){
    if(memo.containsKey(amount)){
        return memo.get(amount);
    }   
    if(amount == 0) return 0;  // 硬币刚刚好凑出整数解
    if(amount < 0)  return -1; // 硬币凑不出该金额
    int result = Integer.MAX_VALUE;
    for( int coin : coins){
        tmpResult = coinChange(coins, amount-coin);
        if( result < 0) continue;
        result = Math.min(result,tmpResult);
        memo.put(amount, result);
    }
    return result == Integer.MAX_VALUE ? -1 : result;
}
//3. 使用dp数组 dp[i]表示，i的金额需要的最少硬币
//动态转移方程  dp[i] = min(dp[i-icon] + 1 , dp[i])
public static int coinChangeByDp(int[] coins, int amount, int[] dp){
    for(int i=0;i<dp.length;i++){
        for(int j=0;j<coins.length;j++){
            if(i < coins[j]){
                continue;
            }
            dp[i] = Math.min(dp[i],dp[i-coin] + 1);
        }   
    }
    return dp[amount]  == amount +1 ? -1 : dp[amount];
}
```
## 题目类型
### topK问题
+ 变形快排解决( O(N) )
+ 小根堆( N * LogK )
+ 二叉搜索树 ( N * LogK )