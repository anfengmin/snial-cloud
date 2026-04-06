package com.snail.sys.dubbo;

import com.snail.sys.api.RemoteLogService;
import com.snail.sys.api.domain.SysLoginInfo;
import com.snail.sys.api.domain.SysOperateLog;
import com.snail.sys.service.SysLoginInfoService;
import com.snail.sys.service.SysOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 操作日志记录
 *
 * @author Levi.
 * Created time 2026/4/6
 * @since 1.0
 */
@RequiredArgsConstructor
@DubboService
public class RemoteLogServiceImpl implements RemoteLogService {

    private final SysOperateLogService sysOperateLogService;
    private final SysLoginInfoService sysLoginInfoService;

    @Override
    public Boolean saveLog(SysOperateLog sysOperateLog) {
        return sysOperateLogService.insertOperlog(sysOperateLog);
    }

    @Override
    public Boolean saveLogininfor(SysLoginInfo sysLoginInfo) {
        return sysLoginInfoService.insertLogininfor(sysLoginInfo);
    }
}
