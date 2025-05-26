package com.snail.sys.dao;

import com.snail.sys.domain.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 参数配置表(SysConfig)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:32:38
 */
@Mapper
public interface SysConfigDao extends BaseMapper<SysConfig> {

}

