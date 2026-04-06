package com.snail.sys.dao;

import com.github.yulichang.base.MPJBaseMapper;
import com.snail.sys.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 菜单权限表(SysMenu)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:36:54
 */
@Mapper
public interface SysMenuDao extends MPJBaseMapper<SysMenu> {

}

