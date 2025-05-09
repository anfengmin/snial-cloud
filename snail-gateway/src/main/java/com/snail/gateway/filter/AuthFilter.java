package com.snail.gateway.filter;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 网关鉴权配置
 */
@Configuration
public class AuthFilter {

    /**
     * 注册Sa-Token全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")
                // 开放地址
                .addExclude("/actuator/**")
                .addExclude("/sys/auth/login")
                .addExclude("/sys/auth/register")
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除白名单
                    SaRouter.match("/**")
                            .check(r -> {
                                // 检查是否登录
                                StpUtil.checkLogin();
                            });
                })
                // 异常处理方法：每次setAuth发生异常时进入
                .setError(e -> {
                    return SaRouter.getResponse()
                            .setStatus(HttpStatus.UNAUTHORIZED.value())
                            .setBody("{\"code\":401,\"msg\":\"未授权：" + e.getMessage() + "\"}");
                });
    }
}