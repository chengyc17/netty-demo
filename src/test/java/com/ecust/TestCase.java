package com.ecust;

import java.util.concurrent.TimeUnit;

/**
 * @Author cheng_ye
 * @create 2021/6/22 13:58
 */
public class TestCase {
    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            threadLocal.set(1);
            System.out.println(threadLocal.get());
        }).start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            System.out.println(threadLocal.get());
        }).start();
    }
}
