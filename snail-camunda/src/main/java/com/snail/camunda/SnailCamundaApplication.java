package com.snail.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 流程服务模块启动程序
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SnailCamundaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SnailCamundaApplication.class, args);
    }
}