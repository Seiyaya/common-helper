package xyz.seiyaya.leetcode.series.string;

import org.junit.Test;

import java.util.Arrays;

/**
 * 字符串的全排列
 *
 * @author wangjia
 * @date 2020/5/21 10:58
 */
public class ArrangementString {

    @Test
    public void allArrangement() {
        String str = "abc";
        dfs(str.toCharArray(), 0);
    }

    private void dfs(char[] array, int count) {
        if (count == array.length) {
            System.out.println(Arrays.toString(array));
        }

        for (int i = count; i < array.length; i++) {
            swap(array, i, count);
            dfs(array, count + 1);
            swap(array, i, count);
        }
    }

    /**
     * 交换
     *
     * @param array
     * @param i
     * @param count
     */
    private void swap(char[] array, int i, int count) {
        if (i == count) {
            return;
        }
        char tmp = array[i];
        array[i] = array[count];
        array[count] = tmp;
    }


    /**
     * 可以重复使用元素的全排列
     */
    @Test
    public void testRepeatAllArrangement() {
        String str = "abc";
        char[] result = new char[str.length()];
        int num = repeatAllArrangement(str.toCharArray(), result, 0);
        System.out.println(num);
    }

    private int repeatAllArrangement(char[] template, char[] result, int count) {
        int resultNum = 0;
        if (count == template.length) {
            System.out.println(Arrays.toString(result));
            return 1;
        }
        for (int i = 0; i < template.length; i++) {
            result[count] = template[i];
            resultNum += repeatAllArrangement(template, result, count + 1);
            result[count] = ' ';
        }
        return resultNum;
    }


    /**
     * 如果输入abc，它的组合有a、b、c、ab、ac、bc、abc。
     */
    @Test
    public void testArrangement() {
        String str = "abc";
        int num = arrangement(str.toCharArray(), new char[str.length()], 0);
        System.out.println(num);
    }

    private int arrangement(char[] template, char[] result, int count) {
        int resultNum = 0;
        if(count == template.length){
            return 1;
        }
        for (int i = 0; i < template.length; i++) {
            result[count] = template[i];
            for(int j=0;j<result.length;j++){
                if(result[j] != ' '){
                    System.out.println(Arrays.toString(result));
                }
            }
            resultNum += arrangement(template,result,count+1);
            result[count] = ' ';
        }
        return resultNum;
    }
}
