package com.snail.sys.dao;

import com.snail.sys.domain.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户和角色关联表(SysUserRole)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:53:04
 */
@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRole> {

}

