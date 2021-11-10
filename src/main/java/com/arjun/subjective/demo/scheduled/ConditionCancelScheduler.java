package com.arjun.subjective.demo.scheduled;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 有条件地终止 ScheduledExecutorService 中运行的定时任务.
 *
 * @author arjun
 */
public class ConditionCancelScheduler {

    /**
     * Java 程序员都知道我们可以用 ScheduledExecutorService
     * 按照一定的间隔或频率执行任务，但这个任务一旦开始，就只能到 ThreadPool shutdown 了才能结束。
     * 如何按照一定条件，在不终止整个线程池的情况下结束任务。
     */

//    public static void main(String[] args) throws Exception {
//
//        long l = System.currentTimeMillis();
//
//        System.out.println("开始执行：" + l + "ms");
//
//        final String jobId = "my_job_1";
//
//        final AtomicInteger count = new AtomicInteger(0);
//
//        final Map<String, Future> futures = new HashMap<>();
//
//        // 指定计数个数
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//
//        Future future = scheduler.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//
//                if (count.get() > 10) {
//
//                    Future future = futures.get(jobId);
//                    if (future != null) {
//                        future.cancel(true);
//                    }
//                    // 个数减一
//                    countDownLatch.countDown();
//
//                    System.out.println("关掉任务！");
//                } else {
//                    System.out.println("第" + count.getAndIncrement() + "次执行任务！");
//                }
//            }
//        }, 0, 1, TimeUnit.SECONDS);
//
//        futures.put(jobId, future);
//
//        countDownLatch.await();
//
//        scheduler.shutdown();
//
//        System.out.println("执行结束：" + (System.currentTimeMillis() - l) + "ms");
//    }
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        try {
            // 按顺序执行，多个任务不能并行
            for (int i = 1; i <= 2; i++) {
                System.out.println("循环：" + i);
                task(scheduledExecutorService, i);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void task(ScheduledExecutorService scheduledExecutorService, Integer num) throws ExecutionException, InterruptedException {
        System.out.println("执行任务！");
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {
                // 按顺序执行，多个任务不能并行
                for (int i = 0; i < 2; i++) {
                    task1(num);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, scheduledExecutorService);
        completableFuture.get();
        System.out.println("执行结束！");
    }


    public static void task1(Integer num) throws InterruptedException {
        // scheduler关掉之后将不可用
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        final String jobId = "my_job_" + num;
        final String jobId = "my_job_1";
        final AtomicInteger count = new AtomicInteger(0);
        final Map<String, Future> futures = new HashMap<>();
        // 指定计数个数
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Future future = scheduler.scheduleWithFixedDelay(() -> {
            if (count.get() > 10) {
                Future future1 = futures.get(jobId);
                if (future1 != null) {
                    future1.cancel(true);
                }
                // 个数减一
                countDownLatch.countDown();
                System.out.println("关掉任务！");
            } else {
                int i = count.get();
                System.out.println(i);
                System.out.println("第" + count.getAndIncrement() + "次执行任务！");
                int j = count.get();
                System.out.println(j);
            }
        }, 0, 1, TimeUnit.SECONDS);
        futures.put(jobId, future);
        countDownLatch.await();
        scheduler.shutdown();
    }
}