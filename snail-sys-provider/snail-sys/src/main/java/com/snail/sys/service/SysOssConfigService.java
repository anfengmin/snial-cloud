package com.snail.sys.service;

import com.snail.sys.domain.SysOssConfig;
import com.snail.sys.dto.SysOssConfigPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import com.snail.common.storage.service.StorageConfigProvider;

/**
 * 对象存储配置表
 *
 * @author makejava
 * @since 2025-05-30 23:04:58
 */
public interface SysOssConfigService extends IService<SysOssConfig>, StorageConfigProvider {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysOssConfig>> queryByPage(SysOssConfigPageDTO dto);

    /**
     * 获取默认配置实体
     *
     * @return 默认配置实体
     */
    SysOssConfig getDefaultEntity();

    /**
     * 按配置键获取配置实体
     *
     * @param configKey 配置键
     * @return 配置实体
     */
    SysOssConfig getByConfigKeyEntity(String configKey);

}
