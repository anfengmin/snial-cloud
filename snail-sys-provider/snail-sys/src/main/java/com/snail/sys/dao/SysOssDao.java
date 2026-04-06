package com.snail.sys.dao;

import com.snail.sys.domain.SysOss;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * OSS对象存储(SysOss)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:52:21
 */
@Mapper
public interface SysOssDao extends BaseMapper<SysOss> {

}

