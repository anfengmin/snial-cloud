package com.snail.sys.service;

import com.snail.sys.domain.SysRoleMenu;
import com.snail.sys.dto.SysRoleMenuPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * 角色和菜单关联
 *
 * @author makejava
 * @since 2025-05-30 23:06:44
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysRoleMenu>> queryByPage(SysRoleMenuPageDTO dto);


}
