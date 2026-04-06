package com.snail.sys.dao;

import com.snail.sys.domain.SysRoleDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 角色和部门关联(SysRoleDept)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:53:01
 */
@Mapper
public interface SysRoleDeptDao extends BaseMapper<SysRoleDept> {

}

