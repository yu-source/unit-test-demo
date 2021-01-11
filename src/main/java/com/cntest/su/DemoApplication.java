package com.cntest.su;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description： starter
 * Author: zhangm@seaskylight.com
 * Date: 2018/3/7 11:13
 */
@EnableAutoConfiguration
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
