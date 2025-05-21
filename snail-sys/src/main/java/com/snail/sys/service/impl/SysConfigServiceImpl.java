package com.snail.sys.service.impl;

import com.snail.sys.domain.SysConfig;
import com.snail.sys.dao.SysConfigDao;
import com.snail.sys.service.SysConfigService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 参数配置表(SysConfig)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:32:37
 */
@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfig> implements SysConfigService {

    @Resource
    private SysConfigDao sysConfigDao;


}
