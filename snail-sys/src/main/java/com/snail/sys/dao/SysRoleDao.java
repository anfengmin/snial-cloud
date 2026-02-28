package com.snail.sys.dao;

import com.github.yulichang.base.MPJBaseMapper;
import com.snail.sys.api.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;


/**
 * 角色信息(SysRole)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:53:01
 */
@Mapper
public interface SysRoleDao extends MPJBaseMapper<SysRole> {

}

