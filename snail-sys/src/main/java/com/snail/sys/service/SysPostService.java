package com.snail.sys.service;

import com.snail.sys.domain.SysPost;
import com.snail.sys.dto.SysPostPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * 岗位信息
 *
 * @author makejava
 * @since 2025-05-30 23:05:46
 */
public interface SysPostService extends IService<SysPost> {

 /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
     R<Page<SysPost>> queryByPage(SysPostPageDTO dto);


}
