package xyz.seiyaya.base;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/8/26 19:41
 */
@SuppressWarnings("all")
public class FinalHappenBefore {

    private int i;
    private int j;

    private static FinalHappenBefore finalHappenBefore;

    public FinalHappenBefore(){
        i = 1;
        j = 2;
    }

    public static void write(){
        finalHappenBefore = new FinalHappenBefore();
    }

    public static void read(){
        if(finalHappenBefore != null){
            int a = finalHappenBefore.i;
            int b = finalHappenBefore.j;

            System.out.println(String.format("a:%s --> b:%s",a,b));
        }
    }

    public static void main(String[] args) throws InterruptedException {


        /**
         * 结果是a和b未必 == i和j
         *
         * new操作的步骤
         * 1. 分出一块内存
         * 2. 在内存上初始化，此时i = 1, j = 2
         * 3. 把obj指向这块内存
         * 2,3步可能发生重排序
         *
         * final的happen-before
         * 1. i,j都要加上volatile关键字
         * 2. 或者read和write方法加上synchronized
         * 3. 为i和j加上final修饰
         */
        Thread thread1 = new Thread(() -> {
            write();
        });

        Thread thread2 = new Thread(() -> {
            read();
        });

        thread1.start();
        thread2.start();

        Thread.currentThread().join();
    }
}
