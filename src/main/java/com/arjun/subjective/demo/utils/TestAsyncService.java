package com.arjun.subjective.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
//@EnableAsync
//@EnableScheduling
public class TestAsyncService {

    public void testAsync() throws Exception {
        // 方法一： 先获得代理对象,在通过代理对象调用本类的 addUserLog方法
        TestAsyncService proxy = SpringUtils.getBean(TestAsyncService.class);
        proxy.test();

    }

    @Async
    public void test() throws InterruptedException {
        //让线程休眠，根据输出结果判断主线程和从线程是同步还是异步
//        Thread.sleep(10000);
        System.out.println("异步threadId:" + Thread.currentThread().getId());
    }

//    // 配置类不能为抽象，否则无法注入
//    @Resource(name = "taskExecutor")
//    private ScheduledExecutorService taskScheduler;
//
//    @Scheduled(cron = "0/1 * * * * ? ")
//    public void task() {
//        taskScheduler.execute(() -> {
//            log.info("执行-task()，" + "异步threadId:" + Thread.currentThread().getId());
//            //模拟长时间执行，比如IO操作，http请求
//            try {
//                Thread.sleep(1000 * 10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    @Scheduled(cron = "0/1 * * * * ? ")
//    public void job() {
//        taskScheduler.execute(() -> {
//            log.info("执行-job()，" + "异步threadId:" + Thread.currentThread().getId());
//        });
//    }

    //    @Async
//    @Scheduled(cron = "0/1 * * * * ? ")
    public void task() throws InterruptedException {
        log.info("执行-task()，" + "异步threadId:" + Thread.currentThread().getId());
        Thread.sleep(1000 * 5);
    }

    //
//    @Async
//    @Scheduled(cron = "0/1 * * * * ? ")
    public void job() {
        log.info("执行-job()，" + "异步threadId:" + Thread.currentThread().getId());
    }


    //    @Async
//    @Scheduled(fixedDelay = 1000)  //间隔1秒
    public void first() throws InterruptedException {
        System.out.println("第一个定时任务开始 : " + LocalDateTime.now().toLocalTime() +
                "，线程 : " + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
        Thread.sleep(1000 * 10);
    }

    //    @Async
//    @Scheduled(fixedDelay = 2000)
    public void second() {
        System.out.println("第二个定时任务开始 : " + LocalDateTime.now().toLocalTime() +
                "，线程 : " + Thread.currentThread().getName() + "，异步threadId:" + Thread.currentThread().getId());
    }

}
