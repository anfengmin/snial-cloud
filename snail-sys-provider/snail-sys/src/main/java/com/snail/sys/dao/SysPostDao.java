package com.snail.sys.dao;

import com.snail.sys.domain.SysPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 岗位信息(SysPost)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:53:00
 */
@Mapper
public interface SysPostDao extends BaseMapper<SysPost> {

}

