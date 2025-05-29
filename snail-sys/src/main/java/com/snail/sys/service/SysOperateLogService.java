package com.snail.sys.service;

import com.snail.sys.domain.SysOperateLog;
import com.snail.sys.dto.SysOperateLogPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * 操作日志记录
 *
 * @author makejava
 * @since 2025-05-29 21:52:17
 */
public interface SysOperateLogService extends IService<SysOperateLog> {

 /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
     R<Page<SysOperateLog>> queryByPage(SysOperateLogPageDTO dto);


}
