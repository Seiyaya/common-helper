package xyz.seiyaya.base;

import xyz.seiyaya.common.cache.helper.TimeHelper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/10 10:03
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        Thread thread = new Thread(() -> {
            completableFuture.complete("123");
        });
        thread.start();
        // 调用者阻塞，等待结果
        String result = completableFuture.get();
        System.out.println("first result:" + result);

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("task is running");
            TimeHelper.sleep(3);
        });

        future.get();

        System.out.println("success");


        CompletableFuture<String> supplyFuture = CompletableFuture.supplyAsync(() -> {
            TimeHelper.sleep(3);
            System.out.println("supply task is running end");
            return "end";
        });

        String s = supplyFuture.get();
        System.out.println("supply run result : "+ s);


        CompletableFuture<Void> supplyFutureWithThenRun = CompletableFuture.supplyAsync(() -> {
            TimeHelper.sleep(3);
            System.out.println("supply task is running end");
            return "end";
        }).thenRun(()->{
            System.out.println("supply task end after running");
        });

        supplyFutureWithThenRun.get();
    }
}
