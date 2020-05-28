package xyz.seiyaya.leetcode;

import java.util.Stack;

/**
 * 给定一个经过编码的字符串，返回它解码后的字符串。
 * 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
 * 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
 * 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
 *
 * @author wangjia
 * @date 2020/5/28 8:40
 */
public class Solution394 {

    public static void main(String[] args) {
        String result = new Solution394().decodeString("3[a2[c]]");
        System.out.println(result);
    }

    public String decodeString(String s) {
        Stack<String> stack = new Stack<>();
        for(int i=0;i<s.length();i++){
            if(s.charAt(i) == ']'){
                // 出栈直到匹配到另外一个[
                StringBuilder repeatString = new StringBuilder();
                while(!stack.peek().equals("[")){
                    repeatString.append(stack.pop());
                }
                // 出栈另外一边的[
                stack.pop();
                StringBuilder repeatNum = new StringBuilder();
                while(!stack.isEmpty() && (stack.peek().charAt(0)>='0' && stack.peek().charAt(0) <='9')){
                    repeatNum.append(stack.pop());
                }
                int repeatNums = Integer.parseInt(repeatNum.reverse().toString());
                String repeat = repeatString.toString();
                repeatString = new StringBuilder();
                for(int j=0;j<repeatNums;j++){
                    repeatString.append(repeat);
                }
                stack.push(repeatString.toString());
            }else{
                stack.push(s.charAt(i)+"");
            }
        }

        StringBuilder result = new StringBuilder();
        while(!stack.isEmpty()){
            result.append(stack.pop());
        }
        return result.reverse().toString();
    }

}
