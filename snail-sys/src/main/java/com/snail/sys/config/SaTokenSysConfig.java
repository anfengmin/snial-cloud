//package com.snail.sys.config;
//
//import cn.dev33.satoken.interceptor.SaInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * Sa-Token 配置 - Sys模块
// * <p>
// * 注册Sa-Token拦截器
// *
// * @author Anfm
// * Created time 2025/5/14
// * @since 1.0
// */
//@Configuration
//public class SaTokenSysConfig implements WebMvcConfigurer {
//
//    /**
//     * 注册Sa-Token拦截器
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SaInterceptor(handle -> {
//            // 默认使用全局拦截器配置
//            // 登录验证、角色验证、权限验证会自动处理
//        }))
//        .addPathPatterns("/**")
//        .excludePathPatterns(
//            "/auth/login",
//            "/auth/register",
//            "/actuator/**"
//        );
//    }
//}
