package com.snail.sys.dao;

import com.snail.sys.domain.SysUserPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户与岗位关联表(SysUserPost)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:53:03
 */
@Mapper
public interface SysUserPostDao extends BaseMapper<SysUserPost> {

}

