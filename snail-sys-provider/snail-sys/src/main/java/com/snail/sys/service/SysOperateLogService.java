package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.dto.SysOperateLogPageDTO;
import com.snail.common.log.domain.SysOperateLog;

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
     Page<SysOperateLog> queryByPage(SysOperateLogPageDTO dto);

    /**
     * cleanOperateLog
     *
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    void cleanOperateLog();

    /**
     * insertOperlog
     *
     * @param sysOperateLog sysOperateLog
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean insertOperlog(SysOperateLog sysOperateLog);
}
