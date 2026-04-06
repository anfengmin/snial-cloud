package com.snail;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * No explanation is needed
 *
 * @author Levi.
 * Created time 2025/5/10
 * @since 1.0
 */
@EnableDubbo
@SpringBootApplication
public class SnailSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnailSysApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }

}
