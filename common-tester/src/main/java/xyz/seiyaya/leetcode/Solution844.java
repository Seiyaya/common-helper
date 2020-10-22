package xyz.seiyaya.leetcode;

import java.util.Stack;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/19 13:56
 */
public class Solution844 {

    public static void main(String[] args) {
        boolean b = new Solution844().backspaceCompare("y#fo##f", "y#f#o##f");
        System.out.println(b);
    }

    public boolean backspaceCompare(String S, String T) {
        Stack<Character> sa = new Stack<>();
        for(int i=0;i<S.length();i++){
            if(S.charAt(i) == '#'){
                if(!sa.isEmpty()){
                    sa.pop();
                }
            }else{
                sa.push(S.charAt(i));
            }
        }

        Stack<Character> st = new Stack<>();
        for(int i=0;i<T.length();i++){
            if(T.charAt(i) == '#'){
                if(!st.isEmpty()){
                    st.pop();
                }
            }else{
                st.push(T.charAt(i));
            }
        }
        String a = sa.toString();
        String t = st.toString();
        return a.equals(t);
    }
}
