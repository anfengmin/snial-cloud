package com.snail.common.storage.config;

import com.snail.common.storage.service.StorageClient;
import com.snail.common.storage.service.StorageConfigProvider;
import com.snail.common.storage.service.StorageManager;
import com.snail.common.storage.service.impl.AliyunOssStorageClient;
import com.snail.common.storage.service.impl.MinioStorageClient;
import com.snail.common.storage.service.impl.StorageManagerImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 对象存储自动配置
 *
 * @author Codex
 * @since 1.0
 */
@AutoConfiguration
public class StorageAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public StorageClient aliyunOssStorageClient() {
        return new AliyunOssStorageClient();
    }

    @Bean
    @ConditionalOnMissingBean
    public StorageClient minioStorageClient() {
        return new MinioStorageClient();
    }

    @Bean
    @ConditionalOnBean(StorageConfigProvider.class)
    @ConditionalOnMissingBean
    public StorageManager storageManager(StorageConfigProvider storageConfigProvider, List<StorageClient> storageClients) {
        return new StorageManagerImpl(storageConfigProvider, storageClients);
    }
}
