package xyz.seiyaya.base;

/**
 * 单例模式
 *
 * @author wangjia
 * @version 1.0
 * @date 2020/9/11 13:55
 */
public class SingletonPattern {

    private static class DclSingleton {
        private static DclSingleton instance;

        public static DclSingleton getDclSingleton() {
            if (instance == null) {
                synchronized (DclSingleton.class) {
                    if (instance == null) {
                        instance = new DclSingleton();
                    }
                }
            }
            return instance;
        }
    }
}
