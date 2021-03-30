package com.arjun.subjective.demo.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author arjun
 * @date 2021/02/02
 */
@Data
@Component
@ConfigurationProperties(prefix = "remote-algorithm")
public class RemoteConnect {

    private String ip;

    private Integer port;

    private String username;

    private String password;

    private String path;

    private String command;

}
