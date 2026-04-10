package com.snail.common.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.snail.common.mybatis.handler.CreateAndUpdateMetaObjectHandler;
import com.snail.common.mybatis.config.properties.MybatisCustomProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * method
 *
 * @author Anfm
 * Created time 2025/5/20
 * @since 1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(MybatisCustomProperties.class)
public class MybatisPlusConfiguration {

    private final MybatisCustomProperties properties;

    public MybatisPlusConfiguration(MybatisCustomProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 分页插件，自动识别数据库类型
     */
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 默认限制单页最大查询量，避免误传大分页把数据库直接拉满
        paginationInnerInterceptor.setMaxLimit(properties.getMaxLimit());
        // 分页合理化
        paginationInnerInterceptor.setOverflow(Boolean.TRUE.equals(properties.getOverflow()));
        return paginationInnerInterceptor;
    }

    /**
     * 使用网卡信息绑定雪花生成器
     * 防止集群雪花ID重复
     */
    @Primary
    @Bean
    public IdentifierGenerator idGenerator() {
        if (properties.getWorkerId() != null && properties.getDatacenterId() != null) {
            return new DefaultIdentifierGenerator(properties.getWorkerId(), properties.getDatacenterId());
        }
        return new DefaultIdentifierGenerator();
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new CreateAndUpdateMetaObjectHandler();
    }
}
