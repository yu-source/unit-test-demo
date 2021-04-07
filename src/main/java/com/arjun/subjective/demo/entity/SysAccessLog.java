package com.arjun.subjective.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author arjun
 * @date 2021/04/02
 */
@Data
public class SysAccessLog {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户操作
     */
    private String operation;

    /**
     * 访问路径
     */
    private String path;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 执行时长(毫秒)
     */
    private Long elapsedTime;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 错误信息
     */
    private String message;

}
