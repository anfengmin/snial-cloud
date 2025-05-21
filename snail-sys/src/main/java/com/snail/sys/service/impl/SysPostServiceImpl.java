package com.snail.sys.service.impl;

import com.snail.sys.domain.SysPost;
import com.snail.sys.dao.SysPostDao;
import com.snail.sys.service.SysPostService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 岗位信息(SysPost)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:53:00
 */
@Service("sysPostService")
public class SysPostServiceImpl extends ServiceImpl<SysPostDao, SysPost> implements SysPostService {

    @Resource
    private SysPostDao sysPostDao;


}
