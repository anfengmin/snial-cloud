package com.snail.gateway.config;

import com.snail.gateway.handler.SlidingCaptchaHandler;
import com.snail.gateway.handler.ValidateCodeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import javax.annotation.Resource;

/**
 * 路由配置信息
 *
 * @author ruoyi
 */
@Configuration
public class RouterFunctionConfiguration {

    @Resource
    private ValidateCodeHandler validateCodeHandler;

    @Resource
    private SlidingCaptchaHandler slidingCaptchaHandler;

    @SuppressWarnings("rawtypes")
    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions
                .route(
                        //  请求头必须包含 Accept: text/plain
                       // RequestPredicates.GET("/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                        RequestPredicates.GET("/code"),
                        validateCodeHandler)
                .andRoute(
                        RequestPredicates.GET("/slidingCode"),
                        slidingCaptchaHandler);
    }
}
