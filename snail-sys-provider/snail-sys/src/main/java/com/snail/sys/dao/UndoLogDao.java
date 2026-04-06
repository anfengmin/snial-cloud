package com.snail.sys.dao;

import com.snail.sys.domain.UndoLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * AT transaction mode undo table(UndoLog)表数据库访问层
 *
 * @author makejava
 * @since 2025-05-21 21:53:04
 */
@Mapper
public interface UndoLogDao extends BaseMapper<UndoLog> {

}

