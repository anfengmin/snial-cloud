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
        System.out.println("(♥◠‿◠)ﾉﾞ  认证授权中心启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}