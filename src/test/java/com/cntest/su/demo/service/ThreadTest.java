package com.cntest.su.demo.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 多线程 测试.
 *
 * @author arjun
 * @date 2021/01/19
 */
public class ThreadTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadTest.class);

    /**
     * ThreadFactory 为线程池创建的线程命名
     */
    private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

    static class MyCallable implements Callable<String> {
        @Override
        public String call() {
            System.out.println(Thread.currentThread().getName() + ": 使用Callable初始化一个线程");
            return "zhangsan";
        }
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": 使用runnable初始化一个线程");
        }
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": 使用thread初始化了一个线程");
        }
    }

    public void multiThreadingTest() throws ExecutionException, InterruptedException, IOException {

        /**
         * 通过线程池创建线程
         */
        long start = System.currentTimeMillis();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500));
        for (int i = 0; i < 500; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ": 自定义线程池执行任务");
            });
        }
        // 关闭线程池 - 执行后停止接受新任务，会把队列的任务执行完毕。
        threadPoolExecutor.shutdown();
        // 关闭线程池 - 也是停止接受新任务，但会中断所有的任务，将线程池状态变为 stop。
        //threadPoolExecutor.shutdownNow();
        // 会每隔一秒钟检查一次是否执行完毕（状态为 TERMINATED），当从 while 循环退出时就表明线程池已经完全终止了。
        while (!threadPoolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
            LOGGER.info("线程还在执行。。。");
        }
        long end = System.currentTimeMillis();
        LOGGER.info("一共处理了【{}】", (end - start));

        /**
         * 使用工具类来创建线程池：
         * 除了自己定义的ThreadPool之外，还可以使用开源库 apache guava等。
         * 个人推荐使用guava的ThreadFactoryBuilder() 来创建线程池：
         */
        // 线程池创建 指定属性
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 5, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100), threadFactory, new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> System.out.println("测试一下guava命名的线程：" + Thread.currentThread().getName()));
        }


        /**
         * 定时任务
         * command：执行线程
         * initialDelay：初始化延时
         * period：两次开始执行最小间隔时间
         * unit：计时单位
         */
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);

        // scheduleAtFixedRate()每次执行时间为上一次任务开始起向后推一个时间间隔，是基于固定时间间隔进行任务调度
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + ": 定时执行任务！" + new Date());
        }, 5, 10, TimeUnit.SECONDS);

        // scheduleWithFixedDelay()每次执行时间为上一次任务结束起向后推一个时间间隔，取决于每次任务执行的时间长短
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println(Thread.currentThread().getName() + ": 定时执行任务！" + new Date());
        }, 5, 10, TimeUnit.SECONDS);

        // schedule()只执行一次定时任务
        ScheduledExecutorService scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        scheduledThreadPoolExecutor.schedule(() -> {
            System.out.println(Thread.currentThread().getName() + ": 定时执行任务！");
        }, 20, TimeUnit.SECONDS);


        /**
         * ExecutorService实现多线程
         */
        // ExecutorService提供了submit()方法，传递一个Callable，或Runnable，返回Future。
        // 创建固定数目线程的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName() + ": 线程池执行任务！");
            });
        }
        // 如果Executor后台线程池还没有完成Callable的计算，这调用返回Future对象的get()方法，会阻塞直到计算完成。
        for (int i = 0; i < 10; i++) {
            Future<String> submit = executorService.submit(new MyCallable());
            System.out.println(submit.get().toString());
        }
        // 关闭线程池
        executorService.shutdown();


        /**
         * 实现Callable接口通过FutureTask包装器来创建Thread线程
         */
        // 调用返回Future对象的get()方法，从Future对象上获取任务的返回值，会阻塞直到计算完成。
        // 不管是异常还是正常，只要运行完毕了，isDone()方法结果一样是true
        for (int i = 0; i < 10; i++) {
            FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
            new Thread(futureTask).start();
            //System.out.println(futureTask.get());  // 阻塞
            while (!futureTask.isDone()) { // 轮询
                System.out.println("有结果了吗？");
            }
            System.out.println("对方同意了！");
            System.in.read();
        }


        /**
         * 实现Runnable接口方式实现多线程
         */
        // 为了启动MyThread，需要首先实例化一个Thread，并传入自己的MyRunnable实例
        for (int i = 0; i < 10; i++) {
            new Thread(new MyRunnable()).start();
        }
        // 匿名内部类
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + ": 使用runnable初始化一个线程");
                }
            }).start();
        }
        // Thread本质上也是实现了Runnable接口的一个实例(匿名内部类简化)
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ": 使用runnable匿名内部类初始化一个线程");
            }).start();
        }


        /**
         * 启动MyThread线程
         */
        for (int i = 0; i < 10; i++) {
            new MyThread().start();
        }
    }

    public static void main(String[] args) throws Exception {

    }
}
