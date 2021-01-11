package com.cntest.su.demo.service.impl;

import com.cntest.su.demo.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * Description： TODO
 * Author: zhangm@seaskylight.com
 * Date: 2018/3/7 14:20
 */
@Service
public class DemoServiceImpl implements DemoService {

    public String hi(String name) {
        return "hi:" + name;
    }
}
