package com.arjun.subjective.demo.controller;

import com.arjun.subjective.demo.BaseTest;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Description： controller test demo
 * Author: zhangm@seaskylight.com
 * Date: 2018/3/7 11:34
 */
public class DemoControllerTest extends BaseTest {

    //@Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        //MockMvcBuilders使用构建MockMvc对象   （项目拦截器有效）
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        //单个类  拦截器无效
        // mockMvc = MockMvcBuilders.standaloneSteup(userController).build();

    }

    /**
     * test hi interface
     *
     * @throws Exception
     */
    @Test
    public void testHi() throws Exception {
        String name = "test";
        // 1、get查一下接口
        RequestBuilder request = get("/demo/hi").param("name", name);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("hi:" + name));
    }

    @Test
    public void helloTest() throws Exception {
        String name = "merry";
        RequestBuilder request = get("/demo/hi").param("name", name);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.equalTo("hi:merry")));
    }

}
