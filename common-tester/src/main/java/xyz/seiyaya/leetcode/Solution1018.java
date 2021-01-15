package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/14 9:20
 */
public class Solution1018 {

    public static void main(String[] args) {
        List<Boolean> booleans = new Solution1018().prefixesDivBy5(new int[]{1,0,0,1,0,1,0,0,1,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,0,1,0,0,0,0,1,1,0,1,0,0,0,1});

        System.out.println(booleans);
    }

    public List<Boolean> prefixesDivBy5(int[] A) {
        List<Boolean> list = new ArrayList<>(A.length);
        int sum = 0;
        for(int i=0;i<A.length;i++){
            sum = sum * 2 + A[i];
            if(sum % 5 == 0){
                sum = sum/5;
                list.add(Boolean.TRUE);
            }else{
                list.add(Boolean.FALSE);
            }
        }
        return list;
    }
}
