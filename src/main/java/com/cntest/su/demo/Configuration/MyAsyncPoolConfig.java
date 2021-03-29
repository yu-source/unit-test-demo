package com.cntest.su.demo.Configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author arjun
 * @date 2021/03/26
 */
@Configuration
@EnableAsync
public class MyAsyncPoolConfig implements AsyncConfigurer {
    /**
     * ThreadFactory 为线程池创建的线程命名
     */
    private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("asyncTask-pool-%d").build();

    /**
     * 获取异步线程池执行对象
     */
    @Override
    public Executor getAsyncExecutor() {
        // 使用Spring内置线程池任务对象
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 获取到服务器的cpu内核
        int i = Runtime.getRuntime().availableProcessors();
        // 核心线程数：线程池创建时候初始化的线程数
        executor.setCorePoolSize(5);
        // 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(100);
        // 缓冲队列容量：用来缓冲执行任务的队列
        executor.setQueueCapacity(512);
        // 允许线程的空闲时间(秒)：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(4);
        // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix("task-async-");
        // 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，当pool已经达到max size没有处理能力的时候， CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池参数
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return null;
        return (throwable, method, objects) -> System.out.println(
                "-- exception handler -- " + throwable + "-- method -- " + method + "-- objects -- " + objects);
    }


    /**
     * 自定义线程池
     */
    @Bean(name = "asyncTaskExecutor")
    public Executor asyncTaskExecutor() {
        //获取CPU 核心数
        int nThreads = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                nThreads,
                2 * nThreads + 5,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
        // 先行创建符合corePoolSize参数值的线程数
        threadPoolExecutor.prestartAllCoreThreads();
        return threadPoolExecutor;
    }
}
