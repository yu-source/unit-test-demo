package com.cntest.su.demo.service.impl;

import com.cntest.su.demo.service.AsyncService;
import com.cntest.su.demo.utils.SpringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author arjun
 * @date 2021/03/26
 */
@Service
public class AsyncServiceImpl implements AsyncService {

    @Lazy
    @Autowired
    private AsyncServiceImpl asyncService;

    @Async("asyncTaskExecutor")
    public void task1() throws InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("task1，" + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
        Thread.sleep(10000);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task1任务异步执行耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms");
    }

    @Async
    public void task2() throws InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("task2，" + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
        Thread.sleep(5000);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task2任务异步执行耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms");
    }


    @Async
    public void task3() {
        System.out.println("task3，" + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = 100 / 0;//抛出异常

        System.out.println("task3，" + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
    }


    public void task4() throws InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("task4()，" + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
        task2();
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task4()任务异步执行耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms");
    }

    public void task5() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("task5()，" + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
//        AsyncServiceImpl proxy = SpringUtils.getBean(AsyncServiceImpl.class);
        //首字母必须小写，不然报错获取不到
        AsyncServiceImpl proxy = (AsyncServiceImpl) SpringUtils.getBean("asyncServiceImpl");
//        AopTargetUtils.getTarget(proxy);
        proxy.task2();
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task5()任务异步执行耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms");
    }

    public void task6() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("task6()，" + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
        asyncService.task2();
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task6()任务异步执行耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms");
        String simpleName = AopUtils.getTargetClass(asyncService).getSimpleName();
        System.out.println(simpleName);
        //AsyncServiceImpl
        // this 和 bean 的区别
        System.out.println("asyncService:" + asyncService.getClass().getName());
        //asyncService:com.cntest.su.demo.service.impl.AsyncServiceImpl$$EnhancerBySpringCGLIB$$91e14052
        System.out.println("this:" + this.getClass().getName());
        //this:com.cntest.su.demo.service.impl.AsyncServiceImpl
    }

    public void task7() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("task6()，" + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
        for (int i = 0; i < 100; i++) {
            asyncService.first(1 + i);
        }
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task6()任务异步执行耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms");
    }

    @Resource(name = "asyncTaskExecutor")
    private Executor asyncTaskExecutor;

    //    @Async("asyncTaskExecutor")
    @Async
    public void first(int count) throws InterruptedException {
        System.out.println("定时任务第" + count + "次开始 : " + LocalDateTime.now().toLocalTime() +
                "，线程 : " + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());

        Thread.sleep(1000 * 10);
    }


    /**
     * 通过线程池进行任务处理，查看线程池中任务的执行状态.
     *
     * @throws InterruptedException
     */
    public void testCount() throws InterruptedException {

        ExecutorService es = new ThreadPoolExecutor(50, 100, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(
                        900
                ), new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 1000; i++) {
            es.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }

        while (true) {

            // 通过ThreadPoolExecutor的相关API实时获取线程数量，排队任务数量，执行完成线程数量等信息。
            ThreadPoolExecutor tpe = ((ThreadPoolExecutor) es);
            int queueSize = tpe.getQueue().size();
            System.out.println("当前排队线程数：" + queueSize);

            int activeCount = tpe.getActiveCount();
            System.out.println("当前活动线程数：" + activeCount);

            long completedTaskCount = tpe.getCompletedTaskCount();
            System.out.println("执行完成线程数：" + completedTaskCount);

            long taskCount = tpe.getTaskCount();
            System.out.println("总线程数：" + taskCount);
            Thread.sleep(3000);
        }
    }

}
