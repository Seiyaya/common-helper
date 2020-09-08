package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/3 10:39
 */
public class Solution415 {

    public static void main(String[] args){
        String s = new Solution415().addStrings("1", "9");
        System.out.println(s);
    }

    public String addStrings(String num1, String num2) {
        if(num1.length() < num2.length()){
            String tmp = num1;
            num1 = num2;
            num2 = tmp;
        }

        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for(int i=num1.length()-1,j=num2.length()-1;i>=0;i--,j--){
            int add1 = j<0 ? 0 : Integer.parseInt(num2.charAt(j)+"");
            int add2 = Integer.parseInt(num1.charAt(i)+"");
            int add = add1 + add2 + (flag ? 1 : 0);
            flag = false;
            if(add >= 10){
                result.append(add-10);
                flag = true;
            }else{
                result.append(add);
            }
        }
        if(flag){
            result.append("1");
        }
        return result.reverse().toString();
    }

}
