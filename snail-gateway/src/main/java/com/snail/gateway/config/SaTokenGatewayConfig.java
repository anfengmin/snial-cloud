package com.snail.gateway.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
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
                    // 获取response
                    ServerWebExchange exchange = cn.dev33.satoken.context.SaRouter.getContext();
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    String body = "{\"code\":401,\"msg\":\"未授权：" + e.getMessage() + "\"}";
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
                });
    }
}
