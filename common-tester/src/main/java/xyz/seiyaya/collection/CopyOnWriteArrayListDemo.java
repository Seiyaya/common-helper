package xyz.seiyaya.collection;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/8 14:48
 */
@SuppressWarnings("all")
public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        /**
         * 写数据时先复制一份原始数据，把数据写入到新的数组，最后把新数组赋值给旧的
         * Object[] elements = getArray();
         * int len = elements.length;
         * Object[] newElements = Arrays.copyOf(elements, len + 1);
         * newElements[len] = e;
         * setArray(newElements);
         */
        list.add(3);

        Integer integer = list.get(0);
        System.out.println(integer);
    }
}
