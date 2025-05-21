package com.snail.sys.service.impl;

import com.snail.sys.domain.SysNotice;
import com.snail.sys.dao.SysNoticeDao;
import com.snail.sys.service.SysNoticeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 通知公告(SysNotice)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:51:15
 */
@Service("sysNoticeService")
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeDao, SysNotice> implements SysNoticeService {

    @Resource
    private SysNoticeDao sysNoticeDao;


}
