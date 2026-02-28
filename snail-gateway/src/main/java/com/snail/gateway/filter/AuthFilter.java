package com.snail.gateway.filter;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.snail.common.core.constant.HttpStatus;
import com.snail.gateway.config.properties.IgnoreWhiteProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 网关鉴权配置
 */
@Configuration
public class AuthFilter {

    /**
     * 注册Sa-Token全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter(IgnoreWhiteProperties ignoreWhite) {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")
                .addExclude("/favicon.ico", "/actuator/**")

                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除白名单
                    SaRouter.match("/**")
                            .notMatch(ignoreWhite.getWhites())
                            .check(r -> {
                                // 检查是否登录
                                StpUtil.checkLogin();
                            });
                })
                // 异常处理方法：每次setAuth发生异常时进入
                .setError(e -> SaResult.error("认证失败，无法访问系统资源").setCode(HttpStatus.UNAUTHORIZED));

    }
}