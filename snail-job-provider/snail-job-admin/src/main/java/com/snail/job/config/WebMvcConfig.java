package com.snail.job.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * snail-job-admin 已切换到 Sa-Token 权限体系，
 * 不再注册 XXL-JOB 原生的 Cookie 登录拦截器。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
}
