//package com.cntest.su.demo.scheduled;
//
//import com.google.common.collect.Maps;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.util.StringUtils;
//
//import java.time.LocalDateTime;
//
///**
// * 基于接口SchedulingConfigurer的动态定时任务
// */
//@Configuration //标记配置类，兼备Component的效果
////@EnableScheduling //开启定时任务
//public class SimpleScheduleConfig implements SchedulingConfigurer {
//
//
////    @Autowired
////    CronMapper cronMapper;
//
////    @Override
////    public void configureTasks(ScheduledTaskRegistrar taskRegistrar){
////        taskRegistrar.addTriggerTask(
////                //添加任务内容（Runnable）
////                () -> System.out.println("线程启动：" + LocalDateTime.now().toLocalTime()),
////                //设置执行周期（Trigger）
////                triggerContext -> {
////                    //从数据库获取执行周期
////                    String cron = cronMapper.getCron();
////                    //非空校验
////                    if(StringUtils.isEmpty(cron)){
////					  //TODO
////                    }
////                    //返回执行周期
////                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
////                }
////        );
////
////    }
//
//
//    @Autowired
//    StringRedisTemplate redisTemplate;
//
//
////    String cron = "0/5 * * * * ?";
//
////    String cron = null;
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                super.run();
////                redisTemplate.opsForValue().set("cron", "0/5 * * * * ?", 5, TimeUnit.MINUTES);
//                System.out.println("定时发送线程启动：" + LocalDateTime.now().toLocalTime());
//            }
//        };
//
//        taskRegistrar.addTriggerTask(
//                thread,
//                triggerContext -> {
//                    //从数据库获取执行周期
//                    String cron = redisTemplate.opsForValue().get("cron");
////                    String cron = cronMapper.getCron();
//                    //非空校验
//                    if (StringUtils.isEmpty(cron)) {
//                        //TODO
//                        taskRegistrar.setCronTasks(Maps.newHashMap());
//                    }
//                    //返回执行周期
//                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
//                }
//        );
//
//    }
//}
