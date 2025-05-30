package com.snail.sys.service;

import com.snail.sys.domain.SysUserPost;
import com.snail.sys.dto.SysUserPostPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * 用户与岗位关联表
 *
 * @author makejava
 * @since 2025-05-30 23:13:48
 */
public interface SysUserPostService extends IService<SysUserPost> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysUserPost>> queryByPage(SysUserPostPageDTO dto);


}
