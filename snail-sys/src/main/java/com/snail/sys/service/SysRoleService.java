package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.domain.SysRole;
import com.snail.sys.dto.SysRolePageDTO;

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
    Page<SysRole> queryByPage(SysRolePageDTO dto);

    /**
     * selectRoleById
     *
     * @param roleId roleId
     * @return com.snail.sys.domain.SysRole
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    SysRole selectRoleById(Long roleId);

    /**
     * checkRoleDataScope
     *
     * @param roleId roleId
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    void checkRoleDataScope(Long roleId);

    /**
     * 校验角色是否允许操作
     *
     * @param role role
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    void checkRoleAllowed(SysRole role);

    /**
     * 校验角色名称是否唯一
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean checkRoleNameExists(SysRole role);

    /**
     * 校验角色权限是否唯一
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean checkRoleKeyExists(SysRole role);
}
