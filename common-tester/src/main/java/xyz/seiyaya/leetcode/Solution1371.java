package xyz.seiyaya.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 给你一个字符串 s ，请你返回满足以下条件的最长子字符串的长度：每个元音字母，即 'a'，'e'，'i'，'o'，'u' ，在子字符串中都恰好出现了偶数次。
 * sum-k  求max(sum所在的索引号 - k所在的索引号 + 1 )
 * 奇数-奇数=偶数  偶数-偶数=偶数  ==>  使用异或来计算奇偶性
 *
 * @author wangjia
 * @date 2020/5/20 8:52
 */
public class Solution1371 {

    public static void main(String[] args) {
        // 13
        int eleetminicoworoep = new Solution1371().findTheLongestSubstring2("eleetminicoworoep");
        System.out.println(eleetminicoworoep);
    }

    private int findTheLongestSubstring2(String s) {
        Map<Integer, Integer> map = new HashMap<>();
        char[] ss = s.toCharArray();
        int len = ss.length;
        int max = 0;
        int state = 0;
        int key = 0;
        map.put(0, 0);
        for (int i = 0; i < len; ++i) {
            if (ss[i] == 'a') {
                key = 1;
            } else if (ss[i] == 'e') {
                key = 1 << 1;
            } else if (ss[i] == 'i') {
                key = 1 << 2;
            } else if (ss[i] == 'o') {
                key = 1 << 3;
            } else if (ss[i] == 'u') {
                key = 1 << 4;
            } else {
                key = 0;
            }
            state = state ^ key;
            Integer temp = map.get(state);
            if (temp == null) {
                map.put(state, i);
            } else {
                if (state == 0) {
                    max = Math.max(max, (i - temp + 1));
                } else {
                    max = Math.max(max, (i - temp));
                }
            }
        }

        return max;
    }

    public int findTheLongestSubstring(String s) {
        char[] chars = s.toCharArray();
        HashMap<Integer, Integer> listA = new HashMap<>();
        HashMap<Integer, Integer> listE = new HashMap<>();
        HashMap<Integer, Integer> listI = new HashMap<>();
        HashMap<Integer, Integer> listO = new HashMap<>();
        HashMap<Integer, Integer> listU = new HashMap<>();
        int countA = 0;
        int countE = 0;
        int countI = 0;
        int countO = 0;
        int countU = 0;
        for (int i = 0; i < chars.length; i++) {
            listA.put(i, countA);
            listE.put(i, countE);
            listI.put(i, countI);
            listO.put(i, countO);
            listU.put(i, countU);
            switch (chars[i]) {
                case 'a':
                    countA++;
                    listA.put(i, countA);
                    break;
                case 'e':
                    countE++;
                    listE.put(i, countE);
                    break;
                case 'i':
                    countI++;
                    listI.put(i, countI);
                    break;
                case 'o':
                    countO++;
                    listO.put(i, countO);
                    break;
                case 'u':
                    countU++;
                    listU.put(i, countU);
                    break;
                default:
            }
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            for (int j = i; j < chars.length; j++) {
                if (getNum(listA, i, j) &&
                        getNum(listE, i, j) &&
                        getNum(listI, i, j) &&
                        getNum(listO, i, j) &&
                        getNum(listU, i, j)) {
                    list.add(j - i + 1);
                }
            }
        }
        int count = Integer.MIN_VALUE;
        for (Integer item : list) {
            count = Math.max(item, count);
        }
        return count == Integer.MIN_VALUE ? 0 : count;
    }

    public boolean getNum(Map<Integer, Integer> maps, int start, int end) {
        return (maps.get(end) - maps.getOrDefault(start - 1, 0)) % 2 == 0;
    }
}
