package com.snail.sys.service.impl;

import com.snail.sys.domain.SysOssConfig;
import com.snail.sys.dao.SysOssConfigDao;
import com.snail.sys.service.SysOssConfigService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 对象存储配置表(SysOssConfig)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:52:58
 */
@Service("sysOssConfigService")
public class SysOssConfigServiceImpl extends ServiceImpl<SysOssConfigDao, SysOssConfig> implements SysOssConfigService {

    @Resource
    private SysOssConfigDao sysOssConfigDao;


}
