package com.snail.sys.dao;

import com.snail.sys.domain.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 角色信息(SysRole)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:53:01
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRole> {

}

