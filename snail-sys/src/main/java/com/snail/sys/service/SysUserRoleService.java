package com.snail.sys.service;

import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysUserRolePageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * 用户和角色关联表
 *
 * @author makejava
 * @since 2025-05-30 23:14:07
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysUserRole>> queryByPage(SysUserRolePageDTO dto);

    /**
     * 校验用户是管理员角色
     *
     * @param userId userId
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean checkAdminRole(Long userId);
}
