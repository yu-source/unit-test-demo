//package com.cntest.su.demo.scheduled;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * @author arjun
// * @date 2021/01/27
// */
//@Component
//public class TaskTest {
//
//    private static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    @Scheduled(cron = "${scheduled.cron}")
//    public void test(){
//
//        System.out.println(dateFmt.format(new Date()) + " : 执行定时任务");
//    }
//}
