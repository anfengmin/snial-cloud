package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.domain.SysPost;
import com.snail.sys.dto.SysPostPageDTO;

import java.util.List;

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
    Page<SysPost> queryByPage(SysPostPageDTO dto);

    /**
     * 检查岗位名称是否存在
     *
     * @param sysPost sysPost
     * @return 是否存在
     * @since 1.0
     */
    boolean checkPostNameExists(SysPost sysPost);

    /**
     * 检查岗位编码是否存在
     *
     * @param sysPost sysPost
     * @return 是否存在
     * @since 1.0
     */
    boolean checkPostCodeExists(SysPost sysPost);

    /**
     * 查询岗位列表
     *
     * @param post 岗位信息
     * @return 岗位列表
     */
    List<SysPost> selectPostList(SysPost post);
}
