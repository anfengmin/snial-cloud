package com.snail.sys.service;

import com.snail.sys.domain.SysRole;
import com.snail.sys.dto.SysRolePageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * 角色信息
 *
 * @author makejava
 * @since 2025-05-30 23:06:11
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysRole>> queryByPage(SysRolePageDTO dto);


}
