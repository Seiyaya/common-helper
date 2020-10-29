package xyz.seiyaya.leetcode;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/10/21 9:04
 */
public class Solution925 {

    public static void main(String[] args) {
        boolean longPressedName = new Solution925().isLongPressedName("alex", "aaleex");
        System.out.println("result:" + longPressedName);
    }

    public boolean isLongPressedName(String name, String typed) {
        if(name.length() > typed.length()){
            return false;
        }
        int i=0,j=0;

        while(i< name.length() && j<typed.length()){
            if(name.charAt(i) == typed.charAt(j)){
                i++;
                j++;
            }else if(j>0 && typed.charAt(j) == typed.charAt(j-1)){
                j++;
            }else{
                return false;
            }
        }
        if(j<typed.length()) {
            char c=name.charAt(name.length()-1);
            while (j<typed.length()) {
                if(c==typed.charAt(j)){
                    j++;
                } else{
                    return false;
                }
            }
        }
        return i == name.length();
    }
}
