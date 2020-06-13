package xyz.seiyaya.leetcode;

import java.util.Arrays;
import java.util.Stack;

/**
 *请根据每日 气温 列表，重新生成一个列表。对应位置的输出为：要想观测到更高的气温，至少需要等待的天数。如果气温在这之后都不会升高，请在该位置用 0 来代替。
 * 例如，给定一个列表 temperatures = [73, 74, 75, 71, 69, 72, 76, 73]，你的输出应该是 [1, 1, 4, 2, 1, 1, 0, 0]。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/daily-temperatures
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution739 {

    public static void main(String[] args) {
        int[] ints = new Solution739().dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73});
        System.out.println(Arrays.toString(ints));
    }

    public int[] dailyTemperatures(int[] T) {
        for(int i=0;i<T.length;i++){
            int dummy = T[i];
            T[i] = 0;
            for(int j=i+1;j<T.length;j++){
                if(T[j] > dummy){
                    T[i] = j-i;
                    break;
                }
            }
        }
        return T;
    }



    public int[] dailyTemperatures1(int[] T) {
        Stack<Integer> stack = new Stack<>();
        int[] res = new int[T.length];
        for(int i = 0; i < T.length; ++i){
            while(!stack.isEmpty() && T[i] > T[stack.peek()]){
                int temp = stack.pop();
                res[temp] = i - temp;
            }
            stack.push(i);
        }
        return res;
    }
}
