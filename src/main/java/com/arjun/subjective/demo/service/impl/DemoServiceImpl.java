package com.arjun.subjective.demo.service.impl;

import com.arjun.subjective.demo.service.DemoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * Description： TODO
 * Author: zhangm@seaskylight.com
 * Date: 2018/3/7 14:20
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Resource(name = "consumerQueueThreadPool")
    ExecutorService consumerQueueThreadPool;

    @Override
    public String hi(String name) {
//        for (int i = 0; i < 10; i++) {
//            consumerQueueThreadPool.execute(() -> {
//                System.out.println("当前正在执行线程:" + Thread.currentThread().getName());
//            });
//        }
        return "hi:" + name;
    }

}
