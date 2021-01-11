package com.cntest.su.demo.controller;

import com.cntest.su.demo.service.DemoService;
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

    @RequestMapping("/hi")
    public String hi(String name){
        return demoService.hi(name);
    }

}
