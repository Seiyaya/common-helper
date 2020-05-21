package xyz.seiyaya.leetcode.series.array;

import org.junit.Test;

import java.util.Arrays;

/**
 * 求数组中和为k的两位数
 * @author wangjia
 * @date 2020/5/21 14:11
 */
public class SumIsK {

    /**
     * arr[i]在数组中，那么必然k-arr[i]也在数组中，即先排序后利用二分查找k-arr[i]
     */
    @Test
    public void testBinarySearch(){
        int[] arr = {4,2,1,7,11,15};
        int k = 15;
        Arrays.sort(arr);

        for(int i=0;i<arr.length;i++){
            boolean result = binarySearch(arr,k-arr[i]);
            if(result){
                System.out.println("定值k的两个数:"+arr[i]+"-->" + (k-arr[i]));
                return ;
            }
        }
    }

    private boolean binarySearch(int[] arr, int i) {
        int result = Arrays.binarySearch(arr, i);
        if(result > 0){
            return true;
        }
        return false;
    }


    /**
     * 如果arr
     */
    @Test
    public void testPreNext(){

    }
}
