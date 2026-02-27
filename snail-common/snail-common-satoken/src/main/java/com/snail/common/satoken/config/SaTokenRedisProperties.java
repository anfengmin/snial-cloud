//package com.snail.common.satoken.config;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
///**
// * Sa-Token Redis配置属性
// * <p>
// * 用于配置Sa-Token的Redis存储（使用业务Redis）
// * 注意：此模块使用Spring注入的Redis，不需要单独配置
// *
// * @author Anfm
// * Created time 2025/5/14
// * @since 1.0
// */
//@Data
//@Component
//@ConfigurationProperties(prefix = "satoken")
//public class SaTokenRedisProperties {
//
//    /**
//     * Token名称 (同时也是cookie名称)
//     */
//    private String tokenName = "Authorization";
//
//    /**
//     * Token有效期（单位：秒），默认30天
//     */
//    private long timeout = 2592000;
//
//    /**
//     * 是否允许同一账号多地登录
//     */
//    private boolean isConcurrent = true;
//
//    /**
//     * 在多人登录同一账号时，是否共用一个Token
//     */
//    private boolean isShare = true;
//
//    /**
//     * Token风格
//     */
//    private String tokenStyle = "uuid";
//}
