package com.snail.sys.service.impl;

import com.snail.sys.domain.SysRoleMenu;
import com.snail.sys.dao.SysRoleMenuDao;
import com.snail.sys.dto.SysRoleMenuPageDTO;
import com.snail.sys.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 角色和菜单关联
 *
 * @author makejava
 * @since 2025-05-30 23:06:44
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenu> implements SysRoleMenuService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysRoleMenu>> queryByPage(SysRoleMenuPageDTO dto) {
        Page<SysRoleMenu> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysRoleMenu> result = this.lambdaQuery().page(page);
        return R.ok(result);
    }
}
