package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/5 11:08
 */
public class Solution830 {

    public static void main(String[] args) {
        List<List<Integer>> result = new Solution830().largeGroupPositions("aaa");

        System.out.println(result);
    }

    public List<List<Integer>> largeGroupPositions(String s) {
        s = s + "2";
        char[] chars = s.toCharArray();
        char lastChar = '1';
        int start = 0;
        List<List<Integer>> list = new ArrayList<>();
        for(int i=0;i<chars.length;i++){
            if(lastChar != '1' && chars[i] != lastChar){
                if(i - start >= 3){
                    list.add(Arrays.asList(start,i-1));
                }
                start = i;
            }
            lastChar = chars[i];
        }
        return list;
    }
}
