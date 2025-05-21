package com.snail.sys.service.impl;

import com.snail.sys.domain.SysUserPost;
import com.snail.sys.dao.SysUserPostDao;
import com.snail.sys.service.SysUserPostService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 用户与岗位关联表(SysUserPost)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:53:03
 */
@Service("sysUserPostService")
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostDao, SysUserPost> implements SysUserPostService {

    @Resource
    private SysUserPostDao sysUserPostDao;


}
