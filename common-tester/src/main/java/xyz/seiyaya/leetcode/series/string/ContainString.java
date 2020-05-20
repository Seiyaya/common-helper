package xyz.seiyaya.leetcode.series.string;

/**
 * 字符串包含
 * 给定两个分别由字母组成的字符串A和字符串B，字符串B的长度比字符串A短。请问，如何最快地判断字符串B中所有字母是否都在字符串A里
 * @author wangjia
 * @date 2020/5/20 17:36
 */
public class ContainString {

    public static void main(String[] args) {
        boolean result = new ContainString().containString1("ABCD","BAD");
        System.out.println(result);
    }

    /**
     * 26位分别用二进制标识，哪一个位置上有则表示字符串中函数对应字母
     * @param str1
     * @param str2
     * @return
     */
    private boolean containString(String str1, String str2) {
        int hash = 0;
        for(char item : str1.toCharArray()){
            hash = hash | (1 << (item - 'A'));
        }
        for(char item : str2.toCharArray()){
            if( (hash & (1 << (item-'A'))) == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * 缺点是容易内存溢出
     * @param str1
     * @param str2
     * @return
     */
    private boolean containString1(String str1,String str2){
        int[] p = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59,61, 67, 71, 73, 79, 83, 89, 97, 101};
        long result = 1L;
        for(char item : str1.toCharArray()){
            if(result % p[(item-'A')] != 0){
                result *= p[(item-'A')];
                System.out.println(result);
            }
        }

        for(char item : str2.toCharArray()){
            if( result%p[(item-'A')] != 0){
                return false;
            }
        }
        return true;
    }
}
