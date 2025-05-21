package com.snail.sys.service.impl;

import com.snail.sys.domain.SysLoginInfo;
import com.snail.sys.dao.SysLoginInfoDao;
import com.snail.sys.service.SysLoginInfoService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 系统访问记录(SysLoginInfo)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:50:06
 */
@Service("sysLoginInfoService")
public class SysLoginInfoServiceImpl extends ServiceImpl<SysLoginInfoDao, SysLoginInfo> implements SysLoginInfoService {

    @Resource
    private SysLoginInfoDao sysLoginInfoDao;


}
