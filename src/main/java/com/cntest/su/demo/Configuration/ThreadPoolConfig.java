package com.cntest.su.demo.Configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author arjun
 * @date 2021/01/20
 */
@Configuration
public class ThreadPoolConfig {
    /**
     * ThreadFactory 为线程池创建的线程命名
     */
    private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

    private static ExecutorService pool = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(5), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    /**
     * 消费队列线程
     * <p>
     * ArrayBlockingQueue： 由数组结构组成的有界阻塞队列。
     * LinkedBlockingQueue： 由链表结构组成的有界阻塞队列。
     * PriorityBlockingQueue： 支持优先级排序的无界阻塞队列。
     * DealyQueue： 使用优先级队列实现的无界阻塞队列。
     * SynchronousQueue： 不存储元素的阻塞队列。
     * LinkedTransferQueue： 由链表结构组成的无界阻塞队列。
     * LinkedBlockingDeque： 由链表结构组成的双向阻塞队列。
     * <p>
     * CallerRunsPolicy ：    这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功。
     * AbortPolicy ：         对拒绝任务抛弃处理，并且抛出异常
     * DiscardPolicy ：       对拒绝任务直接无声抛弃，没有异常信息。
     * DiscardOldestPolicy ： 对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列。
     */
    @Bean(value = "consumerQueueThreadPool")
    public ExecutorService buildConsumerQueueThreadPool() {
        return pool;
    }
}
