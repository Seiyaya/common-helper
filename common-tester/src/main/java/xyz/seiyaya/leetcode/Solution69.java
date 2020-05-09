package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version v3.9.3
 * @date 2020/5/9 8:34
 */
public class Solution69 {

    public static void main(String[] args) {
        int i = new Solution69().mySqrt2(2147395599);

        System.out.println(i);
    }

    public int mySqrt(int x) {
        return (int) Math.sqrt(x);
    }

    public int mySqrt2(int x) {
        long start = 0;
        long end = x;
        if(x == 1){
            return x;
        }
        while(end - start > 1){
            long tmp = (start+end)/2;
            if( x < tmp * tmp){
                end = tmp;
            }else{
                start = tmp;
            }
        }
        return (int) start;
    }
}
