package xyz.seiyaya.leetcode.type;

import org.junit.Test;

import java.util.*;

/**
 * topK问题
 *
 * @author wangjia
 * @version 1.0
 * @date 2020/7/27 18:09
 */
public class TopKQuestion {

    /**
     * 通过变形快排类解决topk的问题
     */
    @Test
    public void topKByQuickSort() {
        int[] arr = {3, 2, 1, 4, 5, 6};
        int k = 2;
        int[] result = quickSearch(arr, 0, arr.length - 1, k - 1);
        System.out.println(Arrays.toString(result));
    }

    private int[] quickSearch(int[] arr, int left, int right, int k) {
        int j = partition(arr, left, right);
        if (j == k) {
            return Arrays.copyOf(arr, j + 1);
        }
        return j > k ? quickSearch(arr, left, j - 1, k) : quickSearch(arr, j + 1, right, k);
    }

    private int partition(int[] arr, int left, int right) {
        int v = arr[left];
        int i = left, j = right + 1;
        while (true) {
            while (++i <= right && arr[i] < v) {

            }
            while (--j >= left && arr[j] > v) {

            }
            if (i >= j) {
                break;
            }
            int t = arr[j];
            arr[j] = arr[i];
            arr[i] = t;
        }
        arr[left] = arr[j];
        arr[j] = v;
        return j;
    }

    /**
     * 通过小根堆或者大根堆解决topK问题
     */
    @Test
    public void topKByHeap() {
        /**
         * 获取arr数组中最小的k个元素，因此要维护一个大根堆，遍历的数小于堆顶的数，则这个数被加入到堆中
         */
        int[] arr = {3, 2, 1, 4, 5, 6};
        int k = 2;
        Queue<Integer> queue = new PriorityQueue<>((v1, v2) -> v2 - v1);
        for (int tmp : arr) {
            if (queue.size() < k) {
                queue.offer(tmp);
            } else if (tmp < queue.peek()) {
                queue.poll();
                queue.offer(tmp);
            }
        }

        int[] result = new int[queue.size()];
        int index = 0;
        for (int tmp : queue) {
            result[index++] = tmp;
        }

        System.out.println(Arrays.toString(result));
    }


    /**
     * 通过二叉搜索树解决topK问题
     */
    @Test
    public void testBinarySearchTree() {
        int[] arr = {3, 2, 1, 4, 5, 6};
        int k = 2;
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        // 元素的数量
        int cnt = 0;
        for (int num : arr) {
            if (cnt < k) {
                treeMap.put(num, treeMap.getOrDefault(num, 0) + 1);
                cnt++;
                continue;
            }
            Map.Entry<Integer, Integer> entry = treeMap.lastEntry();
            if (entry.getKey() > num) {
                treeMap.put(num, treeMap.getOrDefault(num, 0) + 1);
                if (entry.getValue() == 1) {
                    treeMap.pollLastEntry();
                } else {
                    treeMap.put(entry.getKey(), entry.getValue() - 1);
                }
            }
        }

        int[] result = new int[k];
        int idx = 0;
        for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
            int freq = entry.getValue();
            while (freq-- > 0) {
                result[idx++] = entry.getKey();
            }
        }
        System.out.println(Arrays.toString(result));
    }
}
