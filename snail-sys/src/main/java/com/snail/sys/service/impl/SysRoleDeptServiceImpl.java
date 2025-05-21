package com.snail.sys.service.impl;

import com.snail.sys.domain.SysRoleDept;
import com.snail.sys.dao.SysRoleDeptDao;
import com.snail.sys.service.SysRoleDeptService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 角色和部门关联(SysRoleDept)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:53:01
 */
@Service("sysRoleDeptService")
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptDao, SysRoleDept> implements SysRoleDeptService {

    @Resource
    private SysRoleDeptDao sysRoleDeptDao;


}
