package com.snail.sys.service.impl;

import com.snail.sys.domain.SysRoleMenu;
import com.snail.sys.dao.SysRoleMenuDao;
import com.snail.sys.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 角色和菜单关联(SysRoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:53:02
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenu> implements SysRoleMenuService {

    @Resource
    private SysRoleMenuDao sysRoleMenuDao;


}
