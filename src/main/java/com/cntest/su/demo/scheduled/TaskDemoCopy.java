package com.cntest.su.demo.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;

/**
 * 定时任务二.
 */
@Configuration
public class TaskDemoCopy extends BaseSchedulingConfigurer {

    /**
     * 注入mapper
     */
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 执行定时任务内容
     */
    @Override
    public void taskService() {
        Integer open = getOpen();
        if (1 == open) {
            System.out.println("定时任务demo2:"
                    + LocalDateTime.now() + "，线程名称：" + Thread.currentThread().getName()
                    + " 线程id：" + Thread.currentThread().getId());
        }
    }

    /**
     * 获取定时任务执行周期表达式
     */
    @Override
    public String getCron() {
        return redisTemplate.opsForValue().get("cron:2");
    }

    /**
     * 得到定时任务，开关状态.
     */
    public Integer getOpen() {
        return Integer.valueOf(redisTemplate.opsForValue().get("open:2"));
    }
}