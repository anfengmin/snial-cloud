package com.snail.common.log.api;

import com.snail.common.log.domain.SysLoginInfo;
import com.snail.common.log.domain.SysOperateLog;

/**
 * 日志服务
 *
 * @author Levi
 * @since 1.0
 */
public interface RemoteLogService {

    /**
     * 保存系统日志
     *
     * @param sysOperateLog 日志实体
     * @return 结果
     */
    Boolean saveLog(SysOperateLog sysOperateLog);

    /**
     * 保存访问记录
     *
     * @param sysLoginInfo 访问实体
     * @return 结果
     */
    Boolean saveLogininfor(SysLoginInfo sysLoginInfo);
}
