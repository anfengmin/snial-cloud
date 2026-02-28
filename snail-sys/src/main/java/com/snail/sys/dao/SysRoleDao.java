package com.snail.sys.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.domain.SysNotice;
import com.snail.sys.api.domain.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 角色信息(SysRole)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:53:01
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRole> {

    /**
     * 查询指定行数据
     *
     * @param page      分页参数
     * @param wrapper   查询条件
     * @return 对象列表
     */
    Page<SysUser> selectAllocatedList(@Param("page") Page<SysNotice> page, @Param(Constants.WRAPPER) QueryWrapper<SysUser> wrapper);
}

