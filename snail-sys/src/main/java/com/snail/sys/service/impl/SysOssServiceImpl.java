package com.snail.sys.service.impl;

import com.snail.sys.domain.SysOss;
import com.snail.sys.dao.SysOssDao;
import com.snail.sys.service.SysOssService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * OSS对象存储(SysOss)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:52:20
 */
@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOss> implements SysOssService {

    @Resource
    private SysOssDao sysOssDao;


}
