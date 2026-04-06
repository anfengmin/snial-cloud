package com.snail.common.log.service;

import com.snail.sys.api.RemoteLogService;
import com.snail.sys.api.domain.SysLoginInfo;
import com.snail.sys.api.domain.SysOperateLog;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author Levi.
 * Created time 2026/4/6
 * @since 1.0
 */
@Service
public class AsyncLogService {

    @DubboReference
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperateLog sysOperLog) {
        remoteLogService.saveLog(sysOperLog);
    }

    /**
     * 保存系统登录记录
     */
    @Async
    public void saveLoginInfo(SysLoginInfo sysLoginInfo) {
        remoteLogService.saveLogininfor(sysLoginInfo);
    }
}
