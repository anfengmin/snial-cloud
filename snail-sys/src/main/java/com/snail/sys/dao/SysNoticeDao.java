package com.snail.sys.dao;

import com.snail.sys.domain.SysNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 通知公告(SysNotice)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:51:15
 */
@Mapper
public interface SysNoticeDao extends BaseMapper<SysNotice> {

}

