package com.snail.sys.service.impl;

import com.snail.sys.domain.SysPost;
import com.snail.sys.dao.SysPostDao;
import com.snail.sys.dto.SysPostPageDTO;
import com.snail.sys.service.SysPostService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 岗位信息
 *
 * @author makejava
 * @since 2025-05-30 23:05:47
 */
@Service("sysPostService")
public class SysPostServiceImpl extends ServiceImpl<SysPostDao, SysPost> implements SysPostService {


        /**
         * 分页查询
         *
         * @param dto      筛选条件
         * @return 查询结果
         */
        @Override
        public R<Page<SysPost>> queryByPage(SysPostPageDTO dto) {
            Page<SysPost> page = new Page<>(dto.getCurrent(), dto.getSize());
            Page<SysPost> result = this.lambdaQuery().page(page);
            return R.ok(result);
        }
}
