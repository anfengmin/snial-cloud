package com.snail.sys.service.impl;

import com.snail.sys.domain.SysNotice;
import com.snail.sys.dao.SysNoticeDao;
import com.snail.sys.dto.SysNoticePageDTO;
import com.snail.sys.service.SysNoticeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 通知公告
 *
 * @author makejava
 * @since 2025-05-29 21:50:41
 */
@Service("sysNoticeService")
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeDao, SysNotice> implements SysNoticeService {


        /**
         * 分页查询
         *
         * @param dto      筛选条件
         * @return 查询结果
         */
        @Override
        public R<Page<SysNotice>> queryByPage(SysNoticePageDTO dto) {
            Page<SysNotice> page = new Page<>(dto.getCurrent(), dto.getSize());
            Page<SysNotice> result = this.lambdaQuery().page(page);
            return R.ok(result);
        }
}
