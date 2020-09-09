package xyz.seiyaya.base;

import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/8 15:45
 */
public class ClassLoaderDemo {

//    private static Object newInstance;

    public static final int M = 1024*1024;

    public static void main(String[] args) throws Exception {
        URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{new File("D:\\wangjia\\MyCode\\common-helper\\common-tester\\target\\classes").toURI().toURL()});
        ReferenceQueue referenceQueue = new ReferenceQueue();
        WeakReference<URLClassLoader> urlClassLoaderWeakReference = new WeakReference<URLClassLoader>(urlClassLoader,referenceQueue);

        System.out.println("package week"+urlClassLoaderWeakReference);

        Class<?> aClass = Objects.requireNonNull(urlClassLoaderWeakReference.get()).loadClass("xyz.seiyaya.base.Person");
        Object newInstance = aClass.newInstance();


        try {
//            urlClassLoaderWeakReference = null;
            urlClassLoader = null;
//            newInstance = null;
            byte[] memory = new byte[6*M];
            System.out.println("申请内容3M");
            TimeUnit.SECONDS.sleep(3);
        }catch (Throwable e){
        }finally {
            System.gc();
            System.out.println(newInstance);
            System.out.println("get() : --> " + urlClassLoaderWeakReference.get());
            System.out.println(urlClassLoaderWeakReference);
            System.out.println(urlClassLoader);
            System.out.println(aClass);


            System.out.println("result");
        }


        TimeUnit.SECONDS.sleep(2);
        System.out.println("aaaaa");
    }
}
