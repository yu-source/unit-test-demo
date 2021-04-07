package com.arjun.subjective.demo.controller;

import com.arjun.subjective.demo.annotation.Syslog;
import com.arjun.subjective.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： demo controller
 * Author: zhangm@seaskylight.com
 * Date: 2018/3/7 11:13
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @Syslog("简单查询")
    @RequestMapping("/hi")
    public String hi(String name, int age) {
//        int i =  1/0;
        return demoService.hi(name);
    }

}
