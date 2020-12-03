package xyz.seiyaya.mybatis.bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MybatisConstant {

    public static final List<Integer> STATUS_LIST = Arrays.asList(1,2,3);

    public static class MybatisInnerConstant{
        public static final Integer INTEGER_0 = 0;

        public static final Integer INTEGER_1 = 0;
    }

    public static String getInParam(List<Integer> list){
        String collect = list.stream().map(Object::toString).collect(Collectors.joining(","));
        return collect;
    }

    public static void main(String[] args) {
        String inParam = getInParam(Arrays.asList(1, 2, 3));
        System.out.println(inParam);
    }
}
