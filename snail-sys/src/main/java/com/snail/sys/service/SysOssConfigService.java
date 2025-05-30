package com.snail.sys.service;

import com.snail.sys.domain.SysOssConfig;
import com.snail.sys.dto.SysOssConfigPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * 对象存储配置表
 *
 * @author makejava
 * @since 2025-05-30 23:04:58
 */
public interface SysOssConfigService extends IService<SysOssConfig> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysOssConfig>> queryByPage(SysOssConfigPageDTO dto);


}
