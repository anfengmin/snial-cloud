package com.snail.sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统管理模块启动程序
 */
//@EnableDiscoveryClient
@SpringBootApplication
public class SnailSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(SnailSysApplication.class, args);
    }
}