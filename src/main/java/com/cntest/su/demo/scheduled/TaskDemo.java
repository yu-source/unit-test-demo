package com.cntest.su.demo.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;

/**
 * 定时任务.
 */
@Configuration
public class TaskDemo extends BaseSchedulingConfigurer {


    @Autowired
    StringRedisTemplate redisTemplate;
    //注入mapper
//    @SuppressWarnings("all")
//    CronMapper cronMapper;

    /**
     * 执行定时任务内容
     */
    @Override
    public void taskService() {
        Integer open = getOpen();
        if (1 == open) {
            System.out.println("定时任务demo1:"
                    + LocalDateTime.now() + "，线程名称：" + Thread.currentThread().getName()
                    + " 线程id：" + Thread.currentThread().getId());
        }
    }

    /**
     * 获取定时任务执行周期表达式
     */
    @Override
    public String getCron() {
//        QueryWrapper<Scheduled> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("name","demo1");
//        String cron = cronMapper.selectOne(queryWrapper).getCron();
//        return cron;
        return redisTemplate.opsForValue().get("cron:1");
    }

    /**
     * 获取定时任务，开关状态.
     */
    public Integer getOpen() {
//        QueryWrapper<Scheduled> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("name", "demo1");
//        Integer open = cronMapper.selectOne(queryWrapper).getOpen();
//        return open;
        return Integer.valueOf(redisTemplate.opsForValue().get("open:1"));
    }

}