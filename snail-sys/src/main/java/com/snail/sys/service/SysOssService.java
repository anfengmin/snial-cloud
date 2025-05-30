package com.snail.sys.service;

import com.snail.sys.domain.SysOss;
import com.snail.sys.dto.SysOssPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * OSS对象存储
 *
 * @author makejava
 * @since 2025-05-30 23:04:29
 */
public interface SysOssService extends IService<SysOss> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysOss>> queryByPage(SysOssPageDTO dto);


}
