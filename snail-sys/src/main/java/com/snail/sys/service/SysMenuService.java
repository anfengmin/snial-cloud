package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.dto.SysMenuPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 菜单权限
 *
 * @author makejava
 * @since 2025-05-29 21:45:00
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysMenu>> queryByPage(SysMenuPageDTO dto);


}
