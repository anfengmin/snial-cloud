package com.snail.sys.service.impl;

import com.snail.sys.domain.SysRole;
import com.snail.sys.dao.SysRoleDao;
import com.snail.sys.dto.SysRolePageDTO;
import com.snail.sys.service.SysRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 角色信息
 *
 * @author makejava
 * @since 2025-05-30 23:06:12
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysRole>> queryByPage(SysRolePageDTO dto) {
        Page<SysRole> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysRole> result = this.lambdaQuery().page(page);
        return R.ok(result);
    }
}
