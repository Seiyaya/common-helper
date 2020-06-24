package xyz.seiyaya.leetcode;

/**
 * 二进制求和
 * @author wangjia
 * @version 1.0
 */
public class Solution67 {

    public static void main(String[] args) {
        String s = new Solution67().addBinary("11", "1");
        System.out.println(s);
    }

    public String addBinary(String a, String b) {
        if(a.length() == 0){
            return b;
        }
        if(b.length() == 0){
            return a;
        }
        StringBuilder result = new StringBuilder();
        int aCount = a.length()-1;
        int bCount = b.length()-1;
        boolean last = false;
        while(aCount >= 0 && bCount>=0){
            int val = Integer.parseInt(a.charAt(aCount)+"")+Integer.parseInt(b.charAt(bCount)+"");
            if(last){
                val++;
                last = false;
            }
            if(val >= 2){
                val -= 2;
                last = true;
            }
            result.append(val);
            aCount--;
            bCount--;
        }
        // 拼接剩余的部分
        if(aCount < 0 && bCount < 0 && last){
            result.append(1);
        }
        if(aCount >= 0){
            calcOther(a, result, aCount, last);
        }
        if(bCount >= 0){
            calcOther(b, result, bCount, last);
        }
        return result.reverse().toString();
    }

    private void calcOther(String a, StringBuilder result, int aCount, boolean last) {
        while(aCount>=0){
            int val = Integer.parseInt(a.charAt(aCount)+"");
            if(last){
                val++;
                last = false;
            }
            if(val >=2){
                val -= 2;
                last = true;
            }
            result.append(val);
            aCount--;
        }
        if(last){
            result.append(1);
        }
    }


}
