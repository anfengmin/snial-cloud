package com.snail.sys.dao;

import com.snail.sys.domain.SysOssConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 对象存储配置表(SysOssConfig)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:52:59
 */
@Mapper
public interface SysOssConfigDao extends BaseMapper<SysOssConfig> {

}

