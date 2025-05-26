package com.snail.sys.dao;

import com.snail.sys.domain.SysOperateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 操作日志记录(SysOperateLog)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:52:04
 */
@Mapper
public interface SysOperateLogDao extends BaseMapper<SysOperateLog> {

}

