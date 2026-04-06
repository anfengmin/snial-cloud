package com.snail.sys.dao;

import com.snail.sys.domain.SysDictData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 字典数据表(SysDictData)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:34:18
 */
@Mapper
public interface SysDictDataDao extends BaseMapper<SysDictData> {

}

