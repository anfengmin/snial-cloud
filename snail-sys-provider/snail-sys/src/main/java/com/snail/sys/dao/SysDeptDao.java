package com.snail.sys.dao;

import com.github.yulichang.base.MPJBaseMapper;
import com.snail.sys.domain.SysDept;
import org.apache.ibatis.annotations.Mapper;


/**
 * 部门表(SysDept)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:30:47
 */
@Mapper
public interface SysDeptDao extends MPJBaseMapper<SysDept> {

}

