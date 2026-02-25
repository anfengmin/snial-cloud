package com.snail.gateway.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 网关 Sa-Token 配置 - 纯JWT无状态模式
 * <p>
 * 网关负责验证Token，但不解析用户信息（用户信息在各微服务中解析）
 *
 * @author Anfm
 * Created time 2025/5/14
 * @since 1.0
 */
@Configuration
public class SaTokenGatewayConfig {

    /**
     * 使用JWT无状态模式
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    /**
     * 注册Sa-Token全局过滤器
     * <p>
     * 网关层只负责验证Token是否有效，不解析用户信息
     * 用户信息在各微服务中通过LoginUtils获取
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")
                // 开放地址（登录、注册、验证码等）
                .addExclude("/sys/auth/login")
                .addExclude("/sys/auth/register")
                .addExclude("/sys/auth/captcha/**")
                .addExclude("/actuator/**")
                // 鉴权方法
                .setAuth(obj -> {
                    SaRouter.match("/**")
                            .check(r -> {
                                // 检查是否登录（只验证Token有效性）
                                StpUtil.checkLogin();
                            });
                })
                // 异常处理
                .setError(e -> {
                    // setError 默认会传递异常信息，这里返回 401 即可
                    // 由于无法直接获取 response，使用返回 Mono.empty() 让 Sa-Token 自动处理
                    return Mono.empty();
                });
    }
}
