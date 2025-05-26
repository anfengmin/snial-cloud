package com.snail.sys.dao;

import com.snail.sys.domain.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 部门表(SysDept)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:30:47
 */
@Mapper
public interface SysDeptDao extends BaseMapper<SysDept> {

}

