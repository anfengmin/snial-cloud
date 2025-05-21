package com.snail.sys.service.impl;

import com.snail.sys.domain.SysOperateLog;
import com.snail.sys.dao.SysOperateLogDao;
import com.snail.sys.service.SysOperateLogService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 操作日志记录(SysOperateLog)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:52:04
 */
@Service("sysOperateLogService")
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogDao, SysOperateLog> implements SysOperateLogService {

    @Resource
    private SysOperateLogDao sysOperateLogDao;


}
