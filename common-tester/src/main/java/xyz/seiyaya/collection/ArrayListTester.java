package xyz.seiyaya.collection;

import java.util.ArrayList;

/**
 * ArrayList实现
 * @author wangjia
 * @version 1.0
 * @date 2019/11/4 17:29
 */
public class ArrayListTester {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        System.out.println(list);

        int result = 10 + (10 >> 1);
        System.out.println(String.format("result:%s",result));
    }
}
