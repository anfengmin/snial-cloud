package com.snail.sys.service.impl;

import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dao.SysUserRoleDao;
import com.snail.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 用户和角色关联表(SysUserRole)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:53:04
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRole> implements SysUserRoleService {

    @Resource
    private SysUserRoleDao sysUserRoleDao;


}
