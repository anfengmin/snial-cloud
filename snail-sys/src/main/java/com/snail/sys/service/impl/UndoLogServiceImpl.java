package com.snail.sys.service.impl;

import com.snail.sys.domain.UndoLog;
import com.snail.sys.dao.UndoLogDao;
import com.snail.sys.service.UndoLogService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * AT transaction mode undo table(UndoLog)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:53:04
 */
@Service("undoLogService")
public class UndoLogServiceImpl extends ServiceImpl<UndoLogDao, UndoLog> implements UndoLogService {

    @Resource
    private UndoLogDao undoLogDao;


}
