package com.snail.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snail.sys.api.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户信息表(User)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-11 20:30:04
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUser> {

}

