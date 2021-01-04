package xyz.seiyaya.jvm.exercise;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/4 16:50
 */
public class ClassLoaderTest {

    public static void main(String[] args) {
        SingleTon singleTon = SingleTon.getSingleTon();
        System.out.println("count1:"+SingleTon.count1);
        System.out.println("count2:"+SingleTon.count2);
    }


    /**
     * 结果count1 =  1   count2 = 0
     * SingleTon SINGLE_TON = new SingleTon()   count1 =  1   count2 = 1
     *  public static int count1;  count1 =  1
     *  public static int count2 = 0; count2 = 0
     */
    private static class SingleTon{
        private static final SingleTon SINGLE_TON = new SingleTon();

        public static int count1;

        public static int count2 = 0;

        public SingleTon(){
            count1++;
            count2++;
        }

        public static SingleTon getSingleTon() {
            return SINGLE_TON;
        }
    }
}
