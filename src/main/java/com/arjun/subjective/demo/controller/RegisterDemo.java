package com.arjun.subjective.demo.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

/**
 * HttpServletRequest获取请求参数
 */
@WebServlet("/RegisterDemo")
public class RegisterDemo extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //通用方法
        //根据参数获取参数值
        String name = request.getParameter("name");

        //根据参数获取参数值的数组
        String[] hobbies = request.getParameterValues("hobby");
        for (String hobby : hobbies) {
            System.out.println(hobby);
        }

        //获取所有请求的参数名称
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name1 = parameterNames.nextElement();
            System.out.print(name1);
            String value = request.getParameter(name1);
            System.out.println(value);
        }

        //获取所有参数的map集合
        Map<String, String[]> parameterMap = request.getParameterMap();
        //遍历
        Set<String> keyset = parameterMap.keySet();
        for (String key : keyset) {
            //获取集合中的键和值
            String[] values = parameterMap.get(key);
            System.out.println(key);
            for (String value : values) {
                System.out.println(value);
            }
            System.out.println("-------");

        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

}