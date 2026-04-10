package com.snail.common.log.service;

import com.snail.common.log.api.RemoteLogService;
import com.snail.common.log.domain.SysLoginInfo;
import com.snail.common.log.domain.SysOperateLog;
import com.snail.common.log.utils.LogIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author Levi.
 * Created time 2026/4/6
 * @since 1.0
 */
@Slf4j
@Service
public class AsyncLogService {

    @DubboReference(check = false, retries = 0)
    private RemoteLogService remoteLogService;

    private final ObjectProvider<LocalLogPersistenceHandler> localLogPersistenceHandlerProvider;

    public AsyncLogService(ObjectProvider<LocalLogPersistenceHandler> localLogPersistenceHandlerProvider) {
        this.localLogPersistenceHandlerProvider = localLogPersistenceHandlerProvider;
    }

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperateLog sysOperLog) {
        if (sysOperLog.getId() == null) {
            sysOperLog.setId(LogIdGenerator.nextId());
        }
        try {
            remoteLogService.saveLog(sysOperLog);
        } catch (Exception ex) {
            log.warn("Dubbo 保存操作日志失败，切换本地兜底: {}", ex.getMessage());
            LocalLogPersistenceHandler handler = localLogPersistenceHandlerProvider.getIfAvailable();
            if (handler != null) {
                handler.saveOperateLog(sysOperLog);
                return;
            }
            log.error("操作日志本地兜底处理器不存在，日志丢失: {}", sysOperLog.getId(), ex);
        }
    }

    /**
     * 保存系统登录记录
     */
    @Async
    public void saveLoginInfo(SysLoginInfo sysLoginInfo) {
        if (sysLoginInfo.getId() == null) {
            sysLoginInfo.setId(LogIdGenerator.nextId());
        }
        try {
            remoteLogService.saveLogininfor(sysLoginInfo);
        } catch (Exception ex) {
            log.warn("Dubbo 保存登录日志失败，切换本地兜底: {}", ex.getMessage());
            LocalLogPersistenceHandler handler = localLogPersistenceHandlerProvider.getIfAvailable();
            if (handler != null) {
                handler.saveLoginInfo(sysLoginInfo);
                return;
            }
            log.error("登录日志本地兜底处理器不存在，日志丢失: {}", sysLoginInfo.getId(), ex);
        }
    }
}
