package xyz.seiyaya.leetcode.interview;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/7/31 8:37
 */
public class Interview08 {

    public static void main(String[] args) {
        int magicIndex = new Interview08().findMagicIndex(new int[]{1, 1, 1});
        System.out.println(magicIndex);
    }

    public int findMagicIndex(int[] nums) {
        for(int i=0;i<nums.length;i++){
            if(nums[i] == i){
                return i;
            }
        }
        return -1;
    }
}
