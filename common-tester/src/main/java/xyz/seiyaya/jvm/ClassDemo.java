package xyz.seiyaya.jvm;

/**
 * @author wangjia
 * @version v1.0
 * @date 2021/1/4 16:28
 */
public class ClassDemo {

    /**
     * 启动参数设置  -XX:+TraceClassLoading
     * @param args
     */
    public static void main(String[] args) {
        /**
         * 必须加载类的情况
         * 1. 创建类的实例,通过new关键字
         * 2. 访问类的静态变量
         * 3. 访问类的静态方法
         * 4. 反射，ClassName
         * 5. 初始化一个子类(首先会初始化子类的父类)
         * 6. 虚拟机启动时，定义了main()方法的那个类
         */
//        ClassLoadingInstance classLoadingInstance = new ClassLoadingInstance();

        // 访问final修饰的静态变量时，不会触发类加载，因为在编译期已经将此常量放在常量池了。
        System.out.println(ClassLoadingInstance.ITEM);

        // 这里才会加载
        System.out.println(ClassLoadingInstance.class);
    }

    private static class ClassLoadingInstance{

        public static final String ITEM = "123";

        static{
            System.out.println("ClassLoadingInstance 类进行初始化");
        }

        public ClassLoadingInstance(){
            System.out.println("ClassLoadingInstance 对象进行初始化");
        }
    }
}
