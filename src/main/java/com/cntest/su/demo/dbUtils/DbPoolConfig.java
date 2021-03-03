package com.cntest.su.demo.dbUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbPoolConfig {

    @Bean
    public DbPool dbPool() {
        return new DbPoolImpl(10);
    }
}