package com.snail.common.satoken.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import com.snail.common.satoken.core.dao.PlusSaTokenDao;
import com.snail.common.satoken.core.service.SaPermissionImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Sa-Token 配置
 * <p>
 * 核心配置类，只提供权限接口实现
 * <p>
 * 注意事项：
 * 1. 业务服务需要添加 sa-token-springmvc 依赖才能使用注解鉴权
 * 2. 拦截器需要在各业务服务中单独配置
 *
 * @author Anfm
 * Created time 2025/5/14
 * @since 1.0
 */
@AutoConfiguration
public class SaTokenConfiguration {


    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
    /**
     * 权限接口实现
     * <p>
     * 默认返回空列表，各业务服务需要重写此实现
     * 从数据库动态获取权限
     *
     * @return 权限接口实现
     */
    @Bean
    public StpInterface stpInterface() {
        return new SaPermissionImpl();
    }


    /**
     * 自定义dao层存储
     */
    @Bean
    public SaTokenDao saTokenDao() {
        return new PlusSaTokenDao();
    }
}
