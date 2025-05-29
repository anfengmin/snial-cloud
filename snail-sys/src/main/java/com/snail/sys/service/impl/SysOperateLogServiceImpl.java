package com.snail.sys.service.impl;

import com.snail.sys.domain.SysOperateLog;
import com.snail.sys.dao.SysOperateLogDao;
import com.snail.sys.dto.SysOperateLogPageDTO;
import com.snail.sys.service.SysOperateLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 操作日志记录
 *
 * @author makejava
 * @since 2025-05-29 21:52:18
 */
@Service("sysOperateLogService")
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogDao, SysOperateLog> implements SysOperateLogService {


        /**
         * 分页查询
         *
         * @param dto      筛选条件
         * @return 查询结果
         */
        @Override
        public R<Page<SysOperateLog>> queryByPage(SysOperateLogPageDTO dto) {
            Page<SysOperateLog> page = new Page<>(dto.getCurrent(), dto.getSize());
            Page<SysOperateLog> result = this.lambdaQuery().page(page);
            return R.ok(result);
        }
}
