package com.snail.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.snail.job", "com.xxl.job.admin"})
public class SnailJobAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnailJobAdminApplication.class, args);
    }
}
