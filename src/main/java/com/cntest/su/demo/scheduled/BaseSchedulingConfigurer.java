package com.cntest.su.demo.scheduled;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 基于接口SchedulingConfigurer的动态定时任务.
 *
 * @author arjun
 * @date 2021/01/27
 */
@Configuration
@EnableScheduling
public abstract class BaseSchedulingConfigurer implements SchedulingConfigurer {

    /**
     * 定时任务周期表达式.
     */
    private String cron;

    /**
     * 重写配置定时任务的方法.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskScheduler());
        scheduledTaskRegistrar.addTriggerTask(
                //执行定时任务
                this::taskService,
                //设置触发器
                triggerContext -> {
                    //获取定时任务周期表达式
                    cron = getCron();
                    //返回执行周期
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );

    }

    @Bean
    public Executor taskScheduler() {
        //设置线程名称
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("scheduler-pool-%d").build();
        //创建线程池
        return Executors.newScheduledThreadPool(3, namedThreadFactory);
    }

    /**
     * 执行定时任务
     */
    public abstract void taskService();

    /**
     * 获取定时任务周期表达式
     */
    public abstract String getCron();

}