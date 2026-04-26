package com.snail.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.common.core.constant.CacheNames;
import com.snail.common.core.utils.R;
import com.snail.common.storage.enums.StorageType;
import com.snail.common.storage.exception.StorageException;
import com.snail.common.storage.model.StorageConfig;
import com.snail.sys.dao.SysOssConfigDao;
import com.snail.sys.domain.SysOssConfig;
import com.snail.sys.dto.SysOssConfigPageDTO;
import com.snail.sys.service.SysOssConfigService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;


/**
 * 对象存储配置表
 *
 * @author makejava
 * @since 2025-05-30 23:04:59
 */
@Service("sysOssConfigService")
public class SysOssConfigServiceImpl extends ServiceImpl<SysOssConfigDao, SysOssConfig> implements SysOssConfigService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysOssConfig>> queryByPage(SysOssConfigPageDTO dto) {
        Page<SysOssConfig> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysOssConfig> result = this.lambdaQuery()
                .like(StrUtil.isNotBlank(dto.getConfigKey()), SysOssConfig::getConfigKey, dto.getConfigKey())
                .like(StrUtil.isNotBlank(dto.getBucketName()), SysOssConfig::getBucketName, dto.getBucketName())
                .like(StrUtil.isNotBlank(dto.getEndpoint()), SysOssConfig::getEndpoint, dto.getEndpoint())
                .eq(dto.getStatus() != null, SysOssConfig::getStatus, dto.getStatus())
                .eq(StrUtil.isNotBlank(dto.getExt1()), SysOssConfig::getExt1, dto.getExt1())
                .orderByAsc(SysOssConfig::getStatus)
                .orderByDesc(SysOssConfig::getUpdateTime)
                .page(page);
        return R.ok(result);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SYS_OSS_CONFIG, key = "'default'")
    public StorageConfig getDefaultConfig() {
        return toStorageConfig(getDefaultEntity());
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SYS_OSS_CONFIG, key = "#configKey")
    public StorageConfig getByConfigKey(String configKey) {
        return toStorageConfig(getByConfigKeyEntity(configKey));
    }

    @Override
    public SysOssConfig getDefaultEntity() {
        SysOssConfig config = this.lambdaQuery()
                .eq(SysOssConfig::getStatus, 0)
                .last("limit 1")
                .one();
        if (config == null) {
            throw new StorageException("未找到默认对象存储配置");
        }
        return config;
    }

    @Override
    public SysOssConfig getByConfigKeyEntity(String configKey) {
        SysOssConfig config = this.lambdaQuery()
                .eq(SysOssConfig::getConfigKey, configKey)
                .last("limit 1")
                .one();
        if (config == null) {
            throw new StorageException("未找到对象存储配置: " + configKey);
        }
        return config;
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SYS_OSS_CONFIG, allEntries = true)
    public boolean save(SysOssConfig entity) {
        refreshDefaultStatus(entity);
        return super.save(entity);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SYS_OSS_CONFIG, allEntries = true)
    public boolean updateById(SysOssConfig entity) {
        refreshDefaultStatus(entity);
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.SYS_OSS_CONFIG, allEntries = true)
    public boolean removeByIds(Collection<?> list) {
        return super.removeByIds(list);
    }

    private void refreshDefaultStatus(SysOssConfig entity) {
        if (entity == null || entity.getStatus() == null || entity.getStatus() != 0) {
            return;
        }
        this.lambdaUpdate()
                .set(SysOssConfig::getStatus, 1)
                .ne(entity.getId() != null, SysOssConfig::getId, entity.getId())
                .update();
    }

    private StorageConfig toStorageConfig(SysOssConfig config) {
        String provider = StrUtil.blankToDefault(config.getExt1(), config.getConfigKey());
        return StorageConfig.builder()
                .configId(config.getId())
                .configKey(config.getConfigKey())
                .type(StorageType.resolve(provider, config.getConfigKey(), config.getEndpoint()))
                .accessKey(config.getAccessKey())
                .secretKey(config.getSecretKey())
                .bucketName(config.getBucketName())
                .prefix(config.getPrefix())
                .endpoint(config.getEndpoint())
                .domain(config.getDomain())
                .https("Y".equalsIgnoreCase(config.getIsHttps()))
                .region(config.getRegion())
                .accessPolicy(config.getAccessPolicy())
                .build();
    }
}
