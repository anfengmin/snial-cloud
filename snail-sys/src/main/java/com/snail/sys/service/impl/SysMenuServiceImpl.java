package com.snail.sys.service.impl;

import com.snail.sys.domain.SysMenu;
import com.snail.sys.dao.SysMenuDao;
import com.snail.sys.service.SysMenuService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 菜单权限表(SysMenu)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:36:54
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuDao sysMenuDao;


}
