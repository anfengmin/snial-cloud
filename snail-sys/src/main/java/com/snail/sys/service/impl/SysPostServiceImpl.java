package com.snail.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.sys.dao.SysPostDao;
import com.snail.sys.domain.SysPost;
import com.snail.sys.dto.SysPostPageDTO;
import com.snail.sys.service.SysPostService;
import org.springframework.stereotype.Service;

import java.util.List;


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
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public Page<SysPost> queryByPage(SysPostPageDTO dto) {
        Page<SysPost> page = new Page<>(dto.getCurrent(), dto.getSize());
        return this.lambdaQuery()
                .like(StrUtil.isNotBlank(dto.getPostCode()), SysPost::getPostCode, dto.getPostCode())
                .eq(ObjectUtil.isNotEmpty(dto.getStatus()), SysPost::getStatus, dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getPostName()), SysPost::getPostName, dto.getPostName())
                .page(page);

    }

    /**
     * 检查岗位名称是否存在
     *
     * @param sysPost sysPost
     * @return 是否存在
     * @since 1.0
     */
    @Override
    public boolean checkPostNameExists(SysPost sysPost) {
        return this.lambdaQuery()
                .eq(SysPost::getPostName, sysPost.getPostName())
                .ne(ObjectUtil.isNotEmpty(sysPost.getId()), SysPost::getId, sysPost.getId())
                .exists();
    }

    /**
     * 检查岗位编码是否存在
     *
     * @param sysPost sysPost
     * @return 是否存在
     * @since 1.0
     */
    @Override
    public boolean checkPostCodeExists(SysPost sysPost) {
        return this.lambdaQuery()
                .eq(SysPost::getPostCode, sysPost.getPostCode())
                .ne(ObjectUtil.isNotEmpty(sysPost.getId()), SysPost::getId, sysPost.getId())
                .exists();
    }

    /**
     * 查询岗位列表
     *
     * @param post 岗位信息
     * @return 岗位列表
     * @since 1.0
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return this.lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(post.getStatus()), SysPost::getStatus, post.getStatus())
                .like(StrUtil.isNotBlank(post.getPostCode()), SysPost::getPostCode, post.getPostCode())
                .like(StrUtil.isNotBlank(post.getPostName()), SysPost::getPostName, post.getPostName())
                .select(SysPost::getId, SysPost::getPostName, SysPost::getPostCode, SysPost::getStatus)
                .list();
    }
}
