package com.snail.sys.service.impl;

import com.snail.common.core.utils.R;
import com.snail.sys.service.SysUserPostService;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.domain.SysUserPost;
import com.snail.sys.dao.SysUserPostDao;
import com.snail.sys.dto.SysUserPostPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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

    /**
     * 新增用户岗位信息
     *
     * @param user user
     * @since 1.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserPost(SysUser user) {
        List<SysUserPost> posts = Arrays.stream(user.getPostIds()).map(postId -> {
            SysUserPost sysUserPost = new SysUserPost();
            sysUserPost.setUserId(user.getId());
            sysUserPost.setPostId(postId);
            return sysUserPost;
        }).collect(Collectors.toList());
        this.saveBatch(posts);
    }

    /**
     * 删除用户岗位信息
     *
     * @param userId userId
     * @since 1.0
     */
    @Override
    public void deleteUserPost(Long userId) {
        this.lambdaUpdate().eq(SysUserPost::getUserId, userId).remove();
    }
}
