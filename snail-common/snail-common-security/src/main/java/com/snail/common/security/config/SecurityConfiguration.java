package com.snail.common.security.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.util.SaResult;
import com.snail.common.core.constant.HttpStatus;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 拦截器配置 - 通用配置
 * <p>
 * 注册 SaInterceptor，使 @SaCheckPermission、@SaCheckRole 等注解生效。
 * 各业务模块引入 snail-common-satoken 后自动生效，无需重复配置。
 * <p>
 * 排除路径可通过 satoken.interceptor.exclude-paths 配置项自定义。
 *
 * @author Anfm
 * Created time 2025/5/14
 * @since 1.0
 */
@AutoConfiguration
public class SecurityConfiguration implements WebMvcConfigurer {

    /**
     * 注册sa-token的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    /**
     * 校验是否从网关转发
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/actuator/**")
                .setAuth(obj -> {
                    if (SaManager.getConfig().getCheckSameToken()) {
                        SaSameUtil.checkCurrentRequestToken();
                    }
                })
                .setError(e -> SaResult.error("认证失败，无法访问系统资源").setCode(HttpStatus.UNAUTHORIZED));
    }
}
