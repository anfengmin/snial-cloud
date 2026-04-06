package com.snail.sys.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.sys.dto.SysOperateLogPageDTO;
import com.snail.sys.dao.SysOperateLogDao;
import com.snail.common.log.domain.SysOperateLog;
import com.snail.sys.service.SysOperateLogService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;


/**
 * 操作日志记录
 *
 * @author makejava
 * @since 2025-05-29 21:52:18
 */
@Service("sysOperateLogService")
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogDao, SysOperateLog> implements SysOperateLogService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public Page<SysOperateLog> queryByPage(SysOperateLogPageDTO dto) {

        Page<SysOperateLog> page = new Page<>(dto.getCurrent(), dto.getSize());

        return this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(dto.getBusinessType()), SysOperateLog::getBusinessType, dto.getBusinessType())
                .like(StrUtil.isNotBlank(dto.getOperateIp()), SysOperateLog::getOperateIp, dto.getOperateIp())
                .like(StrUtil.isNotBlank(dto.getTitle()), SysOperateLog::getTitle, dto.getTitle())
                .in(ArrayUtil.isNotEmpty(dto.getBusinessTypes()), SysOperateLog::getBusinessType, Arrays.asList(dto.getBusinessTypes()))
                .eq(ObjectUtil.isNotNull(dto.getStatus()), SysOperateLog::getStatus, dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getOperatorName()), SysOperateLog::getOperatorName, dto.getOperatorName())
                .between(StrUtil.isNotBlank(dto.getBeginTime()) && StrUtil.isNotBlank(dto.getEndTime()),
                        SysOperateLog::getOperateTime, dto.getBeginTime(), dto.getEndTime())
                .orderByDesc(SysOperateLog::getId)
                .page(page);
    }

    /**
     * 清空操作日志
     *
     * @since 1.0
     */
    @Override
    public void cleanOperateLog() {
        this.remove(new QueryWrapper<>());
    }

    /**
     * 新增操作日志
     *
     * @param sysOperateLog 操作日志
     * @return 是否成功
     * @since 1.0
     */
    @Override
    public boolean insertOperlog(SysOperateLog sysOperateLog) {
        sysOperateLog.setOperateTime(new Date());
        return this.save(sysOperateLog);
    }
}
