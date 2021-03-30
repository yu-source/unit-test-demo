package com.arjun.subjective.demo.scheduled;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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

    @Bean(name = "taskExecutor")
    public ScheduledExecutorService taskScheduler() {
        //创建线程池
        return new ScheduledThreadPoolExecutor(5,
                new BasicThreadFactory.Builder().namingPattern("scheduler-pool-%d").daemon(true).build());
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