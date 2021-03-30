package com.arjun.subjective.demo.controller;

import com.arjun.subjective.demo.service.impl.AsyncServiceImpl;
import com.arjun.subjective.demo.utils.AopTargetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author arjun
 * @date 2021/03/26
 */
@RestController
@RequestMapping("async")
public class AsyncController {

    @Autowired
    private AsyncServiceImpl asyncService;

    @RequestMapping("task")
    public String doTask() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task1()");
        this.asyncTask1();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task1()结束");
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task2()");
        this.asyncTask2();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task2()结束");
        this.asyncTask3();
        Object target = AopTargetUtils.getTarget(asyncService);
        System.out.println(target);
        long currentTimeMillis1 = System.currentTimeMillis();
        return "task任务总耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms";
    }


    @RequestMapping("task2")
    public String task2() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task1()");
        asyncService.task1();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task1()结束");
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task2()");
        asyncService.task2();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task2()结束");
        asyncService.task3();
        System.out.println(this);
        Object target = AopTargetUtils.getTarget(this);
        System.out.println(target);
        long currentTimeMillis1 = System.currentTimeMillis();
        return "task任务总耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms";
    }

    @RequestMapping("task3")
    public String task3() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task4()开始");
        asyncService.task4();
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task4()结束");
        return "task任务总耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms";
    }

    @RequestMapping("task5")
    public String task5() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task5()开始");
        asyncService.task5();
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task5()结束");
        return "task任务总耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms";
    }

    @RequestMapping("task6")
    public String task6() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task6()开始");
        asyncService.task6();
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "主线程请求异步执行task6()结束");
        return "task任务总耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms";
    }



    @Async("asyncTaskExecutor")
    public void asyncTask1() throws InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("task1，" + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
        Thread.sleep(10000);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task1任务异步执行耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms");
    }

    @Async
    public void asyncTask2() throws InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("task2，" + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
        Thread.sleep(5000);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task2任务异步执行耗时:" + (currentTimeMillis1 - currentTimeMillis) + "ms");
    }


    @Async
    public void asyncTask3() {
        System.out.println("task3，" + Thread.currentThread().getName() + "，" + new Date());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = 100 / 0;//抛出异常

        System.out.println("task3，" + Thread.currentThread().getName() + "，" + new Date());
    }


    @Async
    public void test() throws InterruptedException {
        //让线程休眠，根据输出结果判断主线程和从线程是同步还是异步
        System.out.println("进入方法，睡觉十秒");
        Thread.sleep(10000);
        System.out.println("异步threadId:" + Thread.currentThread().getId());
    }
}
