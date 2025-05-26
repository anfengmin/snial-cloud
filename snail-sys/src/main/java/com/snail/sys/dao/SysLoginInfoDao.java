package com.snail.sys.dao;

import com.snail.sys.domain.SysLoginInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 系统访问记录(SysLoginInfo)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:50:07
 */
@Mapper
public interface SysLoginInfoDao extends BaseMapper<SysLoginInfo> {

}

