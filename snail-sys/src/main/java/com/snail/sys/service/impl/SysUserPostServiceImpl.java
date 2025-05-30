package com.snail.sys.service.impl;

import com.snail.sys.domain.SysUserPost;
import com.snail.sys.dao.SysUserPostDao;
import com.snail.sys.dto.SysUserPostPageDTO;
import com.snail.sys.service.SysUserPostService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 用户与岗位关联表
 *
 * @author makejava
 * @since 2025-05-30 23:13:49
 */
@Service("sysUserPostService")
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostDao, SysUserPost> implements SysUserPostService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysUserPost>> queryByPage(SysUserPostPageDTO dto) {
        Page<SysUserPost> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysUserPost> result = this.lambdaQuery().page(page);
        return R.ok(result);
    }
}
