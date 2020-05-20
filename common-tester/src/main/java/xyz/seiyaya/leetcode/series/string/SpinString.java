package xyz.seiyaya.leetcode.series.string;

/**
 * 旋转字符串
 * @author wangjia
 * @date 2020/5/20 17:14
 */
public class SpinString {

    public static void main(String[] args) {
        String result = new SpinString().spinString1("abcdef",2);

        System.out.println(result);
    }

    /**
     * 直接旋转: 时间复杂度为O(m n)，空间复杂度为O(1)
     * 旋转字符串
     * @param str
     */
    private String spinString(String str,int index) {
        int count = index;
        char[] chars = str.toCharArray();
        while( count > 0 ){
            char item = chars[0];
            for(int i=1;i<chars.length;i++){
                chars[i-1] = chars[i];
            }
            chars[chars.length-1] = item;
            count--;
        }
        return new String(chars);
    }

    /**
     * 将字符串分成X和Y两部分,反转函数fun. fun(fun(X)fun(Y)) = 旋转后的结果
     * 旋转字符串
     * @param str
     */
    private String spinString1(String str,int index) {
        char[] chars = str.toCharArray();
        flipCharArray(chars,0,index-1);
        flipCharArray(chars,index,chars.length-1);
        flipCharArray(chars,0,chars.length-1);
        return new String(chars);
    }

    private void flipCharArray(char[] chars,int start ,int end){
        for(int i=start,j=end;i<j;i++,j--){
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
    }
}
