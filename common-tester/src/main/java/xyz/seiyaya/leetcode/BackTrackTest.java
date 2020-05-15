package xyz.seiyaya.leetcode;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 回溯算法
 *
 * @author wangjia
 * @date 2020/5/14 16:21
 */
public class BackTrackTest {


    static int[] book;

    static LinkedList<LinkedList<Integer>> res = new LinkedList<>();

    /**
     * 进行全排列
     */
    @Test
    public void testAllArray() {
        int[] arr = {3, 2, 1};

        book = new int[arr.length];
        Arrays.fill(book, 0);
        int i = allArray(arr, 0);
        System.out.println(i);


        LinkedList<Integer> result = new LinkedList<>();
        backtrack(arr, result);

        res.forEach(System.out::println);
    }

    /**
     * 基于交换的全排列，总的来说是回溯算法的实现
     *
     * @param arr
     * @param count
     * @return
     */
    private int allArray(int[] arr, int count) {
        int result = 0;
        if (arr.length == count) {
            System.out.println(Arrays.toString(arr));
            return 1;
        }
        for (int i = count; i < arr.length; i++) {
            int tmp = arr[count];
            arr[count] = arr[i];
            arr[i] = tmp;
            result += allArray(arr, count + 1);
            tmp = arr[count];
            arr[count] = arr[i];
            arr[i] = tmp;
        }
        return result;
    }

    /**
     * for 选择 in 选择列表:
     *     # 做选择
     *     将该选择从选择列表移除
     *     路径.add(选择)
     *     backtrack(路径, 选择列表)
     *     # 撤销选择
     *     路径.remove(选择)
     *     将该选择再加入选择列表
     * @param nums
     * @param track
     */
    private void backtrack(int[] nums, LinkedList<Integer> track) {
        // 触发结束条件
        if (track.size() == nums.length) {
            res.add(new LinkedList<>(track));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (track.contains(nums[i])) {
                continue;
            }
            track.add(nums[i]);
            backtrack(nums, track);
            track.removeLast();
        }
    }
}
