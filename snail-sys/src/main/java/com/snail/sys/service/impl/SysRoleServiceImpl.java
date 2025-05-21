package com.snail.sys.service.impl;

import com.snail.sys.domain.SysRole;
import com.snail.sys.dao.SysRoleDao;
import com.snail.sys.service.SysRoleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 角色信息(SysRole)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:53:00
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

    @Resource
    private SysRoleDao sysRoleDao;


}
